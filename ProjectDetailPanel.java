import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProjectDetailPanel extends JPanel {

    //listener interface for pledge action
    public interface PledgeListener {
        void onPledge(String projectId, long amount, String rewardTierId);
    }

    private PledgeListener pledgeListener;

    // session project
    private String currentProjectId;

    // labels
    private final JLabel lbName        = plainLabel("-");
    private final JLabel lbCategory    = plainLabel("-");
    private final JLabel lbGoal        = plainLabel("-");
    private final JLabel lbCurrent     = plainLabel("-");
    private final JLabel lbDeadline    = plainLabel("-");
    private final JLabel lbPledgeCount = plainLabel("0");

    // slim progress bar
    private final JProgressBar progress = new JProgressBar(0, 100);

    // pledge form
    private final JSpinner spAmount = new JSpinner(new SpinnerNumberModel(1, 1, 1_000_000_000, 1));
    private final JComboBox<ComboItem> cbTier = new JComboBox<>();
    private final JButton btnPledge = new JButton("Confirm");

    public ProjectDetailPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setBackground(Color.WHITE);

        // Header
        var header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        var title = new JLabel("Project Details");
        title.setFont(title.getFont().deriveFont(Font.PLAIN, 16f));
        header.add(title, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // Content
        var content = new JPanel(new GridBagLayout());
        content.setOpaque(false);
        var gc = new GridBagConstraints();
        gc.insets = new Insets(4, 4, 4, 4);
        gc.anchor = GridBagConstraints.WEST;
        gc.gridx = 0; gc.gridy = 0;

        addRow(content, gc, "Name", lbName);
        addRow(content, gc, "Category", lbCategory);
        addRow(content, gc, "Goal", lbGoal);
        addRow(content, gc, "Current", lbCurrent);
        addRow(content, gc, "Deadline", lbDeadline);
        addRow(content, gc, "Pledges", lbPledgeCount);

        // Progress
        gc.gridx = 0; gc.gridy++; gc.gridwidth = 2; gc.fill = GridBagConstraints.HORIZONTAL;
        progress.setStringPainted(false);
        progress.setPreferredSize(new Dimension(0, 8));
        content.add(progress, gc);

        add(content, BorderLayout.CENTER);

        // Pledge form
        var form = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        form.setOpaque(false);
        form.add(new JLabel("Amount:"));
        spAmount.setPreferredSize(new Dimension(120, spAmount.getPreferredSize().height));
        form.add(spAmount);

        form.add(new JLabel("Reward:"));
        cbTier.setPreferredSize(new Dimension(260, cbTier.getPreferredSize().height));
        form.add(cbTier);

        form.add(btnPledge);
        add(form, BorderLayout.SOUTH);

        // Action
        btnPledge.addActionListener(e -> {
            if (currentProjectId == null) return;
            long amount = ((Number) spAmount.getValue()).longValue();
            ComboItem item = (ComboItem) cbTier.getSelectedItem();
            String rewardId = (item == null || item.value == null || item.value.isBlank()) ? null : item.value;

            if (pledgeListener != null) {
                pledgeListener.onPledge(currentProjectId, amount, rewardId);
            }
        });
    }

    //populate project details
    public void showProject(Project_Model p, List<RewardTier_Model> tiers, int pledgeCount, int progressPct) {
        currentProjectId = p.getProject_id();

        lbName.setText(nz(p.getName()));
        lbCategory.setText(nz(p.getCategory()));
        lbGoal.setText(String.valueOf(p.getCurrent_amount()));
        lbCurrent.setText(String.valueOf(p.getCurrent_amount()));
        lbDeadline.setText(p.getDeadline() == null ? "-" : p.getDeadline().toString());
        lbPledgeCount.setText(String.valueOf(pledgeCount));

        progress.setValue(Math.max(0, Math.min(100, progressPct)));

        cbTier.removeAllItems();
        cbTier.addItem(new ComboItem("— No reward tier —", ""));
        for (var t : tiers) {
            cbTier.addItem(new ComboItem(
                    t.getTitle() + " (min " + t.getMinAmount() + ", quota " + t.getQuota() + ")",
                    t.getId()
            ));
        }
    }

    // Set pledge listener
    public void setPledgeListener(PledgeListener l) {
        this.pledgeListener = l;
    }

    //helpers for layout and style
    private static void addRow(JPanel parent, GridBagConstraints gc, String left, JComponent right) {
        var l = muted(left);
        gc.gridx = 0; gc.weightx = 0; gc.fill = GridBagConstraints.NONE;
        parent.add(l, gc);
        gc.gridx = 1; gc.weightx = 1; gc.fill = GridBagConstraints.HORIZONTAL;
        parent.add(right, gc);
        gc.gridy++;
    }

    private static JLabel plainLabel(String s) {
        var x = new JLabel(s);
        x.setFont(x.getFont().deriveFont(Font.PLAIN, 13f));
        return x;
    }

    private static JLabel muted(String s) {
        var l = new JLabel(s);
        l.setForeground(new Color(0x666666));
        l.setFont(l.getFont().deriveFont(Font.PLAIN, 12f));
        return l;
    }

    private static String nz(String s) { return s == null ? "-" : s; }

    // Combo box item with label and value for reward tier
    public static class ComboItem {
        public final String label;
        public final String value;
        public ComboItem(String label, String value){ this.label = label; this.value = value; }
        @Override public String toString(){ return label; }
    }
}
