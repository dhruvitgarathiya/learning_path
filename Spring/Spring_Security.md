# what spring security solves , why exists

Raw problem: any web app expose endpoints. Without control, anyone hit any URL, do anything. 

Need answer 2 question every request:

Authentication — who you are? (prove identity — login, token)

Authorization — what you allowed do? (permission — role, access level)

Spring Security = framework answer both,

plug into Spring app via servlet filter chain (filter run before DispatcherServlet? Security filters run even earlier, guard door before request reach Spring MVC at all)

Why filter-based, not interceptor-based?

Security must stop bad request BEFORE it waste resource going deep into app — before controller, before service, before touch DB

Filter run at servlet level, earliest point possible in Spring web app life

Interceptor only fire after DispatcherServlet already pick handler — too late for security gate

too much work already done (URL matching, handler resolution) before you even check "should this guy even be here".

Core mechanism — Filter Chain:

Spring Security don't use ONE filter. Uses CHAIN of many small filters, each doing one job, stacked in fixed order

Example real chain (simplified):

SecurityContextPersistenceFilter → check if already logged in (session/context)
UsernamePasswordAuthenticationFilter → handle login form submit
BasicAuthenticationFilter → handle HTTP Basic auth header
ExceptionTranslationFilter → catch security exceptions, decide redirect/403
FilterSecurityInterceptor / AuthorizationFilter → final authorization decision

Request pass through each filter in order. Any filter can stop chain early (reject request) or let pass to next.

Why so many small filters instead one big filter? 

Single Responsibility. Each filter handle ONE auth mechanism (basic auth, form login, JWT, OAuth2...).

Want add new auth method → just add new filter into chain, don't touch existing ones. Want remove form login → remove that filter, rest still work. Modular, swappable.

Key object — SecurityContext:

Holds current authenticated user info (Authentication object) for duration of request (or session, depending setup).

Stored in SecurityContextHolder (ThreadLocal-based by default — from interceptor part, same ThreadLocal leak risk exists here too, framework handle cleanup for you though).

SecurityContext — simple holder object. Job: hold ONE thing — Authentication object (who logged in, what role/authorities they got, whether authenticated true/false).

```java
public interface SecurityContext {
    Authentication getAuthentication();
    void setAuthentication(Authentication authentication);
}
```
That's basically it. Think of it like envelope carrying "who is this request" info.


Authentication object inside — carries:
```java
public interface Authentication {
    Collection<? extends GrantedAuthority> getAuthorities(); // roles/permissions
    Object getCredentials(); // password/token (often cleared after auth)
    Object getPrincipal();   // usually username or UserDetails object
    boolean isAuthenticated();
}
```
Now SecurityContextHolder — different job. This is STORAGE mechanism, place where SecurityContext physically live during request.

```java
public class SecurityContextHolder {
    private static ThreadLocal<SecurityContext> contextHolder = new ThreadLocal<>();
    // get/set methods
}
```

Why ThreadLocal?

 Web server (Tomcat) handle each incoming request on separate thread

ThreadLocal = variable exist SEPARATE copy per thread — thread A's copy invisible to thread B

Thread-15 handle User X's request, SecurityContextHolder on Thread-15 hold User X's context.

Thread-20 handle User Y same time, hold User Y's context. No mixing, no need pass user object manually through every method call

just call SecurityContextHolder.getContext().getAuthentication() from ANYWHERE in code (controller, service, deep in call stack), get correct current user, no parameter passing needed.

How get filled first place, real flow:

Request come with login credentials (or JWT token, or session cookie).

Some filter in chain (e.g. UsernamePasswordAuthenticationFilter) run authentication logic, verify credentials correct.

On success, build Authentication object (fully populated, isAuthenticated() true).

Filter call SecurityContextHolder.getContext().setAuthentication(authObject).

Now REST of request lifecycle (any controller, any service down chain) can call SecurityContextHolder.getContext().

