package Interfaz;
import Motor.TwitterBot;
import Transiciones.Dialog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class TwittearController {

    private TwitterBot bot;

    @FXML private JFXButton publicar_tweetBT;
    @FXML private JFXButton regresarBT;
    @FXML private JFXButton addFileBT;
    @FXML private JFXTextArea tweet_TA;
    @FXML private Label caracteres_LB;
    @FXML private ImageView imagenTweet;
    @FXML private Label archivoLB;
    @FXML private Label nameFile_LB;

    @FXML private AnchorPane tweetearAP;

    private FileChooser fileChooser = new FileChooser();
    private File selectedFile;

    public void initialize(){
        //Inicializar bot
        bot = TwitterBot.getInstance().getBOT();

        //Inicio de Ventana
        publicar_tweetBT.setDisable(true);
        nameFile_LB.setVisible(false);
        publicar_tweetBT.setDisable(true);
        caracteres_LB.setText("0/280");

        //Tweet con Imagenes
        fileChooser.setInitialDirectory(new File("src"));
        fileChooser.setTitle("Buscar Archivo");

    }

    @FXML private void agregarArchivo(){
        selectedFile = fileChooser.showOpenDialog(tweetearAP.getScene().getWindow());
        try {
            /**
             * Si el nameFile_LB, es el label donde muestras la ruta, si es seleccionado un video pones
             * archivoLB.setDisable(true) , pero si es seleccionada una imagen o gif, hay que poner
             * nameFile_LB.setDisable(true). Ahí cacha las expresiones regulares para hacerlo, terminaré de ver
             * lo que está haciendo el Hugo.
             */
            nameFile_LB.setText(selectedFile.getAbsolutePath());
            Image image = new Image("file:"+selectedFile.getAbsolutePath());
            imagenTweet.setImage(image);
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
        String respuesta;
        String tweet = tweet_TA.getText();
        TwitterBot.Messages mensajes = bot.new Messages();
        if (nameFile_LB.getText() == ""){
            respuesta = mensajes.PublicarTweet(tweet);
        }
        else{
            respuesta = mensajes.PublicarTweetImagen(tweet, selectedFile);
        }
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
