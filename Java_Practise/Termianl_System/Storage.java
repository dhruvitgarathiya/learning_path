import java.util.List;

public abstract class Storage {
  
    public abstract void save(List<Task> t);

    public abstract List<Task> load();

}
