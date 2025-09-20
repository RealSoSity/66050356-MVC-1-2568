import javax.swing.JFrame;

public class MainApp {
    public static void main(String[] args) {
        ProjectToCsv projectToCsv = new ProjectToCsv();
        PledgeToCsv pledgeToCsv = new PledgeToCsv();
        RewardTierToCsv rewardTierToCsv = new RewardTierToCsv();
        Service service = new Service(projectToCsv, rewardTierToCsv, pledgeToCsv);

        MainFrame frame = new MainFrame();
        AuthController authController = new AuthController(frame);
        ProjectController projectController = new ProjectController(frame, projectToCsv, rewardTierToCsv, pledgeToCsv, service, authController);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        frame.getBtnSignIn().addActionListener(e -> authController.promptLogin());
        frame.getBtnSignOut().addActionListener(e -> authController.logout());
        try {
            projectController.loadProjectsToList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        
        
        

    }
}
