package Termianl_System;

import java.util.List;
import java.util.Scanner;

public class TerminalUI {

    public static void main(String[] args) {

        // MemoryStroage ms = new MemoryStroage();
        Storage fm = new FileStorage();
        TaskManager tm = new TaskManager(fm);
        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.println("1.Add Task");
            System.out.println("2.View Tasks");
            System.out.println("3.Complete Task");
            System.out.println("4.Delete Task");
            System.out.println("5.Filter Tasks");
            System.out.println("6.Exit");
            System.out.print("Choose an option: ");

            int input = scanner.nextInt();
            scanner.nextLine(); 

            switch (input) {

                case 1 -> {

                    System.out.println("\nadd task");

                    System.out.print("enter id:");
                    int id = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("enter Title: ");
                    String title = scanner.nextLine();

                    System.out.print("enter description: ");
                    String description = scanner.nextLine();

                    Priority priority = null;

                    while (priority == null) {
                        System.out.println("Choose Priority:");
                        System.out.println("1. HIGH");
                        System.out.println("2. MEDIUM");
                        System.out.println("3. LOW");

                        int choice = scanner.nextInt();
                        scanner.nextLine();

                        switch (choice) {
                            case 1 -> priority = Priority.HIGH;
                            case 2 -> priority = Priority.MEDIUM;
                            case 3 -> priority = Priority.LOW;
                            default -> System.out.println("Invalid Priority!");
                        }
                    }

                    Status status = null;

                    while (status == null) {
                        System.out.println("Choose Status:");
                        System.out.println("1. PENDING");
                        System.out.println("2. COMPLETED");

                        int choice = scanner.nextInt();
                        scanner.nextLine();

                        switch (choice) {
                            case 1 -> status = Status.PENDING;
                            case 2 -> status = Status.COMPLETED;
                            default -> System.out.println("Invalid Status!");
                        }
                    }
                    
                    Task pm = fm.createTask(id, title, description, priority, status);
                    tm.addTasks(pm);

                    System.out.println("Task added successfully.");
                }

                case 2 -> {

                    System.out.println("\nview task list");

                     List<Task> tasks = tm.returnTaskList();

                    if (tasks.isEmpty()) {
                        System.out.println("No tasks available.");
                    } else {
                        for (Task task : tasks) {
                            System.out.println(task.getId() + " " + task.getTitle() + " " + task.getDescription() + " " + task.getPrioriy().toString() + " " + task.getStatus().toString() );
                        }
                    }
                }

                case 3 -> {

                    System.out.print("Enter Task ID to mark completed: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();

                    Task task = tm.FindTasks(id);

                    if (task == null) {
                        System.out.println("Task not found.");
                    } else if (task.getStatus() == Status.COMPLETED) {
                        System.out.println("Task is already completed.");
                    } else {
                        task.setStatus(Status.COMPLETED);
                        System.out.println("Task marked as completed.");
                    }
                }

                case 4 -> {

                    System.out.print("Enter Task ID to delete: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();

                    Task task = tm.FindTasks(id);

                    if (task == null) {
                        System.out.println("Task not found.");
                    } else {
                        tm.deleteTasks(task);
                        System.out.println("Task deleted successfully.");
                    }
                }

                case 5 -> {

                    Status status = null;

                    while (status == null) {

                        System.out.println("Filter By:");
                        System.out.println("1. PENDING");
                        System.out.println("2. COMPLETED");

                        int choice = scanner.nextInt();
                        scanner.nextLine();

                        switch (choice) {
                            case 1 -> status = Status.PENDING;
                            case 2 -> status = Status.COMPLETED;
                            default -> System.out.println("Invalid Choice!");
                        }
                    }

                    List<Task> filteredTasks = tm.FilterTask(status);

                    if (filteredTasks.isEmpty()) {
                        System.out.println("No matching tasks found.");
                    } else {
                        for (Task task : filteredTasks) {
                            System.out.println(task.getId() + " " + task.getTitle() + " " + task.getDescription() + " " + task.getPrioriy().toString() + " " + task.getStatus().toString() );
                        }
                    }
                }

                case 6 -> {

                    System.out.println("Thank you for using Task Manager.");
                    scanner.close();
                    return;
                }

                default -> System.out.println("Invalid Option.");
            }
        }
    }
}