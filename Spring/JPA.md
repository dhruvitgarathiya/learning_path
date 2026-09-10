## what does @Entity is used for in spring boot

In Spring Boot (specifically when using Spring Data JPA and Hibernate), @Entity is a JPA (Jakarta Persistence API) annotation used to mark a Java class as a database table.When you place @Entity above a class, you are telling the framework: "This class represents a table in the database, and every instance (object) of this class represents a row in that table."

Key Responsibilities of an @Entity

Object-Relational Mapping (ORM): It bridges the gap between object-oriented Java code and relational databases (like MySQL, PostgreSQL, or H2). Instead of writing complex SQL queries to create tables, Hibernate looks at your @Entity class and generates the table for you automatically.

State Management: The JPA Persistence Context tracks changes made to an entity object. If you change a value on an entity instance inside a transaction, those changes are automatically saved to the database.


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;


```java
@Entity // 1. Tells Spring Boot this class maps to a database table
public class Product {

    @Id // 2. Marks this field as the Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 3. Enables auto-increment (1, 2, 3...)
    private Long id;

    @Column(name = "product_name", nullable = false) // 4. Customizes the database column settings
    private String name;

    private Double price; // Maps automatically to a column named "price"

    // Standard no-argument constructor (Required by JPA)
    public Product() {}

    // Getters and Setters...
}
```

What happens behind the scenes?

Based on the code above, Hibernate will execute a SQL statement behind the scenes that looks like this:

```sql
CREATE TABLE product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    price DOUBLE
);
```

Crucial Rules for Using @Entity

To make sure your @Entity class works correctly with Spring Boot, it must follow these rules:

Must have a no-argument constructor: JPA requires a public or protected constructor with no arguments so it can instantiate the object dynamically when fetching data from the database.

Must have a Primary Key: You must annotate at least one field with @Id to uniquely identify each row.

Cannot be final: The class, its methods, and its persistent instance variables cannot be declared final because Hibernate needs to create runtime dynamic proxies around your entity.





## what does dto means in spring boot

In Spring Boot, DTO stands for Data Transfer Object. It is a design pattern used to package and transfer data between different layers of an application—most commonly between the Controller (API) layer and the Service layer.

A DTO is essentially a plain old Java object (POJO) or a Java Record that contains only data fields, constructors, getters, and setters. It contains zero business logic

Why Should You Use DTOs?

Security & Privacy: If your database User entity has fields like id, passwordHash, and creationDate, you shouldn't return the raw entity via a REST API. A UserResponseDTO allows you to send back only the username and email.

Performance Optimization: Instead of making multiple API calls to fetch different pieces of data, a DTO allows you to bundle related information together (e.g., combining User and Location data) and send it over the network in a single response payload. 

Decoupling / Maintainability: If you change your database schema tomorrow, you don't want your frontend or mobile app APIs to break. DTOs act as a buffer, ensuring your API contracts remain stable regardless of internal database changes

Validation: You can easily apply Spring validation annotations (like @NotBlank, @Email, or @Min) directly onto your DTO fields to validate incoming client requests before they ever touch your database logic


1. The Database Entity (Hidden from the client)

```java
@Entity
public class User {
    @Id 
    private Long id;
    private String name;
    private String email;
    private String passwordHash; // Highly sensitive!
    
    // Getters and setters...
}
```

2. The DTO (Safe to share with the client)

```java
// Notice the passwordHash is missing from this contract
public record UserDTO(Long id, String name, String email) {}

```

