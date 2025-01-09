package DirectoryWatcher;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.rmi.RemoteException;

import Daemon.Daemon;

public class DirectoryWatcher implements Runnable {
    Daemon daemon;

    public DirectoryWatcher(Daemon daemon) {
        this.daemon = daemon;
    }

    @Override
    public void run() {
        try {
            // Watcher creation
            WatchService watcher = FileSystems.getDefault().newWatchService();
            // Watchkey creation (registration of watchable object with watchservice)
            Path workspaceRoot = Paths.get(System.getProperty("user.dir")).getParent();
            WatchKey key = Paths.get(workspaceRoot.toString(), "/downloads")
                                .register(watcher,
                                    StandardWatchEventKinds.ENTRY_DELETE,
                                    StandardWatchEventKinds.ENTRY_CREATE,
                                    StandardWatchEventKinds.ENTRY_MODIFY
                                );

            // Watch for file deletion
            while ((key = watcher.take()) != null) {

                // Replace this with Daemon notification
                key.pollEvents().stream().forEach(event -> {
                    try {
                        daemon.notifyDeletion(((Path) event.context())); // Notify Daemon of file deletion
                    } catch (RemoteException e) {
                        System.err.println("Error while notifying Daemon");
                        e.printStackTrace();
                    }
                });
                key.reset();
            }
        } catch (Exception e) {
            System.err.println("Error while running DirectoryWatcher component.");
            e.printStackTrace();
        }
    }
}