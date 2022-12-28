

CREATE TABLE class_type (
    code VARCHAR(30) NOT NULL,
    name VARCHAR(300) NOT NULL,
    PRIMARY KEY (code)
);

INSERT INTO class_type (code, name) values ('OMA','Omaharjoitus');
INSERT INTO class_type (code, name) values ('EASY','Astanga easy');
INSERT INTO class_type (code, name) values ('MYSORE','Mysoreharjoittelu');

-- This is class template. Based on recurrence class will be turned to class events in calendar.
-- Class definition instance is class_event??
-- Student can buy individual class events from calendar using credits.
create table class_definition
(
    id                    serial
        primary key,
    version               integer      not null,
    name                  varchar(300) not null,
    class_type_code       varchar(30)  not null
        constraint fk_class_definition_class_type
            references class_type,
    location              varchar(500),
    price_credits         integer,
    max_people            integer,
    description           varchar(3000),
    recurrence_days       varchar(3)[],
    recurrence_start_date date CHECK (recurrence_end_date >= recurrence_start_date),
    recurrence_end_date   date CHECK (recurrence_end_date >= recurrence_start_date),
    start_time            time,
    end_time              time,
    created_at            timestamp    not null,
    updated_at            timestamp    not null
);

INSERT INTO class_definition (version, name, location, class_type_code, price_credits, max_people, description ,recurrence_days , recurrence_start_date, recurrence_end_date, start_time, end_time, created_at, updated_at) values
    (1, 'Astanga mysore', 'Annankatu 29 B Sisäpiha / Courtyard, 00100 Helsinki, Suomi', 'MYSORE', 10, 16, 'Mysoreharjoittelu on perinteinen tapa harjoitella astangajoogaa.', ARRAY['MON', 'WED'],
     '2023-01-01', '2023-05-31', '08:00', '09:15', NOW(), NOW())

-- Teacher can have many classes, class can have many teachers
CREATE TABLE class_definition_teacher(
     teacher_id int NOT NULL,
     class_definition_id int NOT NULL,
     FOREIGN KEY(teacher_id) REFERENCES teacher(id),
     FOREIGN KEY(class_definition_id) REFERENCES class_definition(id)
)

-- Calendar instance of class_definition. Students book this.
CREATE TABLE class_calendar_event(
    id SERIAL,
    class_definition_id INT,
    class_start TIMESTAMP,
    class_end TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY(class_definition_id) REFERENCES class_definition(id)
)

CREATE TABLE teacher(
    id SERIAL,
    version INT NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(254) UNIQUE,
    picture_url VARCHAR(3000),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    PRIMARY KEY (id)
)


CREATE TABLE student(
    id SERIAL,
    version INT NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(254) UNIQUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    PRIMARY KEY (id)
)

-- student can have many classes, class can have many students
CREATE TABLE student_class(
    student_id INT NOT NULL,
    class_id INT NOT NULL,
    FOREIGN KEY(student_id) REFERENCES student(id),
    FOREIGN KEY(class_id) REFERENCES class(id)
)

CREATE TABLE credit_pass(
    id SERIAL,
    version INT NOT NULL,
    name VARCHAR(200),
    description VARCHAR(1000),
    credits INT,
    price_cents INT,
    PRIMARY KEY (id)
)

-- Student buys bunch of credits with a pass. Credits can be used to buy differently priced classes.
-- Class purchases should be somewhere and total number of credits should be in some event sourced table.
-- If class is cancelled (by teacher, system or student) the credits are returned.
CREATE TABLE pass_purchase(
    id SERIAL,
    pass_id INT,
    student_id INT,
    purchase_time TIMESTAMP,
    price_payed_cents INT,
    PRIMARY KEY (id)
    FOREIGN KEY(pass_id) REFERENCES credit_pass(id),
    FOREIGN KEY(student_id) REFERENCES student(id)
)