3. The Controller Layer

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id){
        // service fetches the entity but returns it transformed

        UserDTO userDTO = userService.getUserById(id);
        return ResponseEntity.ok(userDTO);
    }
}
```


spring data jpa - libeary by which we can do database operation

jpa is interface - like set of rules which define how spring talks to database like entitiys and entity manager and id

hibernate is implementation of jpa , and it generates sql queries

hiberanate is one of the implementation 

spring default uses this

why: Hibernate is the undisputed industry standard, offering unmatched maturity, a massive ecosystem, and feature richness compared to alternatives like EclipseLink or OpenJPA

hibernate orm mapping

jpql convertion also done by him

like jpa is goverment body , hibernate is translator hired by jpa

spring data jpa is an abstraction layer on top of jpa to reduce the bpilerplte code required to implement data access object



JPQL — flow + examples, industry style.

Flow (how JPQL work):

Write query string, use entity names + fields (not table/column names) — JPQL query against object model, not db schema.
Spring Data JPA / Hibernate parse JPQL → translate into native SQL for your db dialect.
Execute, get back entity objects (or DTOs), not raw rows.

Two ways use JPQL in Spring Data JPA:

1. @Query annotation on repository method (most common industry approach)

```java
public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query("SELECT p FROM Patient p WHERE p.disease = :disease")
    List<Patient> findPatientsByDisease(@Param("disease") String disease);

    @Query("SELECT p FROM Patient p WHERE p.age > :age AND p.disease = :disease")
    List<Patient> findByAgeAndDisease(@Param("age") int age, @Param("disease") String disease);

    @Query("SELECT p.name FROM Patient p WHERE p.age > :age")
    List<String> findNamesOfPatientsOlderThan(@Param("age") int age);

    @Query("SELECT COUNT(p) FROM Patient p WHERE p.disease = :disease")
    long countByDisease(@Param("disease") String disease);

    @Modifying
    @Transactional
    @Query("UPDATE Patient p SET p.disease = :newDisease WHERE p.id = :id")
    void updateDisease(@Param("id") Long id, @Param("newDisease") String newDisease);

    @Modifying
    @Transactional
    @Query("DELETE FROM Patient p WHERE p.disease = :disease")
    void deleteByDiseaseName(@Param("disease") String disease);
}
```

Explain:

FROM Patient p — Patient is entity class name, not table name. p alias.
p.disease, p.age — Java field names on entity, not db column names.
:disease, :age — named parameters, bind via @Param("disease") on method arg. Safer than string concat — prevent SQL injection, same as JDBC PreparedStatement.
@Modifying — mandatory for UPDATE/DELETE queries (tell Spring this isn't a SELECT, changes data).
@Transactional — needed alongside @Modifying, since update/delete must run inside transaction.
Projection (SELECT p.name) — pull single field, get List<String> back instead of full entity — lighter query, less data transfer.

2. Named parameters vs positional (?1, ?2)

Industry standard: named params (:disease) over positional (?1). Readable, order-independent, less error-prone when many params.

// avoid this style:
@Query("SELECT p FROM Patient p WHERE p.disease = ?1")
List<Patient> findByDisease(String disease);


3. JOIN example (once you add relationships, e.g. Patient → Appointment)

java
@Query("SELECT p FROM Patient p JOIN p.appointments a WHERE a.doctorName = :doctorName")
List<Patient> findPatientsByDoctor(@Param("doctorName") String doctorName);

4. DTO projection (very common in real projects — avoid loading full entity)

java
@Query("SELECT new com.hospital.dto.PatientSummaryDTO(p.name, p.age, p.disease) FROM Patient p WHERE p.age > :age")
List<PatientSummaryDTO> findPatientSummaries(@Param("age") int age);


Need PatientSummaryDTO constructor matching field order exactly. Common pattern for API response — don't expose full entity, don't load unneeded fields.

Where JPQL vs derived query method (findByDisease style, no @Query) — industry preference:

Simple, single-field lookups → derived method name fine, less code.
Anything with joins, multiple conditions, projections, custom logic → @Query JPQL, more control and readability.
Very complex dynamic queries (filters vary at runtime) → Specification / Criteria API (later topic if want).

Want next: native SQL query (@Query(nativeQuery = true)) vs JPQL — when use which, or move to relationships (@OneToMany etc) next?  




DTO Projection — full flow.

Problem it solve:

Default JPA query return full entity (Patient object) — every column, every field, even ones you don't need. Real project, API endpoint often need only 3-4 fields (e.g. name, age, disease) — not full entity with 15 columns, lazy relations, audit fields etc. Loading full entity when unneeded:

Extra memory, extra db columns fetched — waste.
Risk expose sensitive fields (e.g. password hash, internal notes) if entity accidentally serialized to JSON.
Entity object tightly couple db schema to API response — bad practice, schema change break API contract.
N+1 query problem worse — loading full entity graph, related collections trigger lazy load.

DTO projection = fetch only needed fields, map directly into lightweight object (DTO — Data Transfer Object), skip loading full entity.

Flow:

Create DTO class (plain Java class, no @Entity, no JPA annotation needed).
Write JPQL SELECT new <fully.qualified.DTOClassName>(fields...) FROM Entity e WHERE ...
Repository method return List<DTO> (or single DTO) directly — not List<Patient>.
Hibernate execute query, construct DTO objects directly from result set — skip building full entity.

```java
package com.hospital.dto;

public class PatientSummaryDTO {

    private String name;
    private int age;
    private String disease;

