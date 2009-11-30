function [ c ] = primesLessThan(n)
%PRIMESLESSTHAN Returns the number of primes <= n
%   Uses the Inclusion-Exclusion Principle to calculate the number
%   of primes p such that 1<p<=n.


%Smallest prime factor is less than sqrt(n)
p = primes(sqrt(n));

s = zeros(1,length(p));

for i=1:length(s)
    x = nchoosek(p,i);
    for j=1:size(x,1)
        s(i)=s(i)+floor(n/prod(x(j,:)));
    end
    s(i)=s(i)*((-1)^i);
end

c = sum(s)+n+length(p)-1;


end

