package File.Fragment;

import java.rmi.Remote;
import java.rmi.RemoteException;

import Device.ClientInfo;

public interface FileFragment extends Remote {
    public String getName() throws RemoteException;
    public void setName(String name) throws RemoteException;
    public Long getSize() throws RemoteException;
    public void setSize(Long size) throws RemoteException;
    public Long getOffset() throws RemoteException;
    public void setOffset(Long offset) throws RemoteException;
    public ClientInfo getOwner() throws RemoteException;
    public void setOwner(ClientInfo owner) throws RemoteException;
}
