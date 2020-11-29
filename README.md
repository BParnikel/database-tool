# Database Tool
Provides an API to manage PostgreSQL connections.

It allows to 
- Get all databases added previously
- Add a new database to the list
- Update database (schema/user/password)
- Remove database from the list
- Get all schemas in a database
- Get all tables in a schema
- Get all table columns
- Get data preview of a table (100 records)
- Get numeric columns statistics for a table
  - AVG value
  - MIN value
  - MAX value

There is one all-in-one integration test that covers... like hm... everything?

There are some prerequisites:
- Postgres is installed locally
- It has a schema with name *ataccama* for real run
- It has a schema with name *test-ataccama* for test run

Also it's possible to check API documentation via Swagger:
- Run app
- Go to http://localhost:8080/swagger-ui.html
- PROFIT!!!
