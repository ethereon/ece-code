function [z] = triVariateGaussianGen(rho,var,avg)

N=100000;

avg1 = avg(1);
avg2 = avg(2);
avg3 = avg(3);

var1 = var(1);
var2 = var(2);
var3 = var(3);

sig1 = sqrt(var1)
sig2 = sqrt(var2)
sig3 = sqrt(var3)


rho12 = rho(1)
rho23 = rho(2)
rho31 = rho(3)


%% Generate Zero-mean, Unit-variance, and Independent Observations
x = randn(N,3); % 10000 random 2-vector Observations


%% Generate Zero-mean, Correlated Observations

% Assemble COV matrix

k12 = rho12*sig1*sig2;
k13 = rho31*sig1*sig3;
k23 = rho23*sig2*sig3;

K = [
    var1    k12     k13
    k12     var2    k23
    k13     k23     var3
    ];

% Cholesky factorization
A = (chol(K))';

% zero-mean correlated random vectors
y = x*A';                                    


%% Generation of Multi-variate Gaussian Random numbers
% Correlated random vectors with mean
z = y + ones(N,1)*[avg1, avg2, avg3];

%Return as 3xN matrix
z=z'; 
