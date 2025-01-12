package Daemon;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Daemon extends Remote {
    public void notifyFileDeletion(String name) throws RemoteException;
    public void notifyFileCreation(String name, Long size, Integer port) throws RemoteException;
}