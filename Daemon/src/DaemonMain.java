import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.RemoteException;

import Daemon.Daemon;
import Daemon.DaemonImpl;
import DirectoryWatcher.DirectoryWatcher;
import UploadService.UploadService;

public class DaemonMain {
    public static void main(String[] args) {

        try {
            Daemon d = new DaemonImpl();                        // Create Daemon instance
            Thread t = new Thread(new DirectoryWatcher(d));     // Launch watcher with the created Daemon

            t.start();

            Integer port = Integer.parseInt(System.getenv("TCP_PORT"));
            try (ServerSocket s = new ServerSocket(port)) {
                boolean disconnected = false;
                while (true) {

                    // Heartbeats
                    try {
                        Integer heartbeatResult = d.heartbeat();
                        if (heartbeatResult == 0) {
                            disconnected = true;
                        }
                        else if (heartbeatResult == 1 && disconnected) {
                            disconnected = false;
                            d.notifyAllFiles();
                        }
                    }
                    catch (NullPointerException e) {
                        String hostname = System.getenv("RMI_IP");
                        String portRmi = System.getenv("RMI_PORT");
                        System.err.println("Error while connecting to diary : " + hostname + ":" + portRmi + " not found");
                        break;
                    }
                    // new Thread(new UploadService(s.accept())).start();
                }
            }

        } catch (RemoteException e) {
            System.err.println("Error while starting Daemon component.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error while sending file to a downloader.");
            e.printStackTrace();
        }
    }
}
