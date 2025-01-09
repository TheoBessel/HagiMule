package Daemon;
import java.nio.file.Path;
import java.rmi.Remote;
import java.rmi.RemoteException;

import diary.Diary;

public interface Daemon extends Remote {
    public void notifyDeletion(Path context) throws RemoteException;
    public Diary getDiary() throws RemoteException;
}