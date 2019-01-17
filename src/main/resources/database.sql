create database if not exists test;
grant all privileges on test.* to 'test'@'localhost' identified by 'test';
use test;

create table if not exists organisation(
  id integer,
  name varchar(20) not null,
  country_code varchar(2) not null,
  primary key(id)
);
insert ignore into organisation values(10, 'org', 'IN');

create table if not exists department(
  id varchar(6),
  name varchar(20) not null,
  established integer(4) not null,
  organisation_id integer,
  primary key(id),
  foreign key(organisation_id) references organisation(id)
);
insert ignore into department values('80c76f', 'finance', 2015, 10);

create table if not exists employee(
  id integer,
  name varchar(20) not null,
  created datetime not null,
  department_id varchar(6),
  primary key(id),
  foreign key(department_id) references department(id)
);
insert ignore into employee values(1, 'jason', '2018-01-01 00:00:00', '80c76f');
insert ignore into employee values(2, 'wendy', '2018-03-01 00:00:00', '80c76f');
insert ignore into employee values(3, 'karun', '2018-05-01 00:00:00', '80c76f');