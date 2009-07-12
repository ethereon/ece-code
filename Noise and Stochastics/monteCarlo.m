%
%   Monte-Carlo Simulation for estimating area
%   Saumitro Dasgupta <ethereon@gmail.com>
%
%
%                   2
%           -----------------
%           |               |
%           |               |
%           |               |
%         1 |               | 3
%           |               | 
%           |               |
%           |               |
%           ----------------- 
%                   4
%


NUM_POINTS = 1000000;

pointX = 0.2;
pointY = 0.4;

randMatrixForX = -1 + 2*rand(1,NUM_POINTS);
randMatrixForY = -1 + 2*rand(1,NUM_POINTS);


distanceFromPoint = sqrt(((randMatrixForX - pointX).^2) + ((randMatrixForY - pointY).^2));

distanceFromEdge1 = 1 + randMatrixForX;
distanceFromEdge2 = 1 - randMatrixForY;
distanceFromEdge3 = 1 - randMatrixForX;
distanceFromEdge4 = 1 + randMatrixForY;

boolCloserToPoint = (distanceFromPoint<distanceFromEdge1) & (distanceFromPoint<distanceFromEdge2)...
    & (distanceFromPoint<distanceFromEdge3) & (distanceFromPoint<distanceFromEdge4);

filteredIndices = find(boolCloserToPoint>0);
filteredX = randMatrixForX(filteredIndices);
filteredY = randMatrixForY(filteredIndices);

expRatio = length(filteredIndices) / NUM_POINTS;
area = expRatio * 4;

scatter(filteredX, filteredY, 10);
title('Monte-Carlo Simulation for Approximating Area');
xlabel('x');
ylabel('y');
xlim([-1 1]);
ylim([-1 1]);
hold
scatter(pointX, pointY, 'f', 'g')
text(-0.5,-0.7, sprintf('Approximate Area : %f',area));