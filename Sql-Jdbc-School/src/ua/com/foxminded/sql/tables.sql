DROP TABLE groups;
CREATE TABLE groups (
    id SERIAL NOT NULL PRIMARY KEY,
    group_name VARCHAR(50) NOT NULL
);

DROP TABLE students;
CREATE TABLE students (
    id SERIAL NOT NULL PRIMARY KEY,
    group_id INT NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL
);

DROP TABLE courses;
CREATE TABLE courses (
    id SERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(200) NOT NULL
);

DROP TABLE students_courses;
CREATE TABLE students_courses (
    student_id INT REFERENCES students (id),
    courses_id INT REFERENCES courses (id)
);
