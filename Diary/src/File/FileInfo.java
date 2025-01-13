package File;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import Device.ClientInfo;

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
    public Long getSize() throws RemoteException;

    /**
     * The getOwners method returns the owners of the file.
     * @return the owners of the file
     */
    public List<ClientInfo> getOwners() throws RemoteException;

    /**
     * The addOwner method adds an owner of the file.
     * @param owner the new owner of the file
     */
    public void addOwner(ClientInfo owner) throws RemoteException;

    /**
     * The removeOwner method removes an owner of the file.
     * @param owner the owner of the file to remove
     */
    public void removeOwner(ClientInfo owner) throws RemoteException;
}