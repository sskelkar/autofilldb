[![Build Status](https://travis-ci.com/sskelkar/autofilldb.svg?branch=master)](https://travis-ci.com/sskelkar/autofilldb)
[![Coverage Status](https://coveralls.io/repos/github/sskelkar/autofilldb/badge.svg?branch=master)](https://coveralls.io/github/sskelkar/autofilldb?branch=master)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

# autofilldb
`autofilldb` simplies setting up test data in database integration tests in a Spring project. While writing a unit test, you may need to insert some table rows as part of the data setup. If a column has a `not null` constraint, you need to specify some value for it even if that column is not important to your test case. Similarly if a column has a `foreign key` constraint, you first need to insert a row in the referenced table so that you can provide a valid value in the foreign key column. You are required to do this even if the foreign key column or the referenced table are irrelavent to the business rule you're trying to test. This may unnecessarily bloat up the unit test.

`autofilldb` allows you to specify only those column values that are relevant for the unit test. `autofilldb` identifies columns that have the aformentioned constraints and automatically populates them with valid fake data. This makes your unit tests shortered and focussed on the business rule. 

`autofilldb` requires a bean of type `javax.sql.DataSource` in the Spring container. In turn, this library provides an `AutoFill` bean that can be injected in your test class.

##### Supported databases: MySQL

## Example
 

##### Authors
[sskelkar](https://github.com/sskelkar), [shreyasm](https://github.com/shreyasm)
