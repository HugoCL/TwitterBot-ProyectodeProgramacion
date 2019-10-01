package Interfaz;

import Motor.*;
import Transiciones.Dialog;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import twitter4j.TwitterException;

import java.io.IOException;
import java.util.ArrayList;

public class EscenaPrincipalController {

    @FXML private AnchorPane mainAP;
    @FXML private AnchorPane secondAP;

    @FXML private JFXButton tweetearBT;
    @FXML private JFXButton followBT;
    @FXML private JFXButton directBT;
    @FXML private JFXButton timelineBT;
    @FXML private JFXButton cerrar_sesionBT;

    @FXML private Text usernameTX;

    @FXML private VBox vbox = new VBox(10);
    @FXML private ScrollPane scroll = new ScrollPane();

    private ArrayList<Tweet> tweetsHash = new ArrayList<>();

    //Classes
    private Feed feed = new Feed();

    public void initialize() throws TwitterException {
        //Obtener nombre de usuario
        usernameTX.setText(new Usuario().getNombreUsuario());
        //Botones desactivados
        secondAP.setVisible(false);

        scroll.getStyleClass().add("text");
    }

    @FXML public void tweetear() throws IOException {
        Transiciones.Slide.getInstance().left("/Interfaz/Twittear.fxml",tweetearBT,mainAP);
    }

    @FXML public void timeline(){
        ArrayList<Tweet> listaTweets = feed.ObtenerTweets();
        System.out.println(listaTweets.size());
        //tweetsHash = Cadenas.BuscarTweetsHash(listaTweets);

        if (listaTweets != null){
            if (listaTweets.size() != 0){
                for (Tweet tweet: listaTweets) {
                    vbox.getChildren().add(CellVBox.crearGridPane(tweet, mainAP));
                }
                scroll.setContent(vbox);
                botonesMain(true);
                secondAP.setVisible(true);
            }
            else{
                Dialog.getInstance().info(timelineBT,"Refresh muy frecuente, intente más tarde",mainAP);
            }
        }else{
            Dialog.getInstance().info(timelineBT,"No hay últimos mensajes, Intentelo más tarde",mainAP);
        }
    }

    @FXML public void cerrarTimeline(){
        botonesMain(false);
        secondAP.setVisible(false);
    }

    @FXML public void follow() throws IOException {
        Transiciones.Slide.getInstance().left("/Interfaz/Follow.fxml", followBT, mainAP);
    }

    @FXML public void directMessage() throws IOException {
        Transiciones.Slide.getInstance().left("/Interfaz/MensajeDirecto.fxml", directBT, mainAP);
    }

    @FXML public void cerrarSesion() throws IOException {
        TwitterBot.getInstance().getBOT().setSesion(false);
        AdminSesion.getInstance().Serializar(TwitterBot.getInstance().getBOT());
        Transiciones.Fade.getInstance().out("/Interfaz/InicioSesion.fxml", cerrar_sesionBT);
    }

    private void botonesMain(boolean bool){
        tweetearBT.setDisable(bool);
        timelineBT.setDisable(bool);
        followBT.setDisable(bool);
        directBT.setDisable(bool);
    }

    @FXML public void cerrarPrograma(){
        System.out.println("Finalizando programa...");
        System.exit(0);
    }
}