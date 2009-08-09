function randomWalk()
% RANDOMWALK Simulates a random walk
% Plots the mean and variance
%


    M=100;
    N=1000;
    S=1;
    randSeqReal = rand(N+1,M);
    randSteps = S*((randSeqReal>0.5) - (randSeqReal<0.5));
    randDist = cumsum(randSteps);

    figure;
    plot([0:N],randDist);
    xlabel('Steps');
    ylabel('Distance');
    title('Random Walk');
    
    avgDist = sum(randDist')/(N+1);
    figure;
    plot([0:N],avgDist);
    xlabel('Steps');
    ylabel('Distance');
    title('Average Sequence');
    
    
    varDist = sum(((randDist.^2) - repmat((avgDist').^2,1,M))')/(N+1);
    figure;
    plot([0:N],varDist);
    xlabel('Steps');
    ylabel('n');
    title('Variance');
    

end