package Diary;
import java.rmi.Remote;
import java.rmi.RemoteException;

import File.FileInfo;

/**
 * This interface represents a diary, meaning that it contains a listing of all
 * the files and the devices sharing these files to download.
 */
public interface Diary extends Remote {
    public FileInfo getFile(String name) throws RemoteException;
    public void addFile(FileInfo file, Integer port) throws RemoteException;
    public void removeFile(String name) throws RemoteException;
    public Integer heartbeat() throws RemoteException;
}