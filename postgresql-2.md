# PostgreSQL Common Queries

## DISTINCT

Returns unique values.

```sql
SELECT DISTINCT department
FROM employees;
```

---

## AS (Alias)

Rename a column or table in the output.

```sql
SELECT name AS employee_name,
       salary AS monthly_salary
FROM employees;
```

---

## IN

Match multiple values.

```sql
SELECT *
FROM employees
WHERE department IN ('IT', 'HR');
```

---

## BETWEEN

Filter values within a range.

```sql
SELECT *
FROM employees
WHERE salary BETWEEN 40000 AND 70000;
```

---

## LIKE

Pattern matching.

```sql
SELECT *
FROM employees
WHERE name LIKE 'A%';
```

Common wildcards:

- `%` → Any number of characters
- `_` → Exactly one character

---

## IS NULL

Find NULL values.

```sql
SELECT *
FROM employees
WHERE manager_id IS NULL;
```

---

## CASE

Conditional logic.

```sql
SELECT
    name,
    salary,
    CASE
        WHEN salary >= 80000 THEN 'High'
        WHEN salary >= 50000 THEN 'Medium'
        ELSE 'Low'
    END AS salary_level
FROM employees;
```

---

## Joins

### INNER JOIN

Returns matching records.

```sql
SELECT e.name, d.department_name
FROM employees e
INNER JOIN departments d
ON e.department_id = d.id;
```

### LEFT JOIN

Returns all records from the left table.

```sql
SELECT e.name, d.department_name
FROM employees e
LEFT JOIN departments d
ON e.department_id = d.id;
```

### RIGHT JOIN

Returns all records from the right table.

```sql
SELECT e.name, d.department_name
FROM employees e
RIGHT JOIN departments d
ON e.department_id = d.id;
```

### FULL JOIN

Returns all matching and non-matching records.

```sql
SELECT *
FROM employees e
FULL JOIN departments d
ON e.department_id = d.id;
```

---

## EXISTS

Checks whether a subquery returns any rows.

```sql
SELECT *
FROM departments d
WHERE EXISTS (
    SELECT 1
    FROM employees e
    WHERE e.department_id = d.id
);
```

---

## ANY

Compare with any value returned by a subquery.

```sql
SELECT *
FROM employees
WHERE salary > ANY (
    SELECT salary
    FROM employees
    WHERE department = 'HR'
);
```

---

## ALL

Compare with all values returned by a subquery.

```sql
SELECT *
FROM employees
WHERE salary > ALL (
    SELECT salary
    FROM employees
    WHERE department = 'HR'
);
```

---

## UNION

Combine results without duplicates.

```sql
SELECT name FROM customers
UNION
SELECT name FROM suppliers;
```

---

## UNION ALL

Combine results including duplicates.

```sql
SELECT name FROM customers
UNION ALL
SELECT name FROM suppliers;
```

---

## INTERSECT

Returns common rows.

```sql
SELECT employee_id FROM developers
INTERSECT
SELECT employee_id FROM testers;
```

---

## EXCEPT

Returns rows from the first query that don't exist in the second.

```sql
SELECT employee_id FROM developers
EXCEPT
SELECT employee_id FROM testers;
```

---

## Subquery

```sql
SELECT *
FROM employees
WHERE salary >
(
    SELECT AVG(salary)
    FROM employees
);
```

---

## Common Table Expression (CTE)

```sql
WITH high_salary AS (
    SELECT *
    FROM employees
    WHERE salary > 60000
)

SELECT *
FROM high_salary;
```

---

## COALESCE

Returns the first non-NULL value.

```sql
SELECT name,
       COALESCE(phone, 'Not Available')
FROM employees;
```

---

## NULLIF

Returns NULL if two values are equal.

```sql
SELECT NULLIF(10, 10);
```

---

## String Concatenation

```sql
SELECT first_name || ' ' || last_name AS full_name
FROM employees;
```

---

## Sorting by Multiple Columns

```sql
SELECT *
FROM employees
ORDER BY department, salary DESC;
```

---

## OFFSET

Skip rows.

```sql
SELECT *
FROM employees
LIMIT 10 OFFSET 20;
```

---

## Pagination

```sql
SELECT *
FROM employees
ORDER BY id
LIMIT 10 OFFSET 0;
```

---

# Useful PostgreSQL Clauses

| Clause | Purpose |
|---------|---------|
| DISTINCT | Remove duplicate rows |
| AS | Rename columns/tables |
| IN | Match multiple values |
| BETWEEN | Range filtering |
| LIKE | Pattern matching |
| IS NULL | Check NULL values |
| CASE | Conditional expressions |
| JOIN | Combine multiple tables |
| EXISTS | Check subquery existence |
| ANY | Compare with any value |
| ALL | Compare with all values |
| UNION | Combine result sets |
| UNION ALL | Combine including duplicates |
| INTERSECT | Common rows |
| EXCEPT | Difference between queries |
| CTE (WITH) | Temporary result set |
| COALESCE | Replace NULL values |
| NULLIF | Return NULL if equal |
| LIMIT | Restrict number of rows |
| OFFSET | Skip rows |