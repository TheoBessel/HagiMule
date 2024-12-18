import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class FileImpl extends UnicastRemoteObject implements File {
    private String name;
    
    public FileImpl(String name) throws RemoteException {
        this.name = name;
    }

    public String getName() throws RemoteException {
        return this.name;
    }
}