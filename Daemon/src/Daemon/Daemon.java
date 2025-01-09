package Daemon;
import java.rmi.Remote;
import java.rmi.RemoteException;

import diary.Diary;

public interface Daemon extends Remote {
    public void notifyDeletion(String name) throws RemoteException;
    public void notifyCreation(String name, long size) throws RemoteException;
    public Diary getDiary() throws RemoteException;
}