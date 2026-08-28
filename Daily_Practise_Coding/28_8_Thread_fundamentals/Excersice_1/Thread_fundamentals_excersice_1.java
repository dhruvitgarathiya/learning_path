public class Thread_fundamentals_excersice_1 implements Runnable {


    @Override
    public void run() {
        for(int i=1;i<6;i++){
            System.out.println("Thread-1 : "+ i);
        }
    }
}
