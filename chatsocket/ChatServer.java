import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
   Un servidor que ejecuta el ChatService.
   Puede aceptar m�ltiples conexiones de m�ltiples clientes.
*/
public class ChatServer implements Runnable
{  
    ServerSocket server; 
    ChatRoom chat;
    public ChatServer(ChatRoom wChatRoom, ServerSocket wServer) throws IOException{
 server=wServer;
 chat=wChatRoom;
    }
   
   public static void main(String[] args ) throws IOException
   {  
      final int ROOM_SIZE = 10;
      ChatRoom chatRoom = new ChatRoom(ROOM_SIZE);
      final int PORT = 8888;
      ServerSocket server = new ServerSocket(PORT);
      System.out.println("Esperando que se conecten clientes...");
      
    
      
      // su codigo va aqui!
     //Socket socket= server.accept(); 
       for (int i = 0; i < 5; i++) {
            var cv= new  ChatServer( chatRoom, server);
     cv.run();
       }
    
   }

    @Override
    public void run() {
        try { 
            Socket s = server.accept();            
            ChatService servicio = new ChatService(s, this.chat);
           servicio.principal();
             Thread hilo = new Thread((Runnable) servicio);
             hilo.start();
            
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       
    }
}
