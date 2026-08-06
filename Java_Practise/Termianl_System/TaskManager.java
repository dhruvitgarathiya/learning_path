public class TaskManager {

    MemoryStroage ms;

    public TaskManager(MemoryStroage ms){
        this.ms = ms;
    }
    
    public void addTasks(int id, String title, String Description, Priority priority, Status status){
        Task t = new Task(id, title, Description, priority, status);
        this.ms.addToList(t);
    }

    public void deleteTasks(Task t){
        this.ms.removeFromList(t);
    }

    public Task FindTask(int id){
       
        for(Task t:ms.getTempList()){
            if(t.getId() == id){
                Task x = new Task(id, t.getTitle(), t.getDescription(), t.getPrioriy(),t.getStatus());
                return x;
            }
        }
        return null;
    }

    public void FilterTask(Status s){
        for(Task t:this.ms.getTempList()){
            if(t.getStatus() == s){
                System.out.println(t.getId() + t.getTitle() + t.getDescription() + t.getPrioriy().toString());
            }
        }
        return;
    }

  public void returnTaskList() {
   
    for (Task t : this.ms.getTempList()) {
        System.out.println(t.getId() + " " + 
                           t.getTitle() + " " + 
                           t.getDescription() + " " + 
                           t.getPrioriy() + " " + 
                           t.getStatus());
    }
}





}
