# Appeals

Implementation of a HATEOAS based application which allows students to submit appeals. Below is the transition diagram of the developed application:
![alt tag](https://github.com/jatsakthi/Rest-Services/blob/master/Appeals/Resource%20Transition%20Diagram.PNG)

## Web Resources
### Appeal Resource
```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?> 
<appeal xmlns="http://schemas.restbucks.com"> 
<content>Please Consider my request Sir!</content> 
<item>CSE 564</item> 
<status>unreviewed</status> 
</appeal> 
```
### Review Resource
```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?> 
<review xmlns="http://schemas.restbucks.com"> 
<reviewNote>It’s a review request </reviewNote> 
</review> 
```
## Break-Down of the OUTPUT and the TEST CASES:
### Starting HAPPY CASE TEST
1. Step1 - Create an appeal 
2. Step2 - Submit a review request for the appeal 
3. Step3 - Get Result 
4. Step4 - Check on the Appeal status 
5. Step5- See the result

### Starting ABANDON CASE TEST
1. Step 1 - Create an appeal 
2. Step 2 - Delete the appeal

### Starting FORGOTTEN CASE TEST 
1. Step 1 - Create an appeal 
2. Step 2 - Submit a review request for the appeal 
3. Step 3 – (Wait for 7 days) Check on the Appeal status 
4. Step 4 - Submitting again the review request 
5. Step 5 - Get Result 
6. Step 6 - See the result 

### Starting BAD START CASE TEST 
1. Step 1 - Create an appeal

### Starting BAD ID CASE TEST
1. Step 1 - Create an appeal 
2. Step 2 - Submit a review request for the appeal 
3. Step 3 – (Wait for 7 days) Check on the Appeal status 
4. Step 4 - Submitting again the review request 

 


 
 


 