    public PatientSummaryDTO(String name, int age, String disease) {
        this.name = name;
        this.age = age;
        this.disease = disease;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public String getDisease() { return disease; }
}
```


Note: constructor param order and types must match SELECT clause exactly — Hibernate call this constructor directly, reflection-based match by position, not name.

Step 2 — Repository method with JPQL constructor expression

java
public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query("SELECT new com.hospital.dto.PatientSummaryDTO(p.name, p.age, p.disease) FROM Patient p WHERE p.age > :age")
    List<PatientSummaryDTO> findPatientSummaries(@Param("age") int age);
}

Explain:

new com.hospital.dto.PatientSummaryDTO(...) — this is JPQL constructor expression syntax, full package path mandatory (Hibernate need it resolve class via reflection at runtime).
(p.name, p.age, p.disease) — pick exact fields, order match DTO constructor exactly.
Only these 3 columns fetched from db (SQL generated: SELECT name, age, disease FROM patients WHERE age > ?) — not SELECT *.


Alternative — interface-based projection (Spring Data specific, no JPQL needed)

Simpler variant, industry also use often:

```java
public interface PatientNameOnly {
    String getName();
    int getAge();
}
```
```java
List<PatientNameOnly> findByDisease(String disease);
```
Spring Data auto-generate proxy implementing this interface, populate getters from query result — no manual constructor, no new expression. Less boilerplate than DTO class, but less flexible (no custom logic in constructor, can't easily combine/transform fields).

When use which — industry practice:

Interface projection — quick, simple field subset, no transformation needed.
Class-based DTO (constructor expression) — need combine fields, format data, add computed field, or DTO shared across layers (service, controller) as proper typed object — more common in larger real projects, cleaner architecture (DTO layer separate from persistence layer).

Behind scenes: Hibernate skip creating full Patient managed entity — DTO objects not managed by persistence context (no dirty checking, no lazy loading triggered, not tracked). Pure read-only data carrier — exactly what API response needs.


Entity — persistence context managed

Entity object lifecycle tied to EntityManager's persistence context (first-level cache, session-scoped).

Lifecycle states:

Transient — plain new Patient(), not yet saved, no db row, not tracked.
Managed (Persistent) — after save()/persist(), or after find()/JPQL fetch full entity — object now tracked inside persistence context. EntityManager watch this object.
Detached — persistence context closed (transaction end, session close) — object still exist in memory, but no longer tracked.
Removed — after delete(), marked for deletion, gone on flush/commit.

Why matter: managed entity get dirty checking. Change field value (patient.setDisease("Flu")) inside active transaction — no explicit save() call needed. Hibernate compare snapshot at flush time, auto-generate UPDATE SQL if difference found. This automatic sync = core feature persistence context give you.

Also: managed entity trigger lazy loading — access @OneToMany collection field, Hibernate fire additional query fetch related data, only works while still inside persistence context (session open). Access after detach → LazyInitializationException.

Persistence context also give identity map — same row fetched twice inside same session return same Java object reference (not two separate copies) — prevent inconsistency.

DTO — plain POJO, no lifecycle

DTO just plain Java object. Constructor called (via new expression in JPQL, or manually in code), fields populated, done. That's it.

Not tracked by EntityManager.
Not registered in persistence context.
No dirty checking — change field on DTO, nothing happen, no auto UPDATE, no sync to db ever.
No lazy loading — DTO has no proxy fields, all data flat, already fetched, no further db call possible from it.
No states (transient/managed/detached/removed) — just exist in heap like any regular object, garbage collected normal way when no reference left.

So DTO = disconnected snapshot, pure data holder, no magic behind it. Entity = live object wired into ORM machinery.

Practical implication:

Modify entity inside @Transactional method → change persist automatic. Modify DTO → nothing happen unless you write explicit code to map DTO change back to entity and save.
Return entity directly from @RestController (bad practice) risk exposing lazy fields that throw exception once outside session, or expose unwanted columns. Return DTO — safe, flat, no persistence context tie, no exception risk, no accidental over-exposure.
DTO good for crossing layer boundary (service → controller → JSON response) — decoupled from db lifecycle entirely.

Pagination — full flow.

Problem it solve: fetch huge table (e.g. 10,000 patients) — loading all at once = bad, slow, waste memory. Pagination = fetch in chunks (pages), e.g. 20 records at a time.

Core pieces:

Pageable — interface, carry page number, page size, sort info.
PageRequest — concrete implementation, create pageable object: PageRequest.of(pageNumber, pageSize).
Page<T> — result wrapper, hold data + metadata (total elements, total pages, has next, etc).
Slice<T> — lighter version, no total count (no extra COUNT query) — knows only if next page exist or not.

1. Repository method — Spring Data auto-handle pagination

```java
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Page<Patient> findByDisease(String disease, Pageable pageable);
}
```
No JPQL needed here — just add Pageable param last, Spring Data auto-generate paginated query underneath.

2. Using it

```java
Pageable pageable = PageRequest.of(0, 10); // page 0 (first page), 10 records per page
Page<Patient> page = patientRepository.findByDisease("Diabetes", pageable);

List<Patient> patients = page.getContent();       // actual data, this page only
long totalElements = page.getTotalElements();      // total matching rows across all pages
int totalPages = page.getTotalPages();              // total pages count
boolean hasNext = page.hasNext();
int currentPage = page.getNumber();
```
3. With sorting combined

```java
Pageable pageable = PageRequest.of(0, 10, Sort.by("age").descending());
Page<Patient> page = patientRepository.findByDisease("Diabetes", pageable);
```
4. Explicit JPQL with pagination (manual @Query + Pageable)

```java
@Query("SELECT p FROM Patient p WHERE p.age > :age")
Page<Patient> findByAgeGreaterThan(@Param("age") int age, Pageable pageable);
```
Behind scenes, Spring Data auto-append LIMIT/OFFSET (Postgres) based on pageable values, plus run separate COUNT query get totalElements — two queries total: one fetch data, one count.

5. Custom count query (optimization — sometimes default COUNT slow on complex joins)

```java
@Query(value = "SELECT p FROM Patient p WHERE p.age > :age",
       countQuery = "SELECT COUNT(p) FROM Patient p WHERE p.age > :age")
