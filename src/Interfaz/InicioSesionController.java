package Interfaz;

import Motor.TwitterBot;
import Motor.adminSesion;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import twitter4j.TwitterException;

import java.io.IOException;

public class InicioSesionController {

    protected TwitterBot bot;

    @FXML private JFXTextArea enlaceTA;
    @FXML private JFXPasswordField pinPF;
    @FXML private JFXCheckBox no_cierre_sesionCB;
    @FXML private JFXButton iniciar_sesionBT;

    @FXML private AnchorPane inicioSesionAP;

    @FXML private StackPane parentContainer;

    public void initialize() throws TwitterException, IOException {
        adminSesion adm = adminSesion.getInstance();
        TwitterBot botSerializado = adm.desSerializar();
        if (botSerializado == null){
            bot = TwitterBot.getInstance();
            bot.isGuardado = false;
            bot.inicializarBot();
            enlaceTA.setText(bot.OAuthURL());
        }
        else{
            bot = botSerializado;
        }
        if (bot.isGuardado) {
            pinPF.setEditable(false);
            pinPF.setText(bot.pin);
            no_cierre_sesionCB.setSelected(true);
            TwitterBot.getInstance().setBOT(bot);
            enlaceTA.setText("Sesión iniciada con: "+TwitterBot.getInstance().getBOT().new Usuario().getNombreUsuario());
        }else{
            bot = TwitterBot.getInstance();
            bot.inicializarBot();
            enlaceTA.setText(bot.OAuthURL());
        }
        //FadeIn
        Transiciones.Fade.getInstance().in(parentContainer);

    }

    @FXML public void iniciarSesion() throws IOException {
        if (!bot.isGuardado){
            String pin = pinPF.getText();
            bot.OAuthInicio(pin);
            if (no_cierre_sesionCB.isSelected()){
                bot.isGuardado = true;
                bot.pin = pinPF.getText();
                adminSesion.getInstance().Serializar(bot);
                System.out.println("Sesion guardada.");
            }
            TwitterBot.getInstance().setBOT(bot);
        }else {
            if (!no_cierre_sesionCB.isSelected()){
                bot.isGuardado = false;
                adminSesion.getInstance().Serializar(bot);
                TwitterBot.getInstance().setBOT(bot);
                System.out.println("Sesion no guardada.");
            }
        }
        System.out.println("Sesión iniciada...");
        //Transición de escenas
        Transiciones.Slide.getInstance().left("/Interfaz/EscenaPrincipal.fxml",iniciar_sesionBT, inicioSesionAP);
    }
}
