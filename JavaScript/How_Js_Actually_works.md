# js is single threader synchronus language

## mahiti

JavaScript is not tightly coupled with hardware like C++ is, nor does it follow the exact compile-to-bytecode-then-interpret pipeline of traditional Java. 

Instead, modern JavaScript uses a highly sophisticated process called Just-In-Time (JIT) Compilation to execute code at near-native speeds.

Yes, you need a JavaScript engine to run JavaScript, but you almost certainly already have one installed.

Every modern web browser comes with a built-in JavaScript engine. This means if you have Google Chrome, Mozilla Firefox, or Apple Safari installed, your machine is already fully equipped to interpret and execute JavaScript code out of the box

Your browser's engine takes your code and instantly converts it into machine code that your computer's processor can understand.

Chrome & Edge use the V8 engine.Firefox uses SpiderMonkey.Safari uses JavaScriptCore

If you want to run JavaScript directly in your computer's terminal or build backend server applications (just like you would with Python or Java), you do need to install a standalone runtime environment.

The most popular tool for this is Node.js. When you download and install Node.js, it bundles Google's V8 engine together with extra system tools so you can execute .js files straight from your command line.

## difference between nodejs and jvm

While both the JVM (Java Virtual Machine) and Node.js serve a similar ultimate goal—taking your code and running it on your physical machine—they operate differently.

The biggest difference is that the JVM is a virtual machine designed to execute pre-compiled bytecode, whereas Node.js is a runtime environment built around an engine that executes raw source code on the fly.

jvm - It is a Virtual Machine. It simulates a physical computer inside your computer [so it has its own instructions].

Nodejs - It is a Runtime Environment. It wraps around a JS engine and provides system-level access.

jvm - Built-in Java interpreter and JIT (Just-In-Time) Compiler.

Nodejs - Google’s open-source V8 Engine (written in C++).

jvm - Multi-threaded. It can run multiple operations completely in parallel using CPU cores.

nodejs - Single-threaded with an Event Loop. It handles heavy loads by passing tasks off asynchronously.


## how js actually compiles 

 to execute code, combining the speed of a compiler with the flexibility of an interpreter. Historically labeled an "interpreted language" that runs line-by-line, modern engines like Google’s V8 (Chrome/Node.js), SpiderMonkey (Firefox), and JavaScriptCore (Safari) compile your code into machine instructions right before it executes


```
[ Your JS Code ] ──> 
[ 1. Parsing ] ──> 
[ 2. AST ] ──> 
[ 3. Interpreter (Bytecode) ] ──>
[ 4. JIT Compiler ] ──> 
[ Native Machine Code ]
```

**1. 1. Parsing & Tokenisation** : The engine reads your raw code text and breaks it down into individual language units called tokens (keywords, operators, variables)

For example, let x = 10; is tokenised into [let], [x], [=], [10].

**2. Abstract Syntax Tree (AST) Creation** :

The tokens are reconstructed into a hierarchical tree structure called an Abstract Syntax Tree (AST). This tree maps out the structural logic of your program so the engine can validate syntax and understand what actions need to occur.

**3.  Bytecode Generation (The Interpreter)**

