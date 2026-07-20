# PostgreSQL JSONB, UUID & EXPLAIN

## JSON vs JSONB

PostgreSQL supports two JSON data types:

- **JSON** → Stores data as plain text.
- **JSONB** → Stores data in a binary format, making it faster to search and index.

In most applications, **JSONB** is the preferred choice.

---

## Create a JSONB Column

```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    profile JSONB
);
```

Insert data:

```sql
INSERT INTO users (profile)
VALUES (
    '{"name":"Dhruvit","age":22,"city":"Rajkot"}'
);
```

---

## Query JSONB Data

Access a value:

```sql
SELECT profile->>'name'
FROM users;
```

Filter using a JSON field:

```sql
SELECT *
FROM users
WHERE profile->>'city' = 'Rajkot';
```

---

## Why Use JSONB?

- Store flexible or dynamic data
- No need to create new columns for every field
- Supports indexing
- Faster searching than JSON

---

# UUID

A **UUID (Universally Unique Identifier)** is a 128-bit unique value used as an identifier instead of an integer ID.

Example:

```
550e8400-e29b-41d4-a716-446655440000
```

---

## Enable UUID Extension

```sql
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
```

---

## Create a UUID Column

```sql
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100)
);
```

Now PostgreSQL automatically generates a unique ID for each row.

---

## Why Use UUID?

- Globally unique IDs
- Better for distributed systems
- Harder to guess than sequential IDs
- Useful for public APIs

---

# EXPLAIN

`EXPLAIN` shows how PostgreSQL plans to execute a query.

It helps identify slow queries and understand whether indexes are being used.

Example:

```sql
EXPLAIN
SELECT *
FROM employees
WHERE department = 'IT';
```

---

## EXPLAIN ANALYZE

Runs the query and shows the **actual execution plan** along with execution time.

```sql
EXPLAIN ANALYZE
SELECT *
FROM employees
WHERE salary > 60000;
```

Use this when optimizing query performance.

---

## When to Use EXPLAIN?

- Slow queries
- Check if indexes are being used
- Compare different query approaches
- Optimize database performance

---

## Quick Summary

| Feature | Purpose |
|---------|---------|
| JSON | Store JSON as text |
| JSONB | Store JSON in binary format (recommended) |
| UUID | Generate globally unique IDs |
| EXPLAIN | Show query execution plan |
| EXPLAIN ANALYZE | Execute query and measure performance |

> For most PostgreSQL projects, use **JSONB** over **JSON**, **UUID** when globally unique IDs are needed, and **EXPLAIN ANALYZE** whenever you're tuning slow queries.