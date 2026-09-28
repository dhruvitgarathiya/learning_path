# undefined

In JavaScript, undefined is a primitive value and a built-in type that signifies the absolute absence of a value.

his variable exists, but it has not been given a value yet." It acts as a default placeholder

1. Uninitialized Variables

If you declare a variable using let or var but do not assign a value to it, JavaScript automatically sets it to undefined.

```js
let user;
console.log(user); // Outputs: undefined

```

2. Missing Function Arguments

If a function expects arguments, but you don't pass them when calling the function, those missing parameters become undefined inside the function.

```js
function greet(name) {
  console.log("Hello " + name);
}

greet(); // Outputs: "Hello undefined" (because no name was passed)

```

3. Functions That Lack a Return Statement

Every single function in JavaScript must return something. If you do not explicitly use the return keyword, the function automatically returns undefined when it finishes executing.

```js
function add(a, b) {
  let sum = a + b;
  // No return statement here!
}

let result = add(5, 5);
console.log(result); // Outputs: undefined

```

4. Accessing Non-Existent Object Properties or Array Indexes

If you try to read a property from an object or an index from an array that doesn't exist, JavaScript won't crash; it will just return undefined.

```js
const laptop = { brand: "Apple" };
console.log(laptop.price); // Outputs: undefined (property doesn't exist)

const colors = ["red", "blue"];
console.log(colors[5]);    // Outputs: undefined (index out of bounds)

```

### differentiation:

undefined: The variable is declared, but no value has been set yet. 

null : An intentional assignment representing emptiness.

ReferenceError : The variable does not even exist in scope

# Scope in js

scope - where you can access specific var in function

When you see this pointing to the window object inside a nested function's local execution context, it happens because of a fundamental rule in JavaScript: by default, regular functions determine this based on how they are called, not where they are written.

The Core Reason: The "Method vs. Function" RuleIn JavaScript, this is not statically bound to the outer function or the object unless you explicitly tell it to be.

Object Method: When you call a function as a property of an object (e.g., user.sayHello()), this points to that object (user).

Standalone Function: When you call a normal, standalone function (e.g., nestedFunc()), it has no owner object attached to its execution. 

Because it is invoked globally without a leading object, JavaScript defaults this to the global object, which in a browser is window.

Even though nestedFunc is physically located inside another function, calling it simply as nestedFunc() means it behaves as a standalone function call.

```js
const myObject = {
  outerMethod: function() {
    console.log(this); // Points to 'myObject' (because it was called as myObject.outerMethod())

    function nestedFunc() {
      console.log(this); // Points to 'window' (because it was called standalone)
    }

    nestedFunc(); // <--- Called without an object prefix!
  }
};

myObject.outerMethod();

```
*****************************************


Scope determines where your variables can be seen and used, Lexical Environment is the actual background machinery tracking those variables, and the Scope Chain is the link that lets nested functions look outward to find missing data.


A Lexical Environment is a hidden internal JavaScript object where your currently running code stores its local variables, functions, and the this context.

The word "Lexical" means "related to the physical text." JavaScript figures out your environments completely based on where you physically typed the code in your editor before it even runs.

Every environment consists of two parts:

Environment Record: The actual storage dictionary holding all local variables and function declarations.

Reference to the Outer Environment: A pointer linking it to its immediate parent environment.

Scope:

Global Scope: Variables declared outside any function or block. They are accessible from absolutely anywhere in your file.

Function Scope: Variables declared inside a function. They are locked inside that function and hidden from the outside world.

Block Scope: Variables declared with let or const inside a set of curly braces {} (like an if statement or a for loop). They exist only inside those braces.

Scope Chain (The Search Party):

When you ask JavaScript for a variable, it first looks inside the current local Lexical Environment. If it finds it, it stops looking.

If it doesn't find it, it uses that Reference to the Outer Environment to step one level outward to the parent's environment. It keeps climbing up this ladder, layer by layer, until it either finds the variable or hits the Global Environment (window or global).

This physical chain of nested environments linked together is called the Scope Chain.

```js
const globalVar = "Global"; // 1. Global Lexical Environment

function outer() {
  const outerVar = "Outer"; // 2. Outer Lexical Environment
  
  function inner() {
    const innerVar = "Inner"; // 3. Inner Lexical Environment
    
    console.log(innerVar);  // Finds it locally in Environment 3
    console.log(outerVar);  // Not in 3 -> Climbs Scope Chain to 2 -> Found!
    console.log(globalVar); // Not in 3 -> Climbs to 2 -> Not in 2 -> Climbs to 1 -> Found!
    console.log(unknown);   // Not found anywhere -> Hits Global -> Throws ReferenceError
  }
  
  inner();
}

outer();

```

*********************************

# let & const 

let and const are hoisted , but they are in temporal dead zone so we cannot access them

temporal dead zone - it is time where let variable is hoisted and it is declared with val

best way to avoid temproral dead zone declare var early in func