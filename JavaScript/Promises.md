## Callback Hell

Callback Hell (also known as the Pyramid of Doom) is a situation in JavaScript where multiple nested callback functions are chained together to handle sequential asynchronous operations

Because each asynchronous step relies on the result of the previous one, the code starts growing horizontally rather than vertically, forming a large pyramid shape (>) that is incredibly difficult to read, maintain, and debug


```js
// Simulated asynchronous checkout flow
loginUser("user@example.com", "password123", (user) => {
    console.log("User logged in:", user.name);
    
    getCartItems(user.id, (items) => {
        console.log("Cart items retrieved:", items);
        
        calculateTotal(items, (total) => {
            console.log("Total calculated:", total);
            
            processPayment(total, (receipt) => {
                console.log("Payment processed:", receipt.id);
                
                sendEmail(user.email, receipt, (response) => {
                    console.log("Email sent successfully!");
                    // More nesting if you need to do anything next...
                }, (emailError) => { console.error(emailError); });
            }, (paymentError) => { console.error(paymentError); });
        }, (calcError) => { console.error(calcError); });
    }, (cartError) => { console.error(cartError); });
}, (loginError) => { console.error(loginError); });
```


## promises

Promises are objects in JavaScript that represent the eventual completion (or failure) of an asynchronous operation and its resulting value. Think of a Promise like a restaurant pager: you place your order, they give you a device, and it sits there quietly until your food is ready (fulfilled) or something goes wrong in the kitchen (rejected).

Before Promises, JavaScript relied entirely on passing callbacks into functions. Promises change this by returning an object that you can attach listeners to instead.

Promises flatten the nested pyramid structure of Callback Hell into a clean, vertical chain using Promise Chaining (.then()).

Instead of passing a function inside another function, a Promise allows you to return a new Promise at the end of each step. This transforms nested code into a readable sequence, where one single .catch() at the bottom can handle any error that happens anywhere in the chain.

How to Create a Promise

You create a Promise using the new Promise() constructor. It takes a function (called the executor) with two arguments:

resolve: A function to call when the operation succeeds.

reject: A function to call when the operation fails.

```js
function getUserData(userId) {
  return new Promise((resolve, reject) => {
    console.log("Fetching data from server...");
    
    setTimeout(() => {
      const success = true; // Simulate whether the request succeeds or fails

      if (success) {
        // Operation succeeded! Pass the data to resolve()
        resolve({ id: userId, name: "Alice", role: "Admin" });
      } else {
        // Operation failed! Pass an error to reject()
        reject(new Error("Failed to fetch user data"));
      }
    }, 2000); // Simulates a 2-second network delay
  });
}

```

How to Consume a Promise

Once a function returns a Promise, you consume it using .then() for success and .catch() for errors.

```js
// Call the function that returns the promise
getUserData(101)
  .then((user) => {
    // This runs ONLY if resolve() was called inside the Promise
    console.log("Success! User found:", user.name);
  })
  .catch((error) => {
    // This runs ONLY if reject() was called or an error was thrown
    console.error("Error caught:", error.message);
  })
  .finally(() => {
    // This runs no matter what (success or failure)
    console.log("Operation complete.");
  });

```

Promise chaining is a technique in JavaScript where you execute multiple asynchronous operations strictly in sequence, passing the result of one step directly down to the next.

It works because the .then() method always returns a new Promise. By returning a value or a new Promise inside a .then(), you pass control down to the next .then() in the chain. This entirely eliminates the need for nesting functions inside one another.

How Promise Chaining Works Under the Hood

If you return a regular value (like a string or a number), JavaScript automatically wraps it in a resolved Promise and passes it to the next .then().

If you return a new Promise, JavaScript pauses the chain, waits for that specific Promise to resolve, and then passes its result to the next .then().

```js
// A simple function that returns a Promise
function delayWithNumber(number) {
  return new Promise((resolve) => {
    setTimeout(() => resolve(number), 1000);
  });
}

// Start the Promise Chain
delayWithNumber(5)
  .then((result) => {
    console.log("Step 1: Received:", result); // Output: 5
    // Rule 2: We return a NEW promise. The chain waits for 1 second.
    return delayWithNumber(result * 2); 
  })
  .then((result) => {
    console.log("Step 2: Multiplied result:", result); // Output: 10
    // Rule 1: We return a regular value. JavaScript passes it immediately to Step 3.
    return result + 3; 
  })
  .then((result) => {
    console.log("Step 3: Final Added result:", result); // Output: 13
  })
  .catch((error) => {
    console.error("An error occurred somewhere in the chain:", error);
  });

```

One of the best features of Promise chaining is that errors propagate down the chain. If an error happens in the very first .then(), JavaScript will skip all subsequent .then() blocks and jump straight down to the .catch() block at the bottom. You do not need to check for errors at every single step.

