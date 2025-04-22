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
        frame = new JFrame("Chat - " + name);
        chatArea = new JTextArea(20, 40);
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        inputField = new JTextField(30);
        sendButton = new JButton("Send");

        JPanel panel = new JPanel();
        panel.add(inputField);
        panel.add(sendButton);

        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(panel, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> sendMessage());

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void connectToServer() {
        try {
            client = new ChatClientImpl(name, this);
            server = (ChatServer) Naming.lookup("rmi://localhost/ChatServer");
            server.registerClient(client, name);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to connect to server.");
        }
    }

    public void displayMessage(String message) {
        chatArea.append(message + "\n");
    }

    private void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            try {
                server.broadcastMessage(name + ": " + message);
                inputField.setText("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
