function decodeBPSK( inputSig )
% DECODEBPSK decodes a signal modulated using
% binary phase shift keying.
%
% Final exam problem from Comm Sys Course / Fall '09
%
% To test, try the following:
%
% >> load bpskData; % Load modulated data from bpskData.mat
% >> decodeBPSK(v); % Demod and display encoded message
%

threshold = 0;
n = length(inputSig);
r = 16 * 10^3; %Sampling rate
T = 10^-3; %Pulse duration
spb = r*T; %Samples per bit

bpc = 5; %Bits per character
nBits = n/spb; 
nChars = nBits/bpc; %Number of characters in the message
g = ones(1,spb); %The pulse g(t)

%Perform Matched Filtering
mfSig = conv(inputSig,g,'same');

%The output message bits
msgBits = mfSig(([1:nBits]-0.5)*spb)>threshold;
strBits = strrep(int2str(msgBits),' ','');

%Decode and display the message
for i=1:nChars
    
    offset = ((i-1)*bpc)+1;
    ordinal =bin2dec(strBits(offset:offset+bpc-1));
    asc = (ordinal~=0)*char(64+ordinal)+(ordinal==0)*32;
    fprintf('%c',asc);

end

    fprintf('\n');

end
