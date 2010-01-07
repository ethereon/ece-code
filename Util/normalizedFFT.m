function [ f,mag ] = normalizedFFT( x, Fs)
%
%   normalizedFFT Returns the scaled and zero-centered FFT
%   
%   Input Arguments
%   x   : the time-domain signal vector
%   Fs  : Sampling rate of x
%
%   Output values:
%   f   : The freuency axis values
%   mag : The magnitudes corresponding to f
%


nSamples=length(x);
duration = nSamples/Fs;

if mod(nSamples,2)==0
    fUnscaled = -(nSamples)/2:((nSamples)/2)-1;
else
    fUnscaled = -(nSamples-1)/2:(nSamples-1)/2; % N odd
end

f = fUnscaled/duration;

%Calculate the FFT and normalize it.
mag=fft(x)/nSamples;

%Center it about zero and take the absolute value.
mag=abs(fftshift(mag));


end

