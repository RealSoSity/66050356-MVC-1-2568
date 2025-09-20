import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PledgeToCsv {
    private final Path path = Paths.get("./Pledge.csv");
    private static final String header = "pledge_id,project_id,created_at,amount,rewardtier_id,status";

    //Append log of pledge 
    public synchronized void append(Pledge_Model pledge) throws IOException {
        Files.createDirectories(path.getParent());

        if(!Files.exists(path) || Files.size(path) == 0){
            Files.writeString(path, header + System.lineSeparator(), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }

        String line = CsvUtil.toCsv(pledge.getPledge_id(),pledge.getUser_id(),pledge.getProject_id(),pledge.getCreated_at().toString(),String.valueOf(pledge.getAmount()),pledge.getStatus() == null? "" : pledge.getStatus().name());
        Files.writeString(path, line + System.lineSeparator(), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public List<Pledge_Model> findAll() throws Exception{
        List<Pledge_Model> pledges = new ArrayList<>();
        if(!Files.exists(path)) return new ArrayList<>();
        List<String> lines = Files.readAllLines(path);
        for(int i = 1; i < lines.size(); i++){ //i start at 1 to skip header.
            String[] line = lines.get(i).split(",", -1);
            String id = line[0];
            String user_id = line[1];
            String project_id = line[2];
            LocalDateTime createda_at = LocalDateTime.parse(line[3]);
            double amount = Double.parseDouble(line[4]);
            String reward_id = line[5];
            String status = line[6];

           Pledge_Model p = new Pledge_Model(id, user_id, project_id, createda_at, amount, reward_id, status.length() == 0? null : Pledge_Model.Status.valueOf(status));
            
           pledges.add(p);
        }

        return pledges;
    }

    List<Pledge_Model> findByProjectId(String id) throws Exception{
        return findAll().stream().filter(p -> p.getProject_id().equals(id)).toList();
    }

    List<Pledge_Model> findByUserId(String id) throws Exception{
        return findAll().stream().filter(p -> p.getUser_id().equals(id)).toList();
    }

}
