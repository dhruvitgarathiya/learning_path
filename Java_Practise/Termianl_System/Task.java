package Termianl_System;

public class Task {

        private int id;
        private String title;
        private String Description;
        private Priority priority;
        private Status status;

        public Task(int id, String title, String Description, Priority priority, Status status){
            this.id = id;
            this.title  = title;
            this.Description = Description;
            this.priority = priority;
            this.status = status;
        }

        public Task(Task other){
            this.id = other.id;
            this.title = other.title;
            this.Description = other.Description;
            this.priority = other.priority;
            this.status = other.status;
        }
        
        public int getId(){
            return this.id;
        }

        public String getTitle(){
            return this.title;
        }

        public String getDescription(){
            return this.Description;
        }

        public Priority getPrioriy(){
            return this.priority;
        }

        public Status getStatus(){
            return this.status;
        }


        public void setId(int id){
            this.id = id;
        }

        public void setTitle(String title){
            if(title.isEmpty()){
                System.out.println("Please Enter Valid Task Title.");
            }
            this.title = title;
        }

        public void setDescription(String Description){
            this.Description = Description;
        }

        public void setPriority(Priority priority){
            this.priority = priority;
        }

        public void setStatus(Status status){
            this.status = status;
        }
        
}
