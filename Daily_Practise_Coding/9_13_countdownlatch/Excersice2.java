public class BarrierDemo {
    static CyclicBarrier barrier = new CyclicBarrier(3,
            () -> System.out.println("--- Phase complete, moving on ---"));

    static void worker(int id) {
        Random rand = new Random();
        try {
            System.out.println("Worker " + id + " doing phase 1");
            Thread.sleep(500 + rand.nextInt(1000));
            barrier.await();

            System.out.println("Worker " + id + " doing phase 2");
            Thread.sleep(500 + rand.nextInt(1000));
            barrier.await();

            System.out.println("Worker " + id + " done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        for (int i = 1; i <= 3; i++) {
            int id = i;
            new Thread(() -> worker(id)).start();
        }
    }
}