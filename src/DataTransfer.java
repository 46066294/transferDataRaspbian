import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;

/**
 * Created by Mat on 30/05/2016.
 */
public class DataTransfer extends Thread{

    Socket socketDataTransfer;                    //socket
    String filePath;                              //client request path
    Integer counterCall;                          //number of calls to server
    Integer notFound = 4;                         //EOT (end of transmision) index
    byte responseNotFound = notFound.byteValue(); //Not found message buffer

    public DataTransfer(Socket socket, Integer counterCall){
        this.socketDataTransfer = socket;
        this.counterCall = counterCall;
    }


    //synchronized Thread
    @Override
    public synchronized void start() {
        super.start();

        System.out.println("\n--------------------new call--------------------");
        System.out.println("\n...START DATA TRANSFER");
        System.out.println("Call: " + counterCall);
        System.out.println("IP  : " + socketDataTransfer.getInetAddress().toString() +
                            "\nPORT: " + socketDataTransfer.getLocalPort());
        System.out.println("\nwaiting request...");

        try {
            //Streams to transfer data
            InputStream inputStream = socketDataTransfer.getInputStream();
            OutputStream outputStream = socketDataTransfer.getOutputStream();

            //preload buffer for reading modified(eof = "!") client request
            int totalBytes;
            int contBytes = 0;
            Vector<Byte> vector = new Vector<Byte>();
            while((totalBytes = inputStream.read()) != 33){//33 == "!" --> Client adds a "!" char at the end of byte stream
                vector.add((byte) totalBytes);             //               We need this final char to end loop
                contBytes++;
            }

            //load original client request buffer:
            byte[] filePathFromClient = new byte[contBytes];
            for(int i = 0; i < contBytes; i++){
                filePathFromClient[i] = vector.get(i);
            }

            //Verify request
            filePath = new String(filePathFromClient);
            System.out.println("--REQUEST:\n\t" + filePath);

            //Get the file with bytes stream into server system
            byte[] buffer = fileBind(filePath);
            //response
            if(buffer != null) {
                System.out.println("Sending response...");
                outputStream.write(buffer);
                System.out.println("...ok!");
            }
            else {
                System.out.println("\n--RESPONSE\n...FILE DOESN'T EXIST\n");
                outputStream.write(responseNotFound);
            }

            inputStream.close();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socketDataTransfer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }//start Thread


    //Get the file with bytes stream into server system
    private byte[] fileBind(String wantedPath) throws IOException {

        File file = new File(wantedPath);
        if(file.exists()){
            System.out.println("\n--RESPONSE\n...file found!");
            Path path = Paths.get(wantedPath);
            byte[] data = Files.readAllBytes(path);
            int bufferSize = data.length + 1;
            byte[] dataSend = new byte[bufferSize];
            for(int i = 0; i < dataSend.length; i++){
                if(i < data.length)
                    dataSend[i] = data[i];

                else
                    dataSend[i] = 4;//EOT (end of transmission) We put this integer
                                    //                            at the end of buffer to exit loop
            }

            return dataSend;
        }
        else
            return null;

    }//fileBind()

}//class
