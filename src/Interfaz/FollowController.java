package Interfaz;

import Motor.TwitterBot;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class FollowController {

    @FXML private JFXButton followBT;
    @FXML private JFXButton closeBT;
    @FXML private JFXTextField nicknameTF;

    @FXML private AnchorPane followAP;

    TwitterBot bot;

    public void initialize(){
        //Inicializacion de bot
        bot = TwitterBot.getInstance();
    }

    public void seguir(){
        if (nicknameTF.getText().isEmpty()){
            System.out.println("Ingrese algún nombre de usuario.");
        }
        else {
            TwitterBot.Usuario usuario = bot.new Usuario();
            usuario.Follow(nicknameTF.getText());
            System.out.println("Se comenzó a seguir exitosamente a: @"+nicknameTF.getText());
            nicknameTF.setText("");
        }
    }
    public void cerrarVentana() throws IOException {
        System.out.println("Cargando ventana principal...");
        Transiciones.Slide.getInstance().right("/Interfaz/EscenaPrincipal.fxml",closeBT,followAP);
    }
}
