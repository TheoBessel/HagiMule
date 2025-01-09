package file;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class FileInfoImpl extends UnicastRemoteObject implements FileInfo {
    private String name;
    private Integer size;

    public FileInfoImpl(String name, Integer size) throws RemoteException {
        this.name = name;
        this.size = size;
    }

    @Override
    public String getName() throws RemoteException {
        return this.name;
    }

    @Override
    public Integer getSize() throws RemoteException {
        return this.size;
    }
}
