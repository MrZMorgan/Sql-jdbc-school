DROP TABLE groups;
CREATE TABLE groups (
    group_id SERIAL NOT NULL PRIMARY KEY,
    group_name VARCHAR(50) NOT NULL
);

DROP TABLE if exists courses cascade;
CREATE TABLE courses (
    course_id BIGSERIAL NOT NULL PRIMARY KEY,
    course_name VARCHAR(50) NOT NULL,
    course_description VARCHAR(200)
);


DROP TABLE if exists students cascade;
CREATE TABLE students (
    student_id SERIAL NOT NULL PRIMARY KEY,
    group_id INT NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL
);