Page<Patient> findByAgeGreaterThan(@Param("age") int age, Pageable pageable);
```
6. Slice instead of Page — when total count not needed

java
Slice<Patient> findByDisease(String disease, Pageable pageable);

Slice only fetch pageSize + 1 records, check if extra one exist — decide hasNext() — skip expensive COUNT query entirely. Use for infinite-scroll style UI, don't need total pages shown.

7. Test example

```java
@Test
void testPagination() {
    Pageable pageable = PageRequest.of(0, 2);
    Page<Patient> page = patientRepository.findByDisease("Diabetes", pageable);

    assertEquals(2, page.getContent().size());
    System.out.println("Total pages: " + page.getTotalPages());
    System.out.println("Total elements: " + page.getTotalElements());
}
```
8. Controller layer typical usage (real project pattern)

```java
@GetMapping("/patients")
public Page<Patient> getPatients(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
    return patientRepository.findAll(PageRequest.of(page, size));
}
```
Client call /patients?page=1&size=10 — control pagination via query params directly.

Key point summary:

Page number 0-indexed (first page = 0, not 1) — common bug source, watch this.
Page = extra COUNT query cost, but full pagination UI info.
Slice = cheaper, less info, good for scroll-based UI.
Works automatic with derived query methods AND custom JPQL — just add Pageable param last position.


Relationships — core concept first, then each type.

Owning side vs inverse side — core idea

In db, relationship represented by foreign key (FK) column sit on ONE table only. Whichever entity's table hold that FK column = owning side. Other entity = inverse side (also call "mapped by" side).

Owning side responsible actually write FK value to db (INSERT/UPDATE). Inverse side just read-only mirror, use mappedBy attribute point back to owning side field.

How decide who own — simple rule:

Ask: "which table has the foreign key column?" That entity = owning side. Not about business logic, not about "who feel more important" — pure db schema question. FK placement decide ownership, always.

1. @ManyToOne / @OneToMany (most common)

Example: many Patient belong one Doctor.

FK (doctor_id) sit on patients table (many side always hold FK — makes sense, one doctor can't store list of patient ids in single column).
So @ManyToOne side always owning side (Patient owns relationship, has FK column).
@OneToMany side (Doctor) always inverse, use mappedBy.
```java
@Entity
public class Patient {
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
}

@Entity
public class Doctor {
    @OneToMany(mappedBy = "doctor")
    private List<Patient> patients;
}
```
Rule of thumb: "Many" side = owning side, always. No decision needed here, structurally forced.

@JoinColumn(name = "doctor_id") — only on owning side, tell Hibernate exact FK column name in patients table.
mappedBy = "doctor" — on inverse side, point to field name (doctor) in Patient class that own relationship — tell Hibernate "don't create separate FK for me, look at that other field."

2. @OneToOne

Example: one Patient has one MedicalRecord.

Both side technically "one" — decide ownership by: which table make more sense hold FK, based on which side "depend" on other, or which queried more often standalone.

Common pattern: child/dependent entity own relationship.

```java
@Entity
public class MedicalRecord {
    @OneToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
}

@Entity
public class Patient {
    @OneToOne(mappedBy = "patient")
    private MedicalRecord medicalRecord;
}
```
Here MedicalRecord own (has patient_id FK column in its table) — reasoning: record depend on patient existing, record die if patient die, natural direction.

Thinking process for OneToOne: "if I delete row X, does row Y's existence make sense without X?" Whichever depend more = usually hold FK, becomes owning side. Practically, arbitrary sometimes — team convention decide, no strict forced rule like ManyToOne case.

3. @ManyToMany

Example: many Patient can have many Doctor (patient sees multiple doctors, doctor sees multiple patients).

No single FK fit either table — need join table (junction table) hold pairs of FK (patient_id, doctor_id).

```java
@Entity
public class Patient {
    @ManyToMany
    @JoinTable(
        name = "patient_doctor",
        joinColumns = @JoinColumn(name = "patient_id"),
        inverseJoinColumns = @JoinColumn(name = "doctor_id")
    )
    private List<Doctor> doctors;
}

@Entity
public class Doctor {
    @ManyToMany(mappedBy = "doctors")
    private List<Patient> patients;
}
```
Ownership here arbitrary choice (both side symmetric, no natural FK holder) — whichever entity has @JoinTable annotation = owning side, decide it. Common practice: pick side that make more logical sense as "initiator" — e.g. Patient "chooses" doctors → Patient side own, but purely convention, not forced by schema.

@JoinTable — only on owning side, define join table name + both FK column names (joinColumns = this entity's FK, inverseJoinColumns = other entity's FK).
mappedBy — inverse side, again just point back.

Summary — decision thinking:

@ManyToOne/@OneToMany pair → "many" side forced owning, no choice.
@OneToOne → owning side = whichever entity logically depend on / belongs to other, holds FK, team convention if unclear.
@ManyToMany → owning side arbitrary, whichever entity get @JoinTable annotation, other get mappedBy.

General mental rule across all: whoever holds FK column (or defines join table) = owning side = responsible actually persist relationship changes to db. Inverse side (mappedBy) purely for object navigation convenience, changes made only on inverse side won't get saved unless owning side also updated.

Bidirectional consistency — why matter.

Core problem:

In-memory Java objects and db not automatically sync just because you set field on one side. Only owning side change actually written to db (has FK, Hibernate flush that). Inverse side (mappedBy) purely for reading/navigation — Hibernate ignore changes made only there when deciding what SQL to run.

If set relationship only on inverse side, or only on owning side, without updating BOTH — in-memory object graph become inconsistent (Java objects say one thing, don't match each other) even though db state fine.

Example show problem:

```java
Doctor doctor = doctorRepository.findById(1L).get();
Patient patient = new Patient();
patient.setName("Ravi");
```
doctor.getPatients().add(patient); // set only on inverse side (Doctor.patients)

doctorRepository.save(doctor);

Here Doctor.patients inverse side (mappedBy = "doctor" back in Patient). Adding to this list — Hibernate doesn't care, won't generate FK update, because inverse side change ignored for persistence. patient.doctor field still null. Save this patient separately → doctor_id column stay NULL in db. Relationship silently NOT saved, no error thrown, confusing bug.

Correct way — set both side:

```java
Doctor doctor = doctorRepository.findById(1L).get();
Patient patient = new Patient();
patient.setName("Ravi");

