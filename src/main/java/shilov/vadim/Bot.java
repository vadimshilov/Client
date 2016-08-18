package shilov.vadim;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Created by vadim on 17.08.16.
 */

public class Bot extends Thread{

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Random rand;
    private volatile boolean connected;


    public Bot(String address,int port) throws IOException {
        socket = new Socket(address, port);
        connected=true;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        rand=new Random();
    }

    private void disconnect(){
        connected=false;
    }

    private String createString(){
        int len=rand.nextInt(50)+50;
        char[] c=new char[len];
        for(int i=0;i<len;i++)
            c[i]=(char)('a'+rand.nextInt(26));
        return new String(c);
    }

    public void run(){
        new Sender().start();
        String message;
        do{
            message=null;
            try {
                message=in.readLine();
            } catch (IOException e) {
            }
        }while(socket.isConnected()&&message!=null);
        disconnect();

    }

    class Sender extends Thread{

        public void run(){
            do{
                out.println(createString());
                int timeout=rand.nextInt(20)+40;
                try {
                    sleep(timeout*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while(socket.isConnected()&&connected);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ConfigReader configReader=ConfigReader.getInstance();
        int port=configReader.getPort();
        String address=configReader.getAddress();
        for(int i=0;i<2000;i++){
            new Bot(address,port).start();
        }
    }

}
