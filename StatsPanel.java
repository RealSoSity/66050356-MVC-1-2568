import javax.swing.*;
import java.awt.*;

public class StatsPanel extends JPanel {
    private final JLabel lbSuccess = new JLabel("0");
    private final JLabel lbRejected = new JLabel("0");

    public StatsPanel() {
        setLayout(new GridLayout(0,2,8,8));
        setBorder(BorderFactory.createEmptyBorder(16,16,16,16));
        add(new JLabel("Successful: "));
        add(lbSuccess);
        add(new JLabel("Rejected: "));
        add(lbRejected);
    }

    public void setStats(long success, long rejected) {
        lbSuccess.setText(String.valueOf(success));
        lbRejected.setText(String.valueOf(rejected));
    }
}
