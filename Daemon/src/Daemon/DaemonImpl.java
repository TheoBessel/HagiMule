package Daemon;
import java.nio.file.Path;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import diary.Diary;
import file.FileInfoImpl;

public class DaemonImpl extends UnicastRemoteObject implements Daemon {

    private Diary diary;

    public DaemonImpl() throws RemoteException {
        try {
            String hostname = System.getenv("IP");
            String port = System.getenv("PORT");
            diary = (Diary) Naming.lookup("//" + hostname + ":" + port + "/Diary");

            System.out.println("[===========================================]");
            System.out.printf("Daemon started %s:%s\n", hostname, port);
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
        System.out.println("Adding file `" + name + "` with size " + size + " to the diary !");
        this.diary.addFile(new FileInfoImpl(name, size));
    }
}