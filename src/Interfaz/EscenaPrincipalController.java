package Interfaz;

import Motor.*;
import Transiciones.Dialog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import twitter4j.TwitterException;

import java.io.IOException;
import java.util.ArrayList;

public class EscenaPrincipalController {

    @FXML private AnchorPane mainAP;
    @FXML private AnchorPane secondAP;

    @FXML private JFXButton tweetearBT;
    @FXML private JFXButton retweetBT;
    @FXML private JFXButton followBT;
    @FXML private JFXButton likeBT;
    @FXML private JFXButton directBT;
    @FXML private JFXButton timelineBT;
    @FXML private JFXButton cerrar_sesionBT;

    @FXML private Text usernameTX;

    //TableView
    @FXML private TableColumn<Tweet, String> usuarioCL;
    @FXML private TableColumn<Tweet, String> tweetCL;
    @FXML private TableView<Tweet> listaTweets_TV;

    @FXML private JFXListView<Tweet> listaTweets_LV;

    private ObservableList<Tweet> listview = FXCollections.observableArrayList();


    private ArrayList<Tweet> tweetsHash = new ArrayList<>();

    //Classes
    private Feed feed = new Feed();

    public void initialize() throws TwitterException {
        //Obtener nombre de usuario
        usernameTX.setText(new Usuario().getNombreUsuario());
        //Botones desactivados
        secondAP.setVisible(false);
    }

    @FXML public void tweetear() throws IOException {
        Transiciones.Slide.getInstance().left("/Interfaz/Twittear.fxml",tweetearBT,mainAP);
    }

    @FXML public void timeline(){

        listaTweets_LV.getStyleClass().add("list-view");
        listaTweets_LV.setItems(listview);
        listaTweets_LV.setCellFactory(param -> new CustomCell());

        ArrayList<Tweet> listaTweets = feed.ObtenerTweets();

        //tweetsHash = Cadenas.BuscarTweetsHash(listaTweets);

        if (listaTweets != null){
            if (listaTweets.size() != 0){
                for (Tweet tweet: listaTweets) {
                    Tweet newTweet = new Tweet(tweet.getMensaje(),tweet.getId(),tweet.getNombre(),tweet.getImagen());
                    listview.add(newTweet);
                }
                botonesMain(true);
                secondAP.setVisible(true);
            }
            else{
                Dialog.getInstance().info(timelineBT,"Refresh muy frecuente, intente más tarde",
                        "OK",mainAP);
            }
        }else{
            Dialog.getInstance().info(timelineBT,"No hay últimos mensajes,\nIntentelo más tarde",
                    "OK",mainAP);
        }
    }

    @FXML public void cerrarTimeline(){
        botonesMain(false);
        secondAP.setVisible(false);
    }

    @FXML public void retweet(){
        String respuesta;
        Tweet selecTweet = listaTweets_TV.getSelectionModel().getSelectedItem();
        if (selecTweet != null){
            respuesta = new Feed().Retweet(selecTweet.getId());
            Dialog.getInstance().info(retweetBT,respuesta,"OK",mainAP);
        }else {
            Dialog.getInstance().info(retweetBT,"Seleccione algún tweet","OK",mainAP);
        }
    }

    @FXML public void like(){
        String respuesta;
        Tweet selecTweet = listaTweets_TV.getSelectionModel().getSelectedItem();
        if (selecTweet != null){
            respuesta = new Feed().Like(selecTweet.getId());
            Dialog.getInstance().info(retweetBT,respuesta,"OK",mainAP);
        }else {
            Dialog.getInstance().info(likeBT,"Seleccione algún tweet","OK",mainAP);
        }
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