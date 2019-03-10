# Gradebook

Implementation of a Java based GUI(JAVA AWT) of a Gradebook that allows an instructor to perform CRUD operations (i.e. Create, Read, Update, Delete) on the gradebook. Sample is as below:

![alt tag](https://github.com/jatsakthi/Rest-Services/blob/master/Gradebook/Sample.PNG)

1. An instructor can create a new entry for a student.
2. An instructor can read past entries of a student.
3. An instructor can update past entries of a student.
4. An instructor can delete past entries of a student.

## Web Resources

1. WorkItem
```
1. Name (String)
2. Grade (String)
3. Comments (String)
```

2. Student
```
1. Id (String)
2. WorkItems (List of WorkItem resource).
```

3. Students
```
1. Students (List of Student resource).
```

## Requests

**http://localhost:8080/restservices/Gradebook**
1. Create: POST (200: OK, 409: Conflict)
2. Read: GET (200: OK, 404: Not Found)
3. Update: PUT (200: OK, 404: Not Found)
4. Delete: DELETE (200: OK, 404: Not Found)

## Folder Description

1. Sample.png: A screenshot of the application.
2. Server: Server side code.
3. Client: Client side code.
