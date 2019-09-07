package Interfaz;

import Motor.TwitterBot;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import twitter4j.TwitterException;

import java.io.IOException;

public class FollowController {

    @FXML private JFXButton followBT;
    @FXML private JFXButton closeBT;
    @FXML private JFXTextField nicknameTF;

    @FXML private AnchorPane followAP;

    TwitterBot bot = TwitterBot.getInstance();

    public void initialize(){
        //Inicializacion de bot
        bot = TwitterBot.getInstance().getBOT();
    }

    public void seguir() throws TwitterException {
        String respuesta;
        if (nicknameTF.getText().isEmpty()){
            System.out.println("Ingrese algún nombre de usuario.");
        }
        else {
            TwitterBot.Usuario usuario = bot.new Usuario();
            respuesta = usuario.Follow(nicknameTF.getText());
            System.out.println(respuesta);
            nicknameTF.setText("");
        }
    }
    public void cerrarVentana() throws IOException {
        System.out.println("Cargando ventana principal...");
        Transiciones.Slide.getInstance().right("/Interfaz/EscenaPrincipal.fxml",closeBT,followAP);
    }
}
