package FragmentDownloader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FragmentDownloader implements Runnable {

    private Socket socket;

    public FragmentDownloader() {
        try {
            Integer port = Integer.parseInt(System.getenv("PORT"));
            this.socket = new ServerSocket(port).accept(); // ? leak ?
        } catch (IOException e) {
            System.err.println("Error counld not find env file");
        }
    }

    @Override
    public void run() {
        try {
            InputStreamReader in = new InputStreamReader(socket.getInputStream());
            OutputStream out = socket.getOutputStream();
            String request = new LineNumberReader(in).readLine();
			if (request.startsWith("getfile:")) {
                Path workspaceRoot = Paths.get(System.getProperty("user.dir"));
                // filename after "getfile:"
                Path f = Paths.get(workspaceRoot.toString(), "/downloads", request.substring(8));
				if (Files.exists(f) && !Files.isDirectory(f)) {
                    InputStream is = Files.newInputStream(f);
					byte[] data = new byte[is.available()];
					is.read(data);
					is.close();
                    out.write(data);
				} else {
                    System.out.println("file not found"); // TODO : change this
                }
            }


            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