patient.setDoctor(doctor);        // owning side — actually persist FK
doctor.getPatients().add(patient); // inverse side — keep in-memory object graph correct

patientRepository.save(patient);
```
Now db correct (owning side set) AND if you access doctor.getPatients() later in same session/transaction, list reflect this new patient too — no separate re-fetch needed, no stale/incorrect in-memory state.

Why this matter practically:

Correctness of saved data — forget owning side, relationship not persisted, silent data bug, hard catch in testing (db might look fine on schema level, just FK null).
In-memory graph consistency within same transaction — if code later, same transaction, do doctor.getPatients().size() expect updated count — if inverse side not maintained, get stale/wrong count (persistence context might not auto-refresh collection unless explicitly reload).
Avoid confusing bugs in tests — write test, save() entity, immediately assert relationship exists via navigating from other side — fail unexpectedly if only one side set, look like Hibernate broken when actually just inconsistent code.

Common practice — helper method encapsulate both-side update, avoid forgetting:

```java
// inside Doctor entity
public void addPatient(Patient patient) {
    patients.add(patient);
    patient.setDoctor(this);
}

public void removePatient(Patient patient) {
    patients.remove(patient);
    patient.setDoctor(null);
}
```
Usage:

java
doctor.addPatient(patient);
patientRepository.save(patient);

One method call, both side guaranteed sync — standard pattern industry use for bidirectional relationships, avoid scattering manual dual-set logic across service methods, reduce bug risk.

Rule to remember: owning side = what actually get saved to db (never skip); inverse side = what keep Java object graph logically correct for current session (don't skip either, or in-memory state lie to you).

Data domain (relational/db thinking)

Focus: tables, columns, rows, foreign keys, normalization, constraints, indexes. Pure db-level structure, no object concept — just data storage/integrity concern.

Thinking mode: "How store this efficiently, avoid redundancy, enforce constraints (NOT NULL, UNIQUE, FK), what indexes speed up lookup, what normal form fit schema."

Example — data domain view of Patient/Doctor:

```sql
CREATE TABLE doctors (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE patients (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100),
    doctor_id BIGINT REFERENCES doctors(id)
);
```
Concern here: FK constraint, column type, table normalization — nothing about behavior, nothing object-oriented.

JPA domain (object/entity thinking)

Focus: Java objects, relationships between objects (navigable, bidirectional), object identity, lifecycle (managed/detached), sometimes business behavior attached to entity itself.

Thinking mode: "How model this as objects that relate to each other, how do I navigate from Patient to Doctor and back, what lifecycle/cascade rules apply when I save/delete."

Example — same relationship, JPA domain view:

```java
@Entity
public class Patient {
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
}

