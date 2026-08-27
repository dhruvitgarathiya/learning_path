import java.util.ArrayDeque;
import java.util.Deque;
import java.util.PriorityQueue;
import java.util.Queue;

public class Main {
    public static void main(String args[]){
        // BankAccount b1 = new SavingAccount(1, "dhruvit", 200);
        // // b1.deposite(100);
        // // b1.withdraw(50);

        // System.out.println(b1.calculateIntrest());

        // BankAccount b2 = new CurrentAccount(2, "yash", 300);
        // // b2.deposite(200);
        // // b2.withdraw(300);
        // System.out.println(b2.calculateIntrest());

        // BankAccount b3 = new FixedDepositeAccount(3, "meet", 200);
        // b3.withdraw(100);
        // System.out.println(b3.calculateIntrest());
     
        // Deque<String> s = new ArrayDeque<>();

        // s.push("dhruvit");
        // s.push("ram");

        // String mm = s.peek();
        // System.out.println(mm);

       // Elements are ordered naturally (e.g., 1, 2, 3...)
        Queue<Integer> n = new PriorityQueue<>();
  

        n.add(1);
        n.add(3);
        n.add(5);
        n.poll();

        System.out.println(n.peek());


    


        
    }   
}
