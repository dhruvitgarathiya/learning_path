/*
CompletableFuture

you used Future.get() to retrieve a result form a background task

but we leanred .get() blocks the calling thread untill the result is ready

that's limiting : what if you want to say "when this task finishes autmatically run this next step"

without a thread just sitting there waiting

CompletebaleFuture solve this it let;s you chain async operations togather ("do this then when it's done do that , then that")

without ever manually blocking and lets you combine multiple independent async task elegantly

CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
return 10*10;
)

future.thenApplu(result -> result+5)
.thenApply(finalResult -> System.out.println("Result: " finalResult)
.join();
}

no threads sits aorund blokcing between steps-thenApply / thenAccpet register callbacks that autimically run once the previous stage completes , chained togather like a pipeline


 */

public class Excersice{
    static CompletableFuture<String> fetchUserProfile(int userId){}
     return CompletableFuture.supplyAsync(() -> {
        try {
            Thread.sleep(1000); // simulate network delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "User" + userId;
    });


    static CompletableFuture<String> fetchOrderHistory(int userId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1500); // simulate network delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if (new Random().nextBoolean()) {
                throw new RuntimeException("Order service down!");
            }
            return "Orders for User" + userId + ": [item1, item2]";
        }).exceptionally(ex -> {
            System.out.println("Caught exception: " + ex.getMessage());
            return "Orders unavailable"; // fallback value
        });
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        CompletableFuture<String> profileFuture = fetchUserProfile(1);
        CompletableFuture<String> ordersFuture = fetchOrderHistory(1);
        // both started above — they're already running concurrently on the common pool

        CompletableFuture<String> summaryFuture = profileFuture.thenCombine(
                ordersFuture,
                (profile, orders) -> "Summary: " + profile + " has " + orders
        );

        summaryFuture.thenAccept(summary -> System.out.println(summary));

        summaryFuture.join(); // wait here so main doesn't exit early

        long end = System.currentTimeMillis();
        System.out.println("Total time: " + (end - start) + " ms");
    }
}