## The Promise Static Methods.

Static methods are called directly on the global Promise object rather than on an instance. They fall into two main buckets: Combinators (which handle multiple promises concurrently) and Utilities (which create pre-settled promises).

1. Promise.all()

If all pass, it returns an array of values in the exact order of the input array. If one fails, it immediately aborts and throws that specific error, ignoring any pending promises.

```js
const p1 = Promise.resolve(10);
const p2 = new Promise(res => setTimeout(() => res(20), 100));
const p3 = Promise.reject("Boom!");

Promise.all([p1, p2, p3])
  .then(results => console.log(results))
  .catch(err => console.error("Caught:", err)); // Output: Caught: Boom!

```

2.  Promise.allSettled()

It gives you a complete report card. It resolves to an array of objects describing the outcome of each promise.

```js
Promise.allSettled([Promise.resolve("Yes"), Promise.reject("No")])
  .then(results => console.log(results));
/* Output:
[
  { status: "fulfilled", value: "Yes" },
  { status: "rejected", reason: "No" }
]
*/

```
3. Promise.race()

Literally a footrace. The first promise to settle dictates the final outcome.

```js
const timeout = new Promise((_, rej) => setTimeout(() => rej("Timeout!"), 500));
const fetchData = fetch("/api/data"); 

Promise.race([fetchData, timeout])
  .then(data => console.log(data))
  .catch(err => console.error(err)); // Triggers if timeout finishes before fetch
```

4. Promise.any()

It waits for the first successful promise. If every promise fails, it throws a special AggregateError containing all individual rejection reasons.

```js
const brokenLink1 = Promise.reject("404");
const brokenLink2 = Promise.reject("500");
const workingLink = Promise.resolve("Success!");

Promise.any([brokenLink1, brokenLink2, workingLink])
  .then(val => console.log(val)) // Output: "Success!"
  .catch(err => console.log(err.errors)); // Only runs if ALL fail

```

The 2 Utility Methods

These are shortcuts used to convert immediate values or errors into standard Promise objects instantly.

Promise.resolve(value): Returns a promise that is already fulfilled with the given value. If you pass an existing promise to it, it simply returns that promise back to you.

Promise.reject(reason): Returns a promise that is already rejected with the given reason.

```js
// Useful for turning a synchronous cached value into a promise
function getCachedData() {
  if (cache.exists) {
    return Promise.resolve(cache.data); 
  }
  return fetchNewData(); // Returns a promise
}

```

## async / await

async and await are syntactic sugar built on top of JavaScript Promises. They allow you to write asynchronous code that looks and behaves like synchronous code, making it significantly easier to read, maintain, and debug

1. The Core Concepts

The async Keyword

You place the async keyword before a function declaration. It does two main things:

Forces the function to always return a Promise.

If you return a direct value (like a string or a number) instead of a Promise, JavaScript automatically wraps that value in a resolved Promise

```js
// A regular function wrapped in async
async function greet() {
  return "Hello!"; 
}

// Under the hood, it behaves exactly like this:
function greetWithPromise() {
  return Promise.resolve("Hello!");
}

greet().then(value => console.log(value)); // Logs: "Hello!"

```

The await Keyword

The await keyword can only be used inside an async function (or at the top level of a JavaScript Module)

When you place await before a Promise, it pauses the execution of that specific async function until the Promise settles (either resolves or rejects)

While the function is paused, the main thread is not blocked. The JavaScript engine can still perform other tasks like handling user clicks or rendering animations

Why Do We Need It? (Evolution of Async JS)

Readability -- Excellent (Looks synchronous)

Error Handling -- Native try...catch blocks

low Control -- Native (if, for, while work cleanly)

```js
// 1. Simulating an API database call that takes 1.5 seconds
function fetchUserData(userId) {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      if (userId === 99) {
        resolve({ id: 99, name: "Alex Jones", role: "Admin" });
      } else {
        reject(new Error("User not found in database."));
      }
    }, 1500);
  });
}

// 2. Consuming the Promise using Async/Await
async function displayDashboard(id) {
  console.log("🔄 Fetching dashboard data...");
  
  try {
    // Execution stops here for 1.5s until fetchUserData resolves
    const user = await fetchUserData(id); 
    
    // This line only runs if the line above succeeds
    console.log(`✅ Welcome back, ${user.name}!`);
    console.log(`📊 Loading configuration for role: ${user.role}`);
    
  } catch (error) {
    // If the promise rejects, execution jumps straight here
    console.error(`❌ Error rendering dashboard: ${error.message}`);
  } finally {
    console.log("🏁 Operation finalized.");
  }
}

// 3. Triggering the execution
displayDashboard(99);  // Triggers success flow
// displayDashboard(5); // Triggers catch block flow

```

