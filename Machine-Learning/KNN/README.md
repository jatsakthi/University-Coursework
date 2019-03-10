# KNN

Implementation of a facial attractiveness classification task along with the error & training plot after varying K-values in the range [1, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100].

# faces.mat

A file that contains the Matlab variables for
1. traindata (training data)
2. trainlables (training labels)
3. testdata (test data)
4. testlabels (test labels)

# Task

Given a picture of a face, the model predicts whether the average rating of the face is hot or not.

# CosineDistance.m
Implements the cosine distance, a simple distance function. It takes two feature vectors x and y, and computes a nengative, symmetric distance between x and y.

# Procedure

For a given picture, the label of K nearest neighbours is extracted and the majority of the label is assigned as the label to this particular picture.

Compare the assigned label with the acutal label for error plot. The plot is as follows:
![alt tag](https://github.com/jatsakthi/Machine-Learning/blob/master/KNN/Error%20Plot.png)

# Folder Description

1. knn.m: The KNN code.
2. cosineDistance.m: Code for calculation of cosine distance.
3. faces.mat: Matlab Data for initial loading of Matlab variables.
4. Error Plot: The final plot. 
