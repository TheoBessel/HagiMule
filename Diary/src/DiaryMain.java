import java.net.Inet4Address;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import Diary.Diary;
import Diary.DiaryImpl;

public class DiaryMain {
    /**
     * Main function for Diary component.
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        try {
            System.setProperty("java.rmi.server.hostname", Inet4Address.getLocalHost().getHostAddress());
            Diary diary = new DiaryImpl();
            String port = System.getenv("RMI_PORT");
            LocateRegistry.createRegistry(Integer.parseInt(port));
            Naming.rebind("//localhost:" + port + "/Diary", diary);

            System.out.println("[===========================================]");
            System.out.printf("Diary started on port %s\n", port);
            System.out.println("[===========================================]");
        } catch (Exception e) {
            System.err.println("Error while starting Diary component.");
            e.printStackTrace();
        }
    }
}
