package chatSocket.GrupoA.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Un servidor que ejecuta el ChatService. Puede aceptar m�ltiples conexiones de
 * m�ltiples clientes.
 */
public class ChatServer implements Runnable {

    final ChatRoom chatRoom;
    final ServerSocket serverSocket;
    private ChatService servicio;
    private Socket s;
    private static ChatServerForm frm;

    public ChatServer(ChatRoom chat, ServerSocket server) {
        chatRoom = chat;
        serverSocket = server;
    }

    public static void main(String[] args) throws IOException {
        /* Tamaño de chatter que se pueden conectar simultaneamente */
        final int ROOM_SIZE = 10;
        
        /* Puerto de socket */
        final int PORT = 8888;
        
        frm= new ChatServerForm();
        frm.setVisible(true);
        
        /* Sala de chat */
        ChatRoom chat = new ChatRoom(ROOM_SIZE);
        
        /* Servidor Socket*/
        ServerSocket server = new ServerSocket(PORT);
        
        
        System.out.println("Esperando que se conecten clientes...");
        frm.notificarBitacora("Esperando que se conecten clientes...");
        /*Bucle de hilos para escuchar los chatter*/
        while (true) {
            var ch = new ChatServer(chat, server);
            ch.run();
        }
    }

    /*
    * Ejecuta en un hilo el servicio del chat, modo escucha.
    */
    @Override
    public void run() {
        Thread hilo = new Thread(() -> {

            try {
                s = serverSocket.accept();
                servicio = new ChatService(s, chatRoom);
                servicio.principal();

            } catch (IOException ex) {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
                 frm.notificarBitacora("Excepción en escuchar(): " + ex.getMessage());
                System.out.println("Excepción en escuchar(): " + ex.getMessage());
            } finally {
                close();
            }
        });
        hilo.start();
    }

    /*
    * cierra el socket usado en la conexión.
    */
    public void close() {
        try {
            s.close();
        } catch (IOException e) {
            frm.notificarBitacora("Excepción en cerrarConexion(): " + e.getMessage());
            System.out.println("Excepción en cerrarConexion(): " + e.getMessage());
        } finally {
            frm.notificarBitacora("Conversación finalizada....");
            System.out.println("Conversación finalizada....");
            System.exit(0);
        }
    }
}
