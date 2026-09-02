public class Excersice2 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThresd(3);
        List<Future<Integer>> future = new ArrayList<>();

        for(int i=1;i=5;i++){
            int num =i;
            Callable<Integer> task = () -> {
                Thread.sleep(1000);
                return num * num;
            };
            future.add(executor.submit(task));
        }

        for(Future<Integer> future : futures){
            System.out.println("Result: "+future.get());
        }

        executor.shutdown();
    }

}