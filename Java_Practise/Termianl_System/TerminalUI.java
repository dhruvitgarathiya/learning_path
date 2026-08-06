
import java.util.Scanner;

public class TerminalUI {
    public static void main(String args[]) {

        MemoryStroage ms = new MemoryStroage();

        TaskManager tm = new TaskManager(ms);
        Scanner scanner = new Scanner(System.in);

        System.out.println(
                "choose an option: 1. Add Task\n" +
                "2. View Tasks\n" +
                "3. Complete Task\n" +
                "4. Delete Task\n" +
                "5. Filter Tasks\n" +
                "6. Exi"
        );

        int input = scanner.nextInt();

        switch (input) {

            case 1 -> {
                Priority p = null;
                Status s = null;

                System.out.println("Add your task details");

                System.out.println("add id");
                int id = scanner.nextInt();

                System.out.println("add title");
                String title = scanner.nextLine();

                System.out.println("Add description");
                String description = scanner.nextLine();

                System.out.println("Choose priority: 1.High  2.Medium  3.Low");
                int chose = scanner.nextInt();

                switch (chose) {
                    case 1 -> {
                         p = Priority.HIGH;
                    }
                    case 2 -> {
                         p = Priority.MEDIUM;
                    }
                    case 3 -> {
                         p = Priority.LOW;
                    }
                }

                System.out.println("Choose status: 1.Pending 2.Completed ");
                int chaos = scanner.nextInt();

                switch (chaos) {
                    case 1 -> {
                         s = Status.PENDING;
                    }
                    case 2 -> {
                         s = Status.COMPLETED;
                    }
                }

                tm.addTasks(id, title, description, p, s);
                System.out.println("task is successfully created");
            }

            case 2 -> {
                System.out.println("view your tasklist: ");
                tm.returnTaskList();
            }

            case 3 -> {
                System.out.println("tell me which task you want to mark completed");

                int im = scanner.nextInt();
                Task t = tm.FindTask(im);

                if (t.getStatus() != Status.COMPLETED) {
                    t.setStatus(Status.COMPLETED);
                } else {
                    System.out.println("task is already completed");
                }

                System.out.println("task marked");
            }

            case 4 -> {
                System.out.println("tell me which task you want to delete");

                int is = scanner.nextInt();
                Task t = tm.FindTask(is);

                tm.deleteTasks(t);
                System.out.println("task deleted");
            }

            case 5 -> {
                Status s = null;

                System.out.println("Choose status: 1.Pending 2.Completed ");

                int chaos = scanner.nextInt();

                switch (chaos) {
                    case 1 -> {
                         s = Status.PENDING;
                    }
                    case 2 -> {
                         s = Status.COMPLETED;
                    }
                }

                tm.FilterTask(s);   
            }

            case 6 -> {
                break;
            }
        }

        scanner.close();
    }
}