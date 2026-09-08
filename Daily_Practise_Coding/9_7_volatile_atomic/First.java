//synchronized works, but it's a blunt instrument — it locks entire blocks,
//forces threads to fully block/wait,
//and it's easy to over- or under-use.
//For many common situations, Java gives you lighter-weight,
//purpose-built tools that are faster and less error-prone

//volatile — fixes visibility problems (one thread's changes not being seen by another),
//        but does nothing about race conditions on compound operations like count++

//AtomicInteger / AtomicLong / AtomicReference —
//fixes race conditions on single variables without needing synchronized at all, using CPU-level atomic instructions

//ConcurrentHashMap — a thread-safe Map implementation
//that's far more efficient than wrapping a HashMap in synchronized,
//because it doesn't lock the entire map for every operation.

public class First{
    public static void main(String[] args) throws InterruptedException{
        AtomicInteger counter = new AtomicInteger(0);

        Runnable task = () -> {
            for(int i=0;i<10000;i++){
                counter.incermentAndGet();
            }
        }

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("FInal amount: "+ counter.get()); //answer:20000
    }

}