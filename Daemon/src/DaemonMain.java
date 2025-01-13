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
                while (true) {

                    Thread t2 = new Thread(new UploadService(s.accept()));

                    t2.start();
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
