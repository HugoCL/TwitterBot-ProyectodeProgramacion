package Interfaz;

import Motor.Usuario;
import Transiciones.Dialog;
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

    public void initialize(){
    }

    public void seguir(){
        String respuesta;
        if (nicknameTF.getText().isEmpty()){
            System.out.println("Ingrese algún nombre de usuario.");
            Dialog.getInstance().info(followBT,"Ingrese algún nombre de usuario.",followAP);
        }
        else {
            Usuario usuario = new Usuario();
            respuesta = usuario.Follow(nicknameTF.getText());
            Dialog.getInstance().info(followBT,respuesta,followAP);
            nicknameTF.setText("");
        }
    }
    public void cerrarVentana() throws IOException {
        Transiciones.Slide.getInstance().right("/Interfaz/EscenaPrincipal.fxml",closeBT,followAP);
    }
}
