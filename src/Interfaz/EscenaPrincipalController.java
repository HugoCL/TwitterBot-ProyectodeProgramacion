package Interfaz;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class EscenaPrincipalController {

    @FXML private AnchorPane mainAP;

    @FXML private Button tweetearBT;
    @FXML private Button retweetearBT;
    @FXML private Button followBT;
    @FXML private Button directBT;

    public void initialize(){
        //Botones desactivados
        retweetearBT.setDisable(true);
        followBT.setDisable(true);
        directBT.setDisable(true);
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

}
