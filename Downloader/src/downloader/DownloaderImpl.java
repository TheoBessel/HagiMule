package downloader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.rmi.RemoteException;

import device.ClientInfo;
import device.ClientInfoImpl;
import file.fragment.FileFragment;

public class DownloaderImpl implements Downloader {

    private FileFragment fragment;
    private Socket socket;

    public DownloaderImpl(FileFragment fragment) {
        this.fragment = fragment;
        try {
            Integer port = Integer.parseInt(System.getenv("PORT"));
            this.socket = new ServerSocket(port).accept(); // ? leak ?
        } catch (IOException e) {
            System.err.println("Error counld not find env file");
        }
    }

    @Override
    public void run() {
        ClientInfo owner = fragment.getOwner();
        try {
            String adress = owner.getAddress();
            Integer port = owner.getPort();

            try {
                // Sending the request
                Socket clientSocket = new Socket(adress, port);
                // OutputStream cliOut = clientSocket.getOutputStream();
                String requestText = "getfile:" + fragment.getName();
                // byte[] request = requestText.getBytes(); // ?
                // cliOut.write(request, 0, request.length); // ?

                PrintStream out = new PrintStream(socket.getOutputStream());
                out.print(requestText);

                // handling response
                ByteBuffer response = ByteBuffer.allocate(1024); // ? arbitrary max dw
                BufferedInputStream cliIn = new BufferedInputStream(clientSocket.getInputStream());
                // Then read response
                while (true) {
                    int b = cliIn.read();
                    if (b == -1) {
                        break;
                    }
                    response.put((byte) b);
                }

                // create file
                Path workspaceRoot = Paths.get(System.getProperty("user.dir"));
                Path downloadDir = Paths.get(workspaceRoot.toString(), "/downloads");
                Path filePath = Paths.get(downloadDir.toString(), fragment.getName());

                FileChannel testFile = FileChannel.open(filePath, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
                testFile.write(response, fragment.getOffset());

                clientSocket.close();
            } catch (UnknownHostException e) {
                System.err.println("Error when connecting to unknown host");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

}
