package Interfaz;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class FollowController {

    @FXML private JFXButton followBT;
    @FXML private JFXButton closeBT;
    @FXML private JFXTextField nicknameTF;

    @FXML private AnchorPane followAP;

    public void initialize(){
        followBT.setDisable(true);
    }

    public void seguir(){
        System.out.println(nicknameTF.getText());
    }
    public void cerrarVentana(){

    }
}
