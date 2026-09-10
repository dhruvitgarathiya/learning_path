/*
Thread local :

So far, every tool you've learned (synchronized, AtomicInteger, ConcurrentHashMap, locks) has been about safely sharing one piece of data across multiple threads
ThreadLocal flips the idea completely: it gives each thread its own separate, private copy of a variable — so there's nothing to share, and therefore nothing to synchronize.

Think of it as: instead of one shared notebook that everyone fights over (needing locks), every thread gets its own personal notebook with the same label on it.
Thread A writes in its notebook, Thread B writes in its — they never see each other's pages, ever, even though the code refers to "the notebook" using the exact same variable name

TheadLocal<Integer> threadLocalCounter = ThreadLocal.withInitial(() -> 0);
threadLocalCounter.set(threadLocalCounter.get() + 1);
System.out.println(threadLocalCounter.get());

Even though threadLocalCounter is one variable (often static, shared by reference across all threads), each thread that calls .get()/.set() on it is actually reading/writing its own isolated value. No race condition is even possible here — there's no shared state to race over.

Database connections per thread in web servers: instead of one shared Connection object (which would need locking and would bottleneck every request), many frameworks give each request-handling thread its own Connection via ThreadLocal, so each thread can use its connection freely without stepping on others' queries — critical because JDBC Connection objects generally aren't thread-safe at all.

SimpleDateFormat (a classic infamous example): SimpleDateFormat is not thread-safe — sharing one instance across threads causes corrupted date parsing under concurrent use. A common historical fix was wrapping it in a ThreadLocal<SimpleDateFormat> so each thread gets its own formatter instance, avoiding both the bug and the need for locking (though modern Java's DateTimeFormatter is thread-safe natively and makes this workaround unnecessary now — still, you'll see this pattern in older codebases).

SimpleDateFormat (a classic infamous example): SimpleDateFormat is not thread-safe — sharing one instance across threads causes corrupted date parsing under concurrent use. A common historical fix was wrapping it in a ThreadLocal<SimpleDateFormat> so each thread gets its own formatter instance, avoiding both the bug and the need for locking (though modern Java's DateTimeFormatter is thread-safe natively and makes this workaround unnecessary now — still, you'll see this pattern in older codebases).
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadLocalDemo {
    static ThreadLocal<Integer> requestId = new ThreadLocal<>();

    static void processRequest(int id, boolean setId) {
        if (setId) {
            requestId.set(id);
        }
        System.out.println("Processing request " + requestId.get() + " on " + Thread.currentThread().getName());
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Finished request " + requestId.get() + " on " + Thread.currentThread().getName());
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        for (int i = 1; i <= 6; i++) {
            int id = i;
            executor.execute(() -> processRequest(id, true)); // always set — Part A
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }
}