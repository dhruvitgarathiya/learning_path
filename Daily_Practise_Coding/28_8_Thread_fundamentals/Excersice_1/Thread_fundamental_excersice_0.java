// excersice 1 - basic thread creation
// Create two threads using Runnable (or lambdas). Each thread should print numbers from 1 to 5, with its thread name, like:
//
//Thread-0: 1
//Thread-1: 1
//Thread-0: 2
//...
//
//Goal: Observe that the output order is not guaranteed. Run it 3–4 times and see if the interleaving pattern changes each run.

public class Thread_fundamental_excersice_0 implements Runnable {


    @Override
    public void run() {
        for(int i=1;i<6;i++){ System.out.println("Thread-0 : " + i);}

    }
}
