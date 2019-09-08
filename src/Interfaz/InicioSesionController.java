package Interfaz;

import Motor.TwitterBot;
import Motor.adminSesion;
import Transiciones.Dialog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import twitter4j.TwitterException;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.io.IOException;

public class InicioSesionController {

    protected TwitterBot bot;

    @FXML private JFXTextArea enlaceTA;
    @FXML private JFXPasswordField pinPF;
    @FXML private JFXCheckBox no_cierre_sesionCB;
    @FXML private JFXButton iniciar_sesionBT;
    @FXML private JFXButton copyBT;

    @FXML private AnchorPane inicioSesionAP;

    @FXML private StackPane parentContainer;

    public void initialize() throws TwitterException {
        adminSesion adm = adminSesion.getInstance();
        TwitterBot botSerializado = adm.desSerializar();
        if (botSerializado == null){
            bot = TwitterBot.getInstance();
            bot.setSesion(false);
            bot.inicializarBot();
            enlaceTA.setText(bot.OAuthURL());
        }
        else{
            bot = botSerializado;
        }
        if (bot.getSesion()) {
            pinPF.setEditable(false);
            pinPF.setText(bot.getPin());
            no_cierre_sesionCB.setSelected(true);
            TwitterBot.getInstance().setBOT(bot);
            enlaceTA.setText("Sesión iniciada con: "+TwitterBot.getInstance().getBOT().new Usuario().getNombreUsuario());
            copyBT.setDisable(true);
        }else{
            bot = TwitterBot.getInstance();
            bot.inicializarBot();
            enlaceTA.setText(bot.OAuthURL());
            copyBT.setDisable(false);
        }
        //FadeIn
        Transiciones.Fade.getInstance().in(parentContainer);

    }

    @FXML public void iniciarSesion() throws IOException, TwitterException {
        String respuesta;
        if (!bot.getSesion()){
            String pin = pinPF.getText();
            respuesta = bot.OAuthInicio(pin);
            if (respuesta.compareTo("PIN Correcto") == 0) {
                if (no_cierre_sesionCB.isSelected()){
                    bot.setSesion(true);
                    bot.setPin(pinPF.getText());
                    adminSesion.getInstance().Serializar(bot);
                    System.out.println("Sesion guardada.");
                }
                TwitterBot.getInstance().setBOT(bot);
            } else {
                Dialog.getInstance().error(iniciar_sesionBT,respuesta,"Ok, revisaré",inicioSesionAP);
                this.initialize();
                return;}
        }else {
            if (!no_cierre_sesionCB.isSelected()){
                bot.setSesion(false);
                adminSesion.getInstance().Serializar(bot);
                TwitterBot.getInstance().setBOT(bot);
                System.out.println("Sesion no guardada.");
            }
        }
        //Transición de escenas
        Transiciones.Slide.getInstance().left("/Interfaz/EscenaPrincipal.fxml",iniciar_sesionBT, inicioSesionAP);
    }

    @FXML public void Copiar() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(enlaceTA.getText());
        clipboard.setContent(content);
        Dialog.getInstance().error(copyBT,"Enlace copiado","OK",inicioSesionAP);
    }
}
