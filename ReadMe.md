# University education system Rest-API
this project only includes the server side of the mentioned system and provides web services. In this document, the term LCRUD or its sub phrases are abbreviations respectively List, Create, Read, Update, Delete

#### Basic design of entities
![App Screenshot](imgs/entities.png)
<br><br>
#### Description of users:
• We have four types of users: admin, student, instructor and staff.
<br>
• Users have some common information (such as username and password) and some specific information (such as student number that is specific to students).
<br>
• A user can be both a student and an instructor.
<br><br>
#### Lesson description:
• We have a number of terms and a number of courses.
<br>
• In each term, each instructor can offer a number of lessons, which we call a section.
<br>
• Each student can register in a number of study courseSections.
<br><br>
### Required web services
In most of the web services of the system we implemented authentication mechanism, which means that the client should put a suitable header in its request and the server will realize the identity by using it.
<br>
Tip: Search for authentication and authorization keywords along with spring.
<br>
#### User registration
Anyone can register by providing username, password, name, phone number and national code. All fields are mandatory. Username, phone number and national code must be unique. If successful, the personal user will be created passively.
<br>
Stored users passwords in a hashed form in the database.
<br>
#### User Authentication
Anyone can log in to the system by providing a username and password. If it is correct, the server must provide the necessary information to the client to put in the header in the next requests so that the server can authenticate him.
<br>
#### Staff management
Admin can register another person as a staff in the system by providing the username and personnel ID, and in this case, that user will be activated.
Explaining that the staff must have registered himself first and then tell the administrator the username so that he can continue the registration. Other LRUD operations must also be enabled by the administrator.
#### Instructor registration
Similar to staff registration, with the consideration that CUD operations can be performed by admin and staffs and LR operations can be performed by all users.
<br>
#### Student registration
Similar to instructor registration.
<br>
#### Term definition
In the form of LCRUD, LR can be done by all users and CUD can only be done by admin and staffs.
<br>
#### Course definition
In the form of LCRUD, LR can be done by all users and CUD can only be done by admin and staffs.
<br>
#### CourseSection definition
In the form of LCRUD, LR can be done by all users and C can only be done by the admin. UD operations can be performed by admin, staffs and also by the instructor who himself defined this courseSection. Operation D is conditional on the fact that no student is enrolled in this courseSection.
<br>
#### Registration in the courseSection
It is done by the student and by presenting the class ID.
<br>
#### Get the list of class students
It can be done by the instructor, administrator and staff in such a way that the list of registered students will be presented after receiving the ID of the courseSection. For each student, student ID, name, student number and grade are provided.
<br>
#### Grading students
It can be done by the instructor, in this way, by receiving the class ID, student ID and grade, the grade is registered.
<br>
Grading the list of students, in such a way that a courseSection ID and a list of student IDs and grades are received.
<br>
#### View term grades
It can be done by the student, in such a way that upon receiving the term ID, the grade point average of the term as well as the list of student courseSections in that term along with each grade will be provided. For each courseSection, courseSection ID, course name, number of units, instructor's name and grade should be provided.
<br>
#### View academic summary
It can be done by the student, in such a way that it provides the total grade point average of the student as well as the list of terms along with the grade point average of each term. For each term, the term ID, term title and term average should be provided.
<br>
### Automated test
• Write a unit test to calculate the student's GPA in one term. • Write an integration test for this scenario: the instructor defines a courseSection, the student enrolls in that courseSection,
<br>
The instructor grades the student, the student sees the term grades correctly (hint: you can use MockMvc if you wish)
## API Reference
after building dependencies and running these are some sample endpoints to test

#### Get all users:

```bash
curl -X GET -w " %{http_code}\n" "http://127.0.0.1:8080/all-users"
```

| Parameter           | Type  | Description              |
|:--------------------|:------|:-------------------------|
| `list of all users` | `GET` | No authentication needed |

#### Register a new custom user:

```bash
curl -X POST -H "Content-Type: application/json" -d @/absolute/path/to/costumUser.json -w " %{http_code}\n" "http://127.0.0.1:8080/register"
```

| Parameter                           | Type   | Description                      |
|:------------------------------------|:-------|:---------------------------------|
| `customUser json registration file` | `POST` | No authentication needed         |

#### Register a new custom user:

```bash
curl -X POST -H "Content-Type: application/json" -d @/absolute/path/to/userAuth.json -w " %{http_code}\n" "http://127.0.0.1:8080/authenticate"
```

| Parameter                             | Type   | Description                                |
|:--------------------------------------|:-------|:-------------------------------------------|
| `customUser json authentication file` | `POST` | **Required** correct username and password |

| Parameter              | Type  | Description                                |
|:-----------------------|:------|:-------------------------------------------|
| `authentication token` | `GET` | **Required** correct username and password |
#### Register a new custom user:

```bash
curl -X POST -H "Content-Type: application/json" -H "Authorization: Bearer <access_token>" -d @/absolute/path/to/instructor.json -w " %{http_code}\n" "http://127.0.0.1:8080/authorize/instructor?username=ali"
```

| Parameter                             | Type   | Description                                |
|:--------------------------------------|:-------|:-------------------------------------------|
| `customUser json authentication file` | `POST` | **Required** correct username and password |
**If you found this project helpful, consider giving it a star ⭐️ for others to find! ❤️** 

[![GitHub stars](https://img.shields.io/github/stars/clonerplus/rest-api.svg?style=social&label=Star)](https://github.com/clonerplus/rest-api)
