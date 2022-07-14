import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers=new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUserName;

    public ClientHandler(Socket socket){
        try{
            this.socket=socket;
            this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //this.clientUserName=bufferedReader.readLine();
            String p=bufferedReader.readLine();
            this.clientUserName=p.substring(0,p.length()-1);
            //String roomNumber=sc.nextLine();
            clientHandlers.add(this);
            broadcastMessage("SERVER:"+clientUserName+" has entered the chat"+p.charAt(p.length()-1));
        } catch(IOException e){
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }
    @Override
    public void run() {
        // TODO Auto-generated method stub
        String messageFromClient;

        while(socket.isConnected()){
            try {
                messageFromClient=bufferedReader.readLine();
                broadcastMessage(messageFromClient);
            } catch (Exception e) {
                //TODO: handle exception
                closeEverything(socket,bufferedReader,bufferedWriter);
                break;
            }
        }
    }

    private void broadcastMessage(String messageToSend) {
        for(ClientHandler clientHandler:clientHandlers){
            try {
            if(!clientHandler.clientUserName.equals(clientUserName)){
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    bufferedWriter.flush();
                } 
            }catch (IOException e) {
                    // TODO Auto-generated catch block
                closeEverything(socket,bufferedReader,bufferedWriter);
            }
        }
    }

    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
        try {
            //System.out.println("Something went wrong in clientHandler");
            if(bufferedReader!=null)
            bufferedReader.close();
            if(bufferedWriter!=null)
            bufferedWriter.close();
            if(socket!=null)
            socket.close();
            //break;
        } catch (Exception e) {
            //TODO: handle exception
            //System.out.println(e);
            e.printStackTrace();
        }
    }

    private void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage("SERVER:"+clientUserName+"has left the chat");

    }

    
}
    
