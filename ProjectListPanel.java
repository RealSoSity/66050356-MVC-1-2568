import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class ProjectListPanel extends JPanel {
    private final JTextField tfSearch = new JTextField();
    private final JComboBox<String> cbCategory = new JComboBox<>();
    private final JComboBox<String> cbSort = new JComboBox<>(new String[]{"latest","ending","topfunded"});
    private final JButton btnSearch = new JButton("Search/Filter");
    private final JButton btnOpen = new JButton("Open Detail");

    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID","Name" , "Category","Goal_Amount","Current_Amount","Deadline"}, 0);
    private final JTable table = new JTable(model);

    private Runnable onSearchHandler;
    private Consumer<String> onOpenDetailHandler;

    public ProjectListPanel() {
        setLayout(new BorderLayout(8,8));
        var filterBar = new JPanel(new GridLayout(1,5,8,8));
        filterBar.setBorder(BorderFactory.createEmptyBorder(8,8,0,8));

        cbCategory.addItem(""); // all
        filterBar.add(tfSearch);
        filterBar.add(cbCategory);
        filterBar.add(cbSort);
        filterBar.add(btnSearch);
        filterBar.add(btnOpen);

        add(filterBar, BorderLayout.NORTH);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnSearch.addActionListener(e -> { if (onSearchHandler!=null) onSearchHandler.run(); });
        btnOpen.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0 && onOpenDetailHandler != null) {
                String pid = table.getValueAt(row, 0).toString();
                onOpenDetailHandler.accept(pid);
            }
        });
    }

    public void setProjects(List<Project_Model> items) {
        model.setRowCount(0);
        for (Project_Model p : items) {
            model.addRow(new Object[]{
                    p.getProject_id(), p.getName(), p.getCategory(),
                    p.getCurrent_amount(), p.getCurrent_amount(),
                    p.getDeadline()==null? "" : p.getDeadline().toString()
            });
        }
    }

    public void setCategoryOptions(List<String> cats) {
        cbCategory.removeAllItems();
        cbCategory.addItem("");
        for (var c : cats) cbCategory.addItem(c);
    }

    public String getQuery() { return tfSearch.getText(); }
    public String getCategory() { return (String) cbCategory.getSelectedItem(); }
    public String getSortKey() { return (String) cbSort.getSelectedItem(); }

    // Handlers
    public void onSearch(Runnable r) { this.onSearchHandler = r; }
    public void onOpenDetail(Consumer<String> c) { this.onOpenDetailHandler = c; }
}
