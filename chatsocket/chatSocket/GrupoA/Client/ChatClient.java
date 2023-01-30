package chatSocket.GrupoA.Client;

import chatSocket.GrupoA.Listeners.ChatListeners;
import chatSocket.GrupoA.Server.Flag;
import java.awt.Color;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

/**
 * Programa cliente del chat.
 */
public class ChatClient {

    /**
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args)
            throws IOException {
        final int PORT = 8888;
        final String HOST = "localhost";

        ChatClientForm frm = new ChatClientForm();

        frm.setVisible(true);

        frm.notificarHistorico("*****Bienvenido al chat room!*****\n", Color.DARK_GRAY);
//      frm.notificarHistorico("Por favor entre su comando.");
//      frm.notificarHistorico("USO:  LOGIN usuario_o_nick");
//      frm.notificarHistorico("      CHAT mensaje");
//      frm.notificarHistorico("      LOGOUT");
//      frm.notificarHistorico("Presione ENTER para enviar su mensaje.\n");

        Socket s = new Socket(HOST, PORT);
        InputStream inStream = s.getInputStream();
        OutputStream outStream = s.getOutputStream();
        final BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
        PrintWriter out = new PrintWriter(outStream);
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

        String line1 = "LOGIN " + frm.user;
        out.println(line1);
        out.flush();
        final Flag done = new Flag(false);

        class OutputRunnable implements Runnable {

            ChatClientForm clientform;

            public OutputRunnable(ChatClientForm form) {
                clientform = form;
            }

            public void run() {
                try {
                    while (!done.getFlag()) {
                        String response = in.readLine();
                        String separador = Pattern.quote("|");
                        String[] resp = response.split(separador);
                        if (resp.length == 2) {
                            clientform.notificarHistorico(resp[0], Color.decode(resp[1]));
                        } else {
                            clientform.notificarHistorico(response, Color.DARK_GRAY);
                        }

                        if (response.equals("Adios!")) {
                            done.setFlag(true);
                        }
                    }
                } catch (IOException e) {
                }
            }
        }

        frm.getTxtEnviar().addKeyListener(new ChatListeners(frm, out));
        frm.getBtnEnviar().addActionListener(new ChatListeners(frm, out));
        frm.getBtnLogout().addActionListener(new ChatListeners(frm, out));
        frm.getBtnBorrar().addActionListener(new ChatListeners(frm, out));
        ChatListeners cerrarChat = new ChatListeners(frm, out);
        cerrarChat.cerrar();

        OutputRunnable or = new OutputRunnable(frm);
        Thread t = new Thread(or);

        t.start();

        while (!done.getFlag()) {
            String line = console.readLine();
            out.println(line);
            out.flush();

        }

        s.close();

    }
}
