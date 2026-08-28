import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Main {
    public static void main(String[] args) {

     Thread t1 = new Thread(new Thread_fundamental_excersice_0());

        System.out.println("Thread is starting: ");
     t1.run();

        System.out.println("Thread ending: ");
    }
}

// output: 

// Thread is starting: 
// Current threads is: main
// Thread ending: 

// Process finished with exit code 0


// Exercise 2: run() vs start() — Why does run() not create a thread?

// run() is just a regular method, like any other method in a class. Calling obj.run() is no different from calling obj.someOtherMethod() — it executes immediately, on whichever thread called it (main, in this case).

// Reasoning: start() is special — it's a native method that talks to the OS and says "allocate a new stack, register a new thread, and when the OS is ready, run this object's run() method on that new thread." Only start() triggers that OS-level machinery. run() is just... a method. This is why the thread name inside run() shows main when you call it directly — there's no new thread involved at all, it's the exact same call stack.