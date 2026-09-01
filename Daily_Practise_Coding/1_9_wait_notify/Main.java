public class Main {
    public static void main(String[] args){
        Wait_notify_java js = new Wait_notify_java();

        Thread producer = new Thread(()-> {
            for(int i=1;i<=10;i++){
                js.produce(i);
                try{
                    Thread.sleep(200);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });

        Thread consumer = new Thread(()-> {
            for(int i=1;i<=10;i++){
                js.consume();
                try {
                    Thread.sleep(500);
                }catch (InterruptedExceptin e){
                    e.printStackTrace();
                }
            }
        });

        producer.start();
        consumer.start();
    }
}