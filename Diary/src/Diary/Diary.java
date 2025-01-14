package Diary;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalTime;
import java.util.Map;

import Device.ClientInfo;
import File.FileInfo;

/**
 * This interface represents a diary, meaning that it contains a listing of all
 * the files and the devices sharing these files to download.
 */
public interface Diary extends Remote {
    public FileInfo getFile(String name) throws RemoteException;
    public Map<String, LocalTime> getClientsLastAliveTimes() throws RemoteException;
    public void addFile(FileInfo file, Integer port) throws RemoteException;
    public void removeFile(String name) throws RemoteException;
    public void removeClient(ClientInfo owner) throws RemoteException;
    public Integer heartbeat(Integer port) throws RemoteException;
}