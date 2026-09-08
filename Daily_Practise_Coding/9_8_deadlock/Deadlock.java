// Deadlocks

//A deadlock happens when two (or more) threads are each waiting for a lock the other one holds — and neither will ever let go.
//Both threads freeze forever.
//        No crash, no error message — your program just silently hangs.
//
//This is the dark side of everything
//you've learned about synchronized locks — the "key" system from Topic 2 can back you into a corner if you're not careful about how multiple locks are acquired.

//The Four Conditions for Deadlock (all must be true)
//
//Worth knowing because it directly tells you how to prevent one — break any single condition and deadlock becomes impossible:
//
//Mutual exclusion — a resource can only be held by one thread at a time (true of any lock).
//Hold and wait — a thread holds one lock while waiting for another.
//No preemption — locks can't be forcibly taken away from a thread; they must be voluntarily released.
//Circular wait — a cycle exists (A waits for B, B waits for A — or longer cycles: A→B→C→A).

//jstack/thread dumps explicitly detect deadlocks.
//If you run jstack <pid> on a hung Java process, it will literally print "Found one Java-level deadlock" along with exactly which threads are waiting on which locks.
//This is a genuinely useful production debugging skill — deadlocks aren't just theory, they show up in real incident reports.
//Timeouts as a safety net. java.util.concurrent locks (like ReentrantLock,
//                                            which we haven't covered yet but is a more flexible alternative to synchronized) support tryLock(timeout) —
//                                                              instead of waiting forever, a thread gives up after a set time and can back off/retry,
//                                                      breaking the "hold and wait forever" pattern. Raw synchronized has no built-in timeout — this is actually one of ReentrantLock's main advantages, worth knowing exists even before we cover it properly.
//                                                              Deadlock vs. Livelock (bonus distinction). Deadlock = threads frozen, doing nothing.
//        Livelock = threads keep actively doing something (e.g., both politely "step aside" for each other repeatedly, like two people in a hallway both dodging the same direction over and over) but still make zero real progress.
//        Different symptom, same root cause: poor coordination logic.

public class DeadlockDemo {
    public static void main(String[] args) {
        Object lockA = new Object();
        Object lockB = new Object();

        Thread t1 = new Thread(() -> {
            synchronized (lockA) {
                System.out.println("Thread 1: locked A, trying for B...");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                synchronized (lockB) {
                    System.out.println("Thread 1: got both locks!");
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (lockB) {
                System.out.println("Thread 2: locked B, trying for A...");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                synchronized (lockA) {
                    System.out.println("Thread 2: got both locks!");
                }
            }
        });

        t1.start();
        t2.start();
    }
}