package device;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface represents the clients that connect to the diary and declare
 * files available for download.
 */
public interface ClientInfo extends Remote {
    /**
     * This function returns the IP address of the current client.
     * @return The IP address of the client
     */
    public String getAddress() throws RemoteException;

    /**
     * This function returns the port of the current client.
     * @return The port of the client
     */
    public Integer getPort() throws RemoteException;
}