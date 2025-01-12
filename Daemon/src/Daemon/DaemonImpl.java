package Daemon;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            String hostname = System.getenv("RMI_IP");
            String port = System.getenv("RMI_PORT");
            diary = (Diary) Naming.lookup("//" + hostname + ":" + port + "/Diary");

            System.out.println("[===========================================]");
            System.out.printf("Daemon started %s:%s\n", hostname, port);
            System.out.println("[===========================================]");

            // Add currently owned files to the diary
            Path workspaceRoot = Paths.get(System.getProperty("user.dir"));
            Path downloadDir = Paths.get(workspaceRoot.toString(), "/downloads");
            try {
                Stream<Path> files = Files.list(downloadDir);
                files.forEach(file -> {
                    try {
                        Integer socket_port = Integer.parseInt(System.getenv("TCP_PORT"));
                        notifyFileCreation(file.getFileName().toString(), Files.size(file), socket_port); // Notify Daemon of file creation
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

    @Override
    public void notifyFileDeletion(String name) throws RemoteException {
        System.out.println(name);
        // diary.removeFile(name);
    }

    @Override
    public void notifyFileCreation(String name, Long size, Integer port) throws RemoteException {
        System.out.println("Adding file `" + name + "` with size " + size + " to the diary !");
        this.diary.addFile(new FileInfoImpl(name, size), port);
    }
}