To get the program running as fast as possible, the engine passes the AST to an Interpreter (like V8's Ignition). The interpreter quickly converts the tree into an Intermediate Representation (IR) known as bytecode

Why bytecode? It is unoptimized, unpolished machine-like instructions. It starts executing almost instantly without waiting for a lengthy full compilation process

**4. The JIT Compiler (Hot Path Optimization)**

While the interpreter is running the bytecode, a background component called the Profiler (or Monitor) watches the code execute

Warm & Hot Code: If a loop runs thousands of times or a specific function keeps getting called, the Profiler marks it as "Hot Code".

Optimization: The engine passes this hot bytecode to a heavy-duty Optimizing Compiler (like V8's TurboFan). It strips out dynamic redundancies and compiles that specific snippet into highly optimized native machine code (binary 1s and 0s).

The Switch: The engine swaps out the slower bytecode version with this new ultra-fast machine code for all future iterations

**The Dynamic Catch: De-optimization**

Because JavaScript is dynamically typed, the compiler has to make assumptions. If it optimizes a function assuming you will always pass integers (e.g., function add(a, b)), it generates machine code tailored for integer math

why js is dynamically typed:

JavaScript is a dynamically typed language because type checking occurs at runtime (while the program is executing) rather than during a compilation phase. In JavaScript, variables themselves do not have a fixed data type; only the values they hold do. Conversely, Java is a statically typed language, which means the data type of every variable must be explicitly declared beforehand and is verified at compile time before the code can run.

If you suddenly call add("hello", "world") later in the script, the engine realizes its assumption was wrong. It triggers a de-optimization step, throws away the optimized machine code, and falls back safely to the interpreter's generic bytecode

No, JavaScript is not tightly coupled with the machine, unlike C and C++

low-to-mid-level languages that compile directly into processor-specific machine code, JavaScript is a high-level, abstracted language designed to run inside a managed execution environment (a runtime engine like Google's V8 or Node.js

nodejs internally need google b8 engine



## js under the hood

```js
console.log(`value of x is ${x});
var x = 10;
```

this is called source code - what developer write

when you run js code

global execution contenxt is being created

two phases

phase 1: memory phase

phase 2: code phase

js traverse your whole code: - all the variable are being loaded in memory phase

intial value of every var in memoery phase is never intialize , undefined

code phase -- executing the code line by line 

in this js execute - console.log(`value of x is ${x});

value of x in memory is undefined 

so print undefined 

then 2nd line execution x get value of 10

then global execution context being deleted

now when function enters

```js
console.log("g1 starts");

var gvar = "i am var";

function globalfunc(){
    console.log("inside globalfunc");
}

console.log(gvar);

globalfunc();

console.log("g1 ends");
```

when you make variable in and js load them in memory phase there val is undefined;

but if you have funciton in your code then that full funciton will be loaded in memory phase

when you call a func in js - it creats it's own localcontext with memory phase and code phase

## hoisting :

 process by which variable and function declarations are moved to the top of their respective scopes during the compilation phase, before the code is actually executed.

This means that you can use variables and functions in your code before they are actually declared, as long as they are declared within the same scope. However, it’s important to understand how hoisting works in order to avoid any unexpected behavior.

### Example 1: Variable Hoisting

Variable hoisting refers to the behavior where JavaScript variables are moved to the top of their containing scope during the compilation phase, allowing them to be accessed before their actual declaration in the code. However, only the variable declaration (not the assignment) is hoisted.

```js
console.log(x); // Output: undefined
var x = 5;
```

In this example, even though x is logged before it's actually declared and assigned a value, the code doesn't throw an error. This is because the declaration of x is hoisted to the top of the scope (global or function scope), but the assignment of the value 5 to x happens later in the code. When console.log(x) is executed, x has been declared but not yet assigned a value, so its value is undefined.


### Example 2: Function Hoisting

Function hoisting is similar to variable hoisting, but applies to function declarations. Function declarations are moved to the top of their containing scope during the compilation phase, allowing them to be called before their actual declaration in the code.

```js
hello(); // Output: "Hello, world!"
function hello() {
  console.log("Hello, world!");
}
```

### Function Expression Hoisting
Function expression hoisting is different from function declaration hoisting. In JavaScript, function expressions are not hoisted to the top of their containing scope during the compilation phase like function declarations. Instead, they behave like regular variable declarations and are subject to variable hoisting.


```js
hello(); // Output: TypeError: hello is not a function
var hello = function() {
  console.log("Hello, world!");
};
```

In this example, we’re using a function expression to assign a function to a variable hello. However, when we try to call hello() before it's actually declared and assigned, we get a TypeError. 

This is because function expressions are not hoisted in the same way as function declarations. In this case, the variable hello is hoisted to the top of the scope with an initial value of undefined, and at the time of the function call, it's still undefined, resulting in the error.

### Example 4 hoisting in nested functions

Hoisting in nested functions refers to how hoisting works when functions are declared inside other functions.

```js
function greet() {
  console.log("Hello!");

  function sayName() { // Nested function declaration (hoisted)
    console.log("My name is John.");
  }

  sayName();
}

greet();
```

In this example, we have a function greet that contains a nested function declaration sayName. When the greet function is called, it logs "Hello!" to the console, and then calls the sayName function. Even though the sayName function is declared inside the greet function, it is hoisted to the top of the greet function during the compilation phase, allowing it to be called before its actual declaration.

This will output:

Hello!
My name is John.


This is because the nested function sayName is hoisted to the top of the greet function, allowing it to be called after the "Hello!" message is logged.
Note:It’s important to note that hoisting in nested functions only occurs within the containing function’s scope. Nested functions are not hoisted to the global scope or to outer function scopes.

To avoid unexpected behavior due to hoisting, it’s recommended to always declare and initialize variables before using them. It’s also good practice to place function declarations before calling them in your code to ensure they are properly hoisted. Additionally, be aware of the differences between function declarations and function expressions in terms of hoisting behavior.

**why we need the functional expression:** 

While standard function declarations (function greet() {}) work perfectly for simple codebases, JavaScript needs Function Expressions (const greet = function() {}) because they treat functions as first-class citizens—meaning functions can be treated exactly like strings, numbers, or objects.

1. Functions as Data (Callbacks):

In JavaScript, functions frequently need to be passed as arguments into other functions (especially for asynchronous actions). Standard declarations can feel clunky for this, whereas function expressions allow you to create and pass a function exactly where you need it.

```js
// Passing a function expression directly as an argument
setTimeout(function() {
  console.log("Passed as data!");
}, 1000);

```

2. Scope Control and Conditional Creation:

Function declarations are hoisted to the top of their scope, meaning they are created before any code runs. Function expressions are evaluated only when the execution hits that specific line of code. This allows you to conditionally create functions at runtime.

```js
let logAction;

if (user.isAdmin) {
  logAction = function() { console.log("Admin action logged."); };
} else {
  logAction = function() { console.log("Standard user action logged."); };
}

logAction(); // Dynamically resolved at runtime

```

3. Preventing Global Scope Pollution:

Before modern JavaScript modules existed, writing standard function declarations meant everything went into the global scope, leading to name collisions. Function expressions allowed developers to create IIFEs (Immediately Invoked Function Expressions) to sandbox code completely.

```js
(function() {
  let privateVar = "Secret";
  // Everything inside this function expression stays safely isolated
})();

```

In modern React, Node.js, and TypeScript applications, function expressions have largely evolved into Arrow Functions (const foo = () => {}), but the underlying architectural principles remain exactly the same.

where they are used:

1. React Functional Components & Event Handlers
2. Array Methods & Data Transformations
3. Defining Express.js Middleware (Node.js)

## window object

The window object is the massive, top-level global object provided by the web browser environment.

It represents the browser window itself (the tab your webpage lives in). Because it sits at the absolute root of the browser's ecosystem, it serves as the Global Execution Context for all JavaScript code running in that tab [1, 2.1, 2.2].

if you type console.log(window); in your browser's developer console, you will see a massive object containing thousands of properties and methods.

JavaScript was originally designed to run exclusively inside web browsers. The browser needed a way to expose its native capabilities (like the physical screen size, the URL bar, the history, and the webpage document itself) so that JavaScript could interact with them.

The browser developers bundled all of these capabilities into a single object and named it window.

**purpose it solves**

1. It Hosts the Global Scope

Any variable or function you declare globally (using var or standard function declarations) automatically becomes a property of the window object.

```js
var user = "Alice";
function greet() { return "Hi"; }

console.log(window.user);  // Outputs: "Alice"
console.log(window.greet()); // Outputs: "Hi"

```

(Note: Modern variables declared with let and const do not attach themselves to window to prevent global pollution, but they still exist inside the global scope block managed under it).

2. It Provides Access to the DOM (Document Object Model)

The window object holds the document object. The document is the actual HTML tree structure of your webpage. Without window, you wouldn't be able to target or change HTML elements.

```js
// window.document is how JS interacts with your HTML
window.document.getElementById("submit-btn").click();

// Because window is implicit, you can just write:
document.getElementById("submit-btn").click();

```
3. It Exposes Browser and Machine Information

4. It Provides Global Utility Methods

Core JavaScript features that you use every single day are actually just methods sitting quietly inside the window object:

window.setTimeout() and window.setInterval()window.console.log()window.fetch() (for making API network requests)window.alert(), confirm(), and prompt()

Because window is the global execution context, you do not need to type window. before calling these. The engine assumes window by default. Writing fetch() is exactly the same as writing window.fetch().

In the Browser: The global object is window

In Node.js: The window object does not exist. If you try to call window in Node.js, your app will crash with a ReferenceError. This is because Node.js runs on a server, where there is no browser tab, no URL bar, and no screen. Instead, Node.js replaces it with its own global object called global.

(To fix this fragmentation, modern JavaScript introduced a universal keyword called globalThis, which points to window in the browser and global in Node.js).

In modern React development, you generally avoid creating traditional global variables (like attaching things to the window object) because they break React's core philosophy of predictable data flow and component isolation.

Instead of using raw JavaScript global variables, React uses Context. This acts as a scoped global store that automatically alerts components whenever the data changes, triggering a re-render.

Third-Party Global State Managers: 

For massive enterprise applications where thousands of components need access to complex, fast-changing global data, developers use dedicated state management libraries like Redux Toolkit, Zustand, or Recoil.

