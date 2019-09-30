package Interfaz;

import Motor.TwitterBot;
import Motor.Usuario;
import Motor.AdminSesion;
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

    private TwitterBot bot;

    @FXML private JFXTextArea enlaceTA;
    @FXML private JFXPasswordField pinPF;
    @FXML private JFXCheckBox no_cierre_sesionCB;
    @FXML private JFXButton iniciar_sesionBT;
    @FXML private JFXButton copyBT;

    @FXML private AnchorPane inicioSesionAP;

    @FXML private StackPane parentContainer;

    public void initialize() throws TwitterException {
        AdminSesion adm = AdminSesion.getInstance();
        pinPF.setText("");
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
            enlaceTA.setText("Sesión iniciada con: "+new Usuario().getNombreUsuario());
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
                    AdminSesion.getInstance().serializar(bot);
                }
                TwitterBot.getInstance().setBOT(bot);
            } else {
                Dialog.getInstance().info(iniciar_sesionBT,respuesta,"Ok, revisaré",inicioSesionAP);
                this.initialize();
                return;}
        }else {
            if (!no_cierre_sesionCB.isSelected()){
                bot.setSesion(false);
                AdminSesion.getInstance().serializar(bot);
                TwitterBot.getInstance().setBOT(bot);
            }
        }
        //Transición de escenas
        Transiciones.Slide.getInstance().left("/Interfaz/EscenaPrincipal.fxml",iniciar_sesionBT, inicioSesionAP);
    }

    @FXML public void copiar() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(enlaceTA.getText());
        clipboard.setContent(content);
        Dialog.getInstance().info(copyBT,"Enlace copiado","OK",inicioSesionAP);
    }

    @FXML public void cerrarPrograma(){
        System.out.println("Finalizando programa...");
        // SE NECESITA CAMBIAR ESTE SYS.EXIT
        System.exit(0);
    }
}
