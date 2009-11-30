function [u]=qpsk(bitArray, tSampling, tPulse, ampPulse)
%QPSK Map the bitarray to QPSK symbols 
%
%   Arguments
%
%   bitArray  : A logical vector
%   tSampling : Sampling interval
%   tPulse    : Pulse duration
%   ampPulse  : Pulse Amplitude
%
%   Returns
%
%   u         : The complex QPSK signal

assert(mod(length(bitArray),2)==0);

%QPSK : nSyms = nBits/2
Nd = length(bitArray)/2;

syms=zeros(1,Nd);

for i=1:Nd
    
    idx=((i-1)*2)+1;
    
    %Map using gray coding
    sReal = bitArray(idx)*2 -1;
    sImag = bitArray(idx+1)*2 -1;
    
    syms(i)=sReal+(1i*sImag);
    
    
end

nSamples = tPulse*Nd/tSampling;

u = zeros(1,int64(nSamples));

for i=1:nSamples
    u(i) = ampPulse*syms(ceil(i*tSampling/tPulse));
end

end

