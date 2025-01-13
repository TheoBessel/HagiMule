package Downloader;

import java.rmi.Naming;
import java.rmi.RemoteException;

import Device.ClientInfo;
import Diary.Diary;
import Downloader.Fragment.FragmentDownloader;
import File.FileInfo;
import File.Fragment.FileFragment;

public class DownloaderImpl implements Downloader {
    private static Diary diary;
    private String filename;

    public DownloaderImpl(String filename) {
        this.filename = filename;

        try {
            String hostname = System.getenv("RMI_IP");
            String port = System.getenv("RMI_PORT");
            diary = (Diary) Naming.lookup("//" + hostname + ":" + port + "/Diary");

            System.out.println("[===========================================]");
            System.out.printf("Downloader started %s:%s\n", hostname, port);
            System.out.println("[===========================================]");

        } catch (Exception e) {
            System.err.println("Error while starting Downloader component.");
            e.printStackTrace();
        }
    }

    private FileInfo getFile(String name) throws RemoteException {
        FileInfo f = diary.getFile(name);
        System.out.println("Name: " + name);
        System.out.println("[=============== Fichier reçu ==============]");
        System.out.println("  - Nom    : " + f.getName());
        System.out.println("  - Taille : " + f.getSize());
        System.out.println("  - Owners : ");
        for (ClientInfo owner : f.getOwners()) {
            System.out.println("      " + owner.getAddress() + ":" + owner.getPort());
        }
        System.out.println("[===========================================]");
        return f;
    }

    @Override
    public void run() {
        try {
            FileInfo file = getFile(filename);
            System.out.println(file.fragmentFile().size() + " fragments to download.");
            for (FileFragment fragment : file.fragmentFile()) {
                new Thread(new FragmentDownloader(fragment)).start();
            }
        } catch (Exception e) {
            System.err.println("Error while running Downloader component.");
            e.printStackTrace();
        }
    }
}
