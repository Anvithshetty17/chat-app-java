import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class ChatClientImpl extends UnicastRemoteObject implements ChatClient {
    private String name;
    private ChatClientGUI gui;

    protected ChatClientImpl(String name, ChatClientGUI gui) throws RemoteException {
        this.name = name;
        this.gui = gui;
    }

    public void receiveMessage(String message) throws RemoteException {
        gui.displayMessage(message);
    }
}
