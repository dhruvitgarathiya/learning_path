// import java.util.ArrayList;
// import java.util.HashSet;
// import java.util.List;
// import java.util.Set;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

   Thread t = new Thread(()-> {
   for(int i=1;i<=5;i++){
       System.out.println("Worker: "+i);
   }

    });

   t.start();
   System.out.println("Main thread is finsihed");
}}

// output: 
// Main thread is finsihed
// Worker: 1
// Worker: 2
// Worker: 3
// Worker: 4
// Worker: 5

// Process finished with exit code 0


// Exercise 3: Main Thread Doesn't Wait — Why is it unpredictable?

// main starts the worker thread and then immediately moves to the next line (System.out.println("Main thread finished!")). Starting a thread does not pause the thread that started it — main doesn't wait around for the new thread to do anything.

// Reasoning: So you'll almost always see "Main thread finished!" print early — often before any "Worker" lines, sometimes interleaved with them, because the worker thread also needs to be scheduled and actually get CPU time to print. There's a race between "main moving to its next line" and "worker starting up and running its first println." Neither is guaranteed to win — though in practice, main often "wins" because starting a new OS thread has a small delay (thread creation overhead we mentioned earlier), while main just continues immediately. This is exactly the gap that join() exists to fix — coming up soon.