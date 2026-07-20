# PostgreSQL Indexes & Constraints

## Indexes

Indexes are used to make searches faster. PostgreSQL can find rows without scanning the entire table.

### Create Index

```sql
CREATE INDEX idx_employee_name
ON employees(name);
```

### Create Unique Index

```sql
CREATE UNIQUE INDEX idx_employee_email
ON employees(email);
```

### Drop Index

```sql
DROP INDEX idx_employee_name;
```

### When to Use

* Columns used in `WHERE`
* Columns used in `JOIN`
* Columns used in `ORDER BY`
* Columns searched frequently

Example:

```sql
SELECT * FROM employees
WHERE email = 'john@example.com';
```

An index on `email` helps this query a lot.

---

## Constraints

Constraints ensure that invalid data cannot be inserted.

### PRIMARY KEY

```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY
);
```

* Unique
* Cannot be NULL

### FOREIGN KEY

```sql
CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id)
);
```

Ensures `user_id` exists in `users`.

### UNIQUE

```sql
email VARCHAR(255) UNIQUE
```

No duplicate values allowed.

### NOT NULL

```sql
name VARCHAR(100) NOT NULL
```

Value is mandatory.

### CHECK

```sql
age INT CHECK (age >= 18)
```

Only valid values are allowed.

### DEFAULT

```sql
created_at TIMESTAMP DEFAULT NOW()
```

Automatically sets a value when none is provided.

---

## Quick Example

```sql
CREATE TABLE employees (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    age INT CHECK (age >= 18),
    created_at TIMESTAMP DEFAULT NOW()
);
```

This table has:

* Primary key
* Unique constraint
* Not null constraint
* Check constraint
* Default value
