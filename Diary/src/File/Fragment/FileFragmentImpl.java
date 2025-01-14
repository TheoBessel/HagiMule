package File.Fragment;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import Device.ClientInfo;

public class FileFragmentImpl extends UnicastRemoteObject implements FileFragment {
    private String name;
    private Long size;
    private Long offset;
    private ClientInfo owner;

    public FileFragmentImpl(String name, Long size, Long offset, ClientInfo owner) throws RemoteException {
        this.name = name;
        this.size = size;
        this.offset = offset;
        this.owner = owner;
    }

    @Override
    public String getName() throws RemoteException {
        return name;
    }

    @Override
    public void setName(String name) throws RemoteException {
        this.name = name;
    }

    @Override
    public Long getSize() throws RemoteException {
        return size;
    }

    @Override
    public void setSize(Long size) throws RemoteException {
        this.size = size;
    }

    @Override
    public Long getOffset() throws RemoteException {
        return offset;
    }

    @Override
    public void setOffset(Long offset) throws RemoteException {
        this.offset = offset;
    }

    @Override
    public ClientInfo getOwner() throws RemoteException {
        return owner;
    }

    @Override
    public void setOwner(ClientInfo owner) throws RemoteException {
        this.owner = owner;
    }
}

