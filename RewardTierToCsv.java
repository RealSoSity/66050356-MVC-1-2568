import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RewardTierToCsv {
    private final Path path = Paths.get("./Project.csv");

    //get all reward tier from csv
    public List<RewardTier_Model> findAll() throws Exception{
        List<RewardTier_Model> rewardTiers = new ArrayList<>();
        if(!Files.exists(path)) return new ArrayList<>();
        List<String> lines = Files.readAllLines(path);
        for(int i = 1; i < lines.size(); i++){ //i start at 1 to skip header.
            String[] line = lines.get(i).split(",", -1);
            String id = line[0];
            String project_id = line[1];
            String title = line[2];
            double minAmount = Double.parseDouble(line[3]);
            int quota = Integer.parseInt(line[4]);
            
            RewardTier_Model p = new RewardTier_Model(id, project_id, title, minAmount, quota);
            
            rewardTiers.add(p);
        }

        return rewardTiers;
    }

    //find reward tier by id
    public List<RewardTier_Model> findById(String id) throws Exception{
        return findAll().stream().filter(r -> r.getProject_id().equals(id)).toList();
    }

    //Update if exist otherwise insert
    public void updateInsert(RewardTier_Model r) throws Exception{
        List<RewardTier_Model> rewardTiers = findAll();
        boolean updated = false;
        for(int i = 0; i < rewardTiers.size(); i++){
            if(rewardTiers.get(i).getId().equals(r.getId())){
                rewardTiers.set(i, r);
                updated = true;
                break;
            }
        }

        if(!updated) rewardTiers.add(r);

    }

    //save all reward tier to csv
    public void saveAll(List<RewardTier_Model> rewardTiers) throws Exception{
        List<String> out = new ArrayList<>();
        out.add("rewardtier_id,project_id,title,min_amount,quota");
        for(RewardTier_Model r : rewardTiers){
            out.add(CsvUtil.toCsv(r.getId(),r.getProject_id(),r.getTitle(),String.valueOf(r.getMinAmount()), String.valueOf(r.getQuota())));
        }

        CsvUtil.writeAll(path, out);
    }
}
