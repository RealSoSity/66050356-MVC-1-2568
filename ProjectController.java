import javax.swing.*;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProjectController {
    private final MainFrame view;
    private final ProjectToCsv projectRepo;
    private final RewardTierToCsv rewardRepo;
    private final PledgeToCsv pledgeRepo;
    private final Service pledgeService;
    private final AuthController auth;

    public ProjectController(MainFrame view,
                             ProjectToCsv projectRepo,
                             RewardTierToCsv rewardRepo,
                             PledgeToCsv pledgeRepo,
                             Service pledgeService,
                             AuthController auth) {
        this.view = view;
        this.projectRepo = projectRepo;
        this.rewardRepo = rewardRepo;
        this.pledgeRepo = pledgeRepo;
        this.pledgeService = pledgeService;
        this.auth = auth;

        wireActions();
    }

    private void wireActions() {
        // จาก ProjectListPanel: ค้นหา/กรอง/เรียง
        view.getProjectListPanel().onSearch(() -> {
            try {
                loadProjectsToList();
            } catch (Exception e) {
                showErr(e);
            }
        });

        // จาก ProjectListPanel: เปิดหน้า Project Detail
        view.getProjectListPanel().onOpenDetail(pid -> {
            try {
                openProjectDetail(pid);
            } catch (Exception e) {
                showErr(e);
            }
        });

        // จาก ProjectDetailPanel (เวอร์ชันใหม่): ใช้ PledgeListener
        view.getProjectDetailPanel().setPledgeListener((projectId, amount, rewardTierId) -> {
            try {
                if (!auth.ensureLoggedIn()) return;
                String userId = view.getCurrentUserId();
                Pledge_Model result = pledgeService.createPledge(userId, projectId, amount, rewardTierId);

                JOptionPane.showMessageDialog(
                        view,
                        "Result: " + result.getStatus(),
                        "Pledge",
                        JOptionPane.INFORMATION_MESSAGE
                );

                // refresh detail + list (อัปเดตยอด/โควตา/แถบ progress)
                openProjectDetail(projectId);
                loadProjectsToList();
            } catch (Exception e) {
                showErr(e);
            }
        });
    }

    /** โหลดรายการโครงการไปยังหน้ารวม (ค้นหา/กรอง/เรียง) */
    public void loadProjectsToList() throws Exception {
        var pnl = view.getProjectListPanel();
        List<Project_Model> all = projectRepo.findAll();

        String q = pnl.getQuery();
        String cat = pnl.getCategory();
        String sort = pnl.getSortKey();

        if (q != null && !q.isBlank()) {
            String qq = q.toLowerCase();
            all = all.stream()
                    .filter(p -> p.getName() != null && p.getName().toLowerCase().contains(qq))
                    .collect(Collectors.toList());
        }
        if (cat != null && !cat.isBlank()) {
            all = all.stream()
                    .filter(p -> cat.equalsIgnoreCase(p.getCategory()))
                    .collect(Collectors.toList());
        }
        // latest / ending / topfunded
        switch (sort) {
            case "ending" -> all.sort(Comparator.comparing(Project_Model::getDeadline));
            case "topfunded" -> all.sort(Comparator.comparingDouble(Project_Model::getCurrent_amount).reversed());
            default -> all.sort(Comparator.comparing(Project_Model::getProject_id).reversed(
            ));
        }

        // เติมตัวเลือกหมวดหมู่ (สำหรับ combobox)
        var categories = projectRepo.findAll().stream()
                .map(p -> p.getCategory() == null ? "" : p.getCategory())
                .filter(s -> !s.isBlank())
                .distinct()
                .sorted()
                .toList();

        pnl.setCategoryOptions(categories);
        pnl.setProjects(all);
    }

    /** เปิดหน้า Project Detail และกรอกข้อมูล */
    public void openProjectDetail(String projectId) throws Exception {
        var p = projectRepo.findById(projectId).orElse(null);
        if (p == null) {
            JOptionPane.showMessageDialog(view, "Project not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<RewardTier_Model> tiers   = rewardRepo.findById(projectId);
        var pledges = pledgeRepo.findByProjectId(projectId);

        double goal = Math.max(1, p.getGoal_amount());
        int progressPct = (int) Math.floor(p.getCurrent_amount() * 100.0 / goal);

        var detail = view.getProjectDetailPanel();
        detail.showProject(p, tiers, pledges.size(), progressPct);

        view.switchToProjectDetailTab();
    }

    private void showErr(Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
