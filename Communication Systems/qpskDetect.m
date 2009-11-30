function [ dBits, dSig] = qpskDetect(nBits, tS, tPulse, ampPulse, recvSig, thres)
%QPSKDETECT Perform match filtering and bit detection for qpsk-ed signal
%
%   Arguments
%
%   nBits    : Number of bits in the original signal
%   tS       : Sampling interval
%   tPulse   : Pulse duration
%   ampPulse : Pulse amplitude
%   recvSig  : A vector containing the received complex QPSK signal
%   thres    : Threshold above which "it's a 1"
%
%   Returns
%
%   dBits    : The detected bitarray
%   dSig     : The detected complex QPSK signal


nPulses = nBits/2;
samplesPerPulse = length(recvSig)/nPulses;

assert(int64((length(recvSig)*tS/nPulses))==int64(tPulse));

g = ones(1,samplesPerPulse)*ampPulse;

ipRecvSig = real(recvSig);
quadRecvSig = imag(recvSig);


%Perform matched filtering
ipSig = conv(ipRecvSig,g,'same');
quadSig = conv(quadRecvSig,g,'same');


dSig = zeros(1,nPulses);
dBits = zeros(1,nBits);
for i=0:(nPulses-1)
    idx=i*2+1;
    sigIdx = round((i+0.5)*samplesPerPulse);
    dBits(idx) = ipSig(sigIdx) > thres;
    dBits(idx+1) = quadSig(sigIdx) > thres;
    dSig(i+1) = ipSig(sigIdx)+1i*quadSig(sigIdx);
end


end

