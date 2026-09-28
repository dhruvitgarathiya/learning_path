JavaScript features eight core data types, which are divided into two main categories: Primitives and Objects (Non-Primitives). Because JavaScript is a dynamically and weakly typed language, variables do not have fixed types; instead, types are bound to the values they hold at runtime

Primitive Data Types: Number, BigInt, String, Boolean, Null, Undefined, Symbol
Immutable: The value itself cannot be changed.
Stored by value directly on the Stack memory.

Non premitive data types : Object (includes Arrays, Functions, Dates)
Mutable: The properties can be altered freely.
Stored by reference on the Heap memory.

### The Number Type :

Unlike many languages that separate integers and decimals (floats), JavaScript has a single Number type that handles both

Under the Hood: All numbers are represented as 64-bit floating-point numbers following the IEEE 754 standard. This means 7 is stored identically to 7.0

The Safe Integer Limit: Because of the floating-point structure, JavaScript can only accurately represent integers between -(2^53 - 1) and (2^53 - 1). You can check these limits programmatically using Number.MAX_SAFE_INTEGER and Number.MIN_SAFE_INTEGER. Outside this range, calculations lose precision

Special Numeric Values: Number also includes three conceptual values that don't look like regular numbers:

Infinity: Result of dividing a positive number by zero.

Infinity: Result of dividing a negative number by zero.

NaN (Not a Number): Represents a computational error or undefined mathematical operation (e.g., 'abc' / 2

```js
let count = 42;         // Integer
let price = 19.99;      // Floating-point
let billions = 1_000_000_000; // Numeric separator for readability

console.log(0.1 + 0.2); // Outputs 0.30000000000000004 (A famous IEEE 754 precision quirk!)
console.log(typeof NaN); // "number"

```

2. The BigInt Type (Deep Dive)

Introduced in ES2020, BigInt was created specifically to solve the precision limits of the standard Number type.

Mechanism: It allows you to represent integers of arbitrary length, meaning you can work with numbers larger than the max safe integer limit without losing accuracy.

Syntax: You create a BigInt by appending an n to the end of an integer literal or by calling the BigInt() constructor.

The Strict Rule: You cannot mix BigInt and Number types in the same mathematical operation; you must explicitly convert one type to the other

```js
// Exceeding the standard safe integer limit
let hugeNumber = 9007199254740991n; 
let anotherHuge = BigInt("9007199254740991");

// Mixing types causes an error
// let errorCheck = 5n + 10; // TypeError: Cannot mix BigInt and other types
let correctCheck = 5n + BigInt(10); // 15n

```

3. The String Type 

In JavaScript, a String represents textual data.

Under the Hood: JavaScript strings are encoded using UTF-16 (16-bit Unicode Transformation Format). Each character usually takes up 2 bytes of memory, though some emojis or rare symbols take 4 bytes.

Immutability: Strings are strictly immutable. When you modify a string (e.g., using .toUpperCase()), the original string remains untouched in memory; JavaScript creates a completely new string.

Three Ways to Define:

'Single quotes' or "Double quotes": Functionally identical.

Template literals (Backticks): Introduced in ES6, these allow for string interpolation (${variable}) and multi-line strings without needing newline escape characters (\n).

```js
let name = "Alice";
name[0] = "B"; // Silent failure or error in strict mode. The string does not change.
console.log(name); // "Alice"

// Template Literals
let items = 5;
let message = `You have ${items} items 
in your cart.`; // Multi-line works naturally
```

4. The Boolean Type

The Boolean type represents a logical entity and can only have two values: true or false.

Truthy vs. Falsy: JavaScript evaluates any value in a boolean context (like an if statement) as either "truthy" or "falsy".

The 8 Falsy Values: There are exactly 8 values that evaluate to false in JavaScript. Everything else is truthy (including empty arrays [] and empty objects {}).

false

0 (and -0)

0n (BigInt zero)

"" (Empty string)

nullu

ndefined

NaN

document.all (A legacy browser quirk

Undefined vs. Null (The Absence of Value)

While both represent "nothingness," they serve completely different semantic purposes in JavaScript.


typeof result: null -> object 
undefined = undefined

The typeof null Bug Explained

In the first implementation of JavaScript, values were stored in 32-bit units, consisting of a type tag (1-3 bits) and the actual data. The tag for objects was 000. The value null was represented as the null pointer (all zeros). Because its type tag was 000, typeof null incorrectly returned "object". Changing it now would break millions of legacy websites, so it remains in the language.

7. The Symbol Type

Introduced in ES6 (2015), a Symbol is a guaranteed unique and immutable primitive value.

Purpose: Symbols are primarily used as unique property keys for objects. They prevent property name collisions, ensuring that no other part of the code (or third-party libraries) can accidentally overwrite that property.

Syntax: Symbols are created using the Symbol() factory function. You cannot use the new keyword because it is a primitive, not a constructor.

Hidden, but not Private: Symbol properties do not show up in standard loops like for...in or methods like Object.keys(). However, they are not completely private, as they can still be accessed using Object.getOwnPropertySymbols().

```java
const id1 = Symbol("id");
const id2 = Symbol("id");

console.log(id1 === id2); // false (Every single symbol is completely unique)

const user = {
  name: "Alex",
  [id1]: 12345 // Using a symbol as a key
};

console.log(user[id1]); // 12345

```

### The Object Type (Non-Primitive)

Unlike the seven primitive types that hold a single value, the Object type is a structural container used to store collections of data and more complex entities.

Everything Else is an Object: In JavaScript, Arrays, Functions, Dates, RegEx, and Maps/Sets are all fundamentally Objects under the hood.

Mutability: Objects are mutable. You can change, add, or delete their properties at any time, even if the object variable was declared with const.

Passed by Reference: When you assign an object to a new variable, you are not copying the object data; you are copying the memory address (reference) pointing to where that object lives on the Heap.


```js
// Two identical-looking primitives
let str1 = "hello";
let str2 = "hello";
console.log(str1 === str2); // true (Values are identical)

// Two identical-looking objects
let obj1 = { greeting: "hello" };
let obj2 = { greeting: "hello" };
console.log(obj1 === obj2); // false (They point to different locations in memory!)

// Modifying via reference
let obj3 = obj1;
obj3.greeting = "hi";
console.log(obj1.greeting); // "hi" (Changing obj3 altered obj1 because they share the same reference)

```

To understand how JavaScript truly operates under the hood, you must master the dividing line between Primitive Types and Reference Types (Objects and Arrays).

The core distinction does not lie in what these values look like, but in how JavaScript stores and moves them in your computer's memory


The Core Difference: Value vs. Reference


Primitive Types are stored by value on the Stack. The variable holds the actual data.

Reference Types are stored by reference. The actual data is stored on the Heap (where larger, dynamic memory blocks live), while the variable on the Stack only holds a memory address (pointer) pointing to that Heap location.

3 Ways to Distinguish Them in Action

1. Copying Values (Assignment)

When you assign a primitive to a new variable, JavaScript creates a completely independent clone of that value. When you copy a reference type, JavaScript only copies the pointer, meaning both variables now control the exact same data.

```js
// --- PRIMITIVE COPYING ---
let originalNum = 10;
let copyNum = originalNum; // A brand new '10' is created on the stack
copyNum = 20; 

console.log(originalNum); // 10 (Completely unaffected)

// --- REFERENCE COPYING ---
let originalArray =;
let copyArray = originalArray; // Copies the POINTER, not the array
copyArray.push(4);

console.log(originalArray); // [1, 2, 3, 4] (Mutated because both point to the same memory slot!)

```

2. Comparison (===)

Primitives look at the actual contents. Reference types do not care if the contents look identical; they check if the variables point to the same address in memory.

```js
// Primitives
console.log("hello" === "hello"); // true

// Reference Types
let objA = { value: 5 };
let objB = { value: 5 };

console.log(objA === objB); // false (They occupy two different addresses on the Heap)

let objC = objA;
console.log(objA === objC); // true (They point to the exact same address)

```

3. Mutability vs. Reassignment

A common source of confusion is thinking const makes reference types immutable. const only locks the variable's assignment (the value or the pointer). It cannot stop you from modifying the contents inside the Heap.

```js
// Primitive Immutability
const greeting = "Hello";
// greeting[0] = "h"; // Silent failure. Strings cannot be mutated.

// Reference Mutability
const user = { name: "John" };
user.name = "Jane"; // Works perfectly! The object inside the Heap changed.
// user = { name: "Bob" }; // TypeError! You cannot reassign the pointer itself.

```

When you clone a reference type (an object or an array) in JavaScript, you have to choose between a Shallow Copy and a Deep Copy.


Shallow Cloning: Copies only the top-level properties of an object or array. If any property contains a nested reference type, it copies the memory pointer, meaning the cloned object and the original object still share the same nested sub-data.

Deep Cloning: Recursively copies every single layer. It completely duplicates the top level and all nested objects/arrays, ensuring the clone shares absolutely zero memory connections with the original.

1. Shallow Cloning (Mechanisms & Code)

When you perform a shallow clone, changing a primitive on the clone won't affect the original. But modifying a nested array or object on the clone will alter the original.

Common Ways to Shallow Clone:

Spread Operator (...)

Object.assign()

Array.from() or .slice() (for arrays)

```js
const originalUser = {
  name: "Alice",        // Top-level Primitive
  skills: ["JS", "CSS"] // Nested Reference Type
};

// Perform a shallow clone
const shallowClone = { ...originalUser };

// Modifying top-level primitive
shallowClone.name = "Bob"; 
console.log(originalUser.name); // "Alice" (Unaffected!)

// Modifying nested reference type
shallowClone.skills.push("Git");
console.log(originalUser.skills); // ["JS", "CSS", "Git"] (MUTATED!)

```

Why did this happen? The spread operator created a new container object, but inside that container, it just copied the memory pointer of the skills array. Both originalUser.skills and shallowClone.skills point to the exact same block of memory on the Heap.

2. Deep Cloning (Mechanisms & Code)

Deep cloning severs all structural ties. If you mutate any layer of the cloned object, the original remains perfectly pristine.

Method A: The Modern Standard — structuredClone() (Native API)

```js
const originalUser = {
  name: "Alice",
  skills: ["JS", "CSS"]
};

// Perform a native deep clone
const deepClone = structuredClone(originalUser);

deepClone.skills.push("Git");

console.log(originalUser.skills); // ["JS", "CSS"] (Perfectly safe/unaffected!)
console.log(deepClone.skills);     // ["JS", "CSS", "Git"]
```
which one should you use:

Use Shallow Cloning when your object or array is "flat" (only contains primitives like strings or numbers at the top level), or when you intentionally want nested data to stay linked.

Use structuredClone() whenever you have nested objects/arrays and need a guaranteed, isolated copy without breaking data types.



