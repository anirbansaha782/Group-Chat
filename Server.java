/**
 * Server
 */
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
public class Server {

    private ServerSocket serverSocket;
    static String password;

    public Server(ServerSocket serverSocket){
        // System.out.println("Create a token for the chatroom");
        // Scanner sc=new Scanner(System.in);
        // password=sc.nextLine();
        this.serverSocket=serverSocket;
    }

    public void startServer() {
        try{
            while(!serverSocket.isClosed()){
            //     if(true){
            //         System.out.println("Token does not match");
            //     continue;
            // }
                Socket socket=serverSocket.accept();
                System.out.println("A New client has connected");
                ClientHandler clientHandler=new ClientHandler(socket);
                //System.out.println(clientHandler.clientHandlers);
                Thread thread=new Thread(clientHandler);
                thread.start();
            }
        } catch(IOException e){
             System.out.println(1);
            // e.printStackTrace();
            System.out.println(e+"JJJ");
            closeServerSocket();
        }
    }

    public void closeServerSocket() {
        try {
            if(serverSocket!=null){
                serverSocket.close();
            }
        } catch (Exception e) {
            //TODO: handle exception
            System.out.println(2);
            e.printStackTrace();
        }
    }

    public static boolean checkToken(String s){
        System.out.println(s+" "+password);
        if(s.equals(password))
        return true;
        else
        return true;
    }

    public static void main(String[] args) {
        //String pass=sc.nextLine();
        ServerSocket serverSocket;
        Scanner sc=new Scanner(System.in);
        try {
            //password=pass;
            serverSocket = new ServerSocket(1234);
            Server server=new Server(serverSocket);
            // System.out.println("Create a token for the chatroom");
            // password=sc.nextLine();
            // System.out.println(password);
            server.startServer();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println(3);
            e.printStackTrace();
        }
    }
}