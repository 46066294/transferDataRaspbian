import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Mat on 30/05/2016.
 */
public class ServerData {
    public static void main(String[] args) throws IOException {

        Integer[] counterCall = new Integer[1];
        //String ipServer = "0.0.0.0";
        String ipServer = "192.168.1.102";
        Integer portServer = 5555;

        System.out.println("...creating server");

        ServerSocket serverSocket =  new ServerSocket();
        InetSocketAddress address = new InetSocketAddress(ipServer, portServer);
        serverSocket.bind(address);

        Socket socketListener = serverSocket.accept();
        System.out.println("...server socket ::accept::\n");

        System.out.println("----SERVER DATA----");
        System.out.println("IP    : " + ipServer);
        System.out.println("Port  : " + portServer);
        //System.out.println("\tChanel: " + serverSocket.getChannel().toString() + "\n");
        System.out.println("-------------------");

        DataTransfer dataTransfer = new DataTransfer(socketListener, counterCall);
        dataTransfer.start();

        serverSocket.close();

    }
}
