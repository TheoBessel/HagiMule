package Device;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientInfoImpl extends UnicastRemoteObject implements ClientInfo {
    private String address;
    private Integer port;

    public ClientInfoImpl(String address, Integer port) throws RemoteException {
        this.address = address;
        this.port = port;
    }

    @Override
    public String getAddress() throws RemoteException {
        return this.address;
    }

    @Override
    public Integer getPort() throws RemoteException {
        return this.port;
    }

    @Override
    public boolean equals(ClientInfo client) throws RemoteException {
        return this.address.equals(client.getAddress()) && this.port == client.getPort();
    }
}