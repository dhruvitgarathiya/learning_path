# PostgreSQL Functions & Stored Procedures

## Functions

A **Function** is a reusable block of SQL or PL/pgSQL code that performs a task and **returns a value**.

### Why use Functions?

- Reuse business logic
- Reduce duplicate SQL
- Keep queries clean
- Can be called inside `SELECT`

### Create a Function

```sql
CREATE FUNCTION get_bonus(salary NUMERIC)
RETURNS NUMERIC
AS $$
BEGIN
    RETURN salary * 0.10;
END;
$$ LANGUAGE plpgsql;
```

### Call a Function

```sql
SELECT get_bonus(50000);
```

You can also use it in queries:

```sql
SELECT
    name,
    salary,
    get_bonus(salary) AS bonus
FROM employees;
```

---

## Stored Procedures

A **Stored Procedure** is similar to a function, but it is mainly used to perform operations like inserts, updates, or deletes.

Unlike functions, procedures are executed using `CALL`.

### Why use Stored Procedures?

- Execute multiple SQL statements together
- Automate repetitive database tasks
- Handle transactions inside the procedure

### Create a Procedure

```sql
CREATE PROCEDURE increase_salary()
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE employees
    SET salary = salary + 5000;
END;
$$;
```

### Call a Procedure

```sql
CALL increase_salary();
```

---

## Function vs Stored Procedure

| Function | Stored Procedure |
|----------|------------------|
| Returns a value | Usually performs an action |
| Called using `SELECT` | Called using `CALL` |
| Can be used inside SQL queries | Cannot be used inside a `SELECT` |
| Best for calculations | Best for business operations |

---

## When to Use?

### Use a Function when:

- Performing calculations
- Formatting data
- Returning a single value or table
- Reusing logic inside queries

### Use a Stored Procedure when:

- Updating multiple tables
- Running batch operations
- Processing business workflows
- Managing transactions

---

## Quick Example

Function:

```sql
SELECT get_bonus(60000);
```

Procedure:

```sql
CALL increase_salary();
```

> **Tip:** Think of a **Function** as something that **returns information**, while a **Stored Procedure** is something that **performs an action**.