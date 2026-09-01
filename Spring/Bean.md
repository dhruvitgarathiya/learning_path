
### what is bean := 

Beans are indeed java objects. 

What's specific about them is that they are managed by Spring IoC container. IoC means "Inversion of Control". That is, if you have an object which relies on other objects to function properly, you let Spring handle the relationships (dependencies) between these objects instead of doing it yourself.



### ApplicationContext container / Bean container

Tomcat (Servlet Container) manages the communication between the outside world and your app, while the ApplicationContext (Spring Container) manages the internal business logic and objects of your app.

They do not compete with each other; instead, they work together in a parent-child relationship.

Feature                     Tomcat(Servlet Container)                 Spring ApplicationContext 
Primary Focus                Network & Web Lifecycle                 Business Logic & Dependency Injection

What it manages              Servlets, Filters, Listeners,           Spring Beans (@Service, @Repository, @Component)
                               and HTTP sessions.

Trigger Point                Triggered by HTTP                       Triggered by Internal Java code and method calls.
                             requests (e.g., GET, POST).

Protocol Aware              Yes, it natively speaks HTTP             No, it is purely a Java object manager.


What Does ApplicationContext Do?

ApplicationContext is the heart of the Spring IoC (Inversion of Control) Container. It handles object management:

Dependency Injection (DI): It instantiates your classes (beans), wires them together, and manages their lifetimes so you don't have to manually write new MyService() everywhere

Enterprise Features: It provides cross-cutting features like declarative transaction management (@Transactional), AOP (Aspect-Oriented Programming), and security.


How They Work Together (The Architecture)

When you run a modern Spring or Spring Boot web application, Tomcat actually wraps around Spring.

Tomcat starts up and initializes a special, single servlet provided by Spring called the DispatcherServlet.

During its startup, this DispatcherServlet triggers the creation of the Spring ApplicationContext.

When a user sends an HTTP request, Tomcat catches it and hands it over to Spring’s DispatcherServlet.Spring looks inside its ApplicationContext to find the right @RestController bean to process the business logic


### Bean factory

A BeanFactory is the fundamental engine underneath everything. It is the root interface that defines Spring’s core inversion of control (IoC) and dependency injection features.

To put it simply: ApplicationContext is just a advanced, feature-rich wrapper around a BeanFactory.

ApplicationContext actually extends the BeanFactory interface.

BeanFactory (The Core Engine): It reads configuration files, instantiates Java objects (beans), handles lazy loading, and injects dependencies. 

It does the bare-minimum work.

ApplicationContext (The Enterprise Wrapper): It includes the BeanFactory but adds production-ready features like internationalization (i18n), event propagation, automated @Component scanning, and easy integration with Spring AOP and Web frameworks.

In 99% of modern Spring and Spring Boot applications, you will never interact with BeanFactory directly. You always use ApplicationContext, which manages the BeanFactory under the hood.

### applicationcontext is implementation of beanfactory interface then why beanfactory lazy loads it but applicationcontext eager loads the bean what is reason , their behaviour should be same?

1. Catching Configuration Errors at Startup (Fail-Fast):

If a bean is lazy-loaded, Spring only tries to create it when a user triggers a piece of code that needs it.

The Problem: If you made a typo in your database password, or forgot to add @Autowired correctly, the application will start up fine. But hours later, when a real customer clicks a button, the app will suddenly crash with a BeanCreationException.

The ApplicationContext Solution: By eagerly loading everything at startup, ApplicationContext validates your entire dependency graph immediately. If a bean is missing or misconfigured, the application fails to start. It is much better for a deploy script to fail than for a live customer to experience a crash.

2. Eliminating First-Request LatencyThe Problem: In a lazy-loaded system, the very first user to visit a specific webpage or hit an API endpoint experiences a lag. They have to wait while the server instantiates the Controller, instantiates the Service, creates the Database Connection, and wires them together.

The ApplicationContext Solution: Server applications prioritize fast response times for users. By doing all the heavy lifting (eager loading) during the startup phase, the beans are already sitting in memory. When the first user hits the API, the response is instant.

How it works technically (The Code Reality):-

In Java, an interface only defines what methods exist, not how those methods behave under the hood.

When ApplicationContext starts up, it actually uses a standard BeanFactory implementation (usually a class called DefaultListableBeanFactory) underneath.

Once the ApplicationContext finishes reading all your configuration classes, it explicitly calls a method on that internal BeanFactory called:javabeanFactory.preInstantiateSingletons();

This single line of code tells the underlying BeanFactory: "Hey, I know your default behavior is to wait, but because I am an enterprise ApplicationContext, go ahead and instantiate all non-lazy singletons right now."



## @ComponentScan

spring framework exactly where to search for classes that should be managed as beans by ioc container

instead of manually declaring every bean in a configuration file, Spring scans your project packages to automatically discover and register these classes, enabling seamless Dependency Injection (DI)

 How it Works Under the Hood:

When your application starts, Spring activates a classpath "radar". It inspects the classes in your designated packages looking for specialized class-level annotations known as Stereotype Annotations:

@Component : The root, generic stereotype for any Spring-managed Java class.

@Service: Specialization for business logic layers.

@Repository: Specialization for data access and persistence layers.

@Controller / @RestController: Specialization for web and API layers.

Whenever Spring finds a class with any of these annotations, it dynamically instantiates it and stores it in the ApplicationContext.

In Spring Boot: You rarely need to write @ComponentScan explicitly. The starter annotation @SpringBootApplication implicitly includes @ComponentScan behind the scenes. By default, it scans the package containing the main class and all of its sub-packages.

in core-Spring: You must pair @ComponentScan alongside a @Configuration class to enable auto-detection.

