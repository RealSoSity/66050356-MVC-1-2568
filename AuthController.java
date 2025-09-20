import javax.swing.*;

public class AuthController {
    private final MainFrame view;

    public AuthController(MainFrame view) {
        this.view = view;
    }

    /** Ensure a user is logged in. If not, show a minimal login prompt (English). */
    public boolean ensureLoggedIn() {
        if (view.getCurrentUserId() != null) return true;
        return promptLogin();
    }

    /** Show a simple login prompt and store userId into the session (MainFrame). */
    public boolean promptLogin() {
        String userId = JOptionPane.showInputDialog(
                null,
                "Enter your user ID (e.g., u001):",
                "Sign in",
                JOptionPane.QUESTION_MESSAGE
        );
        if (userId == null) { // user canceled
            return false;
        }
        userId = userId.trim();
        if (userId.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null,
                    "User ID cannot be empty.",
                    "Invalid input",
                    JOptionPane.WARNING_MESSAGE
            );
            return false;
        }

        view.setCurrentUserId(userId);
        view.updateUserBadge();
        return true;
    }

    /** Clear the session user and update badge. */
    public void logout() {
        view.setCurrentUserId(null);
        view.updateUserBadge();
        JOptionPane.showMessageDialog(null, "Signed out.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
