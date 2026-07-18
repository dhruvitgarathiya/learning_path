# Database Normalization

## What is Normalization?

Normalization is the proces of organizing data in a database to reduce redundncy (duplicate data) and improve data integrity.

The main idea is simple:

> **Store each piece of information only once and keep related data connected using relationships.**

Instead of copying the same information again and again, we split it into separate tables and connect them using primary keys and foreign keys.

---

## Why Do We Need Normalization?
Imagine a student database like this.

| Student_ID | Student_Name | Course | Faculty | Faculty_Phone |
|------------|--------------|---------|----------|---------------|
| 101 | Dhruvit | DBMS | Rahul | 9876543210 |
| 102 | Amit | DBMS | Rahul | 9876543210 |
| 103 | Priya | DBMS | Rahul | 9876543210 |

Notice something?
The faculty information is repeated for every student.

This causes several problems:
- Wastes storage,Difficult to update,Chances of inconsistent data,Harder to maintain
Suppose Rahul changes his phone number.

Now we have to update every row.
If we miss even one row, the database becomes inconsistent.
Normalization helps solve these kinds of problems.

# Problems Caused by Redundant Data

Normalization mainly tries to eliminate three common anomalies.

## 1. Update Anomaly
Suppose Rahul's phone number changes.
Instead of updating one record, we must update every row.
If one row is missed, different phone numbers exist for the same faculty.
This creates inconsistent data.

## 2. Insert Anomaly
Suppose a new faculty joins the college.
But currently no student is enrolled in their course.
Since our table stores students, we cannot insert the faculty information without creating a fake student record.

## 3. Delete Anomaly
Suppose Priya is the only student enrolled in AI.
If we delete Priya's record,
we also lose the faculty information for AI.
That information disappears completely.
# Goal of Normalization
A normalized database should:

- Minimize duplicate data
- Avoid update anomalies
- Avoid insert anomalies
- Avoid delete anomalies
- Improve consistency
- Make maintenane easier

# Primary Key and Foreign Key

Normalization heavily depends on keys.

## Primary Key
A column (or combination of columns) that uniquely identifies each row.
Example:

```

Student
---------
Student_ID (PK)
Name
Age

```
Every student has a unique Student_ID.

---

## Foreign Key

A column that refers to the primary key of another table.

Example

```

Faculty
--------
Faculty_ID (PK)
Faculty_Name

Student
--------
Student_ID (PK)
Name
Faculty_ID (FK)

```

Now every student simply stores Faculty_ID instead of repeating faculty details.

---

# Normal Forms

Normalization is done in multiple stages called **Normal Forms**.

Each normal form solves a specific problem.
The most commonly used ones are:

- First Normal Form (1NF)
- Second Normal Form (2NF)
- Third Normal Form (3NF)
- Boyce-Codd Normal Form (BCNF)
Most production databases are designed up to **3NF** or **BCNF**.

---

# First Normal Form (1NF)

## Rule

Every column should contain only one value.No multiple values.No arrays.No comma-separated values.

### Bad Example

| Student_ID | Name | Subjects |
|------------|------|----------------|
|101|Dhruvit|DBMS, OS, CN|

The Subjects column stores multiple values.
This violates 1NF.

---

### Correct Version

| Student_ID | Name |
|------------|------|
|101|Dhruvit|

Student_Subject

| Student_ID | Subject |
|------------|----------|
|101|DBMS|
|101|OS|
|101|CN|

Now each cell contains only one value.This satisfies 1NF.

---

# Second Normal Form (2NF)

## Rule

The table must already satisfy 1NF.Also,Every non-key column should depend on the entire primary key.Not just part of it.This mainly applies when the table has a **composite primary key**.

---

### Example

```

Student_Course

----------------------------
Student_ID
Course_ID
Student_Name
Course_Name

Primary Key = (Student_ID, Course_ID)

```

Problems :Student_Name depends only on Student_ID.Course_Name depends only on Course_ID.Neither depends on the complete key.This is called **Partial Dependency**.

---

### Solution

Split into : Student

```

Student_ID
Student_Name

```

Course

```

Course_ID
Course_Name

```

Enrollment

```

Student_ID
Course_ID

```

Now every attribute depends on the full primary key.

---

# Third Normal Form (3NF)

## Rule

The table must satisfy 2NF.Also,Non-key columns should not depend on another non-key column.This is called **Transitive Dependency**.

---

### Example

Employee

| Emp_ID | Emp_Name | Department_ID | Department_Name |
|---------|----------|---------------|-----------------|

Primary Key Emp_ID Department_Name depends on Department_ID, not on Emp_ID directly.This violates 3NF.

---

### Solution

Employee

| Emp_ID | Name | Department_ID |
|---------|------|---------------|

Department

| Department_ID | Department_Name |
|---------------|-----------------|

Now department information exists only once.

---

# BCNF (Boyce-Codd Normal Form)

BCNF is a stricter version of 3NF.

Rule:Every determinant must be a candidate key.In simple words,If one column determines another column,that first column should itself be capable of uniquely identifying rows.BCNF handles a few edge cases that 3NF cannot.In most real-world applications,3NF is enough.BCNF is used only when those special dependency cases exist.

---

# Example of a Fully Normalized Database

Instead of

```

Student
---------------------------------------------
Student_ID
Student_Name
Department_Name
Faculty_Name
Faculty_Phone

```

We create

Student

```

Student_ID
Student_Name
Department_ID

```

Department

```

Department_ID
Department_Name
Faculty_ID

```

Faculty

```

Faculty_ID
Faculty_Name
Phone

```

Relationships

```

Student
    |
    | Department_ID
    ↓
Department
    |
    | Faculty_ID
    ↓
Faculty

```

No duplicate faculty data.Easy to update.Easy to maintain.

---

# Advantages of Normalization

- Reduces duplicate data
- Improves data consistency
- Prevents anomalies
- Saves storage
- Easier maintenance
- Better database design
- Easier updates

---

# Disadvantages

Normalization is not always perfect.Sometimes we intentionally keep duplicate data to improve performance.This process is called **Denormalization**.Highly normalized databases may require:
- More joins
- Slightly slower read queries
- More complex SQL queries
That's why many production systems use a balance between normalization and denormalization.

---

# Quick Revision

| Normal Form | Removes |
|-------------|-------------------------------|
| 1NF | Repeating groups / Multiple values |
| 2NF | Partial dependency |
| 3NF | Transitive dependency |
| BCNF | Special dependency cases |

---

# Easy Way to Remember

**1NF**
> One cell = One value

**2NF**
> Every column depends on the whole primary key.

**3NF**
> Non-key columns should depend only on the primary key.

**BCNF**
> Every determinant must be a candidate key.

---

# Final Thought

Normalization is not just about passing interviews.It is about designing databases that are:
- clean
- maintainable
- scalable
- consistent
A well-normalized schema reduces bugs, simplifies updates, and makes the database easier to work with as the application grows.
