package Interfaz;
import Motor.TwitterBot;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import twitter4j.TwitterException;

import java.io.File;
import java.io.IOException;

public class TwittearController {

    private TwitterBot bot;

    @FXML private JFXButton publicar_tweetBT;
    @FXML private JFXButton regresarBT;
    @FXML private JFXButton addFileBT;
    @FXML private JFXTextArea tweet_TA;
    @FXML private Label caracteres_LB;
    @FXML private Label nameFile_LB;

    @FXML private AnchorPane tweetearAP;

    private FileChooser directoryChooser = new FileChooser();
    private File selectedDirectory;

    public void initialize(){
        //Inicializar bot
        bot = TwitterBot.getInstance().getBOT();

        //Inicio de Ventana
        publicar_tweetBT.setDisable(true);
        nameFile_LB.setVisible(false);

        //Caracteres
        KeyFrame frame = new KeyFrame(Duration.millis(100), e -> Caracteres());
        Timeline timeline = new Timeline(frame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        //Tweet con Imagenes
        directoryChooser.setInitialDirectory(new File("src"));

    }
    @FXML private void agregarArchivo(){
        selectedDirectory = directoryChooser.showOpenDialog(tweetearAP.getScene().getWindow());
        try {
            nameFile_LB.setText(selectedDirectory.getAbsolutePath());
            nameFile_LB.setVisible(true);
        }
        catch (Exception e){
            System.out.println("Archivo no agregado.");
        }

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
        if (nameFile_LB.getText() == ""){
            mensajes.PublicarTweet(tweet);
        }
        else{
            mensajes.PublicarTweetImagen(tweet, selectedDirectory);
        }

        System.out.println("Tweet: "+ tweet + " enviado con exito...");
        tweet_TA.setText("");
        publicar_tweetBT.setDisable(true);
        nameFile_LB.setText("");
        nameFile_LB.setVisible(false);
    }

    @FXML public void regresar() throws IOException {
        System.out.println("Cargando ventana para twittear...");

        Parent root = FXMLLoader.load(getClass().getResource("/Interfaz/EscenaPrincipal.fxml"));
        Scene scene = regresarBT.getScene();

        root.translateXProperty().set(-scene.getWidth());
        StackPane parentContainer = (StackPane) scene.getRoot();
        parentContainer.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateXProperty(),0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.5),kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(event -> {
            parentContainer.getChildren().remove(tweetearAP);
        });
        timeline.play();
    }
}
