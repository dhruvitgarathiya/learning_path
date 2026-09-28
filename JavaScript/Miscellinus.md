In JavaScript (ES6 Modules), you share and reference code across files using export and import statements. There are two main ways to do this: Named Exports (using export const) and Default Exports (using export default)



Named Exports (export const X)

Use named exports when a file contains multiple utilities, variables, or functions that you want to make available to other files]

mathUtils.js (The Source File)

```js
// You can have multiple named exports in a single file
export const PI = 3.14159;

export const add = (a, b) => a + b;

export const subtract = (a, b) => a - b;

```

app.js (The Consuming File)

When importing named exports, you must wrap them in curly braces { } and use their exact names:

```js
import { add, PI } from './mathUtils.js';

console.log(add(5, 3)); // Output: 8
console.log(PI);        // Output: 3.14159

```
Tip: If you want to change the name of a named import to avoid conflicts, use the as keyword

```js
import { add as sum } from './mathUtils.js';
console.log(sum(10, 2)); // Output: 12

```

Default Exports (export default)

Use a default export when a file has one main thing it's responsible for—like a single core function, a configuration object, or a class component

Note: You cannot use const, let, or var on the same line as export default. You must either declare the variable first or export the value directly

```js
const logger = (message) => {
  console.log(`[LOG]: ${message}`);
};

// Only ONE default export allowed per file
export default logger;

```
app.js (The Consuming File)

When importing a default export, you do not use curly braces. Because it is the "default" thing coming out of that file, you can also name it whatever you want

```js
// No curly braces, and we can rename 'logger' to 'customLog' freely
import customLog from './Logger.js';

customLog('Hello World!'); // Output: [LOG]: Hello World!

```

Combining Both in the Same File

```js
// Named exports (helper variables)
export const API_URL = 'https://example.com';
export const maxConnections = 5;

// Default export (the main service)
const fetchUsers = async () => {
  const response = await fetch(API_URL);
  return response.json();
};

export default fetchUsers;

```

```js
import fetchUsers, { API_URL, maxConnections } from './userService.js';

console.log(API_URL); // Output: https://example.com
// fetchUsers() can now be called as the main function

```

## logical operators


The Logical AND (&&) Operator

The && operator checks if both sides are true. In JavaScript, it stops and returns the first "falsy" value it hits. If everything is "truthy", it returns the last value

The Rule: Falsy && Anything Else → JavaScript sees the falsy value and immediately stops ("short-circuits"). It never looks at the right side

```js
// Case A: isLoading is true (truthy)
const isLoading = true;
const UI = isLoading && <Spinner />; 
// 1. JS evaluates 'isLoading' -> it's truthy.
// 2. It keeps going to the right side and returns it.
// Result: UI = <Spinner />

// Case B: isLoading is false (falsy)
const isLoading = false;
const UI = isLoading && <Spinner />; 
// 1. JS evaluates 'isLoading' -> it's falsy.
// 2. It short-circuits and immediately returns false.
// Result: UI = false (React ignores 'false' and renders nothing)

```

The Logical OR (||) Operator

The || operator checks if at least one side is true. It stops and returns the first "truthy" value it hits. If everything is falsy, it returns the last value

The Rule: Truthy || Anything Else → JavaScript sees a truthy value and stops immediately. It doesn't care what is on the right side because the overall expression is already guaranteed to be satisfied

```js
// Case A: userAvatar is missing (null/falsy)
const userAvatar = null;
const ImageSrc = userAvatar || "default-profile.png";
// 1. JS checks 'userAvatar' -> it's falsy.
// 2. It moves to the right and returns the fallback.
// Result: ImageSrc = "default-profile.png"

// Case B: userAvatar exists (truthy)
const userAvatar = "user-pic.jpg";
const ImageSrc = userAvatar || "default-profile.png";
// 1. JS checks 'userAvatar' -> it's truthy!
// 2. It short-circuits and returns it immediately.
// Result: ImageSrc = "user-pic.jpg"

```

## optional chaining

In JavaScript, optional chaining (?.) allows you to read properties deep within a chain of connected objects without having to manually validate that each reference in the chain is valid.

If a reference is null or undefined, the expression short-circuits and cleanly returns undefined instead of crashing your application with a TypeError.

The Problem It Solves: The Dreaded "Cannot read properties of undefined

Before optional chaining, fetching a deeply nested property from data that might be missing (like an API response) required tedious checks. If you tried to access a property on something that didn't exist, your app would crash.

```js
const user = {}; // The 'profile' property is missing completely

// ❌ The Old Dangerous Way
const avatar = user.profile.avatar; 
// Result: 💥 Crash! TypeError: Cannot read property 'avatar' of undefined

// ⚠️ The Old Safe Way (Verbose & Messy)
const avatar = (user && user.profile) ? user.profile.avatar : undefined;

```

The Solution: How ?. Works

By placing ?. before a property lookup, you tell JavaScript: "Try to read this. If the thing before the ?. is null or undefined, stop immediately and just give me undefined."

```js
const user = {}; // Profile is missing

//  The Modern Safe Way
const avatar = user?.profile?.avatar;
// Result: undefined (No crash!)

```

How it evaluates step-by-step:JavaScript looks at user.

It exists.It moves to user?.profile. 

profile is undefined.Because it hit undefined, the optional chain short-circuits.


It completely skips trying to read .avatar and immediately evaluates the whole expression to undefined.

Beyond Objects: Calling Methods and Arrays Safely

Optional chaining isn't just for object properties. You can also use it for safely executing functions or accessing array items that might not exist.

1. Safe Function Calls (?.())

```js
const user = {
  // admin: () => "Deleted user"
};

// If admin() doesn't exist, it returns undefined instead of crashing
const result = user.admin?.(); 

```
2. Safe Array Lookup (?.[])


```js
const userList = null; 

// Safely try to get the first user
const firstUser = userList?.[0]; // Result: undefined

```

## template literals

Template literals (backticks `) are a foundational JavaScript feature used to construct dynamic strings by embedding variables and expressions directly inside the text. Defined under the ES6 specification, they replace traditional single or double quotes (' or ") and completely eliminate the need for cumbersome string concatenation with the + operator

Dynamic and Conditional Class Names

```js
import React, { useState } from 'react';

function AccordionItem() {
  const [isOpen, setIsOpen] = useState(false);

  return (
    // Combines static classes with a conditional class using a ternary operator
    <div className={`accordion-wrapper ${isOpen ? 'is-expanded' : 'is-collapsed'}`}>
      <button onClick={() => setIsOpen(!isOpen)}>Toggle</button>
      <div className="content">Accordion Content</div>
    </div>
  );
}
```


Dynamic Component Attributes (URLs and IDs)

```js
function UserProfile({ username, userId }) {
  return (
    <div className="profile-card">
      {/* Constructing a dynamic image URL */}
      <img 
        src={`https://dicebear.com{username}`} 
        alt={`${username}'s avatar`} 
      />
      
      {/* Generating unique accessible IDs */}
      <label htmlFor={`bio-input-${userId}`}>Bio</label>
      <textarea id={`bio-input-${userId}`} />
    </div>
  );
}

```

 Inline Styles with Dynamic Values


```js
function ProgressBar({ progressPercentage, duration }) {
  const fillStyle = {
    // Injecting numbers into standard CSS unit strings
    width: `${progressPercentage}%`,
    transition: `width ${duration}ms ease-in-out`,
    backgroundColor: progressPercentage > 80 ? '#2ecc71' : '#3498db'
  };

  return (
    <div className="progress-bar-container">
      <div style={fillStyle} className="progress-bar-fill" />
    </div>
  );
}

```

Dynamic Localization and Text Formatting


```js
function CartSummary({ itemCount, totalAmount, userTier }) {
  return (
    <div className="summary">
      {/* Clean text formatting without breaking JSX flow */}
      <h2>{`Welcome back, ${userTier} member!`}</h2>
      <p>{`You have ${itemCount} ${itemCount === 1 ? 'item' : 'items'} in your cart.`}</p>
      <p>{`Total Cost: $${totalAmount.toFixed(2)}`}</p>
    </div>
  );
}

```


 Advanced: Styled Components (Tagged Templates

 ```js
 import styled from 'styled-components';

// The backticks accept standard CSS and allow embedded React props directly
const StyledButton = styled.button`
  background-color: ${props => props.$primary ? '#0070f3' : '#fff'};
  color: ${props => props.$primary ? '#fff' : '#0070f3'};
  padding: 10px 20px;
  border: 2px solid #0070f3;
  border-radius: 4px;
  cursor: pointer;
  
  &:hover {
    opacity: 0.9;
  }
`;

function App() {
  return <StyledButton \$primary>Click Me</StyledButton>;
}

 ```


 ## spread operator

 The spread operator (...) is a powerful JavaScript feature that allows you to expand elements of an iterable (like an array) or properties of an object into a new place

 In modern web development—especially inside frameworks like React—it is the go-to tool for updating state safely without mutating (changing) the original data


Why We Avoid Mutation (The "React Way")

```js
// ❌ BAD: Direct Mutation
const todos = ['Buy milk', 'Walk dog'];
todos.push('Clean room'); 
// The original 'todos' array is changed. 
// In React, this breaks state tracking because React won't realize the data changed!

```

By using the spread operator, you create a brand-new copy in memory. Because it is a new copy, React instantly detects the change and updates your screen smoothly.

Working with Arrays


```js
const todos = ['Buy milk', 'Walk dog'];
const newTodo = 'Clean room';

// ✅ GOOD: Creating a new array without touching 'todos'
const updatedTodos = [...todos, newTodo];

console.log(updatedTodos); // ['Buy milk', 'Walk dog', 'Clean room']
console.log(todos);        // ['Buy milk', 'Walk dog'] (Original is safe!)

```

Working with Objects

```js
const user = { name: 'Alice', age: 25, role: 'User' };

// ✅ GOOD: Update the age, copy everything else
const updatedUser = { ...user, age: 26 };

console.log(updatedUser); // { name: 'Alice', age: 26, role: 'User' }
console.log(user);        // { name: 'Alice', age: 25, role: 'User' } (Unchanged)
```


Note: If a property name matches (like age), the one placed last will overwrite the previous one.


Rest assignment


When the ... syntax is used to bundle remaining elements together, it is called the Rest Parameter (in functions) or Rest Assignment (in object/array destructuring).

While it looks identical to the spread operator, it does the exact opposite: instead of unpacking a box, it gathers the remaining loose items and packs them into a new box.

Imagine you are cleaning a room. You pick up a laptop and a phone to keep on your desk, and then you sweep everything else left on the floor into a single storage bin

In code, the rest syntax acts as that storage bin. It must always come last because it catches whatever is left over.

In Functions (Rest Parameters)

```js
// ✅ Gathering unlimited arguments into a single array named 'numbers'
function sum(...numbers) {
  return numbers.reduce((total, num) => total + num, 0);
}

console.log(sum(1, 2, 3));       // 6
console.log(sum(10, 20, 30, 40)); // 100

```

```js
function trackRace(gold, silver, ...everyoneElse) {
  console.log(`Gold goes to: ${gold}`);
  console.log(`Silver goes to: ${silver}`);
  console.log(`Everyone else:`, everyoneElse); // This becomes an array
}

trackRace('Alex', 'Blake', 'Charlie', 'Dana', 'Eli');
// Output: Everyone else: ['Charlie', 'Dana', 'Eli']

```

In Array Destructuring

```js
const fruits = ['Apple', 'Banana', 'Orange', 'Mango', 'Pineapple'];

// ✅ Extract 'first' and 'second', bundle the 'rest'
const [first, second, ...restFruits] = fruits;

console.log(first);      // 'Apple'
console.log(second);     // 'Banana'
console.log(restFruits); // ['Orange', 'Mango', 'Pineapple'] (A brand new array!)

```

In Object Destructuring (Omit Patterns)

```js
const user = {
  id: 'usr_123',
  username: 'coder99',
  password: 'superSecretPassword123',
  email: 'coder@example.com'
};

// ✅ Extract the password, bundle the remaining public profile data
const { password, ...publicProfile } = user;

console.log(publicProfile); 
// Output: { id: 'usr_123', username: 'coder99', email: 'coder@example.com' }
// The 'password' property was cleanly omitted from the new object!

```

## object destructering

Object destructuring is a JavaScript expression that lets you unpack values from objects into distinct variables using a syntax that mirrors the object's literal structure.

1. The Core Syntax: Traditional vs. Destructured

```js
const user = { name: 'Alex', age: 28, city: 'Berlin' };

// ❌ The Traditional Way
const name = user.name;
const age = user.age;

//  The Destructured Way
const { name, age } = user;

```

Advanced Destructuring Techniques

Renaming Variables

```js
const user = { name: 'Alex', age: 28 };

// Syntax: { targetProperty: newVariableName }
const { name: userName, age: userAge } = user;

console.log(userName); // 'Alex'

```

Setting Default Values


```js
const user = { name: 'Alex' };

// 'guest' is the fallback if 'role' is missing or undefined
const { name, role = 'guest' } = user;

console.log(role); // 'guest'

```

Deep/Nested Destructuring


```js
const user = {
  name: 'Alex',
  metadata: {
    browser: 'Chrome',
    location: { country: 'Germany', city: 'Berlin' }
  }
};

// Extracting 'city' out of the nested structure
const { metadata: { location: { city } } } = user;

console.log(city); // 'Berlin'
// Note: 'metadata' and 'location' are NOT created as variables here; only 'city' is.

```

Function Parameter Destructuring


```js
const user = { id: 101, email: 'alex@example.com', active: true };

// Destructuring directly in the function signature
function sendNotification({ email, active }) {
  if (!active) return;
  console.log(`Sending email to ${email}`);
}

sendNotification(user);

```
Rest Syntax (...) for Object Splitting

```js
const product = { id: 50, title: 'Laptop', price: 999, stock: 12 };

// Extract 'id', group the rest into 'productDetails'
const { id, ...productDetails } = product;

console.log(id);             // 50
console.log(productDetails); // { title: 'Laptop', price: 999, stock: 12 }

```

The Null or Undefined Crash

```js
let user = null;
const { name } = user; // ❌ TypeError: Cannot destructure property 'name' of 'null'.

```
Fix: Safely guard your operations using logical OR (||) defaults: const { name } = user || {};

Destructuring Without Variable Declarations (let/const)

```js
let name = 'Guest';
const user = { name: 'Alex' };

// ❌ SyntaxError: Unexpected token '='
// { name } = user; 

//  Correct syntax
({ name } = user); 

```

If you want to destructure into variables that have already been declared, you must wrap the entire assignment statement in parentheses (). Otherwise, JavaScript misinterprets the { as the start of a code block.

-----------------------------------------

Array destructuring in JavaScript is a shorthand syntax that allows you to unpack values from arrays into distinct variables.

Why It Matters for React Hooks


When you call useState(0), React returns an array containing exactly two elements:

The current state value (e.g., 0)

A updater function to change that value

Without array destructuring, you would have to write your React code like this:

```js
// Without destructuring (verbose)
const countState = useState(0); 
const count = countState[0];
const setCount = countState[1];

```

With array destructuring, you compress those three lines into one clean, readable sentence:

```js
// With destructuring (clean)
const [count, setCount] = useState(0);

```

Position-Based Assignment


```js
const fruits = [' Apple', ' Banana', ' Cherry'];

// The first variable gets index 0, the second gets index 1
const [first, second] = fruits;

console.log(first);  // Output:  Apple
console.log(second); // Output:  Banana

```

Skipping Elements

```js
const colors = [' Red', ' Green', ' Blue', ' Yellow'];

// Skip the first and third elements
const [, green, , yellow] = colors;

console.log(green);  // Output: Green
console.log(yellow); // Output: Yellow

```

Default Values

```js
const settings = ['Dark Mode'];

// 'system' is used as a fallback because there is no second element
const [theme, layout = 'Grid'] = settings;

console.log(theme);  // Output: Dark Mode
console.log(layout); // Output: Grid
```

Gathering Remaining Elements (Rest Pattern)

```js
const highScores =;

// Top score goes to 'winner', the rest go into the 'runnersUp' array
const [winner, ...runnersUp] = highScores;

console.log(winner);    // Output: 98
console.log(runnersUp); // Output: [85, 72, 60, 55]

```

Swapping Variables

```js
let a = 1;
let b = 2;

[a, b] = [b, a];

console.log(a); // Output: 2
console.log(b); // Output: 1

```

ested Array Destructuring

```js
const matrix = [[1, 2], [3, 4]];

// Extracting '2' from the first array and '3' from the second array
const [[, two], [three]] = matrix;

console.log(two);   // Output: 2
console.log(three); // Output: 3

```

why we should default to const??

In JavaScript (JS), defaulting to const is the gold standard for writing clean, predictable code


JavaScript has unique architectural features—like dynamic typing, global object binding, and asynchronous event loops—that make const especially critical compared to other languages.

It Eliminates Temporal Dead Zone (TDZ) Bugs vs. var

Before ES6, JavaScript only had var, which is "hoisted" to the top of its scope and initialized as undefined. This allowed developers to accidentally use variables before declaring them, causing silent bugs.

Both const and let use block-scoping and are subject to the Temporal Dead Zone.

They cannot be accessed before their declaration line, immediately throwing a ReferenceError instead of failing silently with undefined.

It Prevents Global Window Pollution

If you accidentally declare a variable with var in the global scope, it attaches itself to the global window object (e.g., window.myVariable), potentially overwriting native browser APIs. Both const and let do not attach themselves to window, keeping the global namespace clean and secure.

It Halts Accidental Overwriting in Closures

JavaScript relies heavily on closures (functions inside functions that "remember" their outer variables). If you use let for an outer variable, a nested asynchronous function (like a fetch call or a setTimeout) might accidentally change that value, breaking the logic of another function relying on it. const guarantees that the value inside the closure remains locked to its original reference.

A JavaScript const must be initialized on the exact line it is declared.

No, let and var do not need to be initialized when declared. 
