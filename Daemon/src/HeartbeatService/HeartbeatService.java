package HeartbeatService;

import java.rmi.RemoteException;

import Daemon.Daemon;

public class HeartbeatService implements Runnable {

    Daemon daemon;

    public HeartbeatService(Daemon d){
        this.daemon = d;
    }

    @Override
    public void run() {
        boolean disconnected = false;
        while (true) {
            // Heartbeats
            try {
                Integer heartbeatResult = daemon.heartbeat();
                if (heartbeatResult == 0) {
                    disconnected = true;
                }
                else if (heartbeatResult == 1 && disconnected) {
                    disconnected = false;
                    daemon.notifyAllFiles();
                }
            } catch (NullPointerException e) {
                String hostname = System.getenv("RMI_IP");
                String portRmi = System.getenv("RMI_PORT");
                System.err.println("Error while connecting to diary : " + hostname + ":" + portRmi + " not found");
                break;
            } catch (RemoteException e) {
                System.err.println("Error while starting Daemon component.");
                e.printStackTrace();
            }
        }
    }
}
