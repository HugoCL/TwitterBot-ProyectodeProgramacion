package Interfaz;
import Motor.TwitterBot;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import twitter4j.TwitterException;

import java.io.IOException;
import java.net.URL;

public class TwittearController {

    private TwitterBot bot;

    @FXML private JFXButton publicar_tweetBT;
    @FXML private JFXTextArea tweet_TA;
    @FXML private Label caracteres_LB;

    @FXML private BorderPane tweetearBP;

    public void initialize(){
        //Inicializar bot
        bot = TwitterBot.getInstance().getBOT();

        //Inicio de Ventana
        publicar_tweetBT.setDisable(true);

        //Caracteres
        KeyFrame frame = new KeyFrame(Duration.millis(100), e -> Caracteres());
        Timeline timeline = new Timeline(frame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    private void Caracteres() {
        if (!tweet_TA.getText().isEmpty()) {
            if (tweet_TA.getText().length() > 280){
                caracteres_LB.setTextFill(Color.web("#ff0000"));
                publicar_tweetBT.setDisable(true);
            }
            else {
                caracteres_LB.setTextFill(Color.web("#000000"));
                publicar_tweetBT.setDisable(false);
            }
            caracteres_LB.setText(tweet_TA.getText().length()+"/280");
        }
        else {
            caracteres_LB.setTextFill(Color.web("#000000"));
            caracteres_LB.setText("0/280");
        }

    }

    @FXML public void publicar() throws TwitterException {
        String tweet = tweet_TA.getText();
        TwitterBot.Messages mensajes = bot.new Messages();
        mensajes.PublicarTweet(tweet);
        System.out.println("Tweet: "+ tweet + " enviado con exito...");
        tweet_TA.setText("");
        publicar_tweetBT.setDisable(true);
    }

    @FXML public void regresar() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        System.out.println("Cargando ventana principal...");
        URL location = EscenaPrincipalController.class.getResource("EscenaPrincipal.fxml");
        loader.setLocation(location);
        AnchorPane newBP = loader.load();
        Scene scene = new Scene(newBP);
        ((Stage)tweetearBP.getScene().getWindow()).setScene(scene);
    }
}
