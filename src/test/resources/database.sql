create table if not exists organisation(
  id integer,
  name varchar(20) not null,
  country_code varchar(2) not null,
  primary key(id)
);

create table if not exists department(
  id varchar(6),
  name varchar(20) not null,
  established integer(4) not null,
  organisation_id integer,
  primary key(id),
  foreign key(organisation_id) references organisation(id)
);

create table if not exists employee(
  id integer,
  name varchar(20) not null,
  created datetime not null,
  department_id varchar(6),
  primary key(id),
  foreign key(department_id) references department(id)
);