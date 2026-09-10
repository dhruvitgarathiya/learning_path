# Why Spring Boot exist — problem it solve

Plain Spring MVC setup, old way — pain:

* write web.xml or DispatcherServlet config by hand
* write XML bean config or heavy @Configuration class
* pick + match jar versions yourself (Spring version + Jackson version + Hibernate version — mismatch = crash)
* setup embedded server yourself (Tomcat) or deploy WAR to external server

Spring Boot fix all this. Give you:

1. Auto-configuration — guess what you need, configure automatic, based on jars found in classpath.
2. Starter dependencies — one dependency (spring-boot-starter-web) pull whole compatible set, no version clash.
3. Embedded server — Tomcat built inside jar, run java -jar app.jar, no external server install need.
4. Opinionated defaults — sensible defaults everywhere, override only what you need.

Core idea: Spring Boot not replace Spring. Sit on top, remove boilerplate.


Step 2: Minimum project structure

```
src/main/java/com/example/demo/
    DemoApplication.java
src/main/resources/
    application.properties
pom.xml (or build.gradle)
```

Step 3: Entry point — main method

```java
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

Behind scenes: @SpringBootApplication = combo of 3 annotations mashed together:

@SpringBootConfiguration  // marks this as config class (like @Configuration)
@EnableAutoConfiguration  // trigger auto-config magic
@ComponentScan            // scan this package + sub-packages for @Component, @Service, @Controller etc


SpringApplication.run(...):

Create Spring container (ApplicationContext).
Trigger component scan — find all beans.
Trigger auto-configuration — check classpath, guess setup (found spring-web jar? → configure DispatcherServlet auto).
Start embedded Tomcat.
App ready, listening port 8080 default.


Step 4: build.gradle — starter dependency

```java
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.3.0</version>
</parent>

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

spring-boot-starter-web alone pull: Spring MVC, Jackson (JSON convert), embedded Tomcat, validation lib — all compatible version, no manual matching.


### Going with layered project structure — Controller → Service → Repository.


Step 1: Why layers

One class do everything = mess. Split job:

Controller — handle HTTP, receive request, send response. No business logic here.
Service — business logic, rules, decisions. No HTTP knowledge, no SQL knowledge.
Repository — talk to DB only. No business logic here.

Each layer only know layer below. Controller call Service, Service call Repository. Repository never call Service back.



