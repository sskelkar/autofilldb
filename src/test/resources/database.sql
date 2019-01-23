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

------------------------------------------------------------------------
create table if not exists type_int(
  id integer,
  col1 integer not null,
  col2 integer(4) not null,
  col3 integer not null unique
);

create table if not exists type_varchar(
  id integer,
  col1 varchar(10) not null,
  col2 varchar(4) not null,
  col3 varchar(10) not null unique
);

create table if not exists type_datetime(
  id integer,
  col1 datetime not null,
  col2 datetime not null unique
);

create table if not exists id_tracker(
  int_type integer,
  datetime_type integer
)
insert into id_tracker values(0,0);