Advanced Concept: Parallel Execution vs Sequential Execution

A common trap with async/await is writing code that runs too slowly because you accidentally wait for independent operations sequentially.

The Slow Way (Sequential)

If getUsers() takes 2 seconds and getProducts() takes 2 seconds, this entire block takes 4 seconds to complete.

```js
async function getStoreData() {
  const users = await getUsers();       // Waits 2 seconds
  const products = await getProducts(); // Waits another 2 seconds
  return { users, products };
}

```

The Fast Way (Parallel with Promise.all)

By firing the promises first, they run in the background concurrently. This block takes only 2 seconds total.

```js
async function getStoreDataOptimized() {
  // Fire both operations at the same time
  const usersPromise = getUsers();
  const productsPromise = getProducts();

  // Wait for both to finish together
  const [users, products] = await Promise.all([usersPromise, productsPromise]);
  return { users, products };
}

```

To understand exactly what happens in the Call Stack during async/await execution, we must look at how the JavaScript engine uses Execution Contexts and Generators under the hood

When you use async/await, JavaScript does not actually block or freeze the thread. Instead, it literally pauses the function, tears its execution context off the Call Stack, and saves it in memory to be restored later.

The Code Architecture

```js
console.log("1: Start");

async function fetchProcess() {
  console.log("2: Inside Async");
  const data = await Promise.resolve("Data Ready"); 
  console.log("4: Log " + data);
}

fetchProcess();

console.log("3: End");

```

Phase 1: Global Initialization

The JavaScript engine creates the Global Execution Context (GEC) and pushes it onto the bottom of the Call Stack.

Code execution starts. console.log("1: Start") is pushed to the stack, executes, logs "1: Start", and is popped off

Phase 2: Entering the Async Function

The engine encounters fetchProcess()

A Function Execution Context (FEC) for fetchProcess is created and pushed onto the top of the Call Stack.

The engine executes code inside fetchProcess synchronously.

console.log("2: Inside Async") is pushed to the stack, logs the message, and is popped off

|-----------------------|
|  fetchProcess() FEC   |  <- Active Context (Running synchronously)
|-----------------------|
|  Global Context (GEC) |
|_______________________|
       CALL STACK


The await Suspension (The Magic Moment)   

The engine evaluates the expression next to the await keyword: Promise.resolve("Data Ready")

JavaScript realizes it is dealing with a Promise. Behind the scenes, it attaches the remaining body of the fetchProcess function (everything after the await line) as a callback to that promise's .then() handler

The Stack Ejection: The await keyword triggers a yield command. The JavaScript engine freezes the state of fetchProcess (variables, local memory, and exactly where it paused), pops fetchProcess() FEC off the Call Stack, and moves it into the heap memory for safekeeping

fetchProcess() instantly returns a Pending Promise back to the Global Context

|                       |
|                       |  <- fetchProcess() context is EJECTED and saved to Memory Heap!
|-----------------------|
|  Global Context (GEC) |  <- Control returns here immediately
|_______________________|
       CALL STACK


Phase 4: Completing Synchronous Main Code  

With fetchProcess out of the way, the Call Stack continues executing the Global Context.

console.log("3: End") is pushed to the stack, logs "3: End", and is popped off

The main script finishes. The Global Execution Context (GEC) is popped off the Call Stack. The Call Stack is now entirely empty

Phase 5: The Microtask Queue & Resurrection

Meanwhile, because Promise.resolve() resolved immediately, its attached callback (the remainder of our async function) is pushed into the Microtask Queue

The Event Loop continuously checks the Call Stack. As soon as it sees the Call Stack is completely empty, it pulls the item from the Microtask Queue

MICROTASK QUEUE: [ Resume fetchProcess() with "Data Ready" ]

The Context Resurrection: The engine retrieves the saved fetchProcess() FEC from the heap memory and pushes it back onto the Call Stack.

The function resumes exactly where it was paused. The variable data is assigned the value "Data Ready"

```
|-----------------------|
|  fetchProcess() FEC   |  <- Pushed BACK onto the stack to finish execution
|_______________________|
       CALL STACK
```

console.log("4: Log Data Ready") is pushed onto the stack, logs the output, and is popped off.

Finally, fetchProcess() finishes completely and is popped off the Call Stack for the last time.

Core Takeaways of Stack Behavior:

No Thread Blocking: The Call Stack is never blocked waiting for a promise. If code is waiting, it's off the stack.

Synchronous Start: An async function runs 100% synchronously until it hits its very first await statement.

State Preservation: JavaScript behaves like a video game save-state—it packages up the function's scope, removes it from the active stack, and reinstantiates it perfectly when the microtask queue gives the green light


