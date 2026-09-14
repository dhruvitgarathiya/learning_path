/*
Divide-and-conquer parallelism. Split big task into smaller subtasks recursively, run subtasks in parallel, combine results. Built for CPU-bound work that splits naturally (sorting, summing arrays, tree traversal).

Core idea: fork (split work, spawn subtasks) → join (wait for subtask results, combine).

ForkJoinPool — special thread pool, uses work-stealing: idle threads "steal" tasks from busy threads' queues instead of sitting idle.
RecursiveTask<V> — subtask that returns a result.
RecursiveAction — subtask that returns nothing.

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

class SumTask extends RecursiveTask<Long> {
    int[] arr;
    int start, end;
    static final int THRESHOLD = 1000;

    SumTask(int[] arr, int start, int end) {
        this.arr = arr; this.start = start; this.end = end;
    }

    @Override
    protected Long compute() {
        if (end - start <= THRESHOLD) {
            long sum = 0;
            for (int i = start; i < end; i++) sum += arr[i];
            return sum; // base case, no more splitting
        }
        int mid = (start + end) / 2;
        SumTask left = new SumTask(arr, start, mid);
        SumTask right = new SumTask(arr, mid, end);
        left.fork();               // run left async
        long rightResult = right.compute(); // run right on current thread
        long leftResult = left.join();      // wait for left
        return leftResult + rightResult;
    }
}

// usage:
ForkJoinPool pool = new ForkJoinPool();
long total = pool.invoke(new SumTask(bigArray, 0, bigArray.length));
 */

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.Random;

class SumTask extends RecursiveTask<Long> {
    int[] arr;
    int start, end;
    static final int THRESHOLD = 10_000;

    SumTask(int[] arr, int start, int end) {
        this.arr = arr;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        if (end - start <= THRESHOLD) {
            long sum = 0;
            for (int i = start; i < end; i++) sum += arr[i];
            return sum;
        }
        int mid = (start + end) / 2;
        SumTask left = new SumTask(arr, start, mid);
        SumTask right = new SumTask(arr, mid, end);
        left.fork();
        long rightResult = right.compute();
        long leftResult = left.join();
        return leftResult + rightResult;
    }
}

public class ForkJoinDemo {
    public static void main(String[] args) {
        int size = 10_000_000;
        int[] arr = new int[size];
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(100) + 1;
        }

        // Sequential
        long startSeq = System.currentTimeMillis();
        long seqSum = 0;
        for (int val : arr) seqSum += val;
        long endSeq = System.currentTimeMillis();
        System.out.println("Sequential sum: " + seqSum + " | Time: " + (endSeq - startSeq) + " ms");

        // Fork/Join
        ForkJoinPool pool = new ForkJoinPool();
        long startFJ = System.currentTimeMillis();
        long fjSum = pool.invoke(new SumTask(arr, 0, arr.length));
        long endFJ = System.currentTimeMillis();
        System.out.println("Fork/Join sum: " + fjSum + " | Time: " + (endFJ - startFJ) + " ms");

        System.out.println("Sums match: " + (seqSum == fjSum));
    }
}