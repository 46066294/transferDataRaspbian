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
        System.out.println("***Transfer Data Raspbian(Client Side) v1.0 Author: Marc Cano***");

        Scanner input = new Scanner(System.in);

        System.out.println("\n-Enter IP:");
        input.reset();
        String ipClient = input.nextLine();
        System.out.println("\n-Enter PORT:");
        input.reset();
        Integer portClient = input.nextInt();

        clientTransferData(input, ipClient, portClient);

        optionInput(input, ipClient, portClient);

        input.close();

    }//main



    protected static void clientTransferData(Scanner input, String ipClient, Integer portClient) throws IOException {

        //create client socket and connexion
        Socket socket = new Socket();
        InetSocketAddress address = new InetSocketAddress(ipClient, portClient);
        socket.connect(address);

        //info
        System.out.println("\n...connection successful");
        System.out.println("-------CLIENT DATA-------");
        System.out.println("IP    : " + ipClient);
        System.out.println("Port  : " + portClient);
        System.out.println("-------------------------");

        System.out.println("\nEnter path wanted:");
        input.reset();
        String wantedPath = input.nextLine();

        //send request path for wanted file
        OutputStream outputStream = socket.getOutputStream();
        String sendModifiedPath = wantedPath + "!";
        outputStream.write(sendModifiedPath.getBytes());
        System.out.println("\nSending request...\n");


        //Get response
        InputStream inputStream = socket.getInputStream();
        int totalBytes = 0;
        int contBytes = 0;
        if(inputStream.read() == 4 ){
            System.out.println("--RESPONSE\n\t...FILE NOT FOUND");
        }
        else{//manage modified stream
            Vector<Byte> vector = new Vector<Byte>();
            while((totalBytes = (byte) inputStream.read()) != 4){//EOT (end of transmission) == 4
                vector.add((byte) totalBytes);
                contBytes++;
            }

            //get original bytes from wanted file
            byte[] fileFromServer = new byte[contBytes];
            for(int i = 0; i < contBytes; i++)
                fileFromServer[i] = vector.get(i);


            System.out.println("--Buffer size: " +
                    fileFromServer.length + " bytes");


            System.out.println("\nLoading response...");
            // write stream into client file
            input.reset();
            System.out.println("\nEnter path to save file:");
            String downloadPath = input.nextLine();
            outputStream.flush();
            outputStream =
                    //new FileOutputStream(new File("/home/pi/curwenProj/dataTransfer/data"));
                    new FileOutputStream(new File(downloadPath));
            outputStream.write(fileFromServer, 0, contBytes);

            System.out.println("Done!");
        }

        socket.close();
        inputStream.close();
        outputStream.close();
    }//clientTransferData()



    //repeat another file transfer
    protected static void optionInput(Scanner scan, String ip, Integer port) throws IOException {
        boolean continueExe = true;
        while(continueExe){

            System.out.println("\nAnother file transfer? (y/n)");
            scan.reset();
            String continueTransfer = scan.nextLine();
            continueTransfer.toLowerCase();
            if(continueTransfer.equals("y")){
                clientTransferData(scan, ip, port);

            }
            else if(continueTransfer.equals("n")){
                continueExe = false;
                System.out.println("BYE!");
            }
            else{
                System.out.println("...incorrect input");
            }
        }

    }//optionInput()

}//class
