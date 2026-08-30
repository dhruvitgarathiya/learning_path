public class Main {

    public static void main(String[] args){
        Counter c = new Counter();

        Runnable Task = ()-> {
            for(int i=0;i<10000;i++){
                count++;
            }
        };

        Thread t1 = new Thread(Task);
        Thread t2 = new Thread(Task);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println(c.getCount());

    }



}