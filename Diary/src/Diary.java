import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Diary extends Remote {
    public String getMessage() throws RemoteException;
    public File getFile(String name) throws RemoteException;
}