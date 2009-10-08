function [ phi ] = phaseLockedLoop(r, A, fC, fS, K1, K2, seed)

%   Estimates the phase of a modulated signal
%      
%   Args
%   r    : input signal of the form A*cos(2*pi*fC*t+phase(t))
%   A    : Amplitude of r
%   fC   : Carrier frequency of r
%   fS   : Sampling frequency of r
%   K1   : PLL Constant (eg : 0.1)
%   K2   : PLL Constant (eg : K1/10)
%   seed : Initial phase estimate
%
%   Returns
%   A vector with corresponding phase estimates
%

    %The width of the summation window for the FIR filter
    M = 2*(fS/fC);
    
    %PLL Constants
    K2 = K1/10;
    
    %The sampling interval
    Ts = 1/fS;

    %The number of samples
    N = length(r);
    
    phi = zeros(1,N);
    x = zeros(1,N);
    e = zeros(1,N);
    
    %Initialize phi with our guess
    phi(1) = seed;

    for n=[0:N-1]
        
        idx=n+1;
        
        x(idx) = -2*sin(2*pi*fC*n*Ts + phi(idx))*r(idx)/A;
        
        %Running-sum low-pass FIR filter
        e(idx) = sum(x(max(n-M+1,0)+1:idx))/M;
        
        sigma = sum(e(1:idx));
        
        %Calculate the next phase estimate
        phi(idx+1) = (phi(idx)+K1*e(idx) + K2*sigma);
        
    end
    
    %Remove the phase seed from the vector
    phi(1)=[];
   

end

