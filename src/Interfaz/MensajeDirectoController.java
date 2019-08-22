package Interfaz;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;

public class MensajeDirectoController {

    @FXML private JFXTextField nicknameTF;
    @FXML private JFXTextArea messageTA;
    @FXML private JFXButton enviar_mensajeBT;


    public void initialize(){

    }

    @FXML public void enviarMensaje(){
        System.out.println("Enviado el mensaje ' "+messageTA.getText()+" ' al usuario @"+nicknameTF.getText());
        nicknameTF.setText("");
        messageTA.setText("");
    }
}
