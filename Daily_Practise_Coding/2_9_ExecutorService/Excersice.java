//So far, you've been manually creating Thread objects one by one. In real applications, you don't do that — you use an ExecutorService, which manages a pool of reusable worker threads for you. You just hand it tasks (Runnable/Callable), and it decides which thread runs which task, when.
//
//Think of it as upgrading from "hiring a new chef for every single dish" to "having a fixed kitchen staff that keeps picking up new orders as they come in."
//
//Why not just create a new Thread for every task?
//
//Remember from Topic 1 (behind the scenes): creating a thread is expensive — OS-level stack allocation, registration, etc. If your app handles thousands of short tasks (e.g., a web server with thousands of requests), creating a brand-new Thread per task would:
//
//Waste huge amounts of time/memory on thread creation/destruction instead of actual work.
//Risk crashing the app if too many threads exist simultaneously (each thread reserves stack memory — thousands of threads can exhaust it).
//
//A thread pool solves both: a fixed number of threads are created once, and they keep getting reused for new tasks as old ones finish.
//
//        ExecutorService executor = Executors.newFixedThreadPool(3);
//
//        for(int i=0;i<10;i++){
//            int taskId = i;
//            executor.submit(()-> {
//                Systen.out.println("Task "+ taskId + " Runnig on "+ Thread.currentThread().getName());
//
//        })
//
//        }
//
//        executor.shutdown();

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PartA {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int i = 1; i <= 6; i++) {
            int taskId = i;
            executor.execute(() -> {
                System.out.println("Task " + taskId + " started by " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Task " + taskId + " finished");
            });
        }

        executor.shutdown();
    }
}