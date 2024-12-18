import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedHashMap;
import java.util.Map;

public class DiaryImpl extends UnicastRemoteObject implements Diary {
    /**
     * @param port The runtime port of the Diary component.
     */
    private int port;

    /**
     * @param files The files in the Diary component.
     */
    private Map<String, File> files;

    /**
     * Constructor for Diary component.
     */
    public DiaryImpl() throws RemoteException {
        this.files = new LinkedHashMap<>();
        files.put("file1", new FileImpl("file1"));
        files.put("file2", new FileImpl("file2"));
        files.put("file3", new FileImpl("file3"));
        files.put("file4", new FileImpl("file4"));

        this.port = 4000;
        System.out.printf("Diary started on port %d !\n", this.port);
    }

    /**
     * Main function for Diary component.
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        try {
            Diary diary = new DiaryImpl();

            LocateRegistry.createRegistry(4000);
            
            Naming.rebind("//localhost:4000/DiaryService", diary);
        } catch (Exception e) {
            System.out.println("Error starting Diary component.");
            e.printStackTrace();
        }
    }

    public String getMessage() throws RemoteException {
        return "Hello, world!";
    }

    /**
     * Get the file with the given name.
     */
    public File getFile(String name) throws RemoteException {
        return this.files.get(name);
    }
}
