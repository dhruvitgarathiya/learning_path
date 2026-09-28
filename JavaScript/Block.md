# block

combining multiple javascript statement in group

so we can use it where javascript excpets one statement

same name variable outside the block - this is called variable shadowing

you  can shadwo let using let
but you cannot shadow let using var

2. How var, let, and const Interact with Block Scope

```js
{
    var x = "I am global/functional";
    let y = "I am block scoped";
    const z = "I am also block scoped";
}

console.log(x); // Output: "I am global/functional"
console.log(y); // ReferenceError: y is not defined

```
Behind the scenes: When the JavaScript engine steps into a block, it creates a new environment record for let and const. They are hoisted into this temporary Block scope but remain uninitialized (entering the Temporal Dead Zone (TDZ)) until their actual line of code runs. var, conversely, is hoisted straight to the enclosing function or global scope, completely ignoring the curly braces

**A. Legal Shadowing (Perfectly Fine)**


```js
//case 1: showdin let with let

let a = 100;
{
    let a = 10; // legal ! = this is 'a' lives purely in the block scope
    console.log(a); // output:10
}

// case 2: shadowing var with let
var b = 200;
{
    let b = 20; // legal
    console.log(b); // output: 20
}
console.log(b); // output:200

//illegal shadowing

let target = 50;
{
    var target = 5; // uncaught syntaxerror: identifie target has already been declared
}

```

var does not respect blocks, the JavaScript engine tries to hoist var target to the same outer scope where let target already exists. JavaScript strictly prohibits redeclaring a let identifier in the same execution scope, throwing a compiler-level SyntaxError


**Function Scoping & Function Shadowing**:

```js
function test() {
    var secret = "shh";
}
console.log(secret); // ReferenceError: secret is not defined
```

**B. Function Shadowing (Overriding Illegal Shadowing limits)**

Remember how shadowing a let with a var was illegal inside a block? If you wrap that var inside a function instead of a naked block, it becomes legal again

```js
let config = "production";

{
    // a standard block - var config here would throw an illegal shadowing error

    function boundary(){
        var config = "developement";
        console.log(config); // output done
    }
    boundry();
}
console.log(config); // output done
```


 The var config is now bounded securely inside boundary's function scope, meaning it no longer attempts to hoist itself into the outer scope where the let config lives

 **5. Nested Scopes & The Scope Chain**

 JavaScript utilizes Lexical Scoping, which means the accessibility of variables is determined entirely by the physical position of the code text during creation

 When scopes are nested, they form a Scope Chain. If the JavaScript engine cannot find a variable inside the immediate local block, it sequentially reaches outward to the parent block/function, and continues climbing until it reaches the Global scope

 ```js
 const globalVal = "Global";
 {
    const parentVal = "Parent";
    {
        const childVal = "child";

        console.log(childVal); //Found locally
        console.log(parentVal); //Found i parent block climb 1 step
        console.log(globalVal);//fouund in global scope climb 2 steps
    }
 }
 ```

 The scope chain only goes one way (inside out). A parent scope can never look inside a child block to read a let or const variable

 # closer

 ```js
 function x(){
    var a = 7;
    function y(){
        console.log(a);
    }
    y();
 }
 x();
 ```
 finding the variable in parent's lexical scope is simply closer


 closer is biding function along with it's lexical scope

 uses of closer:

 module desgin pattenr
 currying
 function like once
 memoize
 maintainging state in async world
setTimeouts
iterators
and many more...

## setTimeOut:

The setTimeout() method schedules the execution of a function after a specified delay in milliseconds.

However, to deeply understand setTimeout, you have to realize that it does not pause code execution and the delay time is a minimum guarantee, not a fixed guarantee

```js
const timeoutId = setTimeout(callback, delay, param1, param2, ...);

```

callback: The function to execute.

delay: Time in milliseconds (defaults to 0).

param1, param2...: Extra arguments passed directly into your callback function. This prevents you from having to create wrapper arrow functions just to pass arguments.

Returns: A unique Timeout ID (integer), which can be passed to clearTimeout(timeoutId) to cancel the timer before it fires


```js
function greet(user , role){
    console.log(`hello ${user} , you are logged in as ${role});
}

const timer = setTimeout(greet, 2000, 'Alice','Admin');

// if we neede to abort clearTimeout(timer);
```

JavaScript is single-threaded—it can only do one thing at a time. setTimeout is actually not a part of the JavaScript engine itself; it is a Web API provided by the browser environment (or the runtime environment in Node.js

When you call setTimeout, here is exactly what happens step-by-step:

Call Stack: setTimeout runs on the Call Stack. It tells the browser, "Hey, hold onto this callback function and start a timer for X milliseconds.

Web API: The browser handles the countdown in the background. Meanwhile, setTimeout completes instantly and is popped off the Call Stack. Your main JavaScript code keeps running without blocking

Task Queue: Once the timer expires, the browser moves your callback function into the Task Queue (or MacroTask Queue)

Event Loop: The Event Loop constantly monitors the Call Stack. It will only push the callback from the Task Queue into the Call Stack if the Call Stack is completely empty

truth: dealy is minimum not promise

Because the Event Loop waits for the Call Stack to be empty, your callback can be delayed longer than specified if the main thread is busy processing heavy computations

```js
console.log("Start");

setTimeout(() => {
    console.log("Inside Timeout);
}, 0);

for(let i=0;i<1000000000;i++){

}
console.log("End");
```

Start
End
Inside Timeout (Prints only after the loop finishes, taking ~2 seconds!)


3. Common Pitfalls & Advanced Behaviors

**Pitfall A: Losing the this Context**

When you pass a method that relies on the this keyword into setTimeout, the context is stripped because the function gets invoked by the global execution context (window or undefined in strict mode)

```js
const person = {
    name: "John",
    sayHi(){
        console.log(`hi , i am ${this.name}`);
    }
};

setTimeout(person.sayHi, 1000);
```

The Fix: Wrap it in an arrow function (which preserves lexical this) or use .bind()

```js
setTimeout(() => person.sayHi(), 1000); 
// OR
setTimeout(person.sayHi.bind(person), 1000);
```

The output is undefined because setTimeout detaches the method from its object, causing the function to lose its connection to person.

When the function finally runs, the JavaScript runtime executes it in the global scope, where this no longer refers to john.

To understand this deeply, we need to look at how JavaScript handles the this keyword and function references.

Step A: Passing a Reference, Not an Execution

```js
setTimeout(person.sayHi, 1000);
```

You are not executing sayHi immediately. You are passing a reference to the sayHi function as an argument. Under the hood, setTimeout receives the function looking roughly like this:

```js
function setTimeout(callback, delay) {
    // ... waits 1000ms ...
    callback(); // <-- Notice: No "person." before callback!
}

```

Step B: The Rule of this

In JavaScript, the value of this is not determined when a function is created. It is determined how the function is called.

If you call person.sayHi(), the dot . explicitly sets this to person.

If you call a function standalone like callback(), this defaults to the global object (window in browsers, global in Node.js) or undefined if you are using strict mode.

Because setTimeout executes the function as a standalone callback, it looks for this.name on the global object. Since window.name is not defined, it returns undefined.

Arrow functions do not have their own this. They inherit this from the surrounding code where they were created.

```js
setTimeout(() => person.sayHi(), 1000); 
// Output: "Hi, I am John"

```
The .bind() method creates a brand new copy of the function and permanently locks its this context to the object you provide.

```js
setTimeout(person.sayHi.bind(person), 1000); 
// Output: "Hi, I am John"
```

Wrap it in an Anonymous Function

Manually call the method directly on the object inside the callback wrapper. This preserves the dot . notation during execution.

```js
setTimeout(function() {
    person.sayHi(); 
}, 1000);
// Output: "Hi, I am John"

```

*************************************

The Infamous var in a Loop

```js
for (var i = 1; i <= 3; i++) {
  setTimeout(() => console.log(i), 1000);
}

```
Expected: 1, 2, 3

Actual Output: 4, 4, 4

Why? Because var is function-scoped. By the time the 1-second timers expire and look for the value of i, the synchronous loop has already completed, leaving i at 4.

The Fix: Use let instead of var. let is block-scoped, creating a brand new variable binding for i on every loop iteration

**why var is function scopes**

Why var is Function-Scoped

In early JavaScript, creators designed the language to mirror elements of languages like Scheme, where functions were the fundamental units of execution.

The Rule: A variable declared with var belongs to the closest enclosing function.

The Consequence: Code blocks like if statements, for loops, and while loops do not create a new scope for var. They share the memory scope of the function they live inside.

```js
function functionScopeExample() {
  if (true) {
    var message = "I am accessible anywhere in this function!";
  }
  console.log(message); // Prints: "I am accessible anywhere..."
}

```

Why let is Block-Scoped

As JavaScript grew into an enterprise-level language, developers from C, Java, and C++ backgrounds found function scoping highly error-prone. They expected variables inside { } curly braces to stay inside those curly braces.

The Rule: A variable declared with let belongs to the closest enclosing block ({ }).

The Consequence: Once the code execution leaves that block, the variable is immediately garbage-collected (wiped from memory).

```js
function blockScopeExample() {
  if (true) {
    let secureMessage = "I am trapped inside this IF block!";
  }
  console.log(secureMessage); // ReferenceError: secureMessage is not defined
}

```

