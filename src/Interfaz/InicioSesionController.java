package Interfaz;

import Motor.TwitterBot;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import twitter4j.TwitterException;
import twitter4j.auth.RequestToken;

import java.io.IOException;
import java.net.URL;

public class InicioSesionController {

    @FXML private JFXTextField enlaceTF;
    @FXML private JFXPasswordField pinPF;
    @FXML private JFXCheckBox noCierreSesionCB;

    @FXML private AnchorPane inicioSesionAP;

    public void initialize(){
    }

    @FXML public void iniciarSesion() throws TwitterException, IOException {
        TwitterBot bot = TwitterBot.getInstance().cargarBot();
        TwitterBot.getInstance().setBOT(bot);
        System.out.println("Sesión iniciada...");

        FXMLLoader loader = new FXMLLoader();
        System.out.println("Cargando ventana principal...");
        URL location = EscenaPrincipalController.class.getResource("EscenaPrincipal.fxml");
        loader.setLocation(location);
        AnchorPane newBP = loader.load();
        Scene scene = new Scene(newBP);
        ((Stage)inicioSesionAP.getScene().getWindow()).setScene(scene);

    }
}
