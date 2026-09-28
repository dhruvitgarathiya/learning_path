function that takes another function as argument ans return another function 

it is called higher order function

```javascript
const radius = [3,1,2,4];

const calculateArea = function(radius){
    const output = [];
    for(int i=0;i<radius.lenght;i++){
        output.push(Math.PI * radius[i]*radius[i]);
    }
    return output;
}

console.log(calculateArea(radius));

const calculateCircumference = function(radius){
    const output = [];

    for(let i=0;i<radius.length;i++){
        output.push(2*Math.PI * radius[i]);
    }
    return output;
}

console.log(calculateCircumference(radius));

const calculateDiameter = function(radius){
    const output = [];

    for(let i = 0;i<radius.length;i++){
        output.push(2*radius[i]);
    }
    return output;
};
```

problem : we are repeating outselves alot

```javascript
const radius = [3,1,2,4];

const area = function(radius){
    return Math.PI * radius * radius;

};

const cacluate = function(radius , logic){
    const output =[];
    for(let i =0;i<radius.length;i++){
        output.push(logic(radius[i]));
    }
    return logic;
};

console.log(calculate(radius, area));
```


this is the buety of functional programming


also the map function will give same output

```javascript
console.log(radius.map(area));
```

```javascript
Array.prototype.calculate = function(arr,logic){
    const output = [];

    for(let i=0;i<arr.length;i++){
        output.push(logic(arr[i]));
    }

    return output;
}
```

now this prototype function will be availbe for the all the array present in the code

like this is making map function of our own


## prototypial inheritance

inheritance in javascript : 

prototpye : when we create js object , js engine attach some object to it

function also being attach to som hidden func and method 

arr._proto_.

prototype is a built-in blueprint or "parent" object that every JavaScript object secretly links to. It allows objects to inherit features, properties, and methods from one another without duplicating code in memory

Think of a prototype like a shared recipe book in a restaurant kitchen. Instead of copying the exact same recipe card for every individual chef, all chefs look at one central master cookbook on the wall


```js
// 1. The Constructor (The Factory)
function Person(name) {
  this.name = name; // Unique to each person
}

// 2. The Prototype (The Shared Blueprint)
Person.prototype.sayHello = function() {
  console.log(`Hello, my name is ${this.name}!`);
};

// 3. Creating Instances
const alice = new Person("Alice");
const bob = new Person("Bob");

alice.sayHello(); // Output: Hello, my name is Alice!
bob.sayHello();   // Output: Hello, my name is Bob!

```

###  What is the "Prototype Chain"?

When you type alice.sayHello(), JavaScript plays a quick game of hide-and-seek called the Prototype Chain:

Step 1: JavaScript looks directly inside the alice object: "Do you have a personal method named sayHello?" ➡️ No.

Step 2: JavaScript automatically follows a hidden link (called __proto__) to Alice's prototype object: "Does your prototype blueprint have sayHello?" ➡️ Yes! JavaScript runs it

Step 3: If it wasn't found there, JavaScript would keep looking higher up the chain until it hits the ultimate base object (Object.prototype), and finally null. If it reaches null without finding it, you get an error

Prototypal inheritance is the process by which one object can automatically access properties and methods from another object, using the prototype chain.

```js
// 1. The Parent Object (The Basic Blueprint)
const animal = {
  isAlive: true,
  eat: function() {
    console.log("Nom nom nom...");
  }
};

// 2. The Child Object (Inheriting from Animal)
const dog = Object.create(animal);

// 3. Giving the Child its own unique properties
dog.bark = function() {
  console.log("Woof woof!");
};

// 🧪 Testing it out:
dog.bark(); // Output: "Woof woof!" (Its own method)
dog.eat();  // Output: "Nom nom nom..." (Inherited from animal!)
console.log(dog.isAlive); // Output: true (Inherited from animal!)


```

Prototypal Inheritance vs. Traditional Class Inheritance

Concept : 


Class ➡️ Object. A class is a rigid blueprint. You instantiate objects out of it like casting metal from a mold.

js : Object ➡️ Object. Objects link directly to other objects. It is fluid and dynamic.


Rigid hierarchies. Harder to change at runtime.

Highly flexible. Objects can share methods dynamically on the fly.

In modern JavaScript (ES6+), developers often use the class and extends keywords because it looks cleaner and mimics other languages. However, under the hood, JavaScript is still just doing prototypal inheritance.

```js
class Animal {
  eat() { console.log("Eating..."); }
}

// "extends" triggers prototypal inheritance behind the scenes
class Dog extends Animal {
  bark() { console.log("Woof!"); }
}

const myDog = new Dog();
myDog.eat(); // Works via the prototype chain!
```


The short answer is: JavaScript does not actually have native classes.When you write the word class in JavaScript, the browser or Node.js environment treats it as a disguise. Under the hood, JavaScript immediately converts that class into functions and objects linked together by prototypes.Developers call this syntactic sugar—a beautiful, easier-to-read syntax wrapped around a completely different underlying engine.

```js
// What YOU write:
class Wizard {
  constructor(name) {
    this.name = name;
  }
  castSpell() {
    console.log("⚡ Expelliarmus!");
  }
}

```

code looks like this but it actuall is :

```js
// What JAVASCRIPT actually creates under the hood:
function Wizard(name) {
  this.name = name;
}

// The method is quietly attached to an object blueprint!
Wizard.prototype.castSpell = function() {
  console.log("⚡ Expelliarmus!");
};

```

Even if you use the class syntax, the methods you create are physically stored inside an object called Wizard.prototype. When you create an instance of that class, that new instance is just an object linking back to the prototype object.

__proto__ is the actual link inside an object instance, whereas prototype is a special blueprint property belonging only to functions/classes

🗺️ The Map Analogy:

prototype is the master blueprint sitting inside the developer's main office. The houses don't have this blueprint inside their walls; the office holds onto it.

__proto__ is the physical road leading from an individual house back to the developer's office so it can check the blueprint whenever it needs maintenance.


While __proto__ is great for understanding how the engine works, it is technically deprecated and slow to use directly in production code

To read a prototype: Use Object.getPrototypeOf(myDog) instead of myDog.__proto__.

To set a prototype: Use Object.setPrototypeOf(myDog, newBlueprint) instead of myDog.__proto__ = newBlueprint



```js
// 1. The Parent Object (The General User)
const user = {
  name: "Unknown",
  age: 0,
  address: "Not Provided",
  greet() {
    console.log(`Hello, my name is ${this.name} and I live in ${this.address}.`);
  }
};

// 2. The Child Object (The Specific Student)
const student = {
  major: "Computer Science",
  study() {
    console.log(`${this.name} is studying hard for exams!`);
  }
};

// 3. Establish the Prototypal Inheritance Link
Object.setPrototypeOf(student, user);

// 4. Update the student's unique data
student.name = "Rahul";
student.age = 21;
student.address = "Gandhinagar, India";

// 🧪 Testing the Methods
student.greet(); // Output: Hello, my name is Rahul and I live in Gandhinagar, India.
student.study(); // Output: Rahul is studying hard for exams!

// 🔍 Confirming the Link
console.log(Object.getPrototypeOf(student) === user); // Output: true

```

When you wrote student.name = "Rahul", you did not overwrite the name property inside the user object. Instead, JavaScript created a brand new name property directly on the surface of the student object. This is called property shadowing. The original user.name remains safely untouched as "Unknown".


### map , filter , reduce

map transofrm to specific val

filter the val

reduce is to find sum or maxium value of array

