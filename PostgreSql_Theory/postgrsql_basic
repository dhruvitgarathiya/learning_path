# PostgreSQL Basic Queries
PostgreSQL is a powerful open-source relational database management system (RDBMS). It uses SQL (Structured Query Language) to create, retrieve, update, and manage data stored in tables.

# Basic SQL Commands

## Create a Database

```sql
CREATE DATABASE company_db;
```
## Connect to a Database

```sql
\c company_db
```
## Create a Table

```sql
CREATE TABLE employees (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    department VARCHAR(50),
    salary NUMERIC(10,2)
);
```

## Insert Data

```sql
INSERT INTO employees (name, department, salary)
VALUES ('John', 'IT', 60000);
```

Insert multiple rows:

```sql
INSERT INTO employees (name, department, salary)
VALUES
('Alice', 'HR', 50000),
('Bob', 'Finance', 70000);
```

## Retrieve Data

Get all records:

```sql
SELECT * FROM employees;
```

Select specific columns:

```sql
SELECT name, salary
FROM employees;
```
## Filter Data

```sql
SELECT *
FROM employees
WHERE department = 'IT';
```

Using multiple conditions:

```sql
SELECT *
FROM employees
WHERE salary > 50000
AND department = 'IT';
```

## Sort Data

Ascending order:

```sql
SELECT *
FROM employees
ORDER BY salary;
```

Descending order:

```sql
SELECT *
FROM employees
ORDER BY salary DESC;
```
## Limit Results

```sql
SELECT *
FROM employees
LIMIT 5;
```
## Update Data

```sql
UPDATE employees
SET salary = 65000
WHERE id = 1;
```
## Delete Data

```sql
DELETE FROM employees
WHERE id = 1;
```

## Remove All Records

```sql
TRUNCATE TABLE employees;
```

## Delete a Table

```sql
DROP TABLE employees;
```
# Aggregate Functions

Count records:

```sql
SELECT COUNT(*) FROM employees;
```

Average salary:

```sql
SELECT AVG(salary) FROM employees;
```

Maximum salary:

```sql
SELECT MAX(salary) FROM employees;
```

Minimum salary:

```sql
SELECT MIN(salary) FROM employees;
```

Total salary:

```sql
SELECT SUM(salary) FROM employees;
```

# Group By

```sql
SELECT department, COUNT(*)
FROM employees
GROUP BY department;
```


# Having

```sql
SELECT department, AVG(salary)
FROM employees
GROUP BY department
HAVING AVG(salary) > 60000;
```


# Useful Operators

```sql
=
!=
>
<
>=
<=
BETWEEN
IN
LIKE
IS NULL
```

Example:

```sql
SELECT *
FROM employees
WHERE name LIKE 'J%';
```
# Query Execution Order

1. FROM
2. WHERE
3. GROUP BY
4. HAVING
5. SELECT
6. ORDER BY
7. LIMIT

# Quick Reference

| Command | Purpose |
|---------|---------|
| CREATE | Create database/table |
| INSERT | Add new records |
| SELECT | Retrieve data |
| WHERE | Filter records |
| ORDER BY | Sort records |
| GROUP BY | Group similar data |
| HAVING | Filter grouped data |
| UPDATE | Modify existing data |
| DELETE | Remove records |
| TRUNCATE | Remove all rows |
| DROP | Delete database object |
