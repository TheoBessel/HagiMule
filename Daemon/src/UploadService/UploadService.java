package UploadService;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class UploadService implements Runnable {
    private Socket s;
    private ByteBuffer buffer;

    public UploadService(Socket s) {
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

            // Request form is "getfile:[<offset;<size>]<filename>"

            // Check if request form is correct
			if (request.startsWith("getfile:")) {
                // Request form is "getfile:<filename>"
                Path workspaceRoot = Paths.get(System.getProperty("user.dir"));
                Long fragmentOffset = Long.parseLong(request.substring(9, request.indexOf(";")));
                Long fragmentSize = Long.parseLong(request.substring(request.indexOf(";") + 1, request.indexOf("]")));
                String fragmentName = request.substring(request.indexOf("]") + 1);
                Path filePath = Paths.get(workspaceRoot.toString(), "/downloads", fragmentName);
                System.out.println("Requested file is : " + fragmentName + " with offset " + fragmentOffset + " and size " + fragmentSize);

                // Read the file into the buffer
                FileChannel file = FileChannel.open(filePath, StandardOpenOption.READ);
                file.read(buffer, fragmentOffset);
                System.out.println("Buffer is : `" + StandardCharsets.UTF_8.decode(buffer).toString() + "`");

                // Write into the socket output
                //out.write(buffer.array());
                out.write(buffer.array(), fragmentOffset.intValue(), fragmentSize.intValue());
            }

            in.close();
            out.close();
            s.close();
        } catch (IOException e) {
            System.err.println("Error while trying to communicate with a downloader.");
            e.printStackTrace();
        }
    }

}
