import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Thread f1 = new Thread(new FileDownloader("homie", 2000));
        Thread f2 = new Thread(new FileDownloader("raja", 3000));
        Thread f3 = new Thread(new FileDownloader("rekha", 2500));

        f1.start();
        f2.start();
        f3.start();
}}

// output: 
// Starting downlaod: raja
// Starting downlaod: homie
// Starting downlaod: rekha
// finished downlaod: homie
// finished downlaod: rekha
// finished downlaod: raja

// Process finished with exit code 0


// Simulated Downloads — Why does shortest finish first regardless of start order?

// Once you call .start() on all three threads, each one runs independently, on its own timeline. Thread.sleep(duration) blocks only that thread — it doesn't pause the other threads or main.

// Reasoning: So even if you started the 3000ms download first, it's just sitting there "sleeping" (not blocking anyone else) while the 1000ms download's thread is also running concurrently and finishes its sleep much sooner. This proves the threads are truly independent workers, not a queue executing one after another — that would defeat the whole purpose of threading (it'd be no different from just calling all three run()s back to back).