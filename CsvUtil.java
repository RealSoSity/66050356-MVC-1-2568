import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CsvUtil {
    //Read csv
    public static List<String[]> readAll(Path path) throws IOException{
        if(!Files.exists(path)) return new ArrayList<>();
        List<String> lines = Files.readAllLines(path);
        List<String[]> row = new ArrayList<>();
        for(int i = 0; i < lines.size(); i++){
            String line = lines.get(i);
            if(i == 0) continue; //skip header
            row.add(line.split(",", -1)); //-1 make empty space for project doesn't have category
        }

        return row;
    }

    public static void writeAll(Path path, List<String> lines) throws IOException{
        Files.createDirectories(path.getParent());
        Files.write(path, lines);
    }

    public static String toCsv(String...cols){
        return String.join(",", cols);
    }
}
