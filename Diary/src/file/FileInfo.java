package file;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The File interface reprensents the essential informations the client needs to
 * download files from other clients.
 */
public interface FileInfo extends Remote {
    /**
     * The getName method returns the name of the file.
     * @return the name of the file
     */
    public String getName() throws RemoteException;

    /**
     * The getSize method returns the size of the file.
     * @return the size of the file
     */
    public Integer getSize() throws RemoteException;
}