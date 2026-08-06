package Termianl_System;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    Storage st;

    public TaskManager(Storage st){
        this.st = st;
    }

    //add task
    
    public void addTasks(Task t){
      List<Task> tasks =  st.load();
      tasks.add(t);
      st.save(tasks);
    }

    // delete task

    public void deleteTasks(Task t){
        List<Task> tasks = st.load();
        tasks.remove(t);
        st.save(tasks);
    }

    //find task

    public Task FindTasks(int id){
        List<Task> tasks = st.load();
        for(Task t:tasks){
            if(t.getId() == id){
                return t;
            }
        }
        return null;
    }

    //filter task

    public List<Task> FilterTask(Status s){
        List<Task> filteredTask = new ArrayList<>();
        List<Task> tasks = st.load();
        for(Task t: tasks){
            if(t.getStatus() == s){
                filteredTask.add(t);
            }
        }
        return filteredTask;
    }

    // return list 

  public List<Task> returnTaskList() {
   List<Task> tasks = st.load();
   return tasks;
}





}
