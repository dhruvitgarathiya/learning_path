# Request LifeCycle

spring mvc: front controller pattern , one servlet guard whole app, not hundred servlet each own url( old servlet way ) one gate one brain routes everywhere

**Request enters => dispatcherServlet**

Browser hit POST /api/users. Request land at web server (Tomcat, embedded later in Boot). Tomcat check web.xml (or Boot's auto-config) — who handle URL pattern /?

Answer: DispatcherServlet. Mapped as front controller for all requests (or /api/* etc, depend config).

DispatcherServlet extends HttpServlet — same base class you studied in Servlets

Only difference: instead writing business logic inside doGet/doPost yourself like raw servlet days, DispatcherServlet job is dispatch only — find right handler, delegate, get result back, render response.

Itself does zero business logic.

DispatcherServlet boot up with WebApplicationContext (Spring's IoC container, subclass of ApplicationContext

hold all beans: controllers, view resolvers, converters, everything registered.

DispatcherServlet internally hold reference list of:

HandlerMapping beans (find controller)
HandlerAdapter beans (know how invoke controller correctly)
ViewResolver beans
HandlerExceptionResolver beans

All discovered at startup, stored in fields inside DispatcherServlet


# HandlerMapping - finding right controller

**why needed**

DispatcherServlet know nothing 'bout your controllers itself. Need someone map URL → method. That someone = HandlerMapping.

***RequestMappingHandlerMapping***

Job: scan all beans marked @Controller/@RestController, read their @RequestMapping/@GetMapping/@PostMapping annotations, build internal map: URL pattern + HTTP method → target method


### How build map (behind scene)

At app startup, RequestMappingHandlerMapping (itself a bean, registered by Spring auto-config)

loop through all beans in ApplicationContext

For each bean check class-level @RequestMapping("/api/users") + method-level @GetMapping("/{id}")

Combine → full path /api/users/{id} + GET

Store in internal registry (MappingRegistry class holds this).

So when request GET /api/users/5 come, map already built (not searched live each time from scratch — pre-indexed at startup for speed)

ex.

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) { ... }

    @PostMapping
    public User createUser(@RequestBody User user) { ... }
}
```

Startup: RequestMappingHandlerMapping register:

GET /api/users/{id} → UserController.getUser()
POST /api/users → UserController.createUser()


Request come POST /api/users → DispatcherServlet ask HandlerMapping: "who handle this?"
==>
HandlerMapping return HandlerExecutionChain — wrap target method + any interceptors (auth check, logging etc, chain before/after logic


**Why not just one big if-else**

Old servlet way = one servlet, manual if-else URL check inside doPost. Messy, no scale.

HandlerMapping = clean registry, O(1)-ish lookup, supports pattern matching ({id}, wildcards), pluggable (can write custom HandlerMapping for special routing need).

# HandlerAdepter - Controller execution

HandlerMapping only find right method (like getUser(Long id)).

But DispatcherServlet can't just call method.invoke() blind --

need figure out: where params come from? @PathVariable? @RequestBody? Return type — JSON? View name?

That figuring-out job = HandlerAdapter.

Think of it translator between generic DispatcherServlet and your specific method signature.

Common impl: RequestMappingHandlerAdapter.

how param resolve

HandlerAdapter use bunch HandlerMethodArgumentResolver classes internally. Each resolver expert one annotation type:

PathVariableMethodArgumentResolver → handle @PathVariable

RequestParamMethodArgumentResolver → handle @RequestParam

RequestResponseBodyMethodArgumentResolver → handle @RequestBody (this one delegate to HttpMessageConverter, e.g. Jackson, convert JSON → Java object)

For your getUser(@PathVariable Long id) — adapter loop resolvers, ask each "you handle this param?" PathVariableMethodArgumentResolver say yes, pull id value from URL (/api/users/5 → 5), convert String → Long, pass in.

```java
@PostMapping
public User createUser(@RequestBody User user) {
    return userService.save(user);
}
```

Request body raw JSON: {"name":"Rahul","email":"r@x.com"}.

handlerAdepter see @requestBody ==> delegate RequestResponseBodyMethodArgumentResolver 
===>
that call httmessageConverter (Jackson's mappingJackson2HttmpMessageConveter) -> JSON bytes desrlialize into User object -> pass as method param.

method run = call userService.save(User) -- pure buisness logic

return value user object come back to HandlerAdapter.

why separate adapter from mapping

mapping = "who handle this" (routing decision)

adapter = "how to invoke them"(execution mechanics)

separation let spring support different controller styles (old school controller interface, annotation based @controller , functional endpoints) each own adapter, same mapping concept resued

# return value handling - view Resolver vs httpmessageConverter

controller method return , two path possible:

@RestController (or @ResponseBody on method) -> return value = data ifeself (JSON/XML) send straight as response body

Plain @Controller -> return value = view name String, need resolve to actual HTML template, render , send html

DispatcherServlet check : does handler have @ResponseBody semantics? (@RestCOntroller = @Controller + @ResponseBody combined)

**Path A: REST / JSON (most modern APIs use this)**

return object (e.g. User) go to HandlerMethodReturnValueHandler chain 

--> 

similar resolver pattern like params but reverse direction

RequestResponseBodyMethodProcessor pick it up, call HttpMessageConverter.write() — Jackson serialize User object -->

JSON bytes → write direct to HttpServletResponse output stream.

Done, no view, no template. Response go back to Tomcat → browser.

Path B: Traditional HTML / ViewResolver 

Controller return String like "userProfile" (view name, not JSON).

```java
@Controller
public class UserController {
    @GetMapping("/profile/{id}")
    public String getProfile(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.find(id));
        return "userProfile"; // logical view name
    }
}
```

DispatcherServlet take "userProfile" string ->

pass to ViewResolver bean (e.g. InternalResourceViewResolver or ThymeleafViewResolver). Resolver job: turn logical name → actual View object.

Config might say prefix /WEB-INF/views/ + suffix .jsp -->

esolve "userProfile" → /WEB-INF/views/userProfile.jsp.

View.render() called — merge Model data (the user object you added) into template

generate final HTML → write to response.


***Why two path exist***

Old Spring MVC (pre-REST era) built HTML-rendering-first — server render page, browser just display

REST/JSON came later, bolt on via @ResponseBody without breaking old flow

Now industry mostly @RestController (frontend separate — React/Angular consume JSON), but old MVC-view flow still used server-rendered apps (Thymeleaf etc)

good know both exist behind same DispatcherServlet engine.


# Base form — @RequestMapping

```java
@RestController
@RequestMapping("/api/users")   // class-level: common prefix
public class UserController {

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User getUser(@PathVariable Long id) { ... }
}
```

Class-level @RequestMapping("/api/users") set base path — every method inside inherit prefix

Combine with method-level path → full URL.

RequestMappingHandlerMapping read both levels, concatenate, register in MappingRegistry.

method = RequestMethod.GET — without this, @RequestMapping match any HTTP method (GET, POST, whatever) same URL

dangerous, ambiguous, bad practice. That's exact reason shortcuts born.


why:
@GetMapping, @PostMapping, @PutMapping, @DeleteMapping, @PatchMapping — introduced Spring 4.3. Each literally meta-annotation wrap @RequestMapping with method pre-fix.

```java
@RequestMapping(method = RequestMethod.GET)
public @interface GetMapping { ... }
```

So @GetMapping("/{id}") = shorthand @RequestMapping(value = "/{id}", method = RequestMethod.GET). Same registry entry result, just cleaner write, less error-prone (forget method= = common old bug).

same URL /api/users/{id} used by GET, PUT, DELETE — no clash, because HandlerMapping registry key = URL + HTTP method combo, not URL alone. That's why method-specificity matter so much.

## REST 

REST (Representational State Transfer) is an architectural style used to design networked applications

It is not a programming language or a piece of software, but rather a set of rules and constraints that dictates how computers should share data over the internet.

In simple terms, instead of creating custom, complex endpoints for every single action a web page might take, REST structures communication around resources (nouns) using standard HTTP protocols.


Before REST, developers built APIs by creating custom endpoints for every specific action (e.g., /getUser, /createNewUser, /deleteUser). REST standardizes this by focusing on the resource (e.g., /users) and using HTTP methods to define the action:

GET /users → Retrieve a list of users.
POST /users → Create a new user.
PUT /users/123 → Update/replace user #123.
DELETE /users/123 → Remove user #123


**why rest design lean on this heavy**

REST convention: same resource URL, different verb = different action.

/api/users/5 — GET read, PUT replace, PATCH partial update, DELETE remove. Shortcut annotations map 1:1 to REST verbs, make controller read like REST contract itself — self-documenting

consumes = "application/json" — restrict which Content-Type accepted

produces = "application/json" — restrict which Accept response return

params = "active=true" — match only if query param present

headers = "X-Api-Version=1" — match on custom header

example : two method same url , differ by header / version - advanced routing , useful api versioning

```java
@GetMapping(value = "/{id}", produces = "application/json")
public User getUserJson(@)PathVariable Long id){...}
```

All These annotations whatever form funnel same place - 
RequestMappingHandlerMapping build RequestMappingInfo object per method (hold URL pattern, HTTP method, consumes, produces, params — full match criteria)

store in MappingRegistry. Lookup at request time = match RequestMappingInfo against incoming request, pick winner.

```java
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProductController {

    // 1. consumes = "application/json"
    // This method will ONLY trigger if the client sends Content-Type: application/json
    @PostMapping(value = "/products", consumes = "application/json")
    public String createProduct(@RequestBody Map<String, Object> productData) {
        return "Product created successfully from JSON payload.";
    }

    // 2. produces = "application/json"
    // This method will ONLY trigger if the client requests Accept: application/json
    @GetMapping(value = "/products/{id}", produces = "application/json")
    public Map<String, String> getProductDetails(@PathVariable String id) {
        return Map.of("id", id, "name", "Sample Product", "status", "active");
    }

    // 3. params = "active=true"
    // This method will ONLY trigger if the URL contains "?active=true"
    // Example: GET /api/products?active=true
    @GetMapping(value = "/products", params = "active=true")
    public String getActiveProducts() {
        return "Returning a list of all active products.";
    }

    // 4. headers = "X-Api-Version=1"
    // This method will ONLY trigger if the request header "X-Api-Version" equals "1"
    @GetMapping(value = "/products/info", headers = "X-Api-Version=1")
    public String getLegacyProductInfo() {
        return "Returning product information from API Version 1.";
    }
}

```

1. The Core Mechanism: Conversion, Not ComparisonSpring MVC never compares JSON to Java objects.Instead, it converts (deserializes) the incoming JSON string into Java structures.This is handled automatically behind the scenes by a library called Jackson ObjectMapper via Spring's MappingJackson2HttpMessageConverter.

2. Structural Alignment RuleYou cannot use just any Collection Framework type. The Java data structure must match the shape of the incoming JSON data:JSON Objects { "key": "value" } → Can only be converted into a Java Map<String, Object> (specifically a LinkedHashMap) or a custom POJO/DTO.JSON Arrays [ "a", "b" ] → Can only be converted into a Java List<T>, Set<T>, or a standard Java array.If you try to map a JSON Object into a List, Jackson will crash and throw an error (HTTP 400 Bad Request).

3. Key Limitations of Using Map<X, Y>While using a Map provides quick flexibility, it comes with downsides:Key Restrictions: The key (X) must always be a String because JSON keys are always strings.Loss of Type Safety: You must use Object as the value (Y) for mixed data types, forcing you to manually cast variables later (e.g., (String) map.get("name")).Nested Complexity: Sub-objects inside the JSON will automatically become nested Map<String, Object> structures, making data extraction messy.

# Request parameter fetching

Request carry data many different places — URL query string, URL path itself, request body, form fields

Each place different extraction mechanism needed.

Spring give one annotation per source — behind scene (HandlerAdapter) — each annotation handled by dedicated HandlerMethodArgumentResolver

1. @RequestParam - query params

**What is a Query Parameter?**

A Query Parameter (or Request Param) is data appended to the end of a URL after a question mark (?). Multiple parameters are separated by the ampersand (&).

Example URL: http://localhost:8080/api/products?category=electronics&limit=10

Here, category and limit are query parameters. They are primarily used for filtering, sorting, or paginating data.


URL: GET /search?query=java&page=2

```java
@GetMapping("/search")
public List<Result> search(
    @RequestParam String query,
    @RequestParam(defaultValue = "0") int page
) { ... }

// http://localhost:8080/search?query=laptop&page=2 
```

RequestParamMethodArgumentResolver grab from HttpServletRequest.getParameter("query") -- plain servlet api.

string "2" auto convert to int via conversionService 

defaultValue handle missing param gracefully - no NullPointerException risk if not supply page

Also form-submit (application/x-www-form-urlencoded) data land here too — same getParameter() underneath don't care GET query string or POST form body, both readable same way at servlet level.

Optional param: @RequestParam(required = false) String filter — else missing param = 400 Bad Request auto-thrown.


2. @PathVariable - URI path segments

URL =  GET /users/5/orders/99


```java
@GetMapping("/users/{userId}/orders/{orderId}")
public Order getOrder(
    @PathVariable Long userId,
    @PathVariable Long orderId
) { ... }
```

HandlerMapping already parse URL pattern into {userId}, {orderId} placeholders at startup, store as UriTemplate

When request come, 

PathVariableMethodArgumentResolver pull matched segment values from UriTemplateVariables map attached to request (Spring stash it as request attribute during mapping phase), 

convert String → Long.

3. @RequestBody — JSON → Java object

if this client sends:

POST /api/users
Content-Type: application/json

{"name":"Rahul","email":"r@x.com","age":25}


```java
@PostMapping
public User create(@RequestBody User user) { ... }
```

- RequestResponseBodyMethodProcessor (this IS HandlerMethodArgumentResolver for @RequestBody) intercept.

- Check Content-Type header → application/json.

- Loop registered HttpMessageConverter list, find one support JSON — MappingJackson2HttpMessageConverter (Jackson library, added auto if on classpath).

Jackson read raw request body InputStream, map each JSON key →

matching Java field by name ("name" → user.name, case-sensitive match by default).

Use reflection + no-arg constructor + setters (or field access) — need class have public no-arg constructor + setters, else Jackson fail (InstantiationException) — common bug reason.


small example for this exception : 

```java
java// ==========================================
// ❌ THE BROKEN WAY (Throws Exception)
// ==========================================
public class UserRequestBroken {
    private String name;
    private int age;

    // Custom constructor eliminates the default constructor.
    // Jackson will CRASH because it cannot call "new UserRequestBroken()"
    public UserRequestBroken(String name, int age) {
        this.name = name;
        this.age = age;
    }
}

// ==========================================
//  THE TRADITIONAL FIX (Standard Java POJO)
// ==========================================
public class UserRequestFixed {
    private String name;
    private int age;

    // 1. MANDATORY: Empty constructor so Jackson can create the blank object shell
    public UserRequestFixed() {}

    // 2. OPTIONAL: Kept for your own code use
    public UserRequestFixed(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // 3. MANDATORY: Setters so Jackson can inject values by matching key names
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }

    public String getName() { return name; }
    public int getAge() { return age; }
}

// ==========================================
//  MODERN APPROACH 1: Java Records (Java 16+)
// ==========================================
// Best choice. No setters or blank constructors needed. 
// Jackson automatically understands how to map JSON keys to Record components.
public record UserRequestRecord(String name, int age) {}

// ==========================================
//  MODERN APPROACH 2: Lombok Annotations
// ==========================================
// Generates getters, setters, and the required blank constructor behind the scenes.
@Data
@NoArgsConstructor  // Crucial for Jackson!
@AllArgsConstructor
public class UserRequestLombok {
    private String name;
    private int age;
}
```

- return the populated User object

If JSON malformed or field type mismatch ("age":"abc" expect int) → HttpMessageNotReadableException thrown, Spring auto-return 400 Bad Request.


4. @ModelAttribute - form data -> java object

Traditional HTML form (not JSON, not JS fetch):

```html
<form method="POST" action="/register">
  <input name="name" />
  <input name="email" />
  <input name="age" />
</form>
```

```java
@PostMapping("/register")
public String register(@ModelAttribute User user) { ... }
```

ModelAttributeMethodProcessor grab all request params (getParameterMap() — same servlet-level source as @RequestParam, just bulk-collected instead one-by-one), match each param name → matching setter method on User object

via data binding (WebDataBinder class involved — reflection-based, call setName(), setEmail(), setAge() automatic)

Key difference vs @RequestBody: @ModelAttribute read application/x-www-form-urlencoded key-value pairs (flat form fields), @RequestBody read raw JSON body (structured, nested-object capable)

real project example

```java
@GETMapping("/api/users/{id}/orders")
public List<Order> filterOrders(
    @PathVariable Long id,                        // which user
    @RequestParam(defaultValue = "10") int limit,  // how many
    @RequestBody OrderFilter filter                // complex filter criteria JSON
) {
    return orderService.filter(id, limit, filter);
}
```

### methods vs what it can't handle

**GET**

Can't reliably carry request body — reason: spec say body "no defined semantics" for GET, browsers/proxies/load balancers strip it before reach server (learned two topic back)

Can't send large/complex data — reason: forced rely URL query string only (since body unreliable), URL length limit (~2000 char typical), nested/structured data awkward cram into query string

Can't perform create/update/delete safely — reason: spec define GET as safe + idempotent (no side effect promise), infra (CDN, browser back-button, crawlers) assume calling GET repeat is harmless, auto-retry/prefetch/cache GET freely — if GET secretly change data, break that assumption, cause bug (e.g. browser prefetch accidentally trigger delete)

HEAD: Asks for the same response headers as a GET request, but without the actual response body. Use this to check file sizes or see if a resource exists before downloading it.

Can't return body at all — reason: spec define HEAD = "same as GET but response body always omit," used only check headers/existence, meta-info, never actual content

Can't carry request body — same reason as GET (identical semantic restriction, HEAD basically GET's twin)


**Delete**

Can't reliably carry meaningful request body — reason: like GET, spec historically say body "no defined semantics" for DELETE too (though newer RFC loosen slightly), most server framework/infra still don't expect/parse DELETE body, risky rely on it

Can't be assumed safe repeat without side-effect awareness — reason: DELETE idempotent (call twice = same end state, resource gone both time) but NOT safe (first call always cause real change) — different guarantee than GET, mixing up cause bug assumption


TRACE: Performs a message loop-back test. Use this to see if any intermediate proxies or servers are changing your request during transit (though often disabled for security)

Can't carry any body — reason: spec explicit forbid, TRACE = pure diagnostic echo-back method, request bounce back as-is for debug, body break the echo purpose, security risk too (can leak header/cookie info) — many server disable entire TRACE method by default


OPtions : Asks the server what communication options and methods are allowed for a specific resource. Use this for CORS (Cross-Origin Resource Sharing) preflight checks.

Can't carry meaningful request body typically — reason: purpose = ask server "what method/capability you support," pure metadata query, no data-handling semantics defined, adding body pointless, ignored by convention

Can't perform any actual operation — reason: spec strict define OPTIONS = query capability only, never trigger real action, safe + idempotent, side-effect strictly forbidden by design

**PUT**

Can't do partial update properly — reason: spec define PUT = full replace resource, must send complete representation, missing field = treat as field wiped/null (not "leave unchanged"), common bug when dev assume partial update work like PATCH

Can't skip specifying resource identity — reason: PUT need know exact resource URL replace (/users/5), can't PUT to collection URL (/users) meaningfully — spec expect client know ID already, unlike POST where server can generate ID

**PATCH**

 Applies partial updates to a resource. (Often grouped with the main methods now, but worth noting if your "famous four" meant just GET/POST/PUT/DELETE)

 Can't guarantee idempotency by spec — reason: unlike PUT, PATCH spec don't force idempotent guarantee (depend how patch document written — e.g. "increment counter by 1" patch = NOT idempotent, repeat = different result each time) — infra can't safely auto-retry PATCH assuming safe, unlike PUT/GET

 Can't have universal body format — reason: spec don't mandate single patch format, multiple competing standard exist (JSON Patch, JSON Merge Patch, plain partial JSON) — client/server must agree format beforehand, no built-in universal parse like @RequestBody JSON usual case



# bean annotations in spring mvc

core annotations:

```java
public class User {
    @NotNull(message = "name required")
    private String name;

    @Size(min = 2, max = 30)
    private String username;

    @Email
    private String email;

    @Min(18)
    @Max(100)
    private int age;

    @NotBlank
    private String password;
}
```

@NotNull — value not null (empty string ok)
@NotBlank — string not null, not empty, not just whitespace
@NotEmpty — collection/string not null, not empty (whitespace ok)
@Size(min=, max=) — length/collection size bounds
@Min / @Max — number bounds
@Email — valid email format
@Pattern(regexp = "...") — regex match
@Past / @Future — date checks


**Trigger in controller with @Valid:**

```java
@PostMapping("/users")
public ResponseEntity<?> createUser(@Valid @RequestBody User user, BindingResult result) {
    if (result.hasErrors()) {
        return ResponseEntity.badRequest().body(result.getAllErrors());
    }
    // save user
    return ResponseEntity.ok(user);
}
```

Without @Valid — annotations sit there, do nothing. Spring skip validation.

Flow:

Request body → Spring bind to User object
@Valid trigger check against all annotations
Fail → BindingResult catch errors (no BindingResult param → Spring throw MethodArgumentNotValidException instead, 400 response auto)

**Global error handling, cleaner than BindingResult everywhere:**

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
          .forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }
}
```

## ResponseEntity<T> wrapper

Control status code, headers, body all together. Instead of just returning object (Spring auto 200 always), you pick exact response.

Why need it: Plain return type—

```java
@GetMapping("/{id}")
public User getUser(@PathVariable Long id) {
    return userService.find(id); // always 200, even if null/not found
}
```

Problem: user not found → still 200 OK, body null. Bad API design.

Fix with ResponseEntity:

```java
@GetMapping("/{id}")
public ResponseEntity<User> getUser(@PathVariable Long id) {
    User user = userService.find(id);
    if (user == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    return ResponseEntity.ok(user); // 200 + body
}
```

Common status builders:

ResponseEntity.ok(body) — 200
ResponseEntity.status(HttpStatus.CREATED).body(x) or .created(uri) — 201
ResponseEntity.noContent().build() — 204
ResponseEntity.badRequest().body(errors) — 400
ResponseEntity.status(HttpStatus.NOT_FOUND).build() — 404
ResponseEntity.status(HttpStatus.CONFLICT).body(msg) — 409, e.g. duplicate entry


## Gloabal exception handling

writing try catch everywhere is messy

bad duplicate code everymethod change error format later -> edit 50 place

solution: 

pull all catch logic out, one place

two tools:

1. @exceptionHandler - catch method, sit inside one controller , catch errors only for that controller

2. @RestControllerAdvice - catch class , sit outside all controllers , catch errors from whole app, one place

ex. @ExceptionHandler - local , one controller only

```java
@RestController
public class UserController{

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id){
        return userService.find(id); //throws exception if not found here, no try catch here
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleNotFound(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
```




 