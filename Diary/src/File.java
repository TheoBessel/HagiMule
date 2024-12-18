import java.rmi.Remote;
import java.rmi.RemoteException;

public interface File extends Remote {
    public String getName() throws RemoteException;
}