function knn()
load('faces.mat');
td = traindata;
tl = trainlabels;
ted = testdata;
tel = testlabels;

x=[1,10,20,30,40,50,60,70,80,90,100];
y1=zeros(1,11);
y2=zeros(1,11);
trdata = size(td);
tedata = size(ted);
numOfFigures = trdata(1,1);
numOfTestFigures = tedata(1,1); 
dp = zeros(numOfTestFigures,numOfFigures);

for testRowNumber=1:numOfTestFigures
testfigure = ted(testRowNumber,:);
dists = zeros(1,numOfFigures);
for i=1 : numOfFigures
    dists(i) = cosineDistance(testfigure,td(i,:));
end
[d,ind] = sort(dists);
dp(testRowNumber,:)= ind;
end

for i=1:11
k=x(i);
misses = 0;
for testRowNumber=1:numOfTestFigures
ind_closest = dp(testRowNumber,:);
%size(ind_closest)
x_closest = ind_closest(1:k);
x_closest = tl(x_closest,:);
numOfOnes = sum(x_closest==1);
numOfTwos = k-numOfOnes;
actclass = tel(testRowNumber,1);
if(numOfOnes > numOfTwos)
    preclass = 1;
else
    preclass = 2;
end
%sprintf('Numbers of 1s: %f\n', numOfOnes)
%sprintf('Numbers of 0s: %f\n', numOfTwos)
%sprintf('Class Label: %f\n', preclass)
%sprintf('Class Label: %f\n', actclass)
if(preclass~=actclass)
    misses = misses +1;
end

end
y1(i)=misses/numOfTestFigures;
end
%sprintf('Total figures: %f\n', numOfTestFigures)
%sprintf('Num of Misses: %f\n', misses)


% TRAINING DATA
dp = zeros(numOfFigures,numOfFigures);
for testRowNumber=1:numOfFigures
testfigure = td(testRowNumber,:);
dists = zeros(1,numOfFigures);
for i=1 : numOfFigures
    dists(i) = cosineDistance(testfigure,td(i,:));
end
[d,ind] = sort(dists);
dp(testRowNumber,:)= ind;
end

for i=1:11
k=x(i);
misses = 0;
for testRowNumber=1:numOfFigures
ind_closest = dp(testRowNumber,:);
%size(ind_closest)
x_closest = ind_closest(1:k);
x_closest = tl(x_closest,:);
numOfOnes = sum(x_closest==1);
numOfTwos = k-numOfOnes;
actclass = tl(testRowNumber,1);
if(numOfOnes > numOfTwos)
    preclass = 1;
else
    preclass = 2;
end
if(preclass~=actclass)
    misses = misses +1;
end
end
y2(i)=misses/numOfFigures;
end
figure
plot(x,y1,'r',x,y2,'b')
title('KNN Error Rate on Faces Dataset')
xlabel('Number Of Neighbours (k)')
ylabel('Error')
legend('Testing Error','Training Error','Location','southeast')
end