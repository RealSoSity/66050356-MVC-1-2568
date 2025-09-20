import java.time.LocalDateTime;

public class Project_Model {
    private String project_id;
    private String name;
    private double goal_amount;
    private LocalDateTime deadline;
    private double current_amount;
    private String category;

    public Project_Model(String project_id, String name, double goal_amount, LocalDateTime deadline, double current_amount, String category){
        this.project_id = project_id;
        this.name = name;
        this.goal_amount = goal_amount;
        this.deadline = deadline;
        this.current_amount = current_amount;
        this.category = category;
    }

    public boolean isDeadlineInFuture(){
        return deadline.isAfter(LocalDateTime.now());
    }

    public String getProject_id() {
        return project_id;
    }

    public String getName() {
        return name;
    }

    public double getGoal_amount() {
        return goal_amount;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public double getCurrent_amount() {
        return current_amount;
    }

    public String getCategory() {
        return category;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGoal_amount(double goal_amount) {
        this.goal_amount = goal_amount;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public void setCurrent_amount(double current_amount) {
        this.current_amount = current_amount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    
}


