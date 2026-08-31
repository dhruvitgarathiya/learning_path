public class Main{
    public static void main(String[] args) throws InterruptedException{}
    Object lock = new Object();

    Thread worker = new Thread(()-> {
        synchronized (lock){
            try{
                Thread.sleep(3000); // hold the lock for 3 second
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    });

    Thread blocker = new Thread(()-> {
        synchronized (lock){
            System.out.println("Blocker got the lock!")
        }
    });

    System.out.println("Step 1 - worker state: "+ worker.getState());

    worker.start();
    System.out.println("Stop 2 - worker state: "+ worker.getState());

    Thread.sleep(100);
    blocker.start();
    Thread.sleep(100);

    System.out.println("Step 3 - blocker state: "+ blocker.getState());
    System.out.println("Step 4 - worker state: "+worker.getState())     ;

    worker.join();
    blocker.join();

    System.out.println("Step 5 - worker state"+ worker.getState());
    System.out.println("Step 5 - blocker state:"+ blocker.getState());

}
