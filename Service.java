import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;

public class Service { //Business Rules
    private final ProjectToCsv projectToCsv;
    private final RewardTierToCsv rewardTierToCsv;
    private final PledgeToCsv pledgeToCsv;

    public Service(ProjectToCsv projectToCsv, RewardTierToCsv rewardTierToCsv, PledgeToCsv pledgeToCsv) {
        this.projectToCsv = projectToCsv;
        this.rewardTierToCsv = rewardTierToCsv;
        this.pledgeToCsv = pledgeToCsv;
    }

    public Pledge_Model createPledge(String user_id, String project_id, double amount, String rewardTier_id) throws Exception{ //Create pledge
        Pledge_Model pledge = new Pledge_Model(generateId(), user_id, project_id, LocalDateTime.now(), amount, rewardTier_id, null);

        Optional<Project_Model> projectOpt = projectToCsv.findById(project_id); //Find project
        if(projectOpt.isEmpty()) return reject(pledge); //Project not found
        Project_Model project = projectOpt.get();
        if(!project.isDeadlineInFuture()) return reject(pledge); //Project deadline is passed

        RewardTier_Model rewardTier = null;
        if(rewardTier_id != null && rewardTier_id.length() > 0){ //If reward tier is defined
            List<RewardTier_Model> rewardTierOpt = rewardTierToCsv.findById(rewardTier_id); //Find reward tier
            if(rewardTierOpt.isEmpty()) return reject(pledge); //Reward tier not found

            rewardTier = rewardTierOpt.get(0);
            if(!rewardTier.getProject_id().equals(project_id)) return reject(pledge); //Reward tier not belong to project
            if(rewardTier.getQuota() > 0){
                long count = pledgeToCsv.findAll().stream().filter(p -> p.getRewardTier_id() != null && p.getRewardTier_id().equals(rewardTier_id) && p.getStatus() == Pledge_Model.Status.SUCCESS).count();
                if(count >= rewardTier.getQuota()) return reject(pledge); //Quota full
            }
            if(amount < rewardTier.getMinAmount()) return reject(pledge); //Amount less than minimum amount for reward tier
        }

        pledge.setStatus(Pledge_Model.Status.SUCCESS); //All checks passed , set pledge status to success
        pledgeToCsv.append(pledge); //Append pledge to csv

        project.setCurrent_amount(project.getCurrent_amount() + amount); //Increase current amount
        projectToCsv.updateInsert(project); //Update project current amount

        if(rewardTier != null && rewardTier.getQuota() > 0){ //Decrease quota
            rewardTier.setQuota(rewardTier.getQuota() - 1);
            rewardTierToCsv.updateInsert(rewardTier);
        }
        
        return pledge;
    }

    //Reject pledge
    private Pledge_Model reject(Pledge_Model p) throws Exception{
        p.setStatus(Pledge_Model.Status.REJECTED);
        pledgeToCsv.append(p);
        return p;
    }

    //Generate unique id
    private String generateId(){
        return java.util.UUID.randomUUID().toString();
    }
}
