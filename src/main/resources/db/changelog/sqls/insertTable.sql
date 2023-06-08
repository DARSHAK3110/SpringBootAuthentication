--liquibase formatted sql

--changeset Darshak:1

create  table  User (  
    user_id  bigint  primary key ,  
    first_name  varchar(16)  not  null,
    last_name  varchar(16)  not  null,
    phone_number  bigint  null UNIQUE,  
    role varchar(5) not null,
    password varchar(255)  not  null,
    created_at  datetime,
    updated_at datetime,
    deleted_at datetime
);


--changeset Darshak:2
drop table user;
create  table  User (  
    user_id  bigint  primary key ,  
    first_name  varchar(16)  not  null,
    last_name  varchar(16)  not  null,
    phone_number  bigint  null UNIQUE,  
    role varchar(5) not null,
    password varchar(255)  not  null,
    created_at  datetime,
    updated_at datetime,
    deleted_at datetime
);

--changeset Darshak:3

create  table  user (  
    user_id  bigint  primary key AUTO_INCREMENT,  
    first_name  varchar(16)  not  null,
    last_name  varchar(16)  not  null,
    phone_number  bigint  null UNIQUE,  
    role varchar(5) not null,
    password varchar(255)  not  null,
    created_at  datetime,
    updated_at datetime,
    deleted_at datetime
);

--changeset Darshak:4
drop  table user;
create  table  user (  
    user_id  bigint  primary key AUTO_INCREMENT,  
    first_name  varchar(16)  not  null,
    last_name  varchar(16)  not  null,
    phone_number  bigint  not null UNIQUE,  
    role varchar(5) not null,
    password varchar(255)  not  null,
    created_at  datetime,
    updated_at datetime,
    deleted_at datetime
);