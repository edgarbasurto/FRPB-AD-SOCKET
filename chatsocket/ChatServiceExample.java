import java.net.Socket;

 
public class ChatServiceExample
{ 
    private final ChatService cs;
    
   public ChatServiceExample(Socket aSocket, ChatRoom aChatRoom)
   {
      cs = new ChatService(aSocket, aChatRoom);
      cs.principal();
   }
}