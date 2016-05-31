import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

/**
 * Created by Mat on 30/05/2016.
 */
public class ClientData {
    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);

        String ipClient = "192.168.1.102";
        Integer portClient = 5555;

        Socket socket = new Socket();
        InetSocketAddress address = new InetSocketAddress(ipClient, portClient);
        socket.connect(address);

        //info
        System.out.println("...connection successful");
        System.out.println("----CLIENT DATA----");
        System.out.println("IP    : " + ipClient);
        System.out.println("Port  : " + portClient);
        //System.out.println("\tChanel: " + socket.getChannel().toString() + "\n");
        System.out.println("-------------------");

        //send request path for file wanted
        input.reset();
        System.out.println("\nEnter path wanted:");
        String wantedPath = input.nextLine();//C:\adesconocido.txt
        OutputStream outputStream = socket.getOutputStream();
        String sendModifiedPath = wantedPath + "!";
        outputStream.write(sendModifiedPath.getBytes());
        System.out.println(sendModifiedPath);
        System.out.println("\nSending request...\n");


        // write the inputStream to a FileOutputStream
        input.reset();
        System.out.println("\nEnter path to save file:");
        //String downloadPath = input.nextLine();
        outputStream =
                //new FileOutputStream(new File("/home/pi/curwenProj/dataTransfer/data"));
                new FileOutputStream(new File("C:\\Users\\Mat\\Downloads\\abc.txt"));

        //response
        InputStream inputStream = socket.getInputStream();
        int totalBytes = 0;
        int contBytes = 0;
        Vector<Byte> vector = new Vector<Byte>();
        //int totalBytes = inputStream.read();
        System.out.println(totalBytes);
        while((totalBytes = (byte) inputStream.read()) != 33){//33 == "!"
            System.out.println(totalBytes);
            vector.add((byte) totalBytes);
            contBytes++;
        }

        System.out.println("contBytes: " + contBytes);
        byte[] fileFromServer = new byte[contBytes];
        for(int i = 0; i < contBytes; i++){
            fileFromServer[i] = vector.get(i);
            System.out.println(fileFromServer[i]);
        }

        System.out.println("buffer Len: " + fileFromServer.length);

        String bytesToString = new String(fileFromServer);
        System.out.println(bytesToString);

        System.out.println("\nLoading request...");
        outputStream.write(fileFromServer, 0, contBytes);

        System.out.println("Int max_val " + Integer.MAX_VALUE);
        System.out.println("Done!");

        input.close();
        socket.close();
        inputStream.close();
        outputStream.close();
    }
}
