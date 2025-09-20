public class RewardTier_Model {
    private String id;
    private String project_id;
    private String title;
    private double minAmount;
    private int quota;

    public RewardTier_Model(String id, String project_id, String title, double minAmount, int quota) {
        this.id = id;
        this.project_id = project_id;
        this.title = title;
        this.minAmount = minAmount;
        this.quota = quota;
    }

    public String getId() {
        return id;
    }
    public String getProject_id() {
        return project_id;
    }
    public String getTitle() {
        return title;
    }
    public double getMinAmount() {
        return minAmount;
    }
    public int getQuota() {
        return quota;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setMinAmount(double minAmount) {
        this.minAmount = minAmount;
    }
    public void setQuota(int quota) {
        this.quota = quota;
    }
}
