import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


/**
 * Created by Mat on 30/05/2016.
 */
public class ServerData {
    public static void main(String[] args) throws IOException {
        System.out.println("***Transfer Data Raspbian(Server Side) v1.0 Author: Marc Cano***");

        Scanner inputServer = new Scanner(System.in);

        //Intro IP & PORT server
        System.out.println("\n-Enter IP:");
        inputServer.reset();
        String ipServer = inputServer.nextLine();
        System.out.println("\n-Enter PORT:");
        inputServer.reset();
        Integer portServer = inputServer.nextInt();
        Integer counterCall = 0;

        //server info
        System.out.println("\n...open server\n");
        System.out.println("-------SERVER DATA-------");
        System.out.println("IP    : " + ipServer);
        System.out.println("Port  : " + portServer);
        System.out.println("-------------------------");
        System.out.println("Listening...");

        //create server socket bind
        ServerSocket serverSocket =  new ServerSocket();
        InetSocketAddress address = new InetSocketAddress(ipServer, portServer);
        serverSocket.bind(address);

        //multiThreading
        boolean serverOpen = true;
        while(serverOpen){
            counterCall++;
            Socket socketListener = serverSocket.accept();
            DataTransfer dataTransfer = new DataTransfer(socketListener, counterCall);
            dataTransfer.start();
        }

        serverSocket.close();
    }
}
