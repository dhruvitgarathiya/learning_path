public class PartB {
    private static volatile boolean running = true;

    public static void main(String[] args) throws InterruptedException {
        Thread worker = new Thread(() -> {
            int i = 0;
            while (running) {
                System.out.println("Working" + (i++));
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("stoped");
        });

        worker.start();
        Thread.sleep(3000);
        running = false;

        worker.join();
        System.out.println("Main done");
    }
}