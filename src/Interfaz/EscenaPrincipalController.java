package Interfaz;

import Motor.Tweet;
import Motor.TwitterBot;
import Motor.adminSesion;
import Transiciones.Dialog;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import twitter4j.TwitterException;

import java.io.IOException;
import java.util.ArrayList;

public class EscenaPrincipalController {

    //Inicializar bot
    private TwitterBot bot = TwitterBot.getInstance().getBOT();

    @FXML private AnchorPane mainAP;
    @FXML private AnchorPane secondAP;

    @FXML private JFXButton tweetearBT;
    @FXML private JFXButton retweetBT;
    @FXML private JFXButton followBT;
    @FXML private JFXButton likeBT;
    @FXML private JFXButton directBT;
    @FXML private JFXButton timelineBT;
    @FXML private JFXButton cerrarTimelineBT;
    @FXML private JFXButton cerrar_sesionBT;

    @FXML private Text usernameTX;

    //TableView
    @FXML private TableColumn<Tweet, String> usuarioCL;
    @FXML private TableColumn<Tweet, String> tweetCL;
    @FXML private TableView<Tweet> listaTweets_TV;

    //Inner Classes
    TwitterBot.Feed feed = TwitterBot.getInstance().getBOT().new Feed();

    public void initialize() throws TwitterException {
        //Obtener nombre de usuario
        usernameTX.setText(TwitterBot.getInstance().getBOT().new Usuario().getNombreUsuario());
        //Botones desactivados
        secondAP.setVisible(false);
    }

    @FXML public void tweetear() throws IOException {
        System.out.println("Cargando ventana para twittear...");
        Transiciones.Slide.getInstance().left("/Interfaz/Twittear.fxml",tweetearBT,mainAP);
    }

    @FXML public void timeline() throws IOException {
        //Inicializar la tableView
        usuarioCL.setCellValueFactory(new PropertyValueFactory<Tweet,String>("nombre"));
        tweetCL.setCellValueFactory(new PropertyValueFactory<Tweet,String>("mensaje"));
        ObservableList<Tweet> tweets = FXCollections.observableArrayList();
        listaTweets_TV.setItems(tweets);
        ArrayList<Tweet> listaTweets = feed.ObtenerTweets();
        if (listaTweets != null){
            if (listaTweets.size() != 0){
                bot.setUltimosTweets(listaTweets);
                adminSesion.getInstance().Serializar(bot);
                System.out.println("serializado");
                for (Tweet tweet: listaTweets) {
                    Tweet newTweet = new Tweet(tweet.getMensaje(),tweet.getId(),tweet.getNombre());
                    tweets.add(newTweet);
                }
                botonesMain(true);
                secondAP.setVisible(true);
            }
            else{
                Dialog.getInstance().error(timelineBT,"Refresh muy frecuente\nCargando últimos tweets",
                        "OK",mainAP);
                ArrayList<Tweet> lastTweets = bot.getUltimosTweets();
                if (lastTweets != null){
                    for (Tweet tweet: lastTweets) {
                        Tweet newTweet = new Tweet(tweet.getMensaje(),tweet.getId(),tweet.getNombre());
                        tweets.add(newTweet);
                    }
                    botonesMain(true);
                    secondAP.setVisible(true);
                }
                else{
                    Dialog.getInstance().error(timelineBT,"No hay últimos mensajes,\nIntentelo más tarde",
                            "OK",mainAP);
                }
            }
        }else{
            Dialog.getInstance().error(timelineBT,"No hay últimos mensajes,\nIntentelo más tarde",
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
            respuesta = TwitterBot.getInstance().getBOT().new Feed().Retweet(selecTweet.getId());
            Dialog.getInstance().error(retweetBT,respuesta,"OK",mainAP);
        }else {
            Dialog.getInstance().error(retweetBT,"Seleccione algún tweet","OK",mainAP);
        }
    }

    @FXML public void like(){
        String respuesta;
        Tweet selecTweet = listaTweets_TV.getSelectionModel().getSelectedItem();
        if (selecTweet != null){
            respuesta = TwitterBot.getInstance().getBOT().new Feed().Like(selecTweet.getId());
            Dialog.getInstance().error(retweetBT,respuesta,"OK",mainAP);
        }else {
            Dialog.getInstance().error(likeBT,"Seleccione algún tweet","OK",mainAP);
        }
    }

    @FXML public void follow() throws IOException {
        System.out.println("Cargando ventana para realizar follow...");
        Transiciones.Slide.getInstance().left("/Interfaz/Follow.fxml", followBT, mainAP);
    }

    @FXML public void directMessage() throws IOException {
        System.out.println("Cargando ventana para mensajes directos...");
        Transiciones.Slide.getInstance().left("/Interfaz/MensajeDirecto.fxml", directBT, mainAP);
    }

    @FXML public void cerrarSesion() throws IOException {
        TwitterBot.getInstance().getBOT().setSesion(false);
        adminSesion.getInstance().Serializar(TwitterBot.getInstance().getBOT());
        System.out.println("Cerrando sesión...");
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