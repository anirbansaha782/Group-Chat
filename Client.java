import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.io.*;
import java.nio.*;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    private char roomNumber;

    public Client(Socket socket,String username){
        try {
            this.socket=socket;
            this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             this.username=username;
            System.out.println("Enter the Room Number");
            Scanner sc=new Scanner(System.in);
            char x=sc.next().charAt(0);
            roomNumber=x;
            //this.username=username+roomNumber;
        } catch (IOException e) {
            //TODO: handle exception
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessages(){
        try {
            bufferedWriter.write(username+roomNumber);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            Scanner sc=new Scanner(System.in);
            while(socket.isConnected()){
                //System.out.println("HH");
                // String msgFromGroupChat=bufferedReader.readLine();
                // System.out.println(msgFromGroupChat);
                //String messageToSend=sc.nextLine();
                String messageToSend;
                if(sc.hasNextLine()){
                messageToSend=sc.nextLine();
                // System.out.println("JJ");
                // System.out.println("ReadLine");
                bufferedWriter.write(username+": "+messageToSend+roomNumber);
                bufferedWriter.newLine();
                bufferedWriter.flush();
                }
                //System.out.println("ReadLine");
            }
        } catch (IOException e) {
            //TODO: handle exception
            System.out.println(e);
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenForMessage(){
        //System.out.println("listenForMessage");
        new Thread(new Runnable() {
            @Override
            public void run(){
                String msgFromGroupChat;
                while(socket.isConnected()){
                    try{
                        msgFromGroupChat=bufferedReader.readLine();
                        //if((msgFromGroupChat.charAt(msgFromGroupChat.length()-1)+"")==roomNumber)
                        char last=msgFromGroupChat.charAt(msgFromGroupChat.length()-1);
                        if(!Character.isDigit(last))
                        System.out.println(msgFromGroupChat);
                        else if(roomNumber==last)
                        System.out.println(msgFromGroupChat.substring(0, msgFromGroupChat.length()-1));
                    }catch(IOException e){
                        System.out.println("Something went wrong in listen:-sendMessage");
                        closeEverything(socket, bufferedReader, bufferedWriter);
                        break;
                    }
                }
            }
        }).start();
    }
    
    public void closeEverything(Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter){
        try {
            System.out.println("Something went wrong in client");
            if(bufferedReader!=null)
            bufferedReader.close();
            if(bufferedWriter!=null)
            bufferedWriter.close();
            if(socket!=null)
            socket.close();
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws UnknownHostException, IOException {
        Scanner sc=new Scanner(System.in);
        // System.out.println("Enter the token of the group char");
        // String passcode=sc.nextLine();
        //Server x=new Server(serverSocket)
        //if(Server.checkToken(passcode)){
        System.out.println("Enter your username for the group chat: ");
        String username="";
        if(sc.hasNextLine())
        username=sc.nextLine();
        Socket socket=new Socket("localhost",1234);
        Client client=new Client(socket, username);
        //System.out.println(client.toString());
        client.listenForMessage();
        client.sendMessages();
        // }else{
        //     System.out.println("Token does not match");
        // }
    }
}
