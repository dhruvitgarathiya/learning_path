/*
CountdownLatch

One-time gate. Starts with count N. Threads call countDown() to decrement. Threads calling await() block until count hits 0. Once 0, gate opens forever — can't reset.

CountDownLatch latch = new CountDownLatch(3);

// 3 worker threads:
latch.countDown();

// main thread:
latch.await(); // blocks until count = 0

Scenario: race start line. Runners wait. Official fires gun 3 times (one per checkpoint ready). At 3rd countdown, everyone starts.

Real use: wait for N services to initialize before app "ready". Or: main thread waits for N worker threads to finish setup before proceeding.

CyclicBarrier

All-threads-meet-here point. N threads must all call await() before any proceed. Unlike latch, reusable — resets automatically after each "release."

CyclicBarrier barrier = new CyclicBarrier(3, () -> System.out.println("All arrived!"));

// each of 3 threads:
barrier.await(); // blocks until all 3 call this

 */

public class LatchDemo {
    static CountDownLatch latch = new CountDownLatch(3);

    static void initService(String name, int delayMs) {
        try {
            System.out.println(name + " initializing...");
            Thread.sleep(delayMs);
            System.out.println(name + " ready.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            latch.countDown();
        }

        public static void main(String[] args) throws InterruptedException {
            long start = System.currentTimeMillis();

            new Thread(() -> initService("DB", 2000)).start();
            new Thread(() -> initService("Cache", 1000)).start();
            new Thread(() -> initService("Auth", 1500)).start();

            latch.await(); // blocks until all 3 countDown()

            long end = System.currentTimeMillis();
            System.out.println("App ready — all services up!");
            System.out.println("Total time: " + (end - start) + " ms"); // ~2000ms
        }
    }
    }