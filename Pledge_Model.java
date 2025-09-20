import java.time.LocalDateTime;

public class Pledge_Model {
    public enum Status {SUCCESS, REJECTED};
    private String pledge_id;
    private String user_id;
    private String project_id;
    private LocalDateTime created_at;
    private double amount;
    private String rewardTier_id;
    private Status status; 

    
    public Pledge_Model(String pledge_id,String user_id, String project_id, LocalDateTime created_at, double amount,
            String rewardTier_id, Status status) {
        this.pledge_id = pledge_id;
        this.user_id = user_id;
        this.project_id = project_id;
        this.created_at = created_at;
        this.amount = amount;
        this.rewardTier_id = rewardTier_id;
        this.status = status;
    }

    public String getPledge_id() {
        return pledge_id;
    }

    public void setPledge_id(String pledge_id) {
        this.pledge_id = pledge_id;
    }
    
    public String getUser_id() {
        return user_id;
    }
    public String getProject_id() {
        return project_id;
    }
    public LocalDateTime getCreated_at() {
        return created_at;
    }
    public double getAmount() {
        return amount;
    }
    public String getRewardTier_id() {
        return rewardTier_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }
    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public void setRewardTier_id(String rewardTier_id) {
        this.rewardTier_id = rewardTier_id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}