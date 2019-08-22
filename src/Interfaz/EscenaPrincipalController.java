package Interfaz;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class EscenaPrincipalController {

    @FXML private AnchorPane mainAP;

    @FXML private JFXButton tweetearBT;
    @FXML private JFXButton retweetearBT;
    @FXML private JFXButton followBT;
    @FXML private JFXButton directBT;
    @FXML private JFXButton cerrar_sesionBT;

    public void initialize(){
        //Botones desactivados
        retweetearBT.setDisable(true);
        followBT.setDisable(true);
    }

    @FXML public void tweetear() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        System.out.println("Cargando ventana para tweetear...");
        URL location = TwittearController.class.getResource("Twittear.fxml");
        loader.setLocation(location);
        BorderPane newBP = loader.load();
        Scene scene = new Scene(newBP);
        ((Stage)mainAP.getScene().getWindow()).setScene(scene);
    }

    @FXML public void directMessage() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        System.out.println("Cargando ventana para mensajes directos...");
        URL location = MensajeDirectoController.class.getResource("MensajeDirecto.fxml");
        loader.setLocation(location);
        AnchorPane newBP = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(newBP);
        stage.setScene(scene);
        stage.setTitle("Twitter Bot - Ramos Overflow");
        stage.getIcons().add(new Image("Imagenes/Icono.png"));
        stage.show();
    }
    @FXML public void cerrarSesion() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        System.out.println("Cargando ventana inicio sesión...");
        URL location = InicioSesionController.class.getResource("InicioSesion.fxml");
        loader.setLocation(location);
        AnchorPane newBP = loader.load();
        Scene scene = new Scene(newBP);
        ((Stage)mainAP.getScene().getWindow()).setScene(scene);
    }
}