@Entity
public class Doctor {
    @OneToMany(mappedBy = "doctor")
    private List<Patient> patients;
}
```
Concern here: navigability (patient.getDoctor()), object graph, cascade behavior, lazy/eager loading — data domain (raw SQL) doesn't have any of this, it's pure JPA/ORM layer concept sit ON TOP of data domain.

Practical difference summary:

Data domain	JPA domain
Table, column, row	Class, field, object
FK constraint	@ManyToOne/@OneToMany relationship
Query = SQL	Query = JPQL (object-based)
No identity beyond PK value	Object identity + persistence context tracking
No behavior, pure storage	Can have methods, business logic (though best practice keep entity mostly data + minimal behavior)

(table didn't render as fragment tool disabled — treat as plain comparison above.)

How implement/think each in Spring Boot, practically:

Data domain layer — schema itself (data.sql, migration scripts like Flyway/Liquibase), db constraints, indexes — you design this thinking pure relational correctness, independent of Java code.
JPA domain layer — entity classes (@Entity), map onto that schema, add object-relationship annotations, this is what your Java code interact with day to day — repository, service layer work with JPA domain, not raw SQL/tables directly.
Bridge between two — @Table, @Column, @JoinColumn annotations literally connect JPA domain (Java class/field) to data domain (table/column) — this mapping layer is core of what JPA/Hibernate do.

Bigger picture — often third layer added too (real project pattern):

Domain/DTO layer — separate from JPA entity — plain objects represent business concept for API/service boundary (what earlier covered: DTO projection). This decouple business logic from persistence detail entirely — DTO doesn't care about FK, table, JPA annotation at all.

So full picture, real project:

Data domain (db schema/tables)
     ↕ (JPA mapping annotations)
JPA domain (@Entity classes — Patient, Doctor)
     ↕ (mapping/conversion, e.g. manual or MapStruct)
DTO/domain layer (PatientSummaryDTO, business logic layer)
     ↕
Controller/API layer (JSON response)

Why keep separate — change db schema (e.g. rename column) shouldn't force API contract change; change API response shape shouldn't force db migration. Each layer isolated, own reason to change — cleaner architecture, easier maintain long term, matches good separation-of-concerns industry practice.

N+1 problem — core issue + fix.

What happen:

Fetch list of parent entities (1 query), then for each parent, lazy-load related child collection separately (N more queries) — total N+1 queries instead of 1 or 2 optimized query.

Example show problem:

```java
List<Doctor> doctors = doctorRepository.findAll(); // 1 query — fetch all doctors
```
for (Doctor doctor : doctors) {
    System.out.println(doctor.getPatients().size()); // N queries — one per doctor, lazy load
}

If Doctor.patients marked @OneToMany(fetch = FetchType.LAZY) (default for @OneToMany) — first query fetch doctors only, no patients loaded yet (proxy placeholder). Loop access getPatients() — each iteration trigger separate SELECT * FROM patients WHERE doctor_id = ? query. 10 doctors = 1 + 10 = 11 queries total. Scale badly — 1000 doctors = 1001 queries, massive perf hit.

Why happen: lazy loading design intentional (avoid loading unneeded data upfront) — but naive access pattern in loop trigger repeated round-trip to db, no batching by default.

Solutions:

1. JOIN FETCH in JPQL (most common fix)

```java
@Query("SELECT DISTINCT d FROM Doctor d JOIN FETCH d.patients")
List<Doctor> findAllWithPatients();
```
Single query, SQL JOIN under hood, fetch doctors + patients together in one round-trip. DISTINCT needed avoid duplicate Doctor rows (JOIN produce one row per doctor-patient pair, dedupe at Java level).

2. @EntityGraph (Spring Data specific, cleaner alternative)

```java
@EntityGraph(attributePaths = {"patients"})
@Query("SELECT d FROM Doctor d")
List<Doctor> findAllWithPatients();
```
or simpler, on derived method directly:

```java
@EntityGraph(attributePaths = {"patients"})
List<Doctor> findAll();
```
Tell Hibernate "fetch this relationship eagerly for this specific query" — without changing global fetch type on entity (keep LAZY as default elsewhere, override just for this query).

3. FetchType.EAGER on entity (avoid this generally, bad practice)

java
@OneToMany(mappedBy = "doctor", fetch = FetchType.EAGER)
private List<Patient> patients;

Fix N+1 for this specific access pattern, but EAGER apply globally, every query fetch this collection always — even when not needed — waste unnecessary. Industry generally avoid, prefer LAZY + JOIN FETCH/@EntityGraph per-query basis instead — more control.

4. Batch fetching (@BatchSize, alternative middle-ground)

```java
@OneToMany(mappedBy = "doctor")
@BatchSize(size = 10)
private List<Patient> patients;
```
Doesn't eliminate N+1 fully, but batch lazy loads together — instead N separate queries, group into fewer queries (e.g. WHERE doctor_id IN (?, ?, ?...) batches of 10). Reduce query count significantly, not as optimal as JOIN FETCH but less invasive change.

5. DTO projection (also solve indirectly)

Fetch flat DTO with JOIN directly (covered earlier) — bypass entity relationship loading entirely, no lazy proxy involved, no N+1 possible since you're not navigating object graph at all.

```java
@Query("SELECT new com.hospital.dto.DoctorPatientDTO(d.name, p.name) FROM Doctor d JOIN d.patients p")
List<DoctorPatientDTO> findDoctorPatientPairs();
```
Detecting N+1 in practice:

Set spring.jpa.show-sql=true (you already have this) + watch console — if see repeated similar SELECT statements in loop pattern, that's N+1 happening. Better tool: Hibernate statistics or libraries like datasource-proxy/p6spy count actual query executions in tests — catch N+1 before it hit prod.

Industry go-to fix order: JOIN FETCH for simple full-entity fetch needs, @EntityGraph when want reusable clean annotation-based control, DTO projection when only need subset fields anyway (best perf, avoid whole problem).

Cascade types + orphanRemoval — full explain.

Problem it solve: without cascade, saving/deleting parent entity doesn't automatically save/delete related child entities — need manually persist/remove each child separately, tedious, error-prone. Cascade = tell JPA "propagate this operation from parent to child automatically."

Cascade types (from jakarta.persistence.CascadeType):

```java
@Entity
public class Doctor {
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<Patient> patients;
}
```
Types:

PERSIST — save parent → auto save all child too (child INSERT triggered along with parent INSERT).
MERGE — update parent (detached entity merge back) → auto merge child changes too.
REMOVE — delete parent → auto delete all child rows too. Careful — powerful, can cause unintended mass-delete if misused.
REFRESH — refresh parent from db → auto refresh child from db too (discard in-memory unsaved child changes, reload from source of truth).
DETACH — detach parent from persistence context → auto detach child too (child no longer tracked either).
ALL — shorthand for all five above combined.

Example flow, CascadeType.PERSIST:

```java
Doctor doctor = new Doctor();
doctor.setName("Dr. Mehta");

Patient p1 = new Patient();
p1.setName("Ravi");
p1.setDoctor(doctor);

doctor.getPatients().add(p1);

