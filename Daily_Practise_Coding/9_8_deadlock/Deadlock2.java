public class Deadlock2 {
    public static void main(String[] args) {
        Object lockA = new Object();
        Object lockB = new Object();

        // Both threads now agree: ALWAYS lock A before B, no exceptions
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
            synchronized (lockA) {   // <-- changed: locks A first now, same as t1
                System.out.println("Thread 2: locked A, trying for B...");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                synchronized (lockB) {
                    System.out.println("Thread 2: got both locks!");
                }
            }
        });

        t1.start();
        t2.start();
    }
}