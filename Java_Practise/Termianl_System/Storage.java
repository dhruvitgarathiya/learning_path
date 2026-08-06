package Termianl_System;

import java.util.List;

public abstract class Storage {
  
    public abstract void save(List<Task> t);

    public abstract List<Task> load();

    public Task createTask(int id, String title, String Description, Priority priority, Status status){
        Task t = new Task(id, title, Description, priority, status);
        return t;
    }

}
