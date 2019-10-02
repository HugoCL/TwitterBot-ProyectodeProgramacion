package Interfaz;

import Motor.AdminBackup;
import Motor.TwitterBot;
import Motor.Usuario;
import Motor.AdminSesion;
import Transiciones.Dialog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import twitter4j.TwitterException;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.io.IOException;
import java.util.ArrayList;

public class InicioSesionController {

    private TwitterBot bot;

    @FXML private JFXPasswordField pinPF;
    @FXML private JFXCheckBox no_cierre_sesionCB;
    @FXML private JFXButton iniciar_sesionBT;
    @FXML private JFXButton copyBT;
    @FXML private JFXButton cerrarBT;
    @FXML private Label infoLB;
    @FXML private String enlace;

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
        }
        else{
            bot = botSerializado;
        }
        if (bot.getSesion()) {
            pinPF.setEditable(false);
            pinPF.setText(bot.getPin());
            no_cierre_sesionCB.setSelected(true);
            TwitterBot.getInstance().setBOT(bot);
            infoLB.setText("Sesión iniciada con: \n"+new Usuario().getNombreUsuario());
            infoLB.setVisible(true);
            cerrarBT.setVisible(true);
            copyBT.setVisible(false);
        }else{
            bot = TwitterBot.getInstance();
            bot.inicializarBot();
            enlace = bot.OAuthURL();
            pinPF.setEditable(true);
            infoLB.setVisible(false);
            cerrarBT.setVisible(false);
            copyBT.setVisible(true);
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
                    AdminSesion.getInstance().Serializar(bot);
                }
                TwitterBot.getInstance().setBOT(bot);
            } else {
                Dialog.getInstance().info(iniciar_sesionBT,respuesta,inicioSesionAP);
                this.initialize();
                return;}
        }else {
            if (!no_cierre_sesionCB.isSelected()){
                bot.setSesion(false);
                AdminSesion.getInstance().Serializar(bot);
                TwitterBot.getInstance().setBOT(bot);
            }
        }
        //Transición de escenas
        Transiciones.Slide.getInstance().left("/Interfaz/EscenaPrincipal.fxml",iniciar_sesionBT, inicioSesionAP);
    }

    @FXML public void Copiar() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(enlace);
        clipboard.setContent(content);
        copyBT.setDisable(true);
        Dialog.getInstance().info(copyBT,"Enlace copiado",inicioSesionAP);
    }

    @FXML public void cerrarSesion() throws IOException, TwitterException {
        bot.setSesion(false);
        AdminSesion.getInstance().Serializar(bot);
        TwitterBot.getInstance().setBOT(bot);
        AdminBackup.getInstance().serializar(new ArrayList<>());
        this.initialize();
    }
}
