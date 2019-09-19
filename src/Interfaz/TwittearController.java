package Interfaz;
import Motor.Messages;
import Transiciones.Dialog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class TwittearController {

    @FXML private JFXButton publicar_tweetBT;
    @FXML private JFXButton regresarBT;
    @FXML private JFXButton addFileBT;
    @FXML private JFXTextArea tweet_TA;
    @FXML private Label caracteres_LB;
    @FXML private ImageView imagenTweet;
    @FXML private Label archivoLB;
    @FXML private Label nameFile_LB;

    @FXML private AnchorPane tweetearAP;

    private Pattern patronImage = Pattern.compile("^.+\\.(jp(e)?g|JP(E)?G|gif|GIF|png|PNG)$");
    private FileChooser fileChooser = new FileChooser();
    private File selectedFile;

    public void initialize(){
        fileChooser.setTitle("Buscar");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG, PNG, GIF, MP4", "*.jpg", "*.png", "*.gif*", ".mp4")
        );

        //Inicio de Ventana
        publicar_tweetBT.setDisable(true);
        nameFile_LB.setVisible(false);
        publicar_tweetBT.setDisable(true);
        caracteres_LB.setText("0/280");

        KeyFrame frame = new KeyFrame(Duration.millis(100), e -> Caracteres());
        Timeline timeline = new Timeline(frame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        //Tweet con Imagenes
        fileChooser.setInitialDirectory(new File("src"));
        fileChooser.setTitle("Buscar Archivo");

    }

    @FXML private void agregarArchivo(){
        selectedFile = fileChooser.showOpenDialog(tweetearAP.getScene().getWindow());
        try {
            nameFile_LB.setText(selectedFile.getAbsolutePath());
            if (patronImage.matcher(selectedFile.getName()).find()){
                Image image = new Image("file:"+selectedFile.getAbsolutePath());
                imagenTweet.setImage(image);
                archivoLB.setVisible(true);
                nameFile_LB.setVisible(false);
                publicar_tweetBT.setDisable(false);
            }else {
                archivoLB.setVisible(false);
                nameFile_LB.setVisible(true);
                publicar_tweetBT.setDisable(false);
            }

        }
        catch (Exception e){
            Dialog.getInstance().info(addFileBT,"Archivo no agregado.","OK, revisaré",tweetearAP);
            imagenTweet.setImage(null);
            tweet_TA.setText("");
            publicar_tweetBT.setDisable(true);
            nameFile_LB.setText("");
            caracteres_LB.setText("0/280");
        }

    }

    public void Caracteres() {
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
            if (nameFile_LB.getText() != ""){
                publicar_tweetBT.setDisable(false);
                caracteres_LB.setTextFill(Color.web("#000000"));
            }else {
                publicar_tweetBT.setDisable(true);
            }
            caracteres_LB.setText("0/280");
        }
    }

    @FXML public void publicar(){
        Pattern patronImage = Pattern.compile("^.+\\.(jp(e)?g|JP(E)?G|gif|GIF|png|PNG)$");
        Pattern patronVideo = Pattern.compile("^.+\\.(mp4|MP4)$");

        String respuesta;
        String tweet = tweet_TA.getText();
        Messages mensajes = new Messages();
        if (nameFile_LB.getText() == ""){
            respuesta = mensajes.PublicarTweet(tweet);
        }
        else if(patronImage.matcher(selectedFile.getName()).find()){
            respuesta = mensajes.PublicarTweetImagen(tweet, selectedFile);
        }else if(patronVideo.matcher(selectedFile.getName()).find())
            respuesta = mensajes.PublicarTweetVideo(tweet, selectedFile);
        else
            respuesta = "ERROR: Revise el tipo de archivo";
        Dialog.getInstance().info(addFileBT,respuesta,"OK, revisaré",tweetearAP);
        imagenTweet.setImage(null);
        tweet_TA.setText("");
        publicar_tweetBT.setDisable(true);
        nameFile_LB.setText("");
        caracteres_LB.setText("0/280");
    }

    @FXML public void regresar() throws IOException {
        Transiciones.Slide.getInstance().right("/Interfaz/EscenaPrincipal.fxml",regresarBT,tweetearAP);
    }
}
