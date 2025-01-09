package diary;
import java.net.InetSocketAddress;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import device.ClientInfo;
import device.ClientInfoImpl;
import file.FileInfo;

public class DiaryImpl extends UnicastRemoteObject implements Diary {
    /**
     * @param files The files in the Diary component.
     */
    private Map<String, FileInfo> files;

    /**
     * @param files The owners of files in the Diary component.
     */
    private Map<String, List<ClientInfo>> owners;

    /**
     * Constructor for Diary component.
     */
    public DiaryImpl() throws RemoteException {
        this.files = new LinkedHashMap<>();
        this.owners = new LinkedHashMap<>();
    }

    /**
     * Get the file with the given name.
     */
    public FileInfo getFile(String name) throws RemoteException {
        return this.files.get(name);
    }

    public List<ClientInfo> getOwners(String name) throws RemoteException {
        return this.owners.get(name);
    }

    /**
     * Add a new file to the diary.
     */
    public void addFile(FileInfo file) throws RemoteException {
        this.files.put(
            file.getName(),
            file
        );

        try {
            getOwners(file.getName()).add(
                new ClientInfoImpl(new InetSocketAddress(getClientHost(), 5000))
            );

            this.owners.put(
                file.getName(),
                getOwners(file.getName())
            );
        } catch (Exception ex) {
            try {
                System.out.println("Adding file `" + file.getName() + "` with size " + file.getSize() + " to the diary !");
                System.out.println("New owner for this file is : " + getClientHost() + ":" + 5000);

                this.owners.put(
                    file.getName(),
                    Arrays.asList(
                        new ClientInfo[] {
                            new ClientInfoImpl(
                                new InetSocketAddress(
                                    getClientHost(),
                                    5000
                                )
                            )
                        }
                    )
                );
            }
            catch (ServerNotActiveException e) {
                System.err.println("Server not active !");
                e.printStackTrace();
            }
        }
    }

    /**
     * Remove a file from the diary.
     */
    public void removeFile(FileInfo file) throws RemoteException {

    }
}
