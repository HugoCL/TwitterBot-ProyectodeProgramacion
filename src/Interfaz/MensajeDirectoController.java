package Interfaz;

import Motor.TwitterBot;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.util.Duration;
import twitter4j.TwitterException;

public class MensajeDirectoController {

    //inicialiar bot
    private TwitterBot bot = TwitterBot.getInstance().getBOT();

    @FXML private JFXTextField nicknameTF;
    @FXML private JFXTextArea messageTA;
    @FXML private JFXButton enviar_mensajeBT;


    public void initialize(){
        //Caracteres de mensaje
        KeyFrame frame = new KeyFrame(Duration.millis(100), e -> Caracteres());
        Timeline timeline = new Timeline(frame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
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
        String arroba = nicknameTF.getText();
        String mensaje = messageTA.getText();
        TwitterBot.Messages mensajes = bot.new Messages();
        mensajes.EnviarMD(arroba,mensaje);
    }
}
