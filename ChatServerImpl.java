import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.*;

public class ChatServerImpl extends UnicastRemoteObject implements ChatServer {
    private final List<ChatClient> clients = new ArrayList<>();

    protected ChatServerImpl() throws RemoteException {
        super();
    }

    public void registerClient(ChatClient client, String name) throws RemoteException {
        clients.add(client);
        broadcastMessage(name + " joined the chat!");
    }

    public void broadcastMessage(String message) throws RemoteException {
        for (ChatClient client : clients) {
            client.receiveMessage(message);
        }
    }
}