getAuthentication().getPrincipal(), get current user, no need pass around.

Cleanup — same gotcha as before (ThreadLocal leak):

Tomcat REUSE threads from pool. If not cleared, next unrelated request landing on same thread could accidentally see PREVIOUS user's SecurityContext — huge security bug

That's why SecurityContextPersistenceFilter (or newer SecurityContextHolderFilter) ALWAYS clear context in finally block after response sent, every single request, no exception.

Real dev use case — get current logged-in user inside any service method:

```java
Authentication auth = SecurityContextHolder.getContext().getAuthentication();
String username = auth.getName();
```

Very common pattern, see this line everywhere in production Spring code — audit fields (createdBy), permission check inside service layer, etc.

### SecurityFilterChain 

modern config , how dev actually wire security today

Old way - (deprecated, don't use, but know it exist since old codebase have it)

extend WebSecurityConfigurerAdapter, override configure(HttpSecurity http).

Removed in Spring Security 5.7+. Why removed? Inheritance-based config = rigid, hard test, hard have multiple security config in same app

multiple filter chains for different URL groups — e.g. one chain for /api/** stateless JWT, one chain for /admin/** session-based)

Forced everyone into one giant config class.


New Way - bean-based , SecurityFilterChain:

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig{
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
        .authorizrHttpRequests(auth -> auth 
                            .requestMatchers("/api/public/**").permitAll()
                            .requestMatchers("/api/admin/**").hasRole("ADMIN)
                            .anyRequest().authenticated();
        )
        .formLogin(Customer.withDefaults())
        .csrf(csrf -> csrf.disabled());

        return http.build();
}
}
```

Why bean not method override now? Composition over inheritance.

HttpSecurity object = builder, you configure step-by-step, return finished SecurityFilterChain as normal Spring bean.

Plain bean means: can define MULTIPLE SecurityFilterChain beans

each with @Order and its own securityMatcher(...) scoping which URLs it apply to

Real production case — REST API app that also serve admin web dashboard:

```java
@Bean
@Order(1)
public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
    http
        .securityMatcher("/api/**")
        .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .csrf(csrf -> csrf.disable())
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
}

@Bean
@Order(2)
public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
        .formLogin(Customizer.withDefaults());
    return http.build();
}
```

@Order(1) chain checked first — securityMatcher("/api/**") mean ONLY requests matching this pattern go through this chain (JWT, stateless, no CSRF need since no browser form/cookie involved)

## CSRF 
In Spring Security, CSRF stands for Cross-Site Request Forgery. It refers to both a type of malicious web vulnerability and the built-in mechanism Spring Security uses to defend your application against it. 

What is a CSRF Attack?

A CSRF attack happens when a malicious website tricks a user's browser into executing an unwanted action on a legitimate site where the user is currently logged in

Because web browsers automatically include cookies (like session IDs) with every request sent to a specific domain, a malicious site can forge a request to your backend

Because web browsers automatically include cookies (like session IDs) with every request sent to a specific domain, a malicious site can forge a request to your backend. The server sees the valid session cookie, assumes the legitimate user intended to make the request, and executes it

How Spring Security Protects You:

To prevent this, Spring Security implements the Synchronizer Token Pattern by default

Instead of relying only on session cookies (which browsers attach automatically), Spring Security forces the client to send a secret, unique, and unpredictable value called a CSRF Token with every state-changing request

```
Legitimate Client ----[ POST Request + Cookie + CSRF Token ]----> Spring Boot (Allowed)
Malicious Client  ----[ POST Request + Cookie (No Token)   ]----> Spring Boot (Rejected - 403 Forbidden)
```

Enabled by Default: Spring Security automatically secures all state-changing HTTP methods (POST, PUT, DELETE, PATCH)

Safe Methods are Ignored: Read-only operations like GET, HEAD, and OPTIONS do not require a CSRF token because they should never alter the application's data state

The CsrfFilter: An internal component acting as a gatekeeper. It intercepts incoming requests, extracts the submitted token (from a hidden form field or a request header), and matches it against the expected token stored on the server. If they don't match, the request is rejected with a 403 Forbidden error

Common Strategies for Handling the Token

When can you safely disable CSRF?Developers often disable CSRF using .csrf(csrf -> csrf.disable()). You should only do this if you are building a stateless REST API that does not use cookie-based session tracking (for example, if your app exclusively uses mobile clients or JWT/OAuth2 tokens passed via the Authorization header)

--------------------------------------

Why two DIFFERENT security setup same app real scenario: REST API consumed by mobile app / frontend SPA — no session wanted, stateless, JWT bearer token, no CSRF risk (no cookie auto-sent).

Admin web panel — traditional server-rendered pages, session cookie, needs CSRF protection since cookie auto-sent by browser (attacker site can trick browser send cookie, CSRF attack).

One-size-fit-all config wrong here — that's real WHY multiple filter chain feature exist.

authorizeHttpRequests matcher order matters: Rules checked TOP TO BOTTOM, first match win. So specific pattern (/api/admin/**) MUST come before generic (anyRequest()), else generic catch it first, admin-specific rule never reached. 

Common beginner bug — put anyRequest().authenticated() too early, rest rules become dead code.

csrf().disable() — why sometime turn off: CSRF protection only matter when browser auto-send credential (cookie). 

Stateless JWT API — token sent manually in header (Authorization: Bearer xxx) by client code, 

browser don't auto-attach it like cookie — no CSRF risk exist there, so disabling not a security hole IN THAT CONTEXT (don't disable blindly on session-based app though).


## login flow :

actors involved:

1. UserDetailsService — interface, ONE job: given username, fetch user record. Like HR database lookup.

2. UserDetails — interface, represent that fetched user (username, hashed password, authorities/roles, account flags like locked/expired).

3. PasswordEncoder — verify raw password (typed by user) match stored hash, WITHOUT ever storing/comparing plain text.

4. AuthenticationManager — orchestrator, receive login attempt, delegate to right AuthenticationProvider, return result.

5. AuthenticationProvider — actual worker doing verification logic (there can be many, e.g. one for username/password, one for LDAP, one for OAuth — AuthenticationManager pick right one).

**Why so many separate pieces, not one big "login" method?**

Same Single Responsibility idea as filter chain.

UserDetailsService don't know HOW password checked.

PasswordEncoder don't know WHERE user data come from (DB, LDAP, in-memory)

### LDAP 

Lightweight Directory Access Protocol

It is a software protocol.It lets users and apps find and manage data in a central directory.It acts like a digital phone book for a network

Main UsesAuthentication: Checks usernames and passwords.Centralization: Stores user accounts in one place for many apps.Quick Searches: Finds network resources fast

-----------------------------------------

AuthenticationManager don't know either detail — just coordinate.

Swap any piece independent — e.g. today user data in PostgreSQL, tomorrow move to LDAP, only swap UserDetailsService implementation, rest untouched.

Step-by-step real flow (form login, non-JWT first, simpler):

User submit username+password via login form (POST /login).

UsernamePasswordAuthenticationFilter (one filter in chain) intercept this request, extract username/password, wrap into UsernamePasswordAuthenticationToken (unauthenticated version — just carry raw credentials so far).

Filter call AuthenticationManager.authenticate(token).

AuthenticationManager (default impl ProviderManager) loop through registered AuthenticationProvider list, find one supports this token type — for username/password case, that's DaoAuthenticationProvider

DaoAuthenticationProvider call UserDetailsService.loadUserByUsername(username) — this hit YOUR code (you implement this, connect to DB):


```java
@Service
public class MyUserDetailsService implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username){
        User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundExecption("User not found"));

        return org.springframework.security.core.userdetails.User
        .witUsernam(user.getUsername())
        .password(user.getPasswordHash())
        .authorities(user.getRoles().toArray(new Strin[0]))
        .build();
    }
}
```

### build() function

The .build() method triggers the Builder Design Pattern to construct and return a fully initialized, immutable UserDetails object.

In Spring Security, User.withUsername(...) initiates a builder helper. The intermediate methods like .password() and .authorities() just gather your configuration data.

The final .build() call takes all that data, validates it, and instantiates the actual org.springframework.security.core.userdetails.User instance.

---------------------------------------------

Got back UserDetails object (with hashed password from DB). Now DaoAuthenticationProvider call PasswordEncoder.matches(rawPasswordFromLogin, hashedPasswordFromDB).

Why never just compare rawPassword.equals(storedHash): Because storedHash NOT reversible plain text — hashing one-way function.

matches() internally re-hash raw password (with same algorithm + salt info embedded in stored hash) and compare resulting hash, NOT compare raw text directly.

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

Why BCrypt specifically, why not just MD5/SHA256 plain hash: BCrypt built SLOW on purpose (configurable "strength"/rounds) — deliberately expensive computation.

### sha256 , md4, bcrypt

SHA-256, MD4, and bcrypt are all cryptographic algorithms used to process data, but they serve completely different purposes and offer radically different levels of security.

MD4 (Message Digest 4): A historically early cryptographic hash function created in 1990. It is completely broken and highly vulnerable to collisions (where two different inputs produce the same output). It is obsolete and should never be used for security today.

SHA-256 (Secure Hash Algorithm 256-bit): A highly secure, modern cryptographic hash function that belongs to the SHA-2 family. It takes any input and creates a unique, fixed 256-bit (32-byte) signature. It is extremely fast and is widely used for data integrity verification, digital signatures, and blockchain technologies.

bcrypt: A dedicated password-hashing function designed in 1999 specifically to protect user passwords. Unlike SHA-256 and MD4, bcrypt is intentionally designed to be slow and computationally expensive. It incorporates a "work factor" (rounds of processing) and a unique "salt" for every password to protect against brute-force hardware attacks.


-----------------------------------------------

Why want SLOW hashing? Defend against brute-force — attacker who steal DB hash dump try crack via trying millions password guess per second.

Fast hash (MD5/SHA256 alone) let attacker try billions guess/sec on cheap hardware. BCrypt make each guess cost noticeably more time

rute-force millions time slower, practically infeasible at scale.

Imagine a thief trying to guess the 4-digit PIN to a vault.If the lock is fast: The thief can guess 10,000 combinations in one second using a computer. The vault opens immediately.

If the lock is slow: The vault is designed to take 1 full second to process every single guess.

Even if the thief uses the fastest computer in the world, the vault physically forces the computer to wait 1 second per try. Guessing all 10,000 combinations will now take 10,000 seconds (nearly 3 hours).

This is how slow computing secures your passwords against brute force attacks.

Also BCrypt auto-generate random SALT per password (embedded in output hash string itself) — defend against rainbow-table attack (precomputed hash lookup tables), since same password by two different user produce DIFFERENT hash (different salt).

### salt

In password hashing, a salt is random data added to a password before it gets hashed.Think of it like adding a unique secret ingredient to a recipe. Even if two people order the exact same dish (the same password), the secret ingredient ensures the final meals look and taste completely different.

Without a salt, the password "Password123" will always produce the exact same hash output.If a hacker steals a database of 1 million users, they don't need to guess passwords one by one. They can pre-calculate the hashes for millions of common passwords (called a Rainbow Table) and just look for matches. If 500 users have the same password, they all have the same hash, and the hacker cracks all 500 instantly.

With a salt:The system generates a unique, random string for every user (e.g., xyz999).It glues the salt to the password: "Password123" + "xyz999".It hashes the combined string.Now, even if ten users choose "Password123", their salts are completely different, meaning their final hashes will look entirely different. The hacker's pre-calculated Rainbow Tables become completely useless.

------------------------------------------------

How bcrypt Uses Saltbcrypt handles salting automatically and elegantly. In other algorithms, developers have to manually generate a salt and store it in a separate database column. bcrypt builds it right into the process

Password match confirm → DaoAuthenticationProvider build FULLY authenticated Authentication object (this time isAuthenticated() = true, carry UserDetails + granted authorities).

This authenticated object flow back up to AuthenticationManager, back to filter.

Filter call SecurityContextHolder.getContext().setAuthentication(authenticatedObject) — NOW badge officially issue

Response sent — session cookie created (JSESSIONID) so NEXT request from same browser auto-carry this session, server recognize already logged in without redo whole flow


*******************************

Password mismatch case: PasswordEncoder.matches() return false → DaoAuthenticationProvider throw BadCredentialsException → bubble up, ExceptionTranslationFilter catch it, send 401/redirect login-failed page

Filter chain execution stop right there, never reach controller, never touch app logic — good, don't waste resource on failed auth.

Real production note: PasswordEncoder bean MUST be defined even during signup/registration flow too — when user first create account, hash password with same encoder BEFORE saving to DB

```java
user.setPasswordHash(passwordEncoder.encode(rawPassword));
userRepository.save(user);
```

Forget this = store plain text password = massive real-world security bug (seen in real breach news often — companies got hacked, plain password leaked because skip this step).

1. Cookies: The Client-Side Storage

A cookie is a small text file (usually under 4KB) that a website stores directly on your browser.

How Cookies Work (Step-by-Step)The Request: You visit a website or log in.The Response: The server processes your request and sends back data along with a special header called Set-Cookie.

The Storage: Your browser intercepts this header, extracts the cookie data, and saves it locally on your device.

The Echo: Every single time you make a new request to that same website, your browser automatically attaches that cookie file inside the Cookie header. The server reads it to identify you.


Types of Cookies

Session Cookies: Temporary cookies stored in your browser's temporary memory. They disappear as soon as you close the browser tab or window.

Persistent Cookies: Stored on your hard drive until a specific expiration date or until you manually clear them. They handle "Remember Me" features.


🔑 2. Sessions: The Server-Side Memory

A session is a temporary data store on the server used to save user information across multiple pages. 

Because users can tamper with data stored in cookies, sensitive info is kept on the server.

How Sessions Work (Step-by-Step)

The Creation: You log into a site. The server validates your password and creates a unique, randomized string called a Session ID (e.g., xyz123abc).

The Storage: The server opens a file or a database entry mapped to xyz123abc and stores your user details there (like your User ID, shopping cart data, or permission levels).

The Link: The server sends this Session ID back to your browser, usually inside a cookie (often named PHPSESSID, JSESSIONID, or connect.sid).

The Retrieval: When you click another page, your browser sends the Session ID cookie back to the server. The server reads the ID, looks it up in its memory, matches it to your active session, and recognizes who you are.

JSESSIONID -- A JSESSIONID is a unique session identifier cookie created by Java-based web application servers to keep track of a user across multiple web pages



## JWT based state

Why session-cookie approach not fit everywhere:

Session need server REMEMBER state (JSESSIONID mapped to session object stored server-side, in memory or Redis).

Fine single server. But real production often run MULTIPLE server instance behind load balancer (scale horizontally).

Request 1 hit Server A (session created there), Request 2 (same user) hit Server B — Server B never heard of this session, user treated as logged-out.

Fix options: sticky session (route same user always same server — fragile, bad load distribution) 

OR 

shared session store (Redis — extra infra, extra hop).

JWT solve differently: don't store session server-side AT ALL — stateless.

What JWT (JSON Web Token) actually is

String, 3 parts separated by dot: header.payload.signature.

Header — algorithm used (e.g. HS256), base64 encoded JSON.

Base64 encoding is a way to convert binary data into a safe string of text made up of regular ASCII characters

Web Safe Data: It helps pass basic user login info inside web headers without breaking the rules of the network.

Why JWTs Rely on Base64URL

1. URL and HTTP SafetyStandard Base64 uses the characters + and /, which have special meanings in URLs (e.g., / indicates a folder path, and + can turn into a space). JWTs are frequently passed in URLs, query parameters, or HTTP headers. Base64URL swaps + for - (minus) and / for _ (underscore), and drops the trailing = padding characters. This ensures the token never breaks a web link or an HTTP request.

2. Standardized StructureA JWT is just a single string that looks like this: header.payload.signature. Base64 turns structured JSON data into flat, uniform text chunks so they can be easily stuck together.

A common mistake is assuming that because a JWT looks like scrambled Base64 text, the data is hidden.Base64 is completely readable. Anyone who intercepts a JWT can easily run the header or payload through a standard Base64 decoder to read the exact JSON inside. Because of this, you should never put sensitive data (like passwords or social security numbers) inside a standard JWT payload.

Breakdown of a JWT:

JWT PartWhat it contains before encodingWhat it looks like after Base64URL encoding1. HeaderJSON data specifying the token type and encryption algorithm (e.g., {"alg": "HS256", "typ": "JWT"}).

eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ92. PayloadJSON data containing the user claims and expiration (e.g., {"sub": "1234", "name": "Alice"}).

eyJzdWIiOiIxMjM0IiwibmFtZSI6IkFsaWNlIn03. SignatureRaw binary cryptographic bytes generated by hashing the header and payload together with a secret key.XpZ1R4e... (safely converted binary bytes)

Payload — actual data (claims) — username, roles, expiry time (exp), issued time (iat). Base64 encoded JSON, NOT encrypted (anyone can decode and read it — important, means NEVER put secret/password inside payload).

Signature — server sign header+payload using SECRET KEY (HMAC) or private key (RSA). This part prove token not tampered.

The signature in a JSON Web Token (JWT) is a cryptographic check used to verify that the sender is who they say they are and to ensure the token has not been tampered with along the way.While anyone can decode the Base64 header and payload to read the data, only someone with the correct cryptographic key can generate or verify a valid signature.


How the Signature is CreatedTo create a signature, a server takes the Base64URL-encoded header, appends a dot, appends the Base64URL-encoded payload, and hashes them together alongside a secret or private key.Mathematically, the process looks like this:textSignature = Algorithm( Base64URL(Header) + "." + Base64URL(Payload), Key )

Common Signature AlgorithmsJWTs primarily rely on two main categories of cryptographic algorithms: Symmetric (Shared Secret) and Asymmetric (Public/Private Key Pairs).


Symmetric (HMAC)HS256 (HMAC using SHA-256)The same exact secret key is used by the server to both create (sign) the token and verify it later.Simple applications where the same backend server creates and reads the token.

Asymmetric (RSA / ECDSA)RS256 (RSA using SHA-256)The authorization server uses a private key to create the token. Other servers use a public key to verify it.Microservices or Single Sign-On (SSO) systems where multiple independent services need to verify tokens without knowing the secret master key.

How HS256 Works (Symmetric)Signing: The server takes the encoded header.payload and runs it through a SHA-256 hashing algorithm combined with a secret string (like my-super-secure-password).

Verification: When the token comes back, the server runs the received header.payload through the exact same formula with its secret string. If the resulting text matches the signature on the token, it is trusted.


Why signature matter, whole security depend on this: 

Anyone can DECODE payload (just base64, no secret needed) and READ it

But CANNOT MODIFY it without invalidating signature — since signature computed from exact header+payload content using secret key

Attacker try change payload (e.g. change role USER → ADMIN), signature won't match anymore when server re-verify (server recompute signature from received header+payload, compare to signature in token — mismatch = reject).


Attacker don't have secret key, can't forge valid new signature.

Real login flow with JWT:


User POST /login with username+password (same as before — UserDetailsService + PasswordEncoder verify, EXACT same mechanism part 3 explained, no difference here).

On success, instead creating session, server GENERATE JWT token, send back in response body (not cookie):


```java
public String generateToken(UserDetails userDetils){
    return JWTs.builder()
            .setSubject(userDetails.getUsername())
            .claim("roles", userDetails.getAuthorities())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTImeMIllis() + 3600000))
            .signWith(SignatureAlgorightm.HS256, secretKey)
            .compact();
}
```

**Compact** 

In this Java code snippet using the jjwt (Java JWT) library, the .compact() method is the final step that packages everything together into the actual, final JWT text string.


Client (mobile app / frontend JS) STORE this token (localStorage, secure storage) — NOT cookie, developer manually attach.


Every SUBSEQUENT request, client attach token manually in header:


Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...


Server side — verify token, custom filter needed (Spring don't provide this out-of-box, YOU write):


```java
@Component
public class JwtAuthFilter extends OncePerRequestFilter{
    @Autowired 
    private JwtUtill jwtUtill;

    @Autowired
    private UserdetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request , HttpServletResponse response, FilterChain filterChain) throws ServletException , IOException{

        String authHeader = request.getHeader("Authorization");

        if(authHeader != null && authHeader.startsWith("Bearer")){
            String token = authHeader.substring(7);
            String username = jwtUtill.extractUsername(token);



            if(username != null && SecurityContextHoler.getContext().getAuthentication() == null){
                UserDetails = userDetails = userDetailsService.loadUserByUsername(username);
            }

            if(jwtUtill.isTokenValid(token, userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChaina.doFilter(request, response);

    }
}
```

OncePerRequestFilter — why extend this, not plain Filter: Guarantee filter run EXACTLY once per request, even if request internally forwarded/included multiple time (servlet dispatch can forward request internally — e.g. error page dispatch).

Plain Filter could accidentally run twice in such case, redundant work / bugs. Spring Security custom filters almost always extend this.

Register this filter into chain — tie back Part 2 exactly:

```java
http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
```

Placed BEFORE UsernamePasswordAuthenticationFilter — meaning JWT check happen early, and since JWT filter directly set SecurityContextHolder itself (no need go through AuthenticationManager full flow again

UsernamePasswordAuthenticationFilter (form-login handler) simply skip, since no form login field present anyway on pure API request.

No session created — statelessness enforced explicit:

```java
http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
```

Tell Spring Security — don't create JSESSIONID, don't store anything server-side

EVERY request independently carry own proof of identity (the token) — that's what "stateless" mean

server hold zero memory of you between requests, each request self-contained.

Why stateless good for scaling (tie back opening problem): Request hit Server A or Server B — DOESN'T MATTER, both server independently verify signature using SAME shared secret key, no need share session data between servers. Horizontal scaling trivial now.

Real gotcha — token can't be "logged out" server-side easily: Since server hold no record, can't just "delete session" to logout. Token valid until exp time hit, no matter what. Real production handle this via: short expiry (e.g. 15 min) + refresh token pattern (separate longer-lived token used to get NEW access token)

OR maintain blacklist (defeats pure-stateless benefit partially, but sometimes needed for immediate revoke — e.g. Redis blacklist check token-id on every request until natural expiry).

jwtutills : A custom utility helper class used to decode, read, and validate JWT strings.

userDetailsService : A core Spring Security interface used to fetch user data (like passwords and roles) from your database.

String authHeader = request.getHeader(...) : Retrieves the value of the Authorization header from the incoming request.

authHeader.substring(7) : Strips off the first 7 characters ("Bearer ") to get only the actual token string.

UsernamePasswordAuthenticationToken : The standard container Spring Security uses to hold authenticated user details and their granted authorities (roles). 