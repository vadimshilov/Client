package shilov.vadim;

import java.io.IOException;

/**
 *
 *
 */
public class App {
    public static void main( String[] args ) {
        ConfigReader configReader=ConfigReader.getInstance();
        int port=configReader.getPort();
        String address=configReader.getAddress();
        if(address==null){
            System.out.println("Не удается прочитать адрес сервера из файла настроек");
        }
        else{
            try {
                Client client=new Client(address,port);
                client.start();
                System.out.println("Соединение с сервером разорвано");
            } catch (IOException e) {
                System.out.println("Не удается установить соединение с сервером");
            }
        }
    }
}
