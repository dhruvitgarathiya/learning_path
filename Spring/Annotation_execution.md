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