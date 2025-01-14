package File;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import Device.ClientInfo;

public class FileInfoImpl extends UnicastRemoteObject implements FileInfo {
    private String name;
    private Long size;
    private List<ClientInfo> owners;

    public FileInfoImpl(String name, Long size) throws RemoteException {
        this.name = name;
        this.size = size;
        this.owners = new ArrayList<>();
    }

    @Override
    public String getName() throws RemoteException {
        return this.name;
    }

    @Override
    public Long getSize() throws RemoteException {
        return this.size;
    }

    @Override
    public List<ClientInfo> getOwners() throws RemoteException {
        return this.owners;
    }

    @Override
    public void addOwner(ClientInfo owner) throws RemoteException {
        this.owners.add(owner);
    }

    @Override
    public void removeOwner(ClientInfo owner) throws RemoteException {
        // this.owners.remove(owner);
        List<ClientInfo> tmp = new ArrayList<>();
        for (ClientInfo c : this.owners) {
            if (!owner.equals(c)) {
                tmp.add(c);
            }
        }
        this.owners = tmp;
        for (ClientInfo c : this.owners) {
            System.out.println(c.getAddress());
        }
    }
}
