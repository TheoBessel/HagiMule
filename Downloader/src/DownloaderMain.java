import Downloader.Downloader;
import Downloader.DownloaderImpl;

public class DownloaderMain {
    public static void main(String[] args) {
        Downloader downloader = new DownloaderImpl(args[0]);
        downloader.run();
    }
}
