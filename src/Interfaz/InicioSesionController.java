package Interfaz;

import Motor.TwitterBot;
import Motor.adminSesion;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import twitter4j.TwitterException;

import java.io.IOException;

public class InicioSesionController {

    protected TwitterBot bot;

    @FXML private JFXTextArea enlaceTA;
    @FXML private JFXPasswordField pinPF;
    @FXML private JFXCheckBox no_cierre_sesionCB;
    @FXML private JFXButton iniciar_sesionBT;

    @FXML private AnchorPane inicioSesionAP;

    @FXML private StackPane parentContainer;

    public void initialize() throws TwitterException, IOException {
        adminSesion adm = adminSesion.getInstance();
        TwitterBot botSerializado = adm.desSerializar();
        if (botSerializado == null){
            bot = TwitterBot.getInstance();
            bot.inicializarBot();
            enlaceTA.setText(bot.OAuthURL());
        }
        else{
            bot = botSerializado;
        }
        if (bot.isGuardado) {
            enlaceTA.setText("Enlace no requerido, sesión iniciada.");
            pinPF.setEditable(false);
            no_cierre_sesionCB.setSelected(true);
        }

        //Ventana
        parentContainer.setOpacity(0);
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.seconds(0.25));
        fadeTransition.setNode(parentContainer);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }

    @FXML public void iniciarSesion() throws TwitterException, IOException {
        if (!bot.isGuardado){
            String pin = pinPF.getText();
            bot.OAuthInicio(pin);
            TwitterBot.getInstance().setBOT(bot);
        }else {
            TwitterBot.getInstance().setBOT(bot);
            System.out.println("Sesión ya iniciada.");
        }

        System.out.println("Sesión iniciada...");

        //Transición de escenas
        Parent root = FXMLLoader.load(getClass().getResource("/Interfaz/EscenaPrincipal.fxml"));
        Scene scene = iniciar_sesionBT.getScene();

        root.translateXProperty().set(scene.getWidth());
        parentContainer.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateXProperty(),0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.5),kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(event -> {
            parentContainer.getChildren().remove(inicioSesionAP);
        });
        timeline.play();
    }
}
