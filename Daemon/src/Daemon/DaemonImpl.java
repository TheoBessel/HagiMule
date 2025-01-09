package Daemon;
import java.nio.file.Path;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import diary.Diary;
import file.FileInfo;

public class DaemonImpl extends UnicastRemoteObject implements Daemon {

    private Diary diary;

    public DaemonImpl() throws RemoteException {
        try {
            diary = (Diary) Naming.lookup("rmi://localhost:4000/Diary");

            System.out.println("[===========================================]");
            System.out.printf("|------ Daemon started on port %d! -------|\n", 4000);
            System.out.println("[===========================================]");
        } catch (Exception e) {
            System.err.println("Error while starting Daemon component.");
            e.printStackTrace();
        }
    }

    public Diary getDiary() throws RemoteException {
        return this.diary;
    }

    public void notifyDeletion(Path context) throws RemoteException {
        System.out.println(context);
    }

    @Override
    public void notifyDeletion(String name) throws RemoteException {
        System.out.println(name);
        // diary.removeFile(name);
    }

    @Override
    public void notifyCreation(String name, long size) throws RemoteException {
        System.out.println(name + " " + size);
        // this.diary.addFile(new FileInfo(name, size));
    }
}