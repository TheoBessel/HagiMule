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

            while (true) {
                // Daemon doing Daemon things
                Thread downloader = new Thread(new FragmentDownloader());
                downloader.start();
            }

        } catch (RemoteException e) {
            System.err.println("Error while starting Daemon component.");
            e.printStackTrace();
        }
    }
}
