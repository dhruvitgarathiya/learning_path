# annotation

annotations by themselves do absolutely nothing

they are just passive metadata attached to classes , methods, fields

there is an external engnie reading them via refletions to execute them and taking actions

## java reflection

feature allows an exeuting java program to inspect , examin , manipulate it's own internal strcutre,classes , fields , methods and constructors at runtime

you must know the name of class and it's methods at compile tim to use them

reflection break this rule allowing you to intract with classes whoes name you might not event know untill program is already runnig

key capabilities:

inspect metadata: retrieve a class name , its parent class , implemented interfaces and any annotation attached to it

instantiate objects dynamically : create a new instance of a class without using the new keyword

invoke method: find them by string name and execute them dynamically

access private memeber: view or modify private variales and execute private methods by bypassing standard access control

how it works:

```java
// 1.using the class literal 

Class<?> clsl = MyClass.class;

// 2.using an object instance

MyClass obj = new MyClass();
Class<?> cls2 = obj.getClass();

// 3. using the fully qualified class name as string

Class<?> cls3 = Class.forname("com.example.MyClass");
```

Real world use cases

1. spring framework : uses reflection for dependcny injection.
2. testing frameworks 
3. json mapping (jackson)
4. orms

we will thought this should not be possible , like isn't this illigal 

but

From a pure object-oriented programming standpoint, breaking encapsulation like this feels like "cheating" or violating a sacred contract.

To understand why Java allows this and how it happens behind the scenes, we have to look at how the Java Virtual Machine (JVM) is architected. Reflection is not a bug or a hack; it is an intentionally engineered "backdoor" built into the JVM for toolmakers.

**Where Does "Private" Actually Exist?**

In Java, access modifiers (private, protected, public) are logical rules for the compiler, not physical brick walls in computer memory.

At Compile Time: When you compile your code using javac, the compiler enforces visibility rules. If it sees your code trying to directly call a private method of another class, it refuses to compile and throws an error.

In the .class File: Once compiled, the bytecode file stores your classes as structural data tables. A private variable is just an entry in a metadata table with a specific binary flag (like ACC_PRIVATE).

**The Internal Engine: FieldAccessor and MethodAccessor**

At Runtime (Inside the JVM): The JVM reads these metadata tables into memory. Because the JVM owns the memory, it has raw, structural access to every byte of your class definition. Reflection is simply the API that lets you query that raw, internal JVM structure directly.

When you call setAccessible(true) and interact with a private member, the JVM bypasses standard language checks using specialized internal objects. Here is the chain of events:

The Safety Switch (override flag): Every Field, Method, and Constructor object inherits from AccessibleObject. Inside this class is a simple boolean flag called override. Calling setAccessible(true) flips this flag to true.

Generating the Accessor: When you call field.get() or method.invoke(), the JVM checks the override flag. If it is true, the JVM skips its usual security checks and hands the request over to an internal interface called FieldAccessor or MethodAccessor.

For the first few calls, the JVM uses Native Code (C++) via Java Native Interface (JNI) to directly read or write to the precise memory address offset where that field lives.

if you call that reflective method many times, the JVM optimizes it via a process called Inflation. It dynamically generates raw Java bytecode on the fly that acts as a direct bridge to that private data, bypassing all access checks at the machine level.

* If it breaks encapsulation, why build it? Reflection was designed for infrastructure, not application logic. Without this backdoor, the modern Java ecosystem could not exist.

Reflection allows the framework to say: "I don't care what your class is named. Hand it to me, I will inspect its blueprint at runtime, find whatever fields you wrote, turn off the access checks temporarily, and map your data to the database."

In older versions of Java, you could use reflection to break into the core internals of the JVM itself (like changing the behavior of java.lang.String or accessing unsafe memory).

This caused massive security vulnerabilities and made upgrading Java difficult.To fix this, modern Java introduced the Module System. 

Now:You can still use reflection on your own code (so Spring and JUnit still work).

The JVM strongly encapsulates its own internal runtime packages. If you try to use setAccessible(true) on deep, private JDK internals today, the JVM will actively block you and throw an InaccessibleObjectException, effectively closing the most dangerous aspects of this backdoor forever.


## back to annotation

In Spring Boot, that engine is the ApplicationContext (the IoC Container).

**Phase 1: Reading metadata(No instance yet)**

When you run SpringApplication.run(), Spring does not immediately create your Java objects

First, it maps out your application structure using an ASM-based bytecode scanner (which reads .class files without loading them into the JVM memory yet).

@ComponentScan Execution: Spring reads your main class and scans the package down

Finding Stereotypes: It looks for classes marked with @Component, @Service, @Repository, or @RestController

Creating BeanDefinition: For every annotated class it finds, Spring creates a Java object called a BeanDefinition. This object acts as a blueprint, storing metadata like:What is the class name?Is it a Singleton or Prototype?Which fields are marked with @Autowired?

**Phase 2: Instantiation & dependency injection**

Once Spring has a complete list of blueprints (BeanDefinitions), it begins translating them into actual Java objects (Beans) inside the container.

Spring calls Constructor.newInstance() to physically instantiate your classes.

@Autowired: To wire your objects together, Spring uses a specialized internal listener called the AutowiredAnnotationBeanPostProcessor. It uses Core Java Reflection (Field.setAccessible(true) and Field.set()) to forcefully inject dependency instances into your fields, even if they are marked private.


**Phase 3: Enhancing Behavior (Proxies & AOP)**

For advanced annotations like @Transactional or @Async, standard Java reflection isn't enough. Spring cannot dynamically alter your compiled Java bytecode, so it wraps your object inside a Proxy object using Java Dynamic Proxies or CGLIB

How a Proxy Works: If your Service class has @Transactional, Spring generates a hidden subclass (the proxy) at runtime.

Behind the Scenes execution: When another bean calls your service method, it is actually calling the proxy. The proxy executes connection.setAutoCommit(false), invokes your actual Java method, and then calls connection.commit().


**Phase 4:Connecting Web Annotations to Servlets**

In a traditional Java web app, you manually register Servlets in a web.xml or use @WebServlet to map URLs. In Spring Boot, there is only one major Servlet: the DispatcherServlet.

[Incoming HTTP Request] 
         │
         ▼
 ┌───────────────┐
 │ Tomcat Server │
 └───────┬───────┘
         │ (Passes request)
         ▼
 ┌───────────────────┐
 │ DispatcherServlet │
 └───────┬───────────┘
         │ 
         │ 1. Looks up URL in HandlerMapping table
         │ 2. Finds method mapped via @GetMapping
         │ 3. Uses Reflection to invoke the Controller method
         ▼
 ┌────────────────────────────────────────┐
 │ @RestController Method                 │
 │ (Converts Java Return Object to JSON)  │
 └────────────────────────────────────────┘


Mapping @RequestMapping / @GetMapping: At startup, a built-in bean called RequestMappingHandlerMapping scans all beans annotated with @RestController. It uses reflection to extract the URL string (e.g., /api/v1/users) and stores it in an internal Routing Table (Map), matching the URL to your specific Java Method.

Handling the Request: When an HTTP request hits the embedded Tomcat server, Tomcat routes it entirely to the DispatcherServlet

Method Invocation: The DispatcherServlet looks up the incoming URL in its routing table, finds your controller's method, and uses standard Java Reflection (Method.invoke()) to execute it.

Executing @ResponseBody / @RestController: If your class is marked as a @RestController, Spring bypasses the traditional Servlet RequestDispatcher (which forwards to JSPs). Instead, it hands your return object to an HttpMessageConverter (like the Jackson library), which writes the serialized JSON directly into response.getWriter().print()


# stereotype annotations

## @Component

What it be: generic stereotype. Marks class as Spring-managed bean. Tell Spring: "scan me, make object of me, put in container (ApplicationContext)."

Why need it: without @Component, class just plain Java class. New object every time you write new EmailValidator(). Spring don't know it exist. 

With @Component, Spring's component-scan find class (during startup), create single instance (bean, default singleton scope), store in IoC container. Then you inject with @Autowired wherever need, no new keyword.

Behind scenes:

Spring Boot app has @ComponentScan (hidden inside @SpringBootApplication).

Scan walks packages, look for classes annotated @Component (or meta-annotated with it — important point, see below).

For each found class, Spring create BeanDefinition — blueprint: class name, scope, dependencies.

Container instantiate bean (constructor call), resolve dependencies, store in registry, keyed by bean name (default = class name, first letter lowercase — emailValidator).

Key fact — meta-annotation: @Service, @Repository, @Controller all internally annotated with @Component. Look inside Spring source:

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Service {
    String value() default "";
}
```

So @Service IS a @Component underneath, just extra semantic meaning slapped on. Component-scan catch all four, same mechanism.

Difference between four ain't technical (mostly) — difference is intent + some extra behavior

When use plain @Component:

for beans not fitting other three roles. Example: validators, utility beans, config helpers, mappers, scheduled task classes.


## @Service

What it be: stereotype for business logic layer.

this class hold business rules, calculations, orchestration between repository and controller.

Technically = @component underneath no extra spring machinary added itself

```java
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final PaymentService paymentService;

    public OrderService(OrderRepository orderRepository, PaymentService paymentService) {
        this.orderRepository = orderRepository;
        this.paymentService = paymentService;
    }

    public Order placeOrder(OrderRequest request) {
        validateStock(request);
        Order order = new Order(request);
        paymentService.charge(request.getAmount());
        return orderRepository.save(order);
    }

    private void validateStock(OrderRequest request) {
        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
    }
}
```

Why separate stereotype if same as @Component underneath?

Readability / intent — dev opens codebase, see @Service on class, know instantly: "business logic here, no DB code, no HTTP code." Big team, big codebase, this matter lot.

Layered architecture enforcement — Spring encourage 3-layer split:
Controller (web) → Service (business) → Repository (data)

@Service mark middle layer. Convention help keep concerns separated — service shouldn't handle HTTP request/response, repository shouldn't hold business rules.

AOP targeting — tools like Spring AOP, @Transactional processing, logging aspects, security aspects often target @Service-annotated classes specifically via pointcut expressions like:


```java
   @Pointcut("@within(org.springframework.stereotype.service.Service)")
```

So marking correct stereotype matters for tooling that scan by annotation type, not just for humans reading code.

@Transactional commanly here:

```java
@Service
public class OrderService {

    @Transactional
    public Order placeOrder(OrderRequest request) {
        // multiple DB operations here
        // all commit together, or all rollback on exception
    }
}
```

@Transactional wrap method in DB transaction 

proxy created aound bean, start transaction before method run 

commit after , rollback if unchecked exception thorws

Common to put on Service layer methods since that where multi-step business operations live (e.g. "deduct stock AND save order" — both must succeed together

## @repository

stereotype for data-access layer — classes talking to DB, files, external data stores

Unlike @Service/@Controller, this one got real extra Spring behavior, not just label.

```java
@Repository
public class OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Order save(Order order) {
        jdbcTemplate.update(
            "INSERT INTO orders (id, amount) VALUES (?, ?)",
            order.getId(), order.getAmount()
        );
        return order;
    }
}
```

Extra behavior — exception translation:

Spring wrap @Repository beans with PersistenceExceptionTranslationPostProcessor

This post-processor create proxy around bean. Proxy catch technology-specific exceptions — like SQLException, JDBC's SQLIntegrityConstraintViolationException, Hibernate's ConstraintViolationException — and translate into Spring's own unified hierarchy: DataAccessException (unchecked, under org.springframework.dao).

Why matter — real problem it solve:

Without translation, service layer code look like:

```java
try{
        orderRepository.save(order);
}catch(SQLException e){ // jdbc specific
        //handle
}catch(ConstraintViolationException e){ // hibernate-specific
        // handle
}
```
Business layer now tightly coupled to which persistence tech used underneath. Switch from JDBC to Hibernate to JPA — all catch blocks broken across whole codebase.

With @Repository, exception translated automatically, service layer catch generic Spring exception instead:

```java
try {
    orderRepository.save(order);
} catch (DataIntegrityViolationException e) {   // Spring's own, tech-agnostic
    // handle duplicate key, constraint fail, etc — regardless of JDBC/Hibernate/JPA
}
```

Behind scenes — how translation actually happen:

- PersistenceExceptionTranslationPostProcessor scan for beans annotated @Repository during context startup.

- For each, look up matching PersistenceExceptionTranslator bean (Spring Boot auto-configure one based on which persistence tech on classpath — JDBC, JPA, Hibernate all got own translator).

- proxy wrap around repository bean's methods

- method call intercepted -> run actual db call -> if exception thrown -> translator convert it -> rethrow as DataAccessException
subtype


DataAccessException (abstract, unchecked)
├── DataIntegrityViolationException     (constraint violations)
├── DuplicateKeyException               (unique key clash)
├── EmptyResultDataAccessException      (expected 1 row, got 0)
├── OptimisticLockingFailureException

When skip it: if class not touching persistence tech (no DB, no file I/O exceptions to translate), no benefit — just use @Component instead. Adding @Repository to non-DB class = pointless proxy overhead, no functional gain.

## @Controller / @RestController

what it be: tereotype for web layer — classes handling incoming HTTP requests, returning responses. Entry point of app.

```java
@Controller
public class OrderPageController {

