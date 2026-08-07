import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileStorage extends Storage {

    private final Path path = Path.of("FileStorageDb.txt");

    @Override
    public void save(List<Task> tasks) {
        try {
            try (BufferedWriter writer = Files.newBufferedWriter(path)) {

                for (Task task : tasks) {

                    writer.write(String.format("%d,%s,%s,%s,%s",
                            task.getId(),
                            task.getTitle(),
                            task.getDescription(),
                            task.getPrioriy(),
                            task.getStatus()
                        ));
                    writer.newLine();
                }
            }

            System.out.println("Tasks saved successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Task> load() {
        List<Task> tasks = new ArrayList<>();

        if (!Files.exists(path)) {
            return tasks;
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {

            String line;
            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(",");

                if (parts.length != 5) continue;

                tasks.add(createTask(
                        Integer.parseInt(parts[0]),
                        parts[1],
                        parts[2],
                        Priority.valueOf(parts[3]),
                        Status.valueOf(parts[4])
                ));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return tasks;
    }

   
}