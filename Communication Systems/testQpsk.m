function testQpsk()

nBits = 2000; %Number of bits

f = 1000; %Carrier Frequency
tSampling = 10^-5; %Sampling Interval
tPulse=10^-3; % Pulse duration

%Generate a random sequence
randSeq = rand(1,nBits) > 0.5;
%Perform QPSK mapping
u=qpsk(randSeq,tSampling, tPulse, 1);
%Plot the In Phase & Quadrature components
len = length(u);
duration = tSampling*(len-1);

t=[0:tSampling:duration];

inPhase = cos(2*pi*f*t).*real(u);
quadrature = sin(2*pi*f*t).*imag(u);
    
figure;
subplot(2,1,1); plot(t,inPhase);
axis([0 10^-2 -2 2]);
title('In Phase');
ylabel('Amplitude');

subplot(2,1,2); plot(t,quadrature);
title('Quadrature');
xlabel('Time (secs)');
ylabel('Amplitude');
axis([0 10^-2 -2 2]);

%Corrupt that nice & clean signal with some AWGN
%Experiment with phase errors.

phiErr   = {0  pi/6 (2*pi/(1000*tPulse)) 0 pi/6 pi/6 0};
shouldAddNoise = {0 0 0 1 1 1 1};
var = { 0 0 0 0.5 2.5 5 4.5};
  
for i=1:length(phiErr)
    

    v=u.*exp(1j*phiErr{i});
   
    if shouldAddNoise{i}
        v=v+sqrt(var{i})*(randn(1,len) +1i*randn(1,len));
        noiseStr = sprintf(' with AWGN, variance = %d',var{i});
    else
        noiseStr = ' with no Noise';
    end

    [dBits, dSig]=qpskDetect(nBits,tSampling, tPulse,1, v,0.5);
    figure;
    scatter(real(dSig),imag(dSig));
    titleStr = sprintf('Phase error = %f',phiErr{i});
    titleStr = strcat(titleStr,noiseStr);
    title(titleStr);
    
    ber = sum(dBits~=randSeq)/nBits;
    fprintf('Bit Error Rate : %f (%s)\n',ber,titleStr);
    
end


end

