public class FileDownloader implements  Runnable{
    private  String name;
    private long duration;


    public FileDownloader(String name, long duration){
        this.name = name;
        this.duration = duration;
    }



    @Override
    public void run() {
        System.out.println("Starting downlaod: " + this.name);
        try{
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("finished downlaod: "+this.name);
    }
}