doctorRepository.save(doctor); // saving doctor auto-saves p1 too, because cascade=PERSIST
```
Without cascade — p1 won't save just by saving doctor, need separate patientRepository.save(p1) call manually.

Where use cascade — practical rule:

Use cascade when child entity's lifecycle fully depend on parent — child make no sense exist without parent (e.g. MedicalRecord depend on Patient, OrderItem depend on Order). Don't cascade when child independent entity that just happen to reference parent (e.g. Doctor shouldn't cascade-delete Patient — patient still exist, record still relevant, even if doctor leave).

orphanRemoval — separate but related concept:

```java
@OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Patient> patients;
```
Problem it solve: what happen when you REMOVE child from parent's collection (not delete parent itself, just remove reference)?

```java
doctor.getPatients().remove(p1); // remove patient from doctor's list
```
Without orphanRemoval — p1 row stay in db, just FK (doctor_id) set null (child become "orphan" — exist but disconnected from parent). With orphanRemoval = true — Hibernate detect removal from collection, auto issue DELETE for that child row entirely — child can't exist without being part of parent's collection, so removing from collection = delete it.

Difference cascade REMOVE vs orphanRemoval — important distinction:

CascadeType.REMOVE — trigger only when parent itself explicitly deleted (doctorRepository.delete(doctor)).
orphanRemoval = true — trigger when child removed from parent's collection, even if parent itself not deleted, still alive.

Both often used together (CascadeType.ALL includes REMOVE, plus orphanRemoval = true separately) for true parent-owns-child relationships — classic example: Order → OrderItem, Patient → MedicalRecord (one-to-one or one-to-many dependent child).

Practical example — full flow with test:

```java
@Entity
public class Patient {
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Prescription> prescriptions = new ArrayList<>();
}
```
```java
@Test
void testOrphanRemoval() {
    Patient patient = patientRepository.findById(1L).get();
    Prescription p = patient.getPrescriptions().get(0);

    patient.getPrescriptions().remove(p); // removed from collection
    patientRepository.save(patient);      // flush triggers DELETE for that prescription row

    assertFalse(prescriptionRepository.existsById(p.getId()));
}
````

Caution — industry practice:

Avoid CascadeType.ALL blindly on every relationship — think per-relationship what actually make sense (e.g. don't cascade REMOVE on Doctor → Patient, patient shouldn't vanish just because doctor record deleted).
orphanRemoval only meaningful on @OneToMany/@OneToOne where child clearly "belongs" to one parent — never use on @ManyToMany (child shared across multiple parents, removing from one collection shouldn't delete entirely, other parent still reference it).

Specification / Criteria API — dynamic queries.

Problem it solve: sometimes query filters vary at runtime — user might filter by name only, or name+age, or age+disease+doctor, any combination, unknown ahead of time. Writing separate @Query/derived method for every combination = explosion of methods, unmanageable. Specification API let build query dynamically, condition by condition, based on what filters actually present.

Setup — repository extend JpaSpecificationExecutor

```java
public interface PatientRepository extends JpaRepository<Patient, Long>, JpaSpecificationExecutor<Patient> {
}
```

This give extra methods: findAll(Specification<T> spec), findAll(Specification<T> spec, Pageable pageable), etc.

Writing a Specification

```java
public class PatientSpecifications {

    public static Specification<Patient> hasDisease(String disease) {
        return (root, query, cb) ->
            disease == null ? null : cb.equal(root.get("disease"), disease);
    }

    public static Specification<Patient> ageGreaterThan(Integer age) {
        return (root, query, cb) ->
            age == null ? null : cb.greaterThan(root.get("age"), age);
    }

    public static Specification<Patient> nameContains(String name) {
        return (root, query, cb) ->
            name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }
}
```

Explain pieces:

Specification<Patient> — functional interface, lambda take three param: root (entity being queried, like p alias in JPQL), query (the query itself, rarely touched directly), cb (CriteriaBuilder — factory build conditions: equal, greaterThan, like, and, or etc).
Return null when filter value not provided — Spring Data auto-ignore null predicate, effectively skip that condition. Core trick make combination flexible.
root.get("disease") — reference entity field by string name (type-safe alternative exists too, via JPA metamodel, cover later if want).

Combining specifications dynamically

```java
Specification<Patient> spec = Specification.where(null);

if (disease != null) {
    spec = spec.and(PatientSpecifications.hasDisease(disease));
}
if (age != null) {
    spec = spec.and(PatientSpecifications.ageGreaterThan(age));
}
if (name != null) {
    spec = spec.and(PatientSpecifications.nameContains(name));
}

List<Patient> results = patientRepository.findAll(spec);
```

Only conditions actually provided (non-null) get combined via .and() — final SQL only include filters that were actually set, fully dynamic based on input.

Typical service method (real project pattern)

```java
public List<Patient> searchPatients(String disease, Integer age, String name) {
    Specification<Patient> spec = Specification.where(PatientSpecifications.hasDisease(disease))
            .and(PatientSpecifications.ageGreaterThan(age))
            .and(PatientSpecifications.nameContains(name));

    return patientRepository.findAll(spec);
}
```

Since each spec method already handle null internally (return null predicate, get ignored) — chain .and() safely even when some param null, no extra if check needed at call site.

With pagination combined

```java
Page<Patient> results = patientRepository.findAll(spec, PageRequest.of(0, 10));
```

Specification + Pageable work together seamlessly — same pattern covered earlier, just pass spec now too.

Controller example (common real-world usage)

```java
@GetMapping("/patients/search")
public List<Patient> search(
        @RequestParam(required = false) String disease,
        @RequestParam(required = false) Integer age,
        @RequestParam(required = false) String name) {
    return patientService.searchPatients(disease, age, name);
}
```

Client hit /patients/search?disease=Diabetes&age=30 — only those two filters apply, works for any combination client send, no extra code needed per combination.

When use Specification vs JPQL @Query:

Fixed, known filter combination → JPQL @Query, simpler, more readable.
Filter combination unknown/variable at runtime (search/filter UI, admin panel, API with many optional query params) → Specification API, built for exactly this.

Behind scenes: Specification ultimately build JPA Criteria API query object — type-unsafe-ish (string field names) by default, but there's stricter type-safe variant using generated Metamodel classes (Patient_.disease instead of "disease" string) — mention if want cover that refinement, catches typos at compile time instead of runtime.

Type-safe Specification (JPA Metamodel) — refine previous topic.

Problem with string-based Specification: root.get("disease") — plain string, typo (root.get("diseae")) compile fine, fail only at runtime. Not ideal for large project, want catch mistake at compile time.

Solution: JPA Static Metamodel

Hibernate/JPA can auto-generate metamodel classes (annotation processor) — for entity Patient, generate class Patient_ with static field reference for every entity field.

Step 1 — enable metamodel generation (Maven)

Add annotation processor dependency in pom.xml:

```xml
<dependency>
    <groupId>org.hibernate.orm</groupId>
    <artifactId>hibernate-jpamodelgen</artifactId>
    <scope>provided</scope>
</dependency>
```

Build project — generated class appear in target/generated-sources/annotations/ (IDE usually auto-detect, no manual write needed).

Generated class look like (auto-generated, don't write manually):

java
@StaticMetamodel(Patient.class)
public class Patient_ {
    public static volatile SingularAttribute<Patient, String> name;
    public static volatile SingularAttribute<Patient, Integer> age;
    public static volatile SingularAttribute<Patient, String> disease;
}

Step 2 — use Patient_ instead of string literal

```java
public class PatientSpecifications {

    public static Specification<Patient> hasDisease(String disease) {
        return (root, query, cb) ->
            disease == null ? null : cb.equal(root.get(Patient_.disease), disease);
    }

    public static Specification<Patient> ageGreaterThan(Integer age) {
        return (root, query, cb) ->
            age == null ? null : cb.greaterThan(root.get(Patient_.age), age);
    }
}
```

Difference: root.get(Patient_.disease) instead root.get("disease") — typo now compile error, not runtime surprise. Rename field in entity (disease → diagnosis) — metamodel regenerate, all Specification code using old name fail compile immediately, force fix everywhere — much safer refactor.

Trade-off — why not always used:

Extra build step, extra generated files, slightly heavier project setup.
Small project / simple filters → plain string version fine, less ceremony.
Large project, many entities/specifications, long-term maintenance, team collaboration → metamodel worth it, safety net pay off.

Industry practice: seen both, larger enterprise codebase lean metamodel more often, smaller startup/project stick plain string for speed.


Auditing — auto-track creation/update metadata.

Problem it solve: almost every real entity need know: who created this record, when, who last modified, when. Writing this manually every service method (patient.setCreatedAt(LocalDateTime.now()) everywhere) — repetitive, error-prone, easy forget somewhere. Spring Data JPA auditing automate this entirely.

Step 1 — enable auditing, main application class or config class

```java
@SpringBootApplication
@EnableJpaAuditing
public class HospitalManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(HospitalManagementApplication.class, args);
    }
}
```

@EnableJpaAuditing — turn on auditing infrastructure globally, Spring now listen for entity save/update events, auto-populate audit fields.

Step 2 — annotate entity fields

```java
@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;
}
```

Explain each:

@CreatedDate — auto-set timestamp on first save (INSERT), never touch again.
@LastModifiedDate — auto-set on every save (INSERT and every UPDATE).
@Column(updatable = false) — prevent accidental overwrite of createdAt on later update (extra safety, DB-level).
@CreatedBy / @LastModifiedBy — capture who did action (username/user id), need extra setup (next step) — not automatic like date fields.

Step 3 — entity needs @EntityListeners

```java
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Patient {
    ...
}
```

This listener class actually intercept persist/update lifecycle event, populate annotated fields — without this, @CreatedDate etc annotation do nothing, just sit there ignored.

Step 4 — @CreatedBy/@LastModifiedBy need "current user" provider

Spring don't know who "current user" is automatically — need supply via bean implement AuditorAware:

```java
@Configuration
public class AuditConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            // in real project, pull from Spring Security context
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.of("SYSTEM");
            }
            return Optional.of(authentication.getName());
        };
    }
}
```
Without Spring Security setup yet (learning stage), can stub simple:

```java
@Bean
public AuditorAware<String> auditorProvider() {
    return () -> Optional.of("SYSTEM"); // placeholder until auth added
}
```
Result — behavior in practice

```java
Patient patient = new Patient();
patient.setName("Ravi");
patientRepository.save(patient);
// createdAt, updatedAt, createdBy, updatedBy auto-populated, no manual code
java
patient.setName("Ravi Updated");
patientRepository.save(patient);
// updatedAt, updatedBy auto-refresh; createdAt, createdBy stay untouched
```

Common practice — base entity class (avoid repeating audit fields every entity)

```java
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;

    // getters setters
}
```

```java
@Entity
public class Patient extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
```
@MappedSuperclass — not an entity itself, no own table — fields get inherited into every subclass entity's table as regular columns. Standard industry pattern, write audit fields once, reuse across all entities.