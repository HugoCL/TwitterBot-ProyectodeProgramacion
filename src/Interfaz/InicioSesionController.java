package Interfaz;

import Motor.TwitterBot;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import twitter4j.TwitterException;

import java.io.IOException;
import java.net.URL;

public class InicioSesionController {

    @FXML private TextField username;
    @FXML private TextField password;

    @FXML private AnchorPane inicioSesionAP;

    public void initialize(){
        username.setDisable(true);
        password.setDisable(true);
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
