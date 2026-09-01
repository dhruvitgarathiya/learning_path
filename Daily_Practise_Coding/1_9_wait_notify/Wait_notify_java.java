// wait() , notify(), notifyAll()

//synchronized solves "only one thread at a time" but sometimes a thread needs to pause and let others go first then be woken up later when a specific condition becomes true. that's wait()/notify() are for
//
//wait() - "i'm giving up the lock and going to sleep, wake up up when something changes"
//
//notify() - "Hey wake up ONE thread that's waiting on this same lock"
//
//notifyAll() - "wake up all thread waiting on this lock - let them all recheck the condtion"
//
//all three can only be called from inside a synchornized block , on the same object you synchronized on. calling wait() outside a synchronized block throws illegalmonintorstateexception
//
//
//    the key insight: why not just use a while(true) loop to check a condition
//
//while(!conditionMet){
//
//        }
//
//this is called busy-waiting the thread burns 100% cpu constantly checking, contributing nothing useful
//
//wait() is the polite version: the thread truly going to sleep ( use zero cpu ) and only wakes up when explicitly told to via notify()/notifyAll()
//
//    a background thread produces items into a shared queue/buffer(e.g. reading log lines from a file, reciveing messages from a network socket)
//
//    one or more worker threads cconsume items from that same queue (e.g. processinf each log lone , handling each message)
//
//    if the queue is empty , consumers must wait() instead of endlessly checking
//
//    if the queue is full (bounded buffer) the producer must wait() untill a consumer makes space
//
//whenever an item is added / removed notifyall() wakes up whoever is waiting to recheck
//
//When a thread wakes up from wait(), it does not automatically mean the condition is now true. It could be a spurious wakeup (the JVM spec explicitly allows threads to wake up without any notify() at all, for low-level implementation reasons), or notifyAll() woke up 5 threads but only 1 dish is available. So every woken thread must re-check the condition itself — hence while, not if
//
//wait() releases the lock; sleep() does NOT. This is a classic interview question. Thread.sleep() keeps holding any lock it has while sleeping — nobody else can enter that synchronized block. wait() fully releases the lock while paused, so other threads can acquire it and eventually call notify().
//
//Prefer notifyAll() over notify() in most real code. notify() wakes exactly one arbitrary waiting thread — if you have multiple different conditions/waiters sharing one lock, you might accidentally wake the "wrong" one (one that still can't proceed), while the "right" one keeps sleeping forever. notifyAll() is safer by default; the performance cost of waking everyone to recheck is usually small compared to the correctness risk.
//
//        This is old-school, low-level machinery. Modern Java code rarely writes raw wait/notify by hand — java.util.concurrent (particularly BlockingQueue, Condition, CountDownLatch) wraps this exact mechanism in safer, easier APIs. But understanding wait/notify is what makes those higher-level tools make sense instead of feeling like magic.
//

public  class Wait_notify_java {

    private int item;
    private boolean hasItem = false;

    public synchronized void product(int value){
        while(hasItem){
            try {
                wait();
            }catch(InterruptedException e){
                e.printStacTrace();
            }
        }

        item = value;
        hasItem = true;
        System.out.println("Produced: "+ value);
        notifyAll(); // wake up consumer

    }

    public synchronized int consume(){
        while(!hasItem){
            try {
                wait();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        hasItem = false;
        System.out.println("consumed: "+ item);
        notfiyAll();
        return item;
    }

}




