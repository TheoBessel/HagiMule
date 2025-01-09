package diary;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedHashMap;
import java.util.Map;

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
        this.files.put(
            file.getName(),
            file
        );

        System.out.println("Adding file `" + file.getName() + "` with size " + file.getSize() + " to the diary !");
    }

    /**
     * Remove a file from the diary.
     */
    public void removeFile(FileInfo file) throws RemoteException {
    }
}
