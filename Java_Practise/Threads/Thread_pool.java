package Threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Thread_pool {
    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(3);

        for(int i=0;i<5;i++){
            int taskid = i;
            es.submit(()-> {System.out.println("thread is being invoked by the "+ taskid + Thread.currentThread().getName());})
        }
        es.shutdown();
    }   
}
