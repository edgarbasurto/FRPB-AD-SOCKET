package chatSocket.GrupoA.Server;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.InputStreamReader;
import java.io.BufferedReader;

/**
 * Ejecuta los comandos de un protocolo de chat room simple recibidos de un
 * socket.
 */
public class ChatService {

    /**
     * Construye un objeto del servicio que procesa comandos de un socket para
     * un chat room.
     *
     * @param aSocket el socket
     * @param aChatRoom el chat room
     */
    public ChatService(Socket aSocket, ChatRoom aChatRoom) {
        s = aSocket;
        chatRoom = aChatRoom;
        chatRoom.add(this);
        loggedIn = false;
    }

    /**
     * Ejecuta los comandos hasta recibir un LOGOUT o el fin de los datos de
     * entrada.
     */
    public void principal() {
        try {
            InputStream inStream = s.getInputStream();
            OutputStream outStream = s.getOutputStream();
            in = new BufferedReader(new InputStreamReader(inStream));
            out = new PrintWriter(outStream);

            while (true) {
                if (!in.ready()) {
                    continue;
                }
                String line = in.readLine();
                int commandDelimiterPos = line.indexOf(' ');
                if (commandDelimiterPos < 0) {
                    commandDelimiterPos = line.length();
                }
                String command = line.substring(0, commandDelimiterPos);
                line = line.substring(commandDelimiterPos);

                String response = executeCommand(command, line);
                putMessage(response);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Envía el mensaje a través del socket.
     *
     * @param msg el mensaje a ser enviado
     */
    public void putMessage(String msg) {
        if (out != null) {
            out.println(msg);
            out.flush();
        }
    }

    /**
     * Ejecuta un comando.
     *
     * @param command el comando
     * @param line el resto de la linea del comando
     * @return la respuesta a ser enviada al cliente
     */
    public String executeCommand(String command, String line) throws IOException {
        if (command.equals("LOGIN")) {
//         userName = line;
//         chatRoom.register(userName);
//         chatRoom.broadcast(userName, "LOGIN", this);

            userChat = new Chatter(line);
            chatRoom.register(userChat.getName());
            chatRoom.broadcast(userChat.getName(), "***LOGIN***"+ "|" + userChat.getColor(), this);
            loggedIn = true;
//         return "Administrador del chat room: Hola, " + userName + ".";
            return "Administrador del chat room: Hola, " + getUserName() + ".";
        } else if (!loggedIn) {
            return "Administrador del chat room: Usted debe hacer LOGIN primero";
        } else if (command.equals("CHAT")) {
            String message = line;
//          chatRoom.broadcast(userName, message, this);
//         return userName + ": " + message;
//         
            chatRoom.broadcast(userChat.getName(), message + "|" + userChat.getColor(), this);
            return getUserName() + ": " + message + "|" + userChat.getColor();
        } else if (!command.equals("LOGOUT")) {
            return "Administrador del chat room: Comando inválido";
        }

//      chatRoom.broadcast(userName, "LOGOUT", this);
//      chatRoom.leave(userName, this);
        chatRoom.broadcast(userChat.getName(), "***LOGOUT***"+ "|" + userChat.getColor(), this);
        chatRoom.leave(userChat.getName(), this);
        return "Adios!";
    }

    /**
     * Retorna el nombre del usuario de este servicio.
     *
     * @return el nombre del usuario de este servicio
     */
    public String getUserName() {
        return userChat.getName();
        // return userName;
    }

    //private String userName;
    private Chatter userChat;
    private Socket s;
    private ChatRoom chatRoom;
    private PrintWriter out;
    private BufferedReader in;
    private boolean loggedIn;
}
