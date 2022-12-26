create table class_type
(
    code varchar(30) not null
        primary key,
    name varchar(300) not null
);

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
    recurrence_start_date date,
    recurrence_end_date   date,
    start_time            time,
    end_time              time,
    created_at            timestamp    not null,
    updated_at            timestamp    not null
);


