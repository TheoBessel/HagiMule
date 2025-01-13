package File;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import Device.ClientInfo;
import File.Fragment.FileFragment;
import File.Fragment.FileFragmentImpl;

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
        long fragmentSize = this.size / this.owners.size(); // Taille de base pour chaque fragment
        long remainder = this.size % this.owners.size();    // Reste à attribuer au dernier fragment

        for (int i = 0; i < this.owners.size(); i++) {
            long offset = i * fragmentSize;
            long currentFragmentSize = (i == this.owners.size() - 1) 
                ? fragmentSize + remainder  // Ajoute le reste au dernier fragment
                : fragmentSize;

            fragments.add(
                new FileFragmentImpl(
                    this.name,
                    currentFragmentSize,
                    offset,
                    this.owners.get(i)
                )
            );
        }

        return fragments;
    }
}
