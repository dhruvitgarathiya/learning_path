
import java.util.ArrayList;
import java.util.List;

public class MemoryStroage extends Storage {

    

    private final List<Task> task_memory_db = new ArrayList<>();

    private final List<Task> temp_list = new ArrayList<>();

    public List<Task> getTempList(){
        return temp_list;
    }

    public void addToList(Task t){
        temp_list.add(t);
    }

    public void removeFromList(Task t){
        temp_list.remove(t);
    }


    @Override
    public void save(List<Task> t) {
        for(Task p:t){
            task_memory_db.add(p);
        }
        return;
    }

    @Override
    public List<Task> load() {
      return task_memory_db;
    }


   
}
