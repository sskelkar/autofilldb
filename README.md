[![Build Status](https://travis-ci.com/sskelkar/autofilldb.svg?branch=master)](https://travis-ci.com/sskelkar/autofilldb)
[![Coverage Status](https://coveralls.io/repos/github/sskelkar/autofilldb/badge.svg?branch=master)](https://coveralls.io/github/sskelkar/autofilldb?branch=master)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://github.com/sskelkar/autofilldb/blob/master/license)

# autofilldb
`autofilldb` simplies setting up test data in database integration tests in a Spring project. While writing a unit test, you may need to insert some table rows as part of the data setup. If a column has a `not null` constraint, you need to specify some value for it even if that column is not important to your test case. Similarly if a column has a `foreign key` constraint, you first need to insert a row in the referenced table so that you can provide a valid value in the foreign key column. You are required to do this even if the foreign key column or the referenced table are irrelavent to the business rule you're trying to test. This may unnecessarily bloat up the unit test.

`autofilldb` allows you to specify only those column values that are relevant to the unit test. `autofilldb` identifies columns that have the aformentioned constraints and automatically populates them with valid fake data. If a column has a `unique` constraint, a valid unique value would be generated. This makes your unit tests shorter and focussed on the use case being tested. 

`autofilldb` requires a bean of type `javax.sql.DataSource` in the Spring container. In turn, this library provides an `AutoFill` bean that can be injected in your test class.

##### Supported databases: MySQL

## Example
Let's say there are following three tables in your project:

**Organisation**

|column|type|key/constraint|
|-----|----|----|
|id|integer|primary key|
|name|varchar|-|

**Department**

|column|type|key/constraint|
|---|---|---|
|id|integer|primary key|
|name|varchar||
|organisation_id|integer|foreign key references organisation(id)|

**Employee**

|column|type|key/constraint|
|---|---|---|
|id|integer|primary key|
|name|varchar|not null|
|phone_number|varchar|not null, unique|
|email|varchar|not null, unique|
|department_id|integer|foreign key references department(id)|

You want to test a simple use case where you have a few employees in the database and you want to fetch an employee on the basis of email. For this, you'd need to insert a few rows in the Employee table. Even though just populating email and id fields in the Employee table would suffice to test this functionality, you'd need to populate all the `not null` columns of the Employee table. Furthermore there is a foreign key dependency from Employee to Department and Department to Organisation. So you'd need to insert a row each in Department and Organisation tables even though they have nothing to do with the use case you are trying to test.

The data setup for a database integration test would look something like this:
```
jdbcTemplate.execute("insert into organisation(id) values(10)");
jdbcTemplate.execute("insert into department(id, organisation_id) values(200, 10)");
jdbcTemplate.execute("insert into employee(id, name, phone_number, email, department_id) values(3001, 'John', '(480) 671-9585', 'john@example.com', 200)");
jdbcTemplate.execute("insert into employee(id, name, phone_number, email, department_id) values(3002, 'Jane', '(130) 516-6392', 'jane@example.com', 200)");
```
There could be many columns with `not null` constraint, and you'd need to populate them all in your simple test case. 

But with `autofilldb`, you can confine the data setup to only those column values that are required in the unit test. The above setup can be rewritten as following:
```
autoFill.into("employee", ImmutableMap.of("id", 3001, "email", "john@example.com"));
autoFill.into("employee", ImmutableMap.of("id", 3002, "email", "jane@example.com"));
```
You'd need to inject `AutoFill` bean into your test class. The above method takes in the table name and a Map of column name and value pairs that are relevant to the unit test.

## Project
##### Pre-requisite: Docker 1.6.0 or above
The project has two modules: 

1. `autofilldb` is the actual library
2. `autofilldb-tester` tests the library against a MySQL instance running in a Docker container.

Command to build the project: `./gradlew clean build -x signArchives`
## Authors
[sskelkar](https://github.com/sskelkar), [shreyasm](https://github.com/shreyasm), [rishikeshdhokare](https://github.com/rishikeshdhokare)
