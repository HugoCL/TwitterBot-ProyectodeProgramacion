package Interfaz;

import Motor.TwitterBot;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

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

        Parent root = FXMLLoader.load(getClass().getResource("/Interfaz/EscenaPrincipal.fxml"));
        Scene scene = closeBT.getScene();

        root.translateXProperty().set(-scene.getWidth());
        StackPane parentContainer = (StackPane) scene.getRoot();
        parentContainer.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateXProperty(),0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.5),kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(event -> {
            parentContainer.getChildren().remove(followAP);
        });
        timeline.play();
    }
}
