# Inequality Joins

Proposed optimization for quicker join operation of join conditions having  two predicates  with inequality operators (<,>,<=, >=). The original algorithm has been taken from the section 3.2 of the following paper.
```
Khayyat, Zuhair, et al. \Lightning fast and space ecient inequality joins" Proceedings of
the VLDB Endowment 8.13 (2015): 2074-2085
```

Example of select operation with two predicates:
```
Select 
Column_1,Column_2 
From
Table a,b 
a_1 < b_1 
And 
a_2 > b_2  
```

## Optimization Proposed:
### Modification of BitArray traversal technique
Instead of using bitarray, usage of a normal array having the indices of the next '1' in the array. By this manner we’ll able to decrease the time required to traverse through the array to find the cells having '1' as value. Though, it increases the Space required as we have shifted from bitarray to normal array which can contain any integer. 

