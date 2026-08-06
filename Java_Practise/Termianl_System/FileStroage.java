// import java.io.BufferedWriter;
// import java.io.File;
// import java.io.FileInputStream;
// import java.io.FileWriter;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.util.List;

// public class FileStroage extends Storage {

//     private final String userHome = System.getProperty("user.home");

//     public String getUserHome(){
//         return userHome;
//     }

//     private final Path filepath = Paths.get(userHome, "Desktop", "TaskList.txt");

//     public Path getFilePath(){
//         return filepath;
//     }

    
//     public void CreateFile(){
//         Path x = filepath.getParent();
//         if(x != null && Files.notExists(x)){
//             Files.createDirectories(x,null);
//             Files.createFile(filepath, null);
//         }
//         return;
//     }
    
    
//     @Override
//     public void save(List<Task> t) {

//         if(Files.exists(filepath, null)){

//             BufferedWriter writer = Files.newBufferedWriter(filepath, null);

//         for(Task i:t){
            
//             writer.write(i.getId());
//             writer.newLine();
//             writer.write(i.getTitle());
//             writer.newLine();
//             writer.write(i.getDescription());
//             writer.newLine();
//             writer.write(i.getPrioriy().toString());
//             writer.newLine();
//             writer.write(i.getStatus().toString());
//             writer.newLine();

//         }
//         }

        

//         return;
      
//     }


//     @Override
//     public List<String> load() {
        
//     }


    

    
    

    
    
// };
