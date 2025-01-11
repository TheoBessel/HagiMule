package Daemon;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.file.Files;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.stream.Stream;

import diary.Diary;
import file.FileInfoImpl;

public class DaemonImpl extends UnicastRemoteObject implements Daemon {

    private Diary diary;

    public DaemonImpl() throws RemoteException {
        try {
            diary = (Diary) Naming.lookup("//localhost:4000/Diary");

            System.out.println("[===========================================]");
            System.out.printf("|------ Daemon started on port %d! -------|\n", 4000);
            System.out.println("[===========================================]");

            // Add currently owned files to the diary
            Path workspaceRoot = Paths.get(System.getProperty("user.dir")).getParent();
            Path downloadDir = Paths.get(workspaceRoot.toString(), "/downloads");
            try {
                Stream<Path> files = Files.list(downloadDir);
                files.forEach(file -> {
                    try {
                        notifyCreation(file.toString(), Files.size(file)); // Notify Daemon of file creation
                    } catch (IOException e) {
                        System.err.println("Error could not find file " + file.toString());
                    }
                });
                files.close();
            }
            catch (IOException e) {
                System.err.println("Error while searching for 'downloads' directory");
            }
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
        // this.diary.addFile(new FileInfoImpl(name, size));
    }
}