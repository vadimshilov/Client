package shilov.vadim;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by vadim on 16.08.16.
 */
public class Client {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner scanner;
    private volatile boolean connected;

    public Client(String address,int port) throws IOException {
        socket = new Socket(address, port);
        connected=true;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        scanner=new Scanner(System.in);
    }

    private void disconnect(){
        connected=false;
    }

    public void start(){
        new Sender().start();
        String message;
        do{
            message=null;
            try {
                message=in.readLine();
            } catch (IOException e) {
            }
            if(message!=null)
                System.out.println(message);
        }while(socket.isConnected()&&message!=null);
        disconnect();
    }

    class Sender extends Thread{

        public void run(){
            do{
                String message=scanner.nextLine();
                out.println(message);
            }while(socket.isConnected()&&connected);
        }

    }
}
