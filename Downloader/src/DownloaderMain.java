import java.rmi.Naming;
import java.rmi.RemoteException;

import device.ClientInfo;
import diary.Diary;
import file.FileInfo;

public class DownloaderMain {
    public static void main(String[] args) {
        try {
            String hostname = System.getenv("IP");
            Diary diary = (Diary) Naming.lookup("//" + hostname + ":4000/Diary");

            System.out.println("[===========================================]");
            System.out.printf("|--- Downloader started %s:%d ! ---|\n", hostname, 4000);
            System.out.println("[===========================================]");

            getFile("test3.ml", diary);

        } catch (Exception e) {
            System.err.println("Error while starting Downloader component.");
            e.printStackTrace();
        }
    }

    private static FileInfo getFile(String name, Diary diary) throws RemoteException {
        FileInfo f = diary.getFile(name);
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
}
