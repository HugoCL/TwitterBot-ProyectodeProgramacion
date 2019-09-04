package Interfaz;

import Motor.Tweet;
import Motor.TwitterBot;
import Motor.adminSesion;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    @FXML private ObservableList<Tweet> tweets;

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

    @FXML public void timeline(){
        secondAP.setVisible(true);
        //Inicializar la tableView
        usuarioCL.setCellValueFactory(new PropertyValueFactory<Tweet,String>("nombre"));
        tweetCL.setCellValueFactory(new PropertyValueFactory<Tweet,String>("mensaje"));
        tweets = FXCollections.observableArrayList();
        tweets.clear();
        listaTweets_TV.setItems(tweets);
        ArrayList<Tweet> listaTweets = feed.ObtenerTweets();
        for (Tweet tweet: listaTweets) {
            Tweet newTweet = new Tweet(tweet.getMensaje(),tweet.getId(),tweet.getNombre());
            System.out.println(newTweet.getId());
            tweets.add(newTweet);
        }
    }
    @FXML public void retweet(){
        Tweet selecTweet = listaTweets_TV.getSelectionModel().getSelectedItem();
        if (selecTweet != null){
            TwitterBot.getInstance().getBOT().new Feed().Retweet(selecTweet.getId());
        }else System.out.println("No ha seleccionado ningun tweet.");

    }
    @FXML public void like(){
        Tweet selecTweet = listaTweets_TV.getSelectionModel().getSelectedItem();
        if (selecTweet != null){
            TwitterBot.getInstance().getBOT().new Feed().Like(selecTweet.getId());
        }else System.out.println("No ha seleccionado ningun tweet.");
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
}