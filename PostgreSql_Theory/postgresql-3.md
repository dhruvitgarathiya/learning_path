# PostgreSQL Common Functions & Operators

## Aggregate Functions

Used to perform calculations on multiple rows.

```sql
SELECT COUNT(*) FROM employees;
SELECT SUM(salary) FROM employees;
SELECT AVG(salary) FROM employees;
SELECT MIN(salary) FROM employees;
SELECT MAX(salary) FROM employees;
```

---

## String Functions

Convert text to uppercase.

```sql
SELECT UPPER(name)
FROM employees;
```

Convert text to lowercase.

```sql
SELECT LOWER(name)
FROM employees;
```

Find string length.

```sql
SELECT LENGTH(name)
FROM employees;
```

Extract part of a string.

```sql
SELECT SUBSTRING(name, 1, 3)
FROM employees;
```

Remove leading and trailing spaces.

```sql
SELECT TRIM('   PostgreSQL   ');
```

Concatenate strings.

```sql
SELECT CONCAT(first_name, ' ', last_name)
FROM employees;
```

---

## Numeric Functions

Round numbers.

```sql
SELECT ROUND(45.678, 2);
```

Round up.

```sql
SELECT CEIL(45.2);
```

Round down.

```sql
SELECT FLOOR(45.9);
```

Absolute value.

```sql
SELECT ABS(-25);
```

Power.

```sql
SELECT POWER(2, 5);
```

---

## Date & Time Functions

Current date.

```sql
SELECT CURRENT_DATE;
```

Current time.

```sql
SELECT CURRENT_TIME;
```

Current timestamp.

```sql
SELECT NOW();
```

Extract year.

```sql
SELECT EXTRACT(YEAR FROM NOW());
```

Add days.

```sql
SELECT CURRENT_DATE + INTERVAL '7 days';
```

Subtract days.

```sql
SELECT CURRENT_DATE - INTERVAL '30 days';
```

---

## Comparison Operators

```sql
=
!=
<>
>
<
>=
<=
```

Example:

```sql
SELECT *
FROM employees
WHERE salary >= 60000;
```

---

## Logical Operators

AND

```sql
SELECT *
FROM employees
WHERE department = 'IT'
AND salary > 50000;
```

OR

```sql
SELECT *
FROM employees
WHERE department = 'IT'
OR department = 'HR';
```

NOT

```sql
SELECT *
FROM employees
WHERE NOT department = 'Finance';
```

---

## Arithmetic Operators

```sql
+
-
*
/
%
```

Example:

```sql
SELECT salary * 12 AS yearly_salary
FROM employees;
```

---

## Aggregate with GROUP BY

```sql
SELECT department,
       COUNT(*),
       AVG(salary)
FROM employees
GROUP BY department;
```

---

## Aggregate with HAVING

```sql
SELECT department,
       AVG(salary)
FROM employees
GROUP BY department
HAVING AVG(salary) > 60000;
```

---

## Sorting

Ascending

```sql
SELECT *
FROM employees
ORDER BY salary ASC;
```

Descending

```sql
SELECT *
FROM employees
ORDER BY salary DESC;
```

Multiple columns

```sql
SELECT *
FROM employees
ORDER BY department, salary DESC;
```

---

## Quick Reference

| Function | Purpose |
|----------|---------|
| COUNT() | Count rows |
| SUM() | Total value |
| AVG() | Average value |
| MIN() | Minimum value |
| MAX() | Maximum value |
| UPPER() | Uppercase text |
| LOWER() | Lowercase text |
| LENGTH() | String length |
| SUBSTRING() | Extract text |
| TRIM() | Remove spaces |
| CONCAT() | Join strings |
| ROUND() | Round number |
| CEIL() | Round up |
| FLOOR() | Round down |
| ABS() | Absolute value |
| POWER() | Exponent |
| NOW() | Current timestamp |
| CURRENT_DATE | Current date |
| CURRENT_TIME | Current time |
| EXTRACT() | Extract date/time part |