package diary;
import java.net.InetSocketAddress;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedHashMap;
import java.util.Map;

import device.ClientInfoImpl;
import file.FileInfo;

public class DiaryImpl extends UnicastRemoteObject implements Diary {
    /**
     * @param files The files in the Diary component.
     */
    private Map<String, FileInfo> files;

    /**
     * Constructor for Diary component.
     */
    public DiaryImpl() throws RemoteException {
        this.files = new LinkedHashMap<>();
    }

    /**
     * Get the file with the given name.
     */
    public FileInfo getFile(String name) throws RemoteException {
        return this.files.get(name);
    }

    /**
     * Add a new file to the diary.
     */
    public void addFile(FileInfo file) throws RemoteException {
        try {
            FileInfo updatedFile = getFile(file.getName());
            updatedFile = updatedFile == null ? file : updatedFile;
            updatedFile.addOwner(new ClientInfoImpl(new InetSocketAddress(getClientHost(), 5000)));

            this.files.put(
                updatedFile.getName(),
                updatedFile
            );

            System.out.println("Adding file `" + updatedFile.getName() + "` with size " + updatedFile.getSize() + " to the diary !");
        } catch (Exception e) {
            System.err.println("Error while adding file to the Diary : unknown host.");
            e.printStackTrace();
        }
    }

    /**
     * Remove a file from the diary.
     */
    public void removeFile(String name) throws RemoteException {
        try {
            FileInfo updatedFile = getFile(name);
            if (updatedFile != null) {
                updatedFile.removeOwner(new ClientInfoImpl(new InetSocketAddress(getClientHost(), 5000)));
                this.files.put(
                    updatedFile.getName(),
                    updatedFile
                );

                System.out.println("Removing file `" + updatedFile.getName() + "` with size " + updatedFile.getSize() + " to the diary !");
            }
        } catch (Exception e) {
            System.err.println("Error while removing file from the Diary : unknown host.");
            e.printStackTrace();
        }
    }
}
