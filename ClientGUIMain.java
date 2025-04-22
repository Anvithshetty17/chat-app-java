import javax.swing.*;

public class ClientGUIMain {
    public static void main(String[] args) {
        String name = JOptionPane.showInputDialog("Enter your name:");
        if (name != null && !name.trim().isEmpty()) {
            SwingUtilities.invokeLater(() -> new ChatClientGUI(name));
        }
    }
}
