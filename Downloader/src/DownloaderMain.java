import java.rmi.Naming;
import java.rmi.RemoteException;

import device.ClientInfo;
import diary.Diary;
import downloader.DownloaderImpl;
import file.FileInfo;
import file.fragment.FileFragment;

public class DownloaderMain {
    public static void main(String[] args) {
        try {
            String hostname = System.getenv("RMI_IP");
            String port = System.getenv("RMI_PORT");
            Diary diary = (Diary) Naming.lookup("//" + hostname + ":" + port + "/Diary");

            System.out.println("[===========================================]");
            System.out.printf("Downloader started %s:%s\n", hostname, port);
            System.out.println("[===========================================]");

            FileInfo file = getFile(args[0], diary);

            new Thread(
                new DownloaderImpl(
                    new FileFragment(
                        file.getName(),
                        Integer.valueOf(50),
                        Integer.valueOf(0),
                        file.getOwners().get(0)
                    )
                )
            ).start();

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
