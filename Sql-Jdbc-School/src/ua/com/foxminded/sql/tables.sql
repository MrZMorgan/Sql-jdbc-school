DROP TABLE groups;
CREATE TABLE groups (
    group_id BIGSERIAL NOT NULL PRIMARY KEY,
    group_name VARCHAR(50) NOT NULL
);

DROP TABLE students;
CREATE TABLE students (
    student_id BIGSERIAL NOT NULL PRIMARY KEY,
    group_id INT NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    courses
);

DROP TABLE courses;
CREATE TABLE courses (
    course_id BIGSERIAL NOT NULL PRIMARY KEY,
    course_name VARCHAR(50) NOT NULL,
    course_description VARCHAR(200) NOT NULL
);

