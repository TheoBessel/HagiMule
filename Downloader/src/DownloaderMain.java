import java.rmi.Naming;
import java.rmi.RemoteException;

import device.ClientInfo;
import diary.Diary;
import file.FileInfo;

public class DownloaderMain {
    public static void main(String[] args) {
        try {
            Diary diary = (Diary) Naming.lookup("rmi://localhost:4000/Diary");

            System.out.println("[===========================================]");
            System.out.printf("|----- Downloader started on port %d! -----|\n", 4000);
            System.out.println("[===========================================]");

            getFile("file1", diary);

            getFile("file2", diary);

            getFile("file3", diary);

        } catch (Exception e) {
            System.err.println("Error while starting Daemon component.");
            e.printStackTrace();
        }
    }

    private static FileInfo getFile(String name, Diary diary) throws RemoteException {
        FileInfo f = diary.getFile(name);
        System.out.println("[============ Fichier téléchargé ===========]");
        System.out.println("  - Nom    : " + f.getName());
        System.out.println("  - Taille : " + f.getSize());
        System.out.println("  - Owners : ");
        for (ClientInfo owner : f.getOwners()) {
            System.out.println("    " + owner.getAddress() + ":" + owner.getPort());
        }
        System.out.println("[===========================================]");
        return f;
    }
}
