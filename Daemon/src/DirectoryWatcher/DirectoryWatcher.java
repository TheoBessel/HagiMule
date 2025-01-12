package DirectoryWatcher;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.rmi.RemoteException;
import java.util.regex.Pattern;

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
            Path workspaceRoot = Paths.get(System.getProperty("user.dir"));
            // We assume files with the same name have the same content -> watch only for creations and deletions
            WatchKey key = Paths.get(workspaceRoot.toString(), "/downloads")
                                .register(watcher,
                                    StandardWatchEventKinds.ENTRY_DELETE,
                                    StandardWatchEventKinds.ENTRY_CREATE
                                );

            // Watch for file deletion
            while ((key = watcher.take()) != null) {

                // Replace this with Daemon notification
                key.pollEvents().stream().forEach(event -> {
                    try {
                        String fileName = ((Path) event.context()).toString();
                        // case file deletion
                        if (
                            event.kind().equals(StandardWatchEventKinds.ENTRY_DELETE)
                            && !Pattern.matches(".*\\.danload",fileName
                            )) {
                                daemon.notifyFileDeletion(fileName); // Notify Daemon of file deletion
                        }
                        // case file creation
                        else {
                            if (!Pattern.matches(".*\\.danload",fileName )) {
                                try {
                                    Path filePath = Paths.get(workspaceRoot.toString(), "/downloads", fileName);
                                    Integer socket_port = Integer.parseInt(System.getenv("TCP_PORT"));
                                    daemon.notifyFileCreation(fileName, Files.size(filePath), socket_port); // Notify Daemon of file creation
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
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