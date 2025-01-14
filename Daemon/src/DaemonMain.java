import java.io.IOException;
import java.net.ServerSocket;

import Daemon.Daemon;
import Daemon.DaemonImpl;
import DirectoryWatcher.DirectoryWatcher;
import HeartbeatService.HeartbeatService;
import UploadService.UploadService;

public class DaemonMain {
    public static void main(String[] args) {

        try {
            Daemon d = new DaemonImpl();                        // Create Daemon instance
            Thread t = new Thread(new DirectoryWatcher(d));     // Launch watcher with the created Daemon
            Thread heart = new Thread(new HeartbeatService(d));
            t.start();
            heart.start();

            Integer port = Integer.parseInt(System.getenv("TCP_PORT"));
            try (ServerSocket s = new ServerSocket(port)) {
                // boolean disconnected = false;
                while (true) {

                    Thread t2 = new Thread(new UploadService(s.accept()));

                    t2.start();
                }
            }
        } catch (IOException e) {
            System.err.println("Error while sending file to a downloader.");
            e.printStackTrace();
        }
    }
}
