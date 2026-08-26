
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class producer_consumer{
    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);

        Runnable producer  = () -> {
            try {
                for(int i=0;i<10;i++){
                    queue.put(i);
                    System.out.println("produced: " + i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        Runnable consumer = () -> {
            try {
                while(!queue.isEmpty()){
                    Integer item =   queue.take();
                    System.out.println("consumed : "+ item);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        new Thread(producer).start();
        new Thread(consumer).start();
        };
    }
}