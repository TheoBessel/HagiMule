package Diary;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import Device.ClientInfo;
import Device.ClientInfoImpl;
import File.FileInfo;
import File.FileInfoImpl;

public class DiaryImpl extends UnicastRemoteObject implements Diary {
    /**
     * @param files The files in the Diary component.
     */
    private Map<String, FileInfo> files;

    /**
     * @param clientLastAliveTime The last time each client was seen alive by the diary
     */
    private Map<String, LocalTime> clientLastAliveTime;

    /**
     * Constructor for Diary component.
     */
    public DiaryImpl() throws RemoteException {
        this.files = new LinkedHashMap<>();
        this.clientLastAliveTime = new LinkedHashMap<>();
    }

    /**
     * Get the file with the given name.
     */
    public FileInfo getFile(String name) throws RemoteException {
        return this.files.get(name);
    }

    /**
     * Get the map of clients last alive times
     * @return
     */
    public Map<String, LocalTime> getClientsLastAliveTimes() throws RemoteException {
        return this.clientLastAliveTime;
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

            System.out.println("File `" + updatedFile.getName() + "` with size " + updatedFile.getSize() + " successfully added to the diary, with host `" + getClientHost() + ":" + port + "`.");
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

                System.out.println("File `" + updatedFile.getName() + "` with size " + updatedFile.getSize() + " successfully removed from the diary !");
            }
        } catch (Exception e) {
            System.err.println("Error while removing file from the Diary : unknown host.");
            e.printStackTrace();
        }
    }

    public void removeClient(ClientInfo owner) {
        files.forEach((key, value) -> {
            System.out.println(key);
            try {
                value.removeOwner(owner);
                List<ClientInfo> l = value.getOwners();
                for (ClientInfo c : l) {
                    System.out.println(c.getAddress());
                }
                System.out.println(l.size());
                clientLastAliveTime.remove(owner.getAddress() + ":" + owner.getPort().toString());
            } catch (RemoteException e) {
                System.err.println("Error couldn't access FileInfo");
                e.printStackTrace();
            }
        clientLastAliveTime.forEach((keyy, val) -> System.out.println(key));
        System.out.println("finished");
        files.forEach((keyy, val) -> {System.out.println(key);
            try {
                val.getOwners().forEach((lll) -> {
                    try {
                        System.out.println(lll.getAddress());
                    } catch (RemoteException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                });
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        System.out.println("finiiiished");
        });
    }

    /**
     * Sends heartbeat response
     */
    public Integer heartbeat(Integer port) throws RemoteException {
        try {
            clientLastAliveTime.put(getClientHost() + ":" + port.toString(), LocalTime.now());
        }
        catch (ServerNotActiveException e) {
            System.err.println("Error no remote method invocation is being processed.");
            e.printStackTrace();
        }
        return Integer.valueOf(1);
    }
}
