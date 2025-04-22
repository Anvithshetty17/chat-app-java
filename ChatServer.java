import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatServer extends Remote {
    void registerClient(ChatClient client, String name) throws RemoteException;
    void broadcastMessage(String message) throws RemoteException;
}
