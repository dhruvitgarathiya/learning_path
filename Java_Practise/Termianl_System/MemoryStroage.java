package Termianl_System;

import java.util.ArrayList;
import java.util.List;

public class MemoryStroage extends Storage {

    private final List<Task> tasks = new ArrayList<>();

    @Override
    public void save(List<Task> t) {
        for(Task p:t){
            tasks.add(p);
        }
        return;
    }

    @Override
    public List<Task> load() {
        return tasks;
    }

    


   
}
