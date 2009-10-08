function testPLL()
%   Test the phaseLockedLoop function 

    %Carrier frequency of 10 kHz
    fC = 10*10^3;

    %We sample at four times the carrier frequency
    fS = 4*fC;
    
    %Create a sample signal spanning a thousand periods
    t = [0:1/fS:1000/fC];
    
    %Signal amplitude
    A = 5;
    
    %PLL Constants
    K1 = 0.1;
    K2 = 10/K1; 
    
    %Create test cases
    phases = {};
    titles = {};
    %The delta between the actual phase and our initial guess
    delta = {};
    idx=1;
    
    %Case I : Constant phase offset
    phi=pi/3;
    phases{idx} = phi;
    titles{idx} = 'Constant phase offset';
    delta{idx} = 0.1;
    idx=idx+1;
    
    %Case II : Constant frequency offset
    phi = 2*pi*(fC/100)*t;
    phases{idx} = phi;
    titles{idx} = 'Constant frequency offset';
    delta{idx}=1;
    idx=idx+1;
    
    %Case III : Sinusoidally varying phase
    phi = 3*cos(2*pi*(fC/1000)*t);
    phases{idx} = phi;
    titles{idx} = 'Sinusoidally varying phase';
    delta{idx}=2;
    idx=idx+1;
    
    %Case IV : Way off initial guess
    phi = 3*cos(2*pi*(fC/1000)*t);
    phases{idx} = phi;
    titles{idx} = 'Way off initial guess';
    delta{idx}=20;
    idx=idx+1;
 
    for i=[1:length(phases)]
        phi = phases{i};
        r = A*cos(2*pi*fC*t + phi);
        phiEst = phaseLockedLoop(r,A,fC,fS,K1,K2,phi(1)-delta{i});
        figure;
        plot(t,phi,'b');
        hold;
        plot(t,phiEst,'r');
        title(titles{i});
        xlabel('Time (secs)');
        ylabel('Phase');
    end
    
end

