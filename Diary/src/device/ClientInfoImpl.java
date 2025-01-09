package device;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class ClientInfoImpl implements ClientInfo {
    private InetAddress address;
    private Integer port;

    public ClientInfoImpl(InetAddress address, Integer port) {
        this.address = address;
        this.port = port;
    }

    public ClientInfoImpl(InetSocketAddress address) {
        this.address = address.getAddress();
        this.port = address.getPort();
    }

    @Override
    public InetAddress getAddress() {
        return this.address;
    }

    @Override
    public Integer getPort() {
        return this.port;
    }
}