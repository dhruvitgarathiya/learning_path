import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Main {
    public static void main(String[] args) {

     Thread t1 = new Thread(new Thread_fundamental_excersice_0());
     Thread t2 = new Thread(new Thread_fundamentals_excersice_1());
        System.out.println("Thread starting: ");
     t1.start();
     t2.start();
        System.out.println("Thread ending: ");
    }
}

/// output
/// Thread starting: 
// Thread ending: 
// Thread-1 : 1
// Thread-1 : 2
// Thread-1 : 3
// Thread-1 : 4
// Thread-1 : 5
// Thread-0 : 1
// Thread-0 : 2
// Thread-0 : 3
// Thread-0 : 4
// Thread-0 : 5



// Why is order random?

// When you call .start() on two threads, you're telling the OS: "here are two independent workers, run them whenever you have CPU time." The OS scheduler decides who gets the CPU and for how long — it might run Thread-0 for a tiny slice, pause it, switch to Thread-1, come back, etc.

// Reasoning: There's no code anywhere telling the threads to coordinate or take turns. So the interleaving you see is basically a snapshot of the OS scheduler's mood at that moment — influenced by CPU load, number of cores, JVM internals. That's why it changes between runs. This is your first direct experience of non-determinism in concurrent code.