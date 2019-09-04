package Interfaz;

import Motor.TwitterBot;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import twitter4j.TwitterException;

import java.io.IOException;
import java.util.ArrayList;

public class MensajeDirectoController {

    //inicialiar bot
    private TwitterBot bot = TwitterBot.getInstance().getBOT();

    @FXML private JFXButton regresarBT;
    @FXML private JFXComboBox<String> followersCB;
    @FXML private JFXTextArea messageTA;
    @FXML private JFXButton enviar_mensajeBT;

    @FXML private AnchorPane directMessageAP;

    public void initialize(){
        //Caracteres de mensaje
        KeyFrame frame = new KeyFrame(Duration.millis(100), e -> Caracteres());
        Timeline timeline = new Timeline(frame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        //Inicializar ComboBox
        ObservableList<String> listaFollowers = FXCollections.observableArrayList();
        followersCB.setItems(listaFollowers);
        ArrayList<String> followers = bot.new Usuario().getFollowers();
        for (String follower : followers){
            listaFollowers.add(follower);
        }
    }

    public void Caracteres(){
        if (!messageTA.getText().isEmpty()) {
            enviar_mensajeBT.setDisable(false);
        }
        else {
            enviar_mensajeBT.setDisable(true);
        }
    }
    @FXML public void enviarMensaje() throws TwitterException {
        String arroba = followersCB.getValue();
        String mensaje = messageTA.getText();
        TwitterBot.Messages mensajes = bot.new Messages();
        mensajes.EnviarMD(arroba,mensaje);
        messageTA.setText("");
    }
    @FXML public void regresar() throws IOException {
        System.out.println("Cargando ventana principal...");
        Transiciones.Slide.getInstance().right("/Interfaz/EscenaPrincipal.fxml", regresarBT, directMessageAP);
    }
}
