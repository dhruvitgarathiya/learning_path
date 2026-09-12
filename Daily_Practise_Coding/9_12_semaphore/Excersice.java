/*
Semaphore

we have used locks synchronized , reentrantlock that allow excatly one thread in at a time. a semaphore generalizes this: it allows up to n threads in at a time , where n is number you choose it's like a lock but with a configurable number of keys instead of just one

think of it as a parking lot with a fixed number of spots, if the lot has 3 spots up to 3 cars can park simultaneusly
a 4th car must wait at the entrance untill one of 3 leaves and frees up a spot

Semaphore semaphore = new semaphore(3)

semaphore.acquire();
try{
}
finally{
semaphore.release();
}

acquire() is like lock() — it blocks until a permit is free. release() is like unlock() — it returns the permit so someone else waiting can proceed. The key difference from a lock: multiple threads can hold permits simultaneously, not just one.

Database connection pools are a textbook use case: if your database can only handle 10 concurrent connections comfortably, you create a Semaphore(10). Every thread that wants to talk to the database calls acquire() first, does its query, then release()s. This prevents your application from opening, say, 500 simultaneous connections and crushing the database — instead, the 491st thread just waits its turn.

Rate limiting API calls: if a third-party API only allows 5 concurrent requests from your service, wrap your API-calling code with Semaphore(5) so you never accidentally violate that limit, regardless of how many threads in your app want to call it simultaneously.

Limiting concurrent file uploads/downloads: allowing at most, say, 4 simultaneous large file transfers so you don't saturate network bandwidth or memory, even if 100 upload requests come in at once.
 */
public class SemaphoreDemo{
    static Semaphore connectionPool = new Semaphore(3);

    static void useConnection(int requestId){
        try {
            connectionPool.acquire();
            System.out.println(requestId + connectionPool.availblePermits());

            Thread.sleep(2000);

            System.out.println(requestId + connectionPool)
        }catch (InteeruptedException e){
            Thread.currentThread().interrupt();
        } finally {
            connectionPool.release();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 1; i <= 10; i++) {
            int requestId = i;
            executor.execute(() -> useConnection(requestId));
        }

        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);
    }
}
}