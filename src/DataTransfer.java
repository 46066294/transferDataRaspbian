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

    Socket socketDataTransfer;
    Integer counterCall;
    String filePath;

    public DataTransfer(Socket socket, Integer[] counterCall){
        this.socketDataTransfer = socket;
        this.counterCall = counterCall[0];
    }


    @Override
    public synchronized void start() {
        super.start();

        System.out.println("\n...START DATA TRANSFER");
        System.out.println("Call: " + counterCall);
        System.out.println("------CALLER------" +
                            "\nIP  : " + socketDataTransfer.getInetAddress().toString() +
                            "\nPORT: " + socketDataTransfer.getLocalPort() +
                            "\n------------------");

        try {
            InputStream inputStream = socketDataTransfer.getInputStream();
            OutputStream outputStream = socketDataTransfer.getOutputStream();

            //preload buffer for reading client request
            int totalBytes;
            int contBytes = 0;
            Vector<Byte> vector = new Vector<Byte>();
            while((totalBytes = inputStream.read()) != 33){//33 == "!"
                System.out.println(totalBytes);
                vector.add((byte) totalBytes);
                contBytes++;
            }

            System.out.println("contBytes: " + contBytes);
            byte[] filePathFromClient = new byte[contBytes];
            for(int i = 0; i < contBytes; i++){
                filePathFromClient[i] = vector.get(i);
                System.out.println(filePathFromClient[i]);
            }

            System.out.println("buffer Len: " + filePathFromClient.length);

            filePath = new String(filePathFromClient);
            System.out.println(filePath);

            outputStream.write(fileBind(filePath));

            Thread.sleep(2000);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public byte[] fileBind(String wantedPath) throws IOException {

        System.out.println(wantedPath);
        File file = new File(wantedPath);//C:\adesconocido.txt
        if(file.exists()){
            System.out.println("exists: " + wantedPath);
            //Path path = Paths.get("C:\\adesconocido.txt");
            Path path = Paths.get(wantedPath);
            byte[] data = Files.readAllBytes(path);
            int bufferSize = data.length + 1;
            //System.out.println("dataLengh: " + data.length);
            byte[] dataSend = new byte[bufferSize];
            for(int i = 0; i < dataSend.length; i++){
                //System.out.println(data[i]);
                if(i < data.length){
                    dataSend[i] = data[i];
                }
                else
                    dataSend[i] = 33;//"!" al final del stream

            }
            return dataSend;
        }
        else{
            System.out.println("NOT exists: " + wantedPath);
            //Path path = Paths.get("RESPONSE FILE NOT EXIST");
            //byte[] dataOut = Files.readAllBytes(path);

            return null;
        }
    }


}
