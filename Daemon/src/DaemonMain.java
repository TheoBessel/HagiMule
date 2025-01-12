import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.RemoteException;

import Daemon.Daemon;
import Daemon.DaemonImpl;
import DirectoryWatcher.DirectoryWatcher;
import FragmentDownloader.FragmentDownloader;

public class DaemonMain {
    public static void main(String[] args) {

        try {
            Daemon d = new DaemonImpl();                        // Create Daemon instance
            Thread t = new Thread(new DirectoryWatcher(d));     // Launch watcher with the created Daemon

            t.start();

            Integer port = Integer.parseInt(System.getenv("TCP_PORT"));
            ServerSocket s = new ServerSocket(port);
            while (true) {
                new Thread(new FragmentDownloader(s.accept())).start();
            }

        } catch (RemoteException e) {
            System.err.println("Error while starting Daemon component.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
