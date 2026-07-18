# PostgreSQL Views & Transactions

## Views

A **View** is a virtual table created from the result of a SQL query. It doesn't store data itself; it simply displays data from one or more tables.

### Why use Views?

- Hide complex queries
- Improve code reusability
- Restrict access to specific columns
- Keep SQL cleaner

### Create a View

```sql
CREATE VIEW employee_details AS
SELECT id, name, department
FROM employees;
```

### Query a View

```sql
SELECT * FROM employee_details;
```

### Update a View

```sql
CREATE OR REPLACE VIEW employee_details AS
SELECT id, name, department, salary
FROM employees;
```

### Delete a View

```sql
DROP VIEW employee_details;
```

> Views are best used when the same query is needed multiple times.

---

# Transactions

A **Transaction** is a group of SQL statements that execute as a single unit.

Either **all statements succeed** or **none of them are applied**.

### Basic Transaction

```sql
BEGIN;

UPDATE accounts
SET balance = balance - 1000
WHERE id = 1;

UPDATE accounts
SET balance = balance + 1000
WHERE id = 2;

COMMIT;
```

If something goes wrong:

```sql
ROLLBACK;
```

This cancels all changes made after `BEGIN`.

---

## Savepoint

A **SAVEPOINT** lets you roll back only part of a transaction.

```sql
BEGIN;

INSERT INTO employees(name)
VALUES ('John');

SAVEPOINT sp1;

INSERT INTO employees(name)
VALUES ('Alice');

ROLLBACK TO sp1;

COMMIT;
```

Only `John` will be inserted.

---

## Transaction Commands

| Command | Purpose |
|---------|---------|
| BEGIN | Start a transaction |
| COMMIT | Save all changes |
| ROLLBACK | Undo all changes |
| SAVEPOINT | Create a rollback point |
| ROLLBACK TO | Roll back to a savepoint |

---

## Where are Transactions Used?

- Bank transfers
- Order placement
- Payment processing
- Inventory updates
- Any operation involving multiple related queries

> If one query fails, the entire transaction can be rolled back to keep the database consistent.