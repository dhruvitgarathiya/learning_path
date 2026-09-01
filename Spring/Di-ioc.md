assume and notification service 
we write

```java
public interface NotificationService{
  
} 

public class EmailNotificationService implements NotificationSerivce(){
    EmailNotificationService(){
      
    }
}

public class Main{
    public static void main(String[] args){
        NotificationService nf = new EmailNotificationService();
    }
}
```

in above code we have created the email notification from notification ,

what if we have to swtich to smsnotificationservice from emailnotificationservice 

notification service have two jobs - doing it's job(class work) and manage the lifecycle of the emailnotificationservice

so now we have to understand that main class is tightly coupled with the emailnotificationservice object.

reason: 

being it is being declared and made in main class with **new keyword** 
new keyword always tighly couples the object with that class where object is declared

reason:

1. **The Architectural Reason**: The Violation of OCPUsing new directly violates the Open/Closed Principle (OCP), which states that software entities should be open for extension but closed for modification.

With new: If you hardcode new EmailNotificationService(), your code is closed to extension. If a client asks to switch to SmsNotificationService, you must modify the existing class code to change the name after the new keyword.

Without new: If the class just asks for a NotificationService interface and receives it from the outside, you can extend the system with 100 new notification types without changing a single line of your original class.

2. **The Compile-Time Dependency** (The Java Binary Link)When you type new EmailNotificationService(), you create a hard binary dependency at compile-time.

text[Main.java] 
     │
     └──> (Compile-time hard link) ──> [EmailNotificationService.class]
    
What happens behind the scenes:Compilation: The Java compiler (javac) reads Main.java. It sees new EmailNotificationService().

Verification: The compiler pauses and searches your project or classpath specifically for a file named EmailNotificationService.class.

Hardcoding: If it doesn't find that exact class name, compilation fails immediately. If it does find it, it bakes a direct reference to that specific class into the bytecode of Main.class.

The Trap: Your Main class can no longer run or be compiled in isolation. It is physically chained to EmailNotificationService.

If you wanted to deploy your application to a lightweight environment that only uses SMS, you cannot just leave the EmailNotificationService file behind. Your program will crash with a NoClassDefFoundError because the bytecode demands that specific class exist.

3. **The Runtime Memory Assignment (The Concrete Blueprint)**

At runtime, the JVM executing the new keyword needs exact physical instructions.Interfaces do not have a physical memory footprint or constructor instructions. They are just abstract sets of rules. To allocate memory on the Heap, the JVM must know the exact concrete class layout.

What happens behind the scenes:

Instruction: The JVM hits the new instruction in the bytecode.

Class Loading: The JVM looks at the hardcoded name EmailNotificationService, loads its specific blueprint into the Metaspace, and looks up its constructor.

Memory Allocation: It calculates the exact byte size needed for an Email notification object (e.g., storing SMTP strings, ports, etc.) and allocates that space on the Heap.

The Trap: Because Main called new, Main is the one orchestrating this low-level lifecycle creation. It cannot dynamically allocate memory for an SMS object instead, because the bytecode specifically requested the memory layout of an Email object.


what if we want to **test** notification service without real emailservice

reason:

1. **You cannot inject a "Mock" object**

Mocking frameworks (like Mockito) work by creating a fake, lightweight version of a class (a "Mock") that simulates real behavior without doing real work (like hitting an actual email server).

The Goal: You want to pass a mock EmailService into NotificationManager.

The Roadblock: NotificationManager creates its own instance on the heap instantly when it is instantiated. It completely ignores your testing framework. There is no constructor, setter, or open door to pass your mock inside.

2. **You force your test to run heavy, real-world code**

Because you cannot replace EmailService with a mock, your test is forced to execute the real EmailService class. 

In a real application, 

this means:Your test might try to connect to a real SMTP server.

If the network is down or the mail server is slow, your unit test will fail or run slowly, even though the code inside NotificationManager is perfectly correct.

You cannot easily test error scenarios (e.g., "What happens if the email server throws a TimeoutException?") because you cannot control the internal EmailService instance to force it to fail.

3. **The Test is no longer a "Unit" Test**

A true unit test focuses on a single "unit" of code (one class).Because of the new keyword, testing NotificationManager automatically tests EmailService as well. If someone breaks the code inside EmailService, the tests for NotificationManager will fail too. 

This is an Integration Test, not a Unit Test.


## fix conceptually : inversion of control

normally our class have control of creating object and managing it

in ioc we flip this by control of who ,when and how create the object moves out of the class to external power

you class stops saying let me go create this object and start saying some one give me this object


## ways to achieve ioc

ioc is principal

one way to achieve this is dependency injection 

this is technique implementing the ioc principal

### dependency injection : - 

object your class need being injection from outside


```java

public class notificationmanager{
    notificationmanager(emailservice es){
        this.es  = es
    }

    public void notify(){
        es.send();
    }
}

public class emailservice{
    public void send();
}

public class main{
    public static void main(String[] args){
        emailservice es = new emailservice;
        notificationmanager nm = new notificationmanager(es);
        nm.notify();
    }
}
```


notification manager dont know how emailservice is built , it just get the object of it 

### 3 types of di:

- **Constructor injection**: 

shown above , dependecny passed through the cnostructor

mostr recommanded

makes dependecny mandatory and object immutable ( final field possible )

ex.

```java
public class NotificationManager {
    // 1. The 'final' keyword ensures this reference can NEVER be reassigned
    private final EmailService emailService; 

    // 2. Constructor Injection: The dependency is supplied EXACTLY at birth
    public NotificationManager(EmailService emailService) {
        if (emailService == null) {
            throw new IllegalArgumentException("Dependency cannot be null");
        }
        this.emailService = emailService; // Locked in memory here
    }

    void notifyUser(String msg) {
        emailService.send(msg);
    }
}
```

- **setter injection**

depedency passed thorugh setter method instead of constructor

```java
class NotificationManager{
    private EmailService emailservice;

    public void SetEmail(EmailSerivce emailservice){
        this.emailservice = emailservice;
    }
}
```
question is why we cannot make this field final , so it can be immutable also

reason:

You cannot make emailService final in this code because the final keyword in Java demands that a variable be initialized before the constructor finishes executing [1].

By using a setter method (SetEmail), you are attempting to assign a value to emailService after the object has already been fully created. Java's compiler will reject this immediately.

**The Compiler Rule**:

When you mark a field as final, you are telling the Java compiler: "This variable gets one assignment, and it must happen during the birth of the object."

There are only two places the compiler allows a final field to be initialized:

Directly where it is declared: private final EmailService emailservice = new EmailService();

Inside a Constructor: Through constructor injection.

Because your code leaves the field uninitialized at declaration and uses an empty implicit constructor, the object is born with emailservice set to null. The moment the constructor finishes, the compiler locks all final fields permanently.When your setter method later tries to execute 

this.emailservice = emailservice;, 

the compiler blocks it because you are trying to change a locked, post-construction value.

- **field injection**

dependecy injected directly via field 

spring does this via @autowired on field , no constructor/setter needed

convienent but bad practise

cant make field final hide depenedcy ,hard to test


## other prmoinent ways to implement the ioc

**Service Locator** 

PatternA central registry known as the "Service Locator" holds references to all dependencies.

Classes request their required objects from the locator.It decouples classes from concrete implementations.

It is sometimes considered an anti-pattern(a common, appealing response to a recurring problem that ultimately causes more harm than good) because it hides class dependencies.

**Factory Pattern**

Creational design patterns hand over the responsibility of object creation to a separate class.

Factory Method: Defines an interface for creating an object but lets subclasses alter the type.

Abstract Factory: Creates families of related objects without specifying their concrete classes.

**Template Method**

this achieves IoC at the behavioral level rather than the structural level.

A base class defines the skeleton of an algorithm.Subclasses override specific steps without changing the algorithm's overall structure.

The framework (base class) calls the user code (subclass), reversing the traditional control flow.

**Strategy Pattern**

This allows a class's behavior or algorithm to be changed at runtime.

The executing class maintains a reference to a strategy interface.

External code injects the specific concrete strategy to execute.


* question is if all this patterns exsisted then why spring choose di pattern instead of all this

reason:

it achieves the absolute highest degree of decoupling and testability

While other patterns invert control in some way, they fail to solve the primary problem Spring aims to fix: keeping business logic independent of the underlying framework.

1. DI vs. Service Locator: "Don't Call Us, We'll Call You"

The major flaw of the Service Locator pattern is that the application code must actively ask the locator for dependencies.

The Problem: Your classes become tightly coupled to the Service Locator API. If you want to move a class to another project, you must bring the Service Locator framework with it.

The Spring DI Fix: With DI, a class is completely passive. It simply declares what it needs (via a constructor or field), and Spring passes it in. The class remains a Plain Old Java Object (POJO), entirely unaware that Spring even exists.

2. DI vs. Factory Pattern: Eliminating Massive BoilerplateThe Factory Pattern shifts object creation away from the consumer, but it introduces an explosion of structural code.

The Problem: For every service, you have to write a corresponding factory class or a massive centralized factory with countless if-else or switch statements. Testing requires mocking both the factory and the dependency.

The Spring DI Fix: Spring acts as a Universal, Automated Factory. Instead of writing custom factory classes for your code, you use simple annotations like @Component or @Autowired. Spring reads these at startup and builds the entire application graph automatically.

3. Maximum Testability (The Killer Feature)

Spring was originally created by Rod Johnson as a direct rebellion against the hard-to-test frameworks of the early 2000s (like EJB). 

DI is the ultimate pattern for unit testing.

Without DI: If a class pulls dependencies from a Factory or Service Locator, writing an isolated unit test requires setting up and mocking those complex registries.

With DI: Because a class exposes its dependencies via a constructor, you can instantiate it in a plain JUnit test using the new keyword and directly pass a Mockito mock. You do not need to boot up the Spring framework just to test a single line of business logic

4. Behavioral Patterns (Template/Strategy) Solve Different Problems

Patterns like Template Method and Strategy achieve IoC at the behavioral level, whereas Spring manages IoC at the structural/architectural level.

The Distinction: Strategy and Template Method dictate how an algorithm runs, but they do not solve the problem of how those strategy objects are created, wired together, or managed throughout their lifecycle. Spring uses DI to inject the concrete Strategy classes into your components.


# no more manual writing 

manual writing is ok for the 2 class

imagine 50 class notificationservice needs - emailservice - it needs logger, userservice need - notification service + userrepositry, userrepositry needs datasource

manual writing means we write by hand in main method-

```java
Logger logger = new Logger();
ConfigService config = new ConfigService(logger);
EmailService emailService = new EmailService(config);
NotificationManager notificationManager = new NotificationManager(emailService);
DataSource dataSource = new DataSource();
UserRepository userRepository = new UserRepository(dataSource);
UserService userService = new UserService(notificationManager, userRepository);
// ...and so on, order matters, easy to mess up
```

this is object graph - hand building object graph is vey complext

missed one sequence you get nullpointerexception

**solution: give this job to container**

Container = program that reads your classes, figures out dependency graph itself, creates objects (called beans in Spring), injects dependencies, in correct order, automatically. You just describe WHAT each class needs (via constructor/setter/annotation) — container figures out HOW to wire.

In Spring this container called ApplicationContext (interface).Actual object at runtime typically AnnotationConfigApplicationContext or (in Boot) built for you automatically.

**bean**

plain object that Spring container creates and manages

No special class needed — same EmailService class from before, just Spring container now owns creating it instead of you typing new.


### way 1  tell spring bean about di through annotations


```java
@component
public class EmailService{
    void send(){
        System.out.println("sending message")
    }
}

@component
public class notificationManager{
    private final EmailService em;


    @autowired
    notificationManager(EmailService em){
        this.em = em;
    }

    void notifyUser(){
        em.send();
    }


}
```


@component = marks class as candidate for spring to manange - to make bean out of it

@autowired on constructor = tells the constructor while making this bean - find matching bean called emailservice and inject it here

if class have only one constrcutor then @autowired is optional but it is good practise to write it explictly

```java

@configuration
@componentScan(basePackage= "com.yourapp")
    class appconfig{}

public class Main{
    public static void main(String[] args){
        ApplicationContext context = new AnnotationConfigApplicationContext(appconfig.class);

        NotificationManager nm = context.getBean(NotificationManager.class);

        manager.notifyUser("hello");
    }
}
```

@componentScan tell the container , scane this package find all @component classes and makes bean of it

context.getBean() give me fully built object of this type

Behind scenes, what container actually does at startup (important part):

1. **Scan phase** — walks specified package, using reflection, finds classes marked @Component (also @Service, @Repository, @Controller — same mechanism, different semantic label).

2. **Definition phase** — for each found class, builds internal metadata object called BeanDefinition (not real object yet — just blueprint: class type, scope, constructor info, dependencies needed). Stored in registry (map-like structure, BeanFactory underneath).

3. **Dependency resolution phase** — container looks at each BeanDefinition, figures out order. If NotificationManager needs EmailService, container makes sure EmailService bean built FIRST. This called dependency graph resolution — internally, kind of topological sort.

4. **Instantiation phase** — container calls constructor via reflection (Constructor.newInstance(...)), passing already-built dependency beans.

5. **bean stored in container** - final object cached in container - singleton scope, one instance whole app, resued every time asked

So context.getBean(NotificationManager.class) doesn't build fresh object each call — returns same cached instance (unless scope changed to prototype, later topic).


## how multiple picks the right one like when you have multiple candidates

```java
public interface MessageSerive{
    public void send(){

    }
}

public class EmaillService implements MessageService{
    public void send(){
        System.out.println("message sent via email");
    }
}

public class SmsService implements MessageService{
    public void send(){
        System.out.println("message sent via sms");
    }
}




public class Main{

    public static void main(String[] args){
    private final MessageService ms;

@Autowired
Main(MessageService ms){
    this.ms = ms;
}
ms.send();
    }

    
}
```

now the question is what will container should execute

Container sees NotificationManager needs MessageService. 

Two beans match type (EmailService, SmsService). 

Container confused — which one give? Throws error at startup: NoUniqueBeanDefinitionException.

# fix 1 : Qulifiers

pick by name .

```java
@component
class notificationManager{
    private final MessageService ms;
    
    @Autowired
    NotificationManager(@Qulifier("emailService") MessageService ms){
        this.ms = ms;
    }
}
```

Defalt bean name = class name

first letter lowercaswe

EmailService -> bean name "emailService"

@Qulifier tells contanier explicitly use bean with this name

we can also rename bean at declare point @component("primaryEmail")

# fix 2 @Primary mark default winner

```java
@component 
@Primary
class EmailSerivce implements MessageService{

}
```

if no @qualifier specified anywhere

container defaults to EmailService whenever MessageService needed since marked primary

Qualifer on injection point still overrides primary if both present

### setter injection , spring way

Imagine you have a NotificationManager that prefers to use a MessageService, but can still function without one. 

However, you have two message service beans registered in your container: EmailService and SmsService.

```java
@Component
public class NotificationManager {

    private MessageService messageService;

    // Optional Setter Injection
    @Autowired(required = false) 
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }
}
```

first understand what is optional setter injection:

Unlike mandatory constructor injection, optional setter injection uses configurations like @Autowired(required = false) or Java’s Optional wrapper. This configuration tells Spring: "If this dependency exists in the application context, inject it; if it doesn't, skip it and keep running without throwing an exception.

How the Container Resolves This:-

When required = false is used, Spring's strategy changes based on whether it can resolve the ambiguity or not:

**Scenario A**: You provide a tie-breaker (Parameter Name, @Qualifier, or @Primary)

If you give Spring a way to choose, it will work perfectly. It ignores the required = false flag because a clear candidate exists.

Using Parameter Name: If you change the parameter name to match a bean name (e.g., setMessageService(MessageService emailService)), Spring injects EmailService.

Using @Qualifier: If you add @Qualifier("smsService"), Spring injects SmsService.

**Scenario B**: No tie-breaker exists (The Core Behavior)

What happens if you don't provide a tie-breaker? You might assume that because required = false, Spring will just skip the setter and leave the dependency as null.

This is a common misconception.Even if required = false, Spring still tries to resolve the dependency if it sees candidates available. 

Because it finds two candidates and cannot choose, the application will still crash at startup with a NoUniqueBeanDefinitionException.

Rule: required = false means "If there are zero beans of this type, skip this method." It does not mean "If there are too many beans, ignore them."

The Trick: How to make it Truly Optional with Multiple Candidates

1. Accept a List of All Candidates (Best Practice)

```java
@Component
public class NotificationManager {

    private List<MessageService> messageServices = new ArrayList<>(); // Default to empty list

    @Autowired(required = false)
    public void setMessageServices(List<MessageService> messageServices) {
        // If 0 beans exist, this setter is skipped, keeping the empty array list.
        // If 2 beans exist, this setter runs and passes a list containing BOTH Email and SMS.
        this.messageServices = messageServices; 
    }
}

```

2. Use Java's Optional wrapper

```java
@Component
public class NotificationManager {

    private MessageService messageService;

    @Autowired
    public void setMessageService(Optional<MessageService> messageServiceOpt) {
        // If a primary bean exists, it fills the Optional. If 0 beans exist, it becomes Optional.empty().
        this.messageService = messageServiceOpt.orElse(null); 
    }
}
```

### Field injection spring way

```java
@Component
class NotificationManager {
    @Autowired
    private MessageService messageService;
}
```

Container uses reflection, sets field directly, bypassing constructor/setter entirely — even bypasses private access modifier (reflection can force access). Works, but:

Can't make field final.
Testing hard — plain new NotificationManager() in unit test gives object with null dependency, no compiler error warning you.
Hides true dependency list — reading constructor signature no longer tells you what class needs.

Community + Spring docs recommend constructor injection as default choice. Setter for optional dependencies. Field injection avoid in real projects (fine for quick demo/learning).


## Bean scopes -- how many objects container actually keeps

default scope = singleton

one instance per container, shared everywhere cached

```java
@Component
@Scope("prototype")
class EmailService { ... }
```

prototype scope = everytime getbean called ( or injected fresh ) container builds new instance

no caching

what happens actually:==

singlton beans builts once during container startup (eager unless marks lazy)

and stored in cache map inside container. 

prototype bean not pre built at statup - container only stores beandefination blueprint

builts fresh actual object each time requested


why we need it: 

Thread Safety / Race Conditions: If Thread A and Thread B both modify the internal variables of a shared Singleton object at the same time, they will overwrite each other's data. A Prototype ensures each thread gets its own isolated instance.

differentiation: 

Singleton (Default): It is Stateless. It only contains logic, methods, and fixed configurations. It acts like a tool or a machine in a factory. Multiple workers (threads) can use the same machine at the same time because the machine itself doesn't change.

Prototype: It is Stateful. It holds temporary data, user inputs, or dynamic progress fields. It acts like a piece of paper. If multiple workers write on the same piece of paper at the same time, the data gets ruined. Each worker needs their own sheet.




## @Bean -- when you don't own the class

@component only works if you write class and can add annotation on top.

what if need make bean out of third-party class - say ObjectMapper from jackson library or datasource from jdbc driver

cant add component on their source

Fix: define bean manually inside @configuration class using @Bean on method

```java
@configuration
class AppConfig{
    @Bean
    ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

    @Bean
    DataSource dataSources(){
        HikariDataSource ds = new HikariDataSource();
        ds.setJDBCUrl("jdbc:postgresql://localhost:3306/mydb");
        ds.setUsername("root");
        ds.setPassword("pass")'
        return ds;
    }
}
```

method name objectmapper and datasource becomes bean name by dfault.

return value = object stored as bean in container , excat same as @component scanned bean , same registry , same scope, rules apply

Wiring @Bean methods togather(one depends on other)

```java
@Configuration
class AppConfig{

    @Bean
    EmailService emailService(){
        return new EmailService();
    }

    @Bean
    NotificationManager notificationManager(EmailService emailService){
        // param = tells container "inject bean here "
        return new NotificationManager(emailService);
    }
}
```

Container smart enough — sees notificationManager method wants EmailService param, looks up already-registered emailService bean

passes it in automatically

Same dependency resolution engine from above runs underneath, doesn't care if bean came from @Component scan or @Bean method — both end up as BeanDefinition in same registry


## Bean lifecycle - what happens after object built

Bean life got stages:

1. Instantiate (constructor called)

2. Dependencies injected (setter/field injection happens here — constructor injection already done at instantiate step)

3. @PostConstruct method called (if present) — hook, run custom init logic AFTER all dependencies set

4. Bean ready, sits in container, used by app

5. On container shutdown — @PreDestroy method called (if present) — hook, cleanup logic (close connections, release resources)

```java
package com.example.demo1;

import com.example.demo1.ConfigService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class EmailService{

        ConfigService config;

        @Autowired
        EmailService(ConfigService config){

                this.config = config;
        }

        @PostConstruct
        void init(){
                System.out.println("EmailService ready, config loaded");
        }

        @PreDestroy
        void cleanup(){
                System.out.println("Email Service shutting");
        }
}

```

Real use case: DB connection pool bean — @PostConstruct opens pool, @PreDestroy closes pool cleanly when app stops (prevents leaked connections).

for @Bean method style , same hooks specified differently

```java
@Bean(initethod = "init" . destroyMethod= "cleanup")
EmailService emailService(){
    return new EmailService();
}
```
Behind scenes note: container maintains internal list of singleton beans needing destroy callback. On context.close() call (or JVM shutdown hook if registered), container loops list, calls each @PreDestroy/destroy method, in reverse-of-creation order roughly, then releases bean registry.


**where does we use postconstrcut**

in real-world Java applications, the @PostConstruct annotation is used for "last-mile" setup. It triggers a method to run automatically after Spring has injected all dependencies (@Autowired), but before the bean is put to work in the application.

1. Caching Static Database Data on Startup
2. Formulating Complex Properties (Data Derived from @Value)
3. Establishing External System Connections
4. Registering a Bean into a Shared Registry (The Plugin Pattern)

we gonna have doubt why not just use a constrcutor

reason:

in Spring, constructors run before dependencies are injected. 

If you try to call countryRepository.findAllCountryNames() inside a constructor, your application will crash with a NullPointerException because countryRepository hasn't been injected yet.

@PostConstruct guarantees that everything Spring manages is fully ready for you to use.

### @predestroy where does it being used

graceful cleanup

it triggers a method to run automatically right before spring destroy bean and remove it from memory

1. releasing active network and database connection
2. Flashing saving in memory data to disk
3. gracefully stopping background thread and tasks
4. deregistering from a service registry

it is highly reliable during standrad shutdown however it will not run if the application crash violently 

or bean is scoped as prototype bean. spring manages the creation of prototype bean but it hands them off you and does not track their destruction
