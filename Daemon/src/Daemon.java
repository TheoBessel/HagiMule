import java.rmi.Naming;

public class Daemon {
    public static void main(String[] args) {
        System.out.printf("Daemon started on port %d !\n", 4000);

        try {
            Diary r = (Diary) Naming.lookup("rmi://localhost:4000/DiaryService");
            File f = r.getFile("file3");
            System.out.println("Fichier renvoyé : " + f.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
