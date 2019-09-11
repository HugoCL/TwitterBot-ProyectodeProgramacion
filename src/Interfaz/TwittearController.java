package Interfaz;
import Motor.TwitterBot;
import Transiciones.Dialog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

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
        publicar_tweetBT.setDisable(true);
        caracteres_LB.setText("0/280");

        //Tweet con Imagenes
        directoryChooser.setInitialDirectory(new File("src"));

    }

    @FXML private void agregarArchivo(){
        selectedDirectory = directoryChooser.showOpenDialog(tweetearAP.getScene().getWindow());
        try {
            nameFile_LB.setText(selectedDirectory.getAbsolutePath());
            nameFile_LB.setVisible(true);
            publicar_tweetBT.setDisable(false);
        }
        catch (Exception e){
            Dialog.getInstance().info(addFileBT,"Archivo no agregado.","OK, revisaré",tweetearAP);
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
                caracteres_LB.setText("0/280");
            }else {
                publicar_tweetBT.setDisable(true);
            }
        }
    }

    @FXML public void publicar(){
        Pattern patronImage = Pattern.compile("^.+\\.(jp(e)?g|JP(E)?G|gif|GIF|png|PNG)$");
        Pattern patronVideo = Pattern.compile("^.+\\.(mp4|MP4)$");

        String respuesta;
        String tweet = tweet_TA.getText();
        TwitterBot.Messages mensajes = bot.new Messages();
        if (nameFile_LB.getText() == ""){
            respuesta = mensajes.PublicarTweet(tweet);
        }
        else if(patronImage.matcher(selectedDirectory.getName()).find()){
            respuesta = mensajes.PublicarTweetImagen(tweet, selectedDirectory);
        }else if(patronVideo.matcher(selectedDirectory.getName()).find())
            respuesta = mensajes.PublicarTweetVideo(tweet, selectedDirectory);
        else
            respuesta = "ERROR: Revise el tipo de archivo";
        Dialog.getInstance().info(addFileBT,respuesta,"OK, revisaré",tweetearAP);
        tweet_TA.setText("");
        publicar_tweetBT.setDisable(true);
        nameFile_LB.setText("");
        caracteres_LB.setText("0/280");
        nameFile_LB.setVisible(false);
    }

    @FXML public void regresar() throws IOException {
        Transiciones.Slide.getInstance().right("/Interfaz/EscenaPrincipal.fxml",regresarBT,tweetearAP);
    }
}
