package chatSocket.GrupoA.Listeners;

import chatSocket.GrupoA.Client.ChatClientForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.PrintWriter;

/**
 *
 * @author Edgar Basurto
 */
public class ChatListeners implements ActionListener, KeyListener {

    private ChatClientForm form;
    private PrintWriter out;

    /**
     * CONSTRUCTOR QUE RECIBE COMO PARAMETROS
     *
     * @param _form OBJETO DE TIPO ChatClientForm QUE HACE REFERENCIA AL
     * FORMULARIO QUE ESTA EJECUTANDOSE
     * @param _out OBJETO DE TIPO PrintWriter QUE ENVIA LOS MENSAJES AL SERVIDOR
     */
    public ChatListeners(ChatClientForm _form, PrintWriter _out) {
        this.form = _form;
        this.out = _out;
    }

    /**
     * SOBREESCRITURA DEL MEDOTO ACTIONPERFORMED DONDE SE INDICAN LAS ACCIONES
     * AL MOMENTO DE DAR CLICK EN LOS BOTONES ENVIAR, BORRAR, LOGOUT
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(form.getBtnEnviar())) {
            EnviarMensaje();
            LimpiarTxt();
            return;
        }
        if (e.getSource().equals(form.getBtnBorrar())) {
            try {
                form.getTxtEnviar().setText("");
            } catch (Exception e1) {
                System.out.println(e1.getMessage());
            }
            return;
        }
        if (e.getSource().equals(form.getBtnLogout())) {
            try {
                String line2 = "LOGOUT";
                out.println(line2);
                out.flush();
                form.dispose();
            } catch (Exception e1) {
                System.out.println(e1.getMessage());
            }
            return;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * SOBREESCRITURA DEL MEDOTO keyPressed DONDE SE INDICAN LAS ACCIONES AL
     * MOMENTO DE PRESIONAR LA TECLA ENTER
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            EnviarMensaje();
        }
    }

    /**
     * SOBREESCRITURA DEL MEDOTO keyReleased DONDE SE INDICAN LAS ACCIONES AL
     * MOMENTO DE LEVANTAR LA TECLA ENTER LUEGO DE PRESIONARLA
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            LimpiarTxt();
        }
    }
    /**
     * MÉTODO QUE RECOGE LOS DATOS DEL FORMULARIO Y LOS ENVIA AL SERVIDOR
     */
    private void EnviarMensaje() {
        try {
            String txtChat = form.getTxtEnviar().getText();
            if (!txtChat.isEmpty()) {
                String line1 = "CHAT " + txtChat;
                out.println(line1);
                out.flush();
            }

        } catch (Exception e1) {
            System.out.println(e1.getMessage());
        }
    }
    /**
     * MÉTODO QUE LIMPIA EL TEXTO DEL FORMULARIO
     */
    private void LimpiarTxt() {
        form.getTxtEnviar().setText("");
    }

}
