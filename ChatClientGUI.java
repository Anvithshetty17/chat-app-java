import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.Naming;

public class ChatClientGUI {
    private JFrame frame;
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private ChatServer server;
    private ChatClientImpl client;
    private String name;

    public ChatClientGUI(String name) {
        this.name = name;
        setupGUI();
        connectToServer();
    }

    private void setupGUI() {
        frame = new JFrame("📨 Java RMI Chat - Logged in as: " + name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout(10, 10));

        // Chat area (display)
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Chat Messages"));

        // Input panel
        inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 14));
        inputField.setPreferredSize(new Dimension(300, 30));

        sendButton = new JButton("Send");
        sendButton.setBackground(new Color(30, 144, 255));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFont(new Font("Arial", Font.BOLD, 14));
        sendButton.setFocusPainted(false);

        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);
        frame.setLocationRelativeTo(null); // center on screen
        frame.setVisible(true);

        // Event listeners
        sendButton.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> sendMessage());
    }

    private void connectToServer() {
        try {
            String serverIP = JOptionPane.showInputDialog(frame, "Enter Server IP Address:", "localhost");
            if (serverIP == null || serverIP.trim().isEmpty()) {
                throw new Exception("No server IP provided");
            }

            client = new ChatClientImpl(name, this);
            server = (ChatServer) Naming.lookup("rmi://" + serverIP.trim() + "/ChatServer");
            server.registerClient(client, name);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error connecting to server:\n" + e.getMessage(),
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void displayMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append(message + "\n");
            chatArea.setCaretPosition(chatArea.getDocument().getLength()); // scroll to bottom
        });
    }

    private void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            try {
                server.broadcastMessage(name + ": " + message);
                inputField.setText("");
            } catch (Exception e) {
                displayMessage("Failed to send message: " + e.getMessage());
            }
        }
    }
}
