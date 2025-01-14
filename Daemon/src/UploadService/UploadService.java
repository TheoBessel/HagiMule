package UploadService;

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

public class UploadService implements Runnable {
    private Socket s;

    public UploadService(Socket s) {
        this.s = s;
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
                Path workspaceRoot = Paths.get(System.getProperty("user.dir"));
                Long fragmentOffset = Long.parseLong(request.substring(9, request.indexOf(";")));
                Long fragmentSize = Long.parseLong(request.substring(request.indexOf(";") + 1, request.indexOf("]")));
                String fragmentName = request.substring(request.indexOf("]") + 1);
                Path filePath = Paths.get(workspaceRoot.toString(), "/downloads", fragmentName);
                System.out.println("Requested file is : " + fragmentName + " with offset " + fragmentOffset + " and size " + fragmentSize);

                // Open the file for reading
                FileChannel file = FileChannel.open(filePath, StandardOpenOption.READ);

                // Set the position of the FileChannel to the offset
                file.position(fragmentOffset);

                // Define a smaller buffer size for each read operation
                int bufferSize = 4096; // Example: 4 KB
                ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

                // Read the file and write to the output stream
                long remainingSize = fragmentSize;
                while (remainingSize > 0) {
                    buffer.clear(); // Reset the buffer for the next read
                    int bytesToRead = (int) Math.min(bufferSize, remainingSize);
                    buffer.limit(bytesToRead); // Limit the buffer to the remaining size
                    int bytesRead = file.read(buffer);

                    if (bytesRead == -1) {
                        break; // End of file reached
                    }

                    buffer.flip(); // Prepare the buffer for writing
                    out.write(buffer.array(), 0, bytesRead);
                    remainingSize -= bytesRead;
                }

                file.close();
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