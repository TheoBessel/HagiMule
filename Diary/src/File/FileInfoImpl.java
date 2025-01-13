package File;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import Device.ClientInfo;
import File.Fragment.FileFragment;

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
        this.owners.remove(owner);
    }

    @Override
    public List<FileFragment> fragmentFile() throws RemoteException {
        List<FileFragment> fragments = new ArrayList<>();
        for (ClientInfo owner : this.owners) {
            fragments.add(
                new FileFragment(
                    this.name,
                    // Fragment size is the total size of the file divided by the number of owners
                    Long.valueOf(this.size / this.owners.size()),
                    // Fragment offset is the index of the owner in the list of owners multiplied by the fragment size
                    Long.valueOf(this.owners.indexOf(owner) * (this.size / this.owners.size())),
                    owner
                )
            );
        }
        return fragments;
    }
}
