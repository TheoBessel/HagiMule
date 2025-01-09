package diary;
import java.rmi.Remote;
import java.rmi.RemoteException;

import file.FileInfo;

/**
 * This interface represents a diary, meaning that it contains a listing of all
 * the files and the devices sharing these files to download.
 */
public interface Diary extends Remote {
    public FileInfo getFile(String name) throws RemoteException;
    public void addFile(FileInfo file) throws RemoteException;
    public void removeFile(FileInfo file) throws RemoteException;
}