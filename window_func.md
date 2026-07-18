# PostgreSQL Window Functions

## What are Window Functions?

Window functions perform calculations across a set of rows while **keeping each row separate**.

Unlike `GROUP BY`, they **do not combine rows into a single result**.

**Syntax**

```sql
function_name() OVER (
    PARTITION BY ...
    ORDER BY ...
)
```

---

## ROW_NUMBER()

Assigns a unique number to each row.

```sql
SELECT
    name,
    salary,
    ROW_NUMBER() OVER (ORDER BY salary DESC) AS row_num
FROM employees;
```

---

## RANK()

Assigns a rank to each row.

If two rows have the same value, the next rank is skipped.

```sql
SELECT
    name,
    salary,
    RANK() OVER (ORDER BY salary DESC) AS rank
FROM employees;
```

Example:

| Salary | Rank |
|--------:|-----:|
| 90000 | 1 |
| 85000 | 2 |
| 85000 | 2 |
| 80000 | 4 |

---

## DENSE_RANK()

Similar to `RANK()`, but does **not** skip numbers.

```sql
SELECT
    name,
    salary,
    DENSE_RANK() OVER (ORDER BY salary DESC) AS dense_rank
FROM employees;
```

Example:

| Salary | Dense Rank |
|--------:|-----------:|
| 90000 | 1 |
| 85000 | 2 |
| 85000 | 2 |
| 80000 | 3 |

---

## LEAD()

Returns the value from the next row.

```sql
SELECT
    name,
    salary,
    LEAD(salary) OVER (ORDER BY salary) AS next_salary
FROM employees;
```

---

## LAG()

Returns the value from the previous row.

```sql
SELECT
    name,
    salary,
    LAG(salary) OVER (ORDER BY salary) AS previous_salary
FROM employees;
```

---

## PARTITION BY

Divides rows into groups before applying the window function.

```sql
SELECT
    name,
    department,
    salary,
    RANK() OVER (
        PARTITION BY department
        ORDER BY salary DESC
    ) AS dept_rank
FROM employees;
```

Each department gets its own ranking.

---

## Running Total

```sql
SELECT
    id,
    salary,
    SUM(salary) OVER (
        ORDER BY id
    ) AS running_total
FROM employees;
```

---

## Moving Average

```sql
SELECT
    id,
    salary,
    AVG(salary) OVER (
        ORDER BY id
    ) AS moving_avg
FROM employees;
```

---

## Common Window Functions

| Function | Purpose |
|----------|---------|
| ROW_NUMBER() | Unique row number |
| RANK() | Ranking with gaps |
| DENSE_RANK() | Ranking without gaps |
| LEAD() | Next row value |
| LAG() | Previous row value |
| SUM() OVER() | Running total |
| AVG() OVER() | Running average |

---

## When to Use Window Functions?

- Ranking employees by salary
- Finding top N records
- Comparing current and previous rows
- Running totals
- Moving averages
- Department-wise rankings

> **Tip:** If you need calculations across rows **without losing individual rows**, window functions are usually the right choice.