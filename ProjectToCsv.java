import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProjectToCsv { //Project model
    private final Path path = Paths.get("./Project.csv");

    public List<Project_Model> findAll() throws Exception {
        List<Project_Model> projects = new ArrayList<>();
        if (!Files.exists(path)) return projects;

        List<String> lines = Files.readAllLines(path, java.nio.charset.StandardCharsets.UTF_8);
        for (int i = 1; i < lines.size(); i++) { // ข้าม header
            String raw = lines.get(i);
            if (raw == null || raw.isBlank()) continue;

            try {
                String[] c = raw.split(",", -1);
                if (c.length < 6) {
                    System.err.println("Skip bad row (cols<6) @" + i + ": " + raw);
                    continue;
                }

                String id   = trimBom(c[0]).trim();
                String name = c[1].trim();

                double goalAmount    = parseDoubleSafe(c[2]);
                java.time.LocalDateTime deadline = parseDateTimeISO(c[3]);
                double currentAmount = parseDoubleSafe(c[4]);

                String category = c[5].trim();
                if (category.isEmpty()) category = null; // เก็บเป็น null ถ้าว่าง

                Project_Model p = new Project_Model(id, name, goalAmount, deadline, currentAmount, category);
                projects.add(p);

            } catch (Exception ex) {
                
                System.err.println("Error parsing row @" + i + ": " + raw);
                ex.printStackTrace();
                
            }
        }
        return projects;
    }

    private static double parseDoubleSafe(String s) {
        String t = (s == null) ? "" : s.trim();
        if (t.isEmpty()) return 0.0;
        return Double.parseDouble(t);
    }

    private static java.time.LocalDateTime parseDateTimeISO(String s) {
        String t = (s == null) ? "" : s.trim();
        if (t.isEmpty()) throw new IllegalArgumentException("deadline_iso is empty");
        return java.time.LocalDateTime.parse(t);
    }

    private static String trimBom(String s) {
        if (s == null || s.isEmpty()) return s;
        final char BOM = '\uFEFF';
        return (s.charAt(0) == BOM) ? s.substring(1) : s;
    }
    public Optional<Project_Model> findById(String id) throws Exception{
        return findAll().stream().filter(p -> p.getProject_id().equals(id)).findFirst();
    }

    public void updateInsert(Project_Model p) throws Exception{
        List<Project_Model> projects = findAll();
        boolean updated = false;
        for(int i = 0; i < projects.size(); i++){
            if(projects.get(i).getProject_id().equals(p.getProject_id())){
                projects.set(i, p);
                updated = true;
                break;
            }
        }

        if(!updated) projects.add(p);

    }

    public void saveAll(List<Project_Model> projects) throws Exception{
        List<String> out = new ArrayList<>();
        out.add("project_id,name,goal_amount,deadline_iso,current_amount,category");
        for(Project_Model p : projects){
            out.add(CsvUtil.toCsv(p.getProject_id(),p.getName(),String.valueOf(p.getGoal_amount()), p.getDeadline().toString(), String.valueOf(p.getCurrent_amount()), p.getCategory() == null? "" : p.getCategory()));
        }

        CsvUtil.writeAll(path, out);
    }
}