    private final OrderService orderService;

    public OrderPageController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders/{id}")
    public String getOrderPage(@PathVariable Long id, Model model) {
        Order order = orderService.findById(id);
        model.addAttribute("order", order);
        return "orderView";   // name of view template — e.g. orderView.html (Thymeleaf)
    }
}
```

by default return value of method treated as view name 

spring pass it to viewResolver which map string "OrderView" to actual templet file( thymeleaf , jsp ,etc) .

render html , send that html back to browser

used for server side rendered web apps(full html pages)

## @RestController

```java
@RestController
public class OrderApiController {

    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/api/orders/{id}")
    public Order getOrder(@PathVariable Long id) {
        return orderService.findById(id);
    }
}
```

return value here not view name, it's actual data, serialized (usually to JSON) and written directly into http response body

used for rest apis mobile backend and frontend talking over json

@RestController = @Controller + @ResponseBody

dont treat return value as view name , write it straight to reponse body. 

Spring use HttpMessageConverter ( uses jackson ) to convert java object -> json string automatically

if used plain controller but wanted json reposne would need responsebody on every method manually

```java
@Controller
public class OrderApiController {

    @GetMapping("/api/orders/{id}")
    @ResponseBody   // needed manually here, else Spring look for view named "order object toString"-ish, fail
    public Order getOrder(@PathVariable Long id) {
        return orderService.findById(id);
    }
}
```

**Request flow full picture**

browser client send http request -> hit dispatcherServlet 

dispatcherServlet consult HandMapping finds which controller method match URL + HTTP verb (based on getmapping , postmapping - these are shortcuts for requestMapping(method = ..))

method invoked params vound (@pathvariable, @requestParam, @requestBody) exctrat values from url / query/ body

method run - call service layer . get result.

return value processed by handlermethodreturnvaluehandler:

        - @controller (no responsebody) => treated as view name => viewResolver => render html

        - @restController (or responsebody) => passed to httpmessageConverter -> serialized (JSON/XML) -> written to response body directly

reponse sent back to client


common format of writing restcontoller code:

```java
@RestController
@RequestMapping("/api/orders")
public class OrderApiController{
        private final OrderService orderService;

        public OrderApiController(OrderService orderService){
                this.orderService = orderService;
        }

        @GetMapping('/{id}')
        public Order getOrder(@pathVarible Long id){
                return orderService.findById(id);
        }

        @PostMapping
        public Order creatOrder(@RequestBody OrderRequest request){
                return orderService.placeOrder(request);
        }

        @GetMapping
        public List<Order> searchOrders(@RequestParam String status){
                return orderService.findByStatus(status);
        }
}
```

@RequestMapping("/api/orders") at class level — base path, prepended to all method paths.

@PathVariable — pull value from URL path segment (/{id}).

@RequestParam — pull value from query string (?status=PENDING).

@RequestBody — deserialize JSON request body into Java object (reverse of @ResponseBody — uses same HttpMessageConverter machinery, opposite direction)