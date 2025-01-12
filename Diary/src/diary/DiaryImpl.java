package diary;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedHashMap;
import java.util.Map;

import device.ClientInfoImpl;
import file.FileInfo;
import file.FileInfoImpl;

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
    public void addFile(FileInfo file, Integer port) throws RemoteException {
        FileInfo updatedFile = this.files.get(file.getName());
        if (updatedFile == null) {
            updatedFile = new FileInfoImpl(file.getName(), file.getSize());
        }

        try {
            updatedFile.addOwner(new ClientInfoImpl(getClientHost(), port));

            System.out.println("Adding file `" + updatedFile.getName() + "` with size " + updatedFile.getSize() + " to the diary, with host `" + getClientHost() + ":" + port + "`.");
        } catch (Exception e) {
            System.err.println("Error while adding file from the Diary : unknown host.");
            e.printStackTrace();
        }

        this.files.put(
            updatedFile.getName(),
            updatedFile
        );
    }

    /**
     * Remove a file from the diary.
     */
    public void removeFile(String name) throws RemoteException {
        try {
            FileInfo updatedFile = this.files.get(name);
            if (updatedFile != null) {
                updatedFile.removeOwner(new ClientInfoImpl(getClientHost(), 5000));
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
