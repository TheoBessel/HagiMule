package Daemon;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.stream.Stream;

import Diary.Diary;
import File.FileInfoImpl;

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

            // Add owned files
            notifyAllFiles();

        } catch (Exception e) {
            System.err.println("Error while starting Daemon component.");
            e.printStackTrace();
        }
    }

    @Override
    public Integer heartbeat() throws RemoteException {
        String hostname = System.getenv("RMI_IP");
        String port = System.getenv("RMI_PORT");
        try {
            try {
                this.diary = (Diary) Naming.lookup("//" + hostname + ":" + port + "/Diary");
            }
            catch (Exception e) {
                System.err.println("Reconnecting...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    System.err.println(e1);
                }
            }
            this.diary.heartbeat();
        }
        // If the Diary has disconnected
        catch (RemoteException e) {
            return Integer.valueOf(0);
        }
        return Integer.valueOf(1);
    }

    @Override
    public void notifyFileDeletion(String name) throws RemoteException {
        System.out.println("Removing file `" + name + "` from the diary ...");
        this.diary.removeFile(name);
    }

    @Override
    public void notifyFileCreation(String name, Long size, Integer port) throws RemoteException {
        System.out.println("Adding file `" + name + "` with size " + size + " to the diary ...");
        this.diary.addFile(new FileInfoImpl(name, size), port);
    }

    @Override
    public void notifyAllFiles(){
        // Add currently owned files to the diary
        Path workspaceRoot = Paths.get(System.getProperty("user.dir"));
        Path downloadDir = Paths.get(workspaceRoot.toString(), "/downloads");
        try {
            Stream<Path> files = Files.list(downloadDir);
            files.forEach(file -> {
                try {
                    Integer socket_port = Integer.parseInt(System.getenv("TCP_PORT"));
                    notifyFileCreation(file.getFileName().toString(), Files.size(file), socket_port); // Notify
                                                                                                      // Daemon of
                                                                                                      // file
                                                                                                      // creation
                } catch (IOException e) {
                    System.err.println(
                            "Error while trying to find the file `" + file.toString() + "` in the file system.");
                    e.printStackTrace();
                }
            });
            files.close();
        } catch (IOException e) {
            System.err.println("Error while searching for 'downloads' directory");
            e.printStackTrace();
        }
    }
}