public class ThreadLocalLeakDemo {
    static ThreadLocal<Integer> requestId = new ThreadLocal<>();

    static void processRequest(int id, boolean setId) {
        if (setId) {
            requestId.set(id);
        }
        System.out.println("Request " + id + " -> requestId.get() = " + requestId.get()
                + " on " + Thread.currentThread().getName());
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        for (int i = 1; i <= 6; i++) {
            int id = i;
            boolean isOdd = (id % 2 != 0);
            executor.execute(() -> processRequest(id, isOdd)); // only odd IDs call .set()
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }
}