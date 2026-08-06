package Termianl_System;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileStorage extends Storage{
    Path path = Path.of("D:\\Project\\learning_path\\Java_Practise\\Termianl_System\\FileStorageDb.txt");

    @Override
    public void save(List<Task> t) {
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(
            new FileWriter(path.toFile())
   );
        
       for(Task tm:t){
        writer.write(tm.getId()+","+tm.getTitle()+","+tm.getDescription()+","+tm.getPrioriy().toString()+","+tm.getStatus().toString());
        writer.newLine();
       }
       writer.close();
       return;
    }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public List<Task> load() {
        List<Task> tasks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            while((line = reader.readLine()) != null){
                String[] parts = line.split(",");

                int id = Integer.parseInt(parts[0]);
                String title = parts[1];
                String description = parts[2];
                Priority priority = Priority.valueOf(parts[3]);
                Status status = Status.valueOf(parts[4]);

                Task p = createTask(id, title, description, priority, status);
                tasks.add(p);
            }
        } catch (NumberFormatException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return tasks;
    }




}