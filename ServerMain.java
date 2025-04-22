import java.rmi.Naming;

public class ServerMain {
    public static void main(String[] args) {
        try {
            ChatServer server = new ChatServerImpl();
            Naming.rebind("ChatServer", server);
            System.out.println("Server is ready...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
