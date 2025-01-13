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
    private ByteBuffer buffer;

    public FragmentDownloader(FileFragment fragment) {
        this.fragment = fragment;
        this.buffer = ByteBuffer.allocate(2048);
    }

    @Override
    public void run() {
        try {
            // Getting the file owner
            ClientInfo owner = fragment.getOwner();

            // Connecting to the owner socket
            System.out.println("Opening new socket for host : " + owner.getAddress() + ":" + owner.getPort());
            Socket s = new Socket(owner.getAddress(), owner.getPort());

            // Request a fragment of the file to the owner
            PrintStream out = new PrintStream(s.getOutputStream());
            out.println("getfile:[" + fragment.getSize() + ";" + fragment.getOffset() + "]" + fragment.getName());

            // Reading the response
            BufferedInputStream in = new BufferedInputStream(s.getInputStream());
            buffer = ByteBuffer.wrap(in.readAllBytes());

            // Create the file associated to the response
            Path workspaceRoot = Paths.get(System.getProperty("user.dir"));
            Path downloadDir = Paths.get(workspaceRoot.toString(), "/downloads");
            Path filePath = Paths.get(downloadDir.toString(), fragment.getName());

            // Write the content of the buffer into the file at the good offset
            FileChannel file = FileChannel.open(filePath, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            file.write(buffer, fragment.getOffset());

            // Close the socket
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
