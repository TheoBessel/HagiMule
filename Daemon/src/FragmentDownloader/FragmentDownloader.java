package FragmentDownloader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FragmentDownloader implements Runnable {
    private Socket s;
    private ByteBuffer buffer;

    public FragmentDownloader(Socket s) {
        this.s = s;
        this.buffer = ByteBuffer.allocate(2048);
    }

    @Override
    public void run() {
        try {
            // Define input and output streams
            InputStreamReader in = new InputStreamReader(s.getInputStream());
            OutputStream out = s.getOutputStream();

            // Get the request
            LineNumberReader reader = new LineNumberReader(in);
            String request = reader.readLine();

            // Check if request form is correct
			if (request.startsWith("getfile:")) {
                // Request form is "getfile:<filename>"
                Path workspaceRoot = Paths.get(System.getProperty("user.dir"));
                Path filePath = Paths.get(workspaceRoot.toString(), "/downloads", request.substring(8));
                System.out.println("Requested file is : " + request.substring(8));

                // Read the file into the buffer
                FileChannel file = FileChannel.open(filePath, StandardOpenOption.READ);
                file.read(buffer);

                // Write into the socker output
                out.write(buffer.array());
            }

            in.close();
            out.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
