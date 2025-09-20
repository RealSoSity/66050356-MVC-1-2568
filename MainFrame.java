import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final ProjectListPanel projectListPanel = new ProjectListPanel();
    private final ProjectDetailPanel projectDetailPanel = new ProjectDetailPanel();
    private final StatsPanel statsPanel = new StatsPanel();
    private final JTabbedPane tabs = new JTabbedPane();

    private String currentUserId;
    private final JLabel userBadge = new JLabel("Not logged in yet");

    private final JButton btnSignIn  = new JButton("Sign in");
    private final JButton btnSignOut = new JButton("Sign out");

    public MainFrame() {
        super("Crowdfunding Platform");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        var topBar = new JPanel(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        var left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        var right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        left.add(new JLabel("Project"));
        right.add(userBadge);
        topBar.add(left, BorderLayout.WEST);
        topBar.add(right, BorderLayout.EAST);


        tabs.addTab("Project summary", projectListPanel);
        tabs.addTab("Project Detail", projectDetailPanel);
        tabs.addTab("Static Summary", statsPanel);

        setLayout(new BorderLayout());
        add(topBar, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

        right.add(btnSignIn);
        right.add(btnSignOut);
        updateAuthButtons();
    }


    public JButton getBtnSignIn()  { return btnSignIn; }
    public JButton getBtnSignOut() { return btnSignOut; }

    private void updateAuthButtons() {
        boolean signedIn = currentUserId != null;
        btnSignIn.setVisible(!signedIn);
        btnSignOut.setVisible(signedIn);
    }

    public void updateUserBadge() {
        userBadge.setText(currentUserId == null ? "Not logged in yet" : "User: " + currentUserId);
        updateAuthButtons();
    }

    public void switchToProjectDetailTab() {
        tabs.setSelectedComponent(projectDetailPanel);
    }

    // getters
    public ProjectListPanel getProjectListPanel() { return projectListPanel; }
    public ProjectDetailPanel getProjectDetailPanel() { return projectDetailPanel; }
    public StatsPanel getStatsPanel() { return statsPanel; }

    // session user
    public String getCurrentUserId() { return currentUserId; }
    public void setCurrentUserId(String uid) { this.currentUserId = uid; }
}
