package device;

import java.net.InetAddress;

/**
 * This interface represents the clients that connect to the diary and declare
 * files available for download.
 */
public interface ClientInfo {
    /**
     * This function returns the IP address of the current client.
     * @return The IP address of the client
     */
    public InetAddress getAddress();

    /**
     * This function returns the port of the current client.
     * @return The port of the client
     */
    public Integer getPort();
}