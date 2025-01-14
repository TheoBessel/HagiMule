package Downloader.Fragment;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.rmi.RemoteException;

import Device.ClientInfo;
import File.Fragment.FileFragment;

public class FragmentDownloader implements Runnable {
    private FileFragment fragment;
    private Socket s;

    public FragmentDownloader(FileFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void run() {
        try {
            // Getting the file owner
            ClientInfo owner = fragment.getOwner();

            // Connecting to the owner socket
            System.out.println("Opening new socket for host : " + owner.getAddress() + ":" + owner.getPort());
            s = new Socket(owner.getAddress(), owner.getPort());

            // Request a fragment of the file to the owner
            PrintStream out = new PrintStream(s.getOutputStream());
            out.println("getfile:[" + fragment.getOffset() + ";" + fragment.getSize() + "]" + fragment.getName());

            // Reading the response
            BufferedInputStream in = new BufferedInputStream(s.getInputStream());

            // Prepare to write to the file
            Path workspaceRoot = Paths.get(System.getProperty("user.dir"));
            Path downloadDir = Paths.get(workspaceRoot.toString(), "/downloads");
            Path filePath = Paths.get(downloadDir.toString(), fragment.getName());

            // Ensure the download directory exists
            if (!downloadDir.toFile().exists()) {
                downloadDir.toFile().mkdirs();
            }

            FileChannel file = FileChannel.open(filePath, StandardOpenOption.CREATE, StandardOpenOption.WRITE);

            // Buffer size for each chunk
            int bufferSize = 4096; // Example: 4 KB
            ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
            long remainingSize = fragment.getSize();
            long fileOffset = fragment.getOffset();

            while (remainingSize > 0) {
                // Clear the buffer for a new read operation
                buffer.clear();

                // Read data into the buffer
                int bytesRead = in.read(buffer.array(), 0, (int) Math.min(buffer.capacity(), remainingSize));
                if (bytesRead == -1) {
                    throw new IOException("Unexpected end of stream while downloading fragment.");
                }

                // Prepare the buffer for writing by flipping it
                buffer.limit(bytesRead).position(0);

                // Write the buffer's content to the file at the correct position
                file.write(buffer, fileOffset);

                // Update remaining size and file offset
                remainingSize -= bytesRead;
                fileOffset += bytesRead;
            }

            System.out.println("Download complete for fragment: " + fragment.getName());

            // Close resources
            file.close();
            in.close();
            s.close();
        } catch (RemoteException e) {
            System.err.println("Error while running FragmentDownloader component.");
            e.printStackTrace();
        } catch (UnknownHostException e) {
            System.err.println("Error while trying to find the owner of a fragment.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error while trying to communicate with a daemon.");
            e.printStackTrace();
        }
    }
}
