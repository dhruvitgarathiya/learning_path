# PostgreSQL Triggers & Cursors

## Triggers

A **Trigger** is a piece of code that automatically executes when a specific event occurs on a table.

Events can be:

- INSERT
- UPDATE
- DELETE

### Why use Triggers?

- Automatically maintain audit logs
- Validate data
- Track changes
- Update related tables automatically

---

## Trigger Workflow

```
Event Occurs
      │
      ▼
Trigger Fires
      │
      ▼
Trigger Function Executes
```

---

## Create a Trigger Function

```sql
CREATE FUNCTION log_employee_update()
RETURNS TRIGGER
AS $$
BEGIN
    INSERT INTO employee_logs(employee_id, updated_at)
    VALUES (NEW.id, NOW());

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
```

---

## Create a Trigger

```sql
CREATE TRIGGER employee_update_trigger
AFTER UPDATE
ON employees
FOR EACH ROW
EXECUTE FUNCTION log_employee_update();
```

Now whenever an employee record is updated, a log entry is automatically created.

---

## Trigger Types

| Type | When it Executes |
|------|------------------|
| BEFORE INSERT | Before inserting data |
| AFTER INSERT | After inserting data |
| BEFORE UPDATE | Before updating data |
| AFTER UPDATE | After updating data |
| BEFORE DELETE | Before deleting data |
| AFTER DELETE | After deleting data |

---

# Cursors

A **Cursor** allows you to process query results **one row at a time**.

Normally, PostgreSQL returns all rows together. A cursor is useful when you need to handle records individually.

---

## Why use Cursors?

- Process large datasets
- Perform row-by-row operations
- Execute complex business logic

---

## Cursor Example

```sql
DECLARE emp_cursor CURSOR FOR
SELECT name, salary
FROM employees;
```

Open the cursor.

```sql
OPEN emp_cursor;
```

Fetch one row.

```sql
FETCH NEXT FROM emp_cursor;
```

Close the cursor.

```sql
CLOSE emp_cursor;
```

---

## Cursor Lifecycle

```
DECLARE
   ↓
OPEN
   ↓
FETCH
   ↓
CLOSE
```

---

## Trigger vs Cursor

| Trigger | Cursor |
|---------|--------|
| Runs automatically | Controlled manually |
| Executes on INSERT, UPDATE, DELETE | Reads query results row by row |
| Used for automation | Used for sequential processing |
| Event-driven | Query-driven |

---

## When to Use?

### Triggers

- Audit logging
- Automatically update timestamps
- Data validation
- Synchronize related tables

### Cursors

- Processing large reports
- Batch data processing
- Row-by-row calculations
- Complex procedural logic

> **Tip:** In PostgreSQL, prefer set-based SQL queries whenever possible. Use **cursors** only when row-by-row processing is truly required, and use **triggers** only for logic that should happen automatically after a database event.