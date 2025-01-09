import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import diary.Diary;
import diary.DiaryImpl;

public class DiaryMain {
    /**
     * Main function for Diary component.
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        try {
            Diary diary = new DiaryImpl();
            LocateRegistry.createRegistry(4000);
            Naming.rebind("//localhost:4000/Diary", diary);

            System.out.println("[===========================================]");
            System.out.printf("|------- Diary started on port %d! -------|\n", 4000);
            System.out.println("[===========================================]");
        } catch (Exception e) {
            System.err.println("Error while starting Diary component.");
            e.printStackTrace();
        }
    }
}
