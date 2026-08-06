    The final Keyword (Restriction)The core meaning of final is finality or unchangeability. It locks a component down so it cannot be altered.1. Applied to a VariableMeaning: It creates a constant. Once a final variable is assigned a value, it can never be changed or reassigned.Variables:Primitive types: The raw value is locked (e.g., final int X = 5;).Reference types (Objects): The reference pointer is locked. You cannot assign it to a new object, but you can change the data inside that object (e.g., adding items to a final ArrayList).2. Applied to a MethodMeaning: It prevents method overriding.Deep Mechanism: A subclass cannot create a method with the exact same signature to change its behavior. This is used when a method’s logic is critical and should never be tampered with by child classes.3. Applied to a ClassMeaning: It prevents inheritance.Deep Mechanism: No other class can use the extends keyword on a final class. All methods inside a final class implicitly become final as well.The static Keyword (Class-Level Scope)The core meaning of static is belonging to the class itself, rather than to an individual instance (object) of the class.1. Applied to a VariableMeaning: It creates a shared variable.Deep Mechanism: Only one single copy of this variable exists in memory, no matter how many objects you create from that class. If one object changes a static variable, it changes for all other objects. You access it using the class name (e.g., Math.PI).2. Applied to a MethodMeaning: It creates a utility method.Deep Mechanism: The method can be called without creating an object of the class (e.g., Math.sqrt(16)).Constraint: Static methods cannot access non-static instance variables or call non-static methods directly because they do not run inside the context of an object.3. Applied to a ClassMeaning: It can only be applied to nested classes (classes inside another class).Deep Mechanism: A static nested class behaves exactly like a regular top-level class. It is grouped inside the outer class for packaging convenience but does not require an instance of the outer class to be created


           // OuterClass.StaticNestedClass obj = new OuterClass.StaticNestedClass();
        }
    }
3. Access Modifiers: private, protected, and publicJust like static, top-level classes have restrictions here. A top-level class can only be public or package-private (no keyword). Only nested classes can be private or protected.A. private (Nested Classes Only)Deep Mechanism: Restricts the visibility of the nested class strictly to the outer class containing it. No external class (even in the same package) can see or instantiate it.Why use it? Perfect for encapsulation. If a helper class is only useful to one specific class, hide it completely.Example: A LinkedList class might have a private static class Node because outside users don't need to know how nodes are structured.B. protected (Nested Classes Only)Deep Mechanism: The nested class is visible within the same package and by any subclasses of the outer class (even if the subclasses are in different packages).C. public (Top-level or Nested)Deep Mechanism: The class is visible to every other class in the entire project (provided the module/package is exported).D. Package-Private / Default (No keyword)Deep Mechanism: If you write class MyClass {} without a modifier, it is only visible to classes inside the exact same package.4. The abstract Keyword (Template Classes)An abstract class is a blueprint that cannot be instantiated on its own.Deep Mechanism: It forces other developer-created classes to extend it and implement its abstract methods. It is the exact opposite of final (an abstract class must be inherited to be useful, while a final class cannot be inherited).Example:javapublic abstract class Animal {
    public abstract void makeSound(); // Subclasses must implement this
}
Use code with caution.
---

### 5. Advanced Modern Keywords (`sealed`, `non-sealed`, `record`)
Java has added highly requested class-level keywords in recent versions:

*   **`sealed`:** Allows you to restrict inheritance, but not completely like `final`. You explicitly list *exactly* which subclasses are allowed to extend it using the `permits` clause.
    ```java
    public sealed class Shape permits Circle, Square {}
    ```
*   **`non-sealed`:** Used by a subclass of a sealed class to open itself back up to regular inheritance by any class.
*   **`record`:** A special type of class designed purely to hold immutable data. It automatically generates constructors, getters, `equals()`, `hashCode()`, and `toString()` under the hood.

---

To help clarify how these rules change depending on where the class is defined, tell me:
* Are you designing a **top-level class** or a **nested inner class**?
* Which **Java version** are you currently working with? 

Knowing this will let me tailor deeper architectural patterns for your specific code!