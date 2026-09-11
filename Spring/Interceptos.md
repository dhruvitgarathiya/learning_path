What interceptor is, why exist.

Interceptor = hook point. Sits between DispatcherServlet and controller. Catch request before controller run, catch response after controller run, before view render.

```java
boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
```

preHandle — run before controller method. Return false → stop chain, controller never called. Good for auth check, logging start time.

postHandle — run after controller method, before view rendered. Can touch ModelAndView. Rare use now (mostly REST, no view).

afterCompletion — run after full request done (view rendered too), even if exception thrown. Good for cleanup, logging total time, MDC clear.

Why need this, not just write code inside every controller? Because cross-cutting stuff — logging, auth check, request-id tagging — needed on MANY endpoints.

Don't want copy-paste same code in every controller method. Interceptor = one place, catch many routes.

scenirio: REST API, want log every request (method, URI, time taken) and check custom header X-Auth-Token before hitting controller.

```java
@Component
public class LoggingAuthInterceptor implements HandlerInterceptor{
    private static final Logger log = LoggerFactory.getLogger(LoggingAuthInterceptor.class);

    @Override
    public boolean preHandle(HtttpServletRequest request, HttpServletResponse response,Object Handler){
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);


        String token = request.getHeader("X-Auth_Token");
        if(token == null || !token.equals("secret123")){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            log.warn("Unauthorized access attempt: {}", request.getRequestURI());
            return false;
        }
        log.info("Incoming request : {} {}" , request.getMethod(), request.getRequestURI());
            }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,Object handler, Exception ex ){
        long startTime = (Long) request.getAttribute("startTime");
        long duration = System.currentTimeMillies() - startTime;
        log.info("Completed {} in {} ms", request.getRequestURI(),duration);
    }
}
```

step 2 register it cause interceptor alone do nothing must plug into WebMvcConfigurer:

```java
@Configuration
public class WebConfig implements WebMvcConfigurer{
    @Autowired
    private LoggingAuthInterceptor loggingAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(logginAuthInterceptor)
        .addPathPatterns("/api/**)
        .excludePathPatterns("/api/public/**");
    }
}
```

addPathPatterns / excludePathPatterns — control exact URLs interceptor touch. Auth check needed on /api/** but not /api/public/** (login endpoint etc).

This targeting = one big reason team pick interceptor over global filter (filter harder to target Spring-level route info, since filter run at servlet level, before Spring even map handler).





Filter va Interceptor:


Filter (javax.servlet.Filter / jakarta.servlet.Filter):

Part of Servlet spec, not Spring. Run by servlet container (Tomcat etc), BEFORE request even reach DispatcherServlet.

No knowledge of Spring handler, controller, @RequestMapping info. Only see raw HttpServletRequest/HttpServletResponse.

Good for: low-level stuff — CORS, gzip compression, request/response wrapping, character encoding, security (Spring Security itself built on filter chain!), rate limiting at very edge.


CORS:(Cross-Origin Resource Sharing) in Spring Boot is a security mechanism that allows your backend server to define who can safely request its data from a web browser

. By default, browsers enforce the Same-Origin Policy, meaning a frontend app running on http://localhost:3000 (like React) is blocked from reading data from a Spring Boot backend running on http://localhost:8080 because the ports are different. 

When a frontend app makes a cross-origin request, the browser sends a quick handshake or checks the response for specific HTTP headers

Configuring CORS in Spring Boot tells your backend to attach these headers (like Access-Control-Allow-Origin), telling the browser: "Yes, I trust this frontend, let the data through."


```java
@Component
public class CorsFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        chain.doFilter(req, res); // pass to next filter/servlet
    }
}
```

Interceptors:

Spring MVC concept. Run AFTER DispatcherServlet pick which controller/handler gonna handle request, BEFORE that handler run.

Know handler details — can grab @RequestMapping annotations, controller class, method name via handler param (cast to HandlerMethod).

Good for: business-level cross-cutting — auth by role, audit logging tied to specific controller, request-id per business flow, checking custom annotations on controller method (like @RequireAdmin).

Key real trade-off table

Need touch raw request before Spring even route it (encoding, CORS, security chain) → filter.

Need know WHICH controller/method gonna run, or want use Spring beans easy (inject @Autowired service) → interceptor.

Filter can also use Spring beans if registered as bean, but interceptor built for this, cleaner.

Filter chain order harder control (@Order annotation, sometimes messy). Interceptor order easy — just list order in addInterceptors

Exception handling: interceptor afterCompletion see exception object directly. Filter must wrap manually or rely on try/catch around chain.doFilter.

interceptor + custom annotation — production pattern.

mark controller method with @RequireAdmin, interceptor check role only on marked methods, skip rest. Cleaner than URL pattern matching when logic tied to method, not path.

Step 1 — make annotation:

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireAdmin {
}
```

Step 2 — use on controller:

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/profile")
    public String getProfile() {
        return "user profile data";
    }

    @RequireAdmin
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        return "deleted user " + id;
    }
}
```

interceptor read annotation via handlerMethod:
```java
@Component
public class AdminCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true; // not controller method, skip (e.g. static resource)
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RequireAdmin annotation = handlerMethod.getMethodAnnotation(RequireAdmin.class);

        if (annotation == null) {
            return true; // no annotation, no admin check needed
        }

        String role = request.getHeader("X-User-Role");
        if (!"ADMIN".equals(role)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        return true;
    }
}
```

Why this better than URL pattern: URL pattern (/api/users/**) block whole path, can't say "only DELETE need admin, GET don't" easy without complex path config

Annotation put decision right on method, interceptor just read it. Very common real pattern — Spring Security itself use similar idea (@PreAuthorize).

Key line: handler instanceof HandlerMethod — always check this first, since handler can be non-controller thing (static resource handler etc), crash if cast blind.

multiple interceptors — order + common gotchas.

```java
@Override
public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(loggingInterceptor).order(1);
    registry.addInterceptor(authInterceptor).order(2);
    registry.addInterceptor(adminCheckInterceptor).order(3);
}
```

Lower number = run first in preHandle. Reverse order in postHandle/afterCompletion — like stack, first in, last out.

Example: logging start (order 1) wrap around whole chain, so afterCompletion of logging run LAST, capture full time correctly.

preHandle return false but already sent partial response: If you set response.setStatus(...) and return false, fine. But if some EARLIER interceptor already return true and did work, then later interceptor return false — earlier interceptor's postHandle skip (never called), but its afterCompletion STILL called (since already ran preHandle true). Common bug: developer assume afterCompletion only run for interceptor whose preHandle fully pass whole chain — wrong, afterCompletion run for any interceptor whose OWN preHandle returned true, regardless what happen after.

mixing filter and interceptor auth: Team put JWT check in both filter AND interceptor by accident (two people, two PRs) → double validation, wasted work, confusing logs. Pick one place per concern.

ThreadLocal cleanup: If interceptor set ThreadLocal (e.g., request-id, current user) in preHandle, MUST clear in afterCompletion, else leak into next request when thread reused from pool (Tomcat thread pool reuse threads).