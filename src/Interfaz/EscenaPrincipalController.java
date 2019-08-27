package Interfaz;

import Motor.Tweet;
import Motor.TwitterBot;
import Motor.adminSesion;
import com.jfoenix.controls.JFXButton;
import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class EscenaPrincipalController {

    @FXML private AnchorPane mainAP;
    @FXML private AnchorPane secondAP;

    @FXML private JFXButton tweetearBT;
    @FXML private JFXButton retweetBT;
    @FXML private JFXButton followBT;
    @FXML private JFXButton likeBT;
    @FXML private JFXButton directBT;
    @FXML private JFXButton cerrar_sesionBT;

    //TableView
    @FXML private TableColumn<Tweet, String> usuarioCL;
    @FXML private TableColumn<Tweet, String> tweetCL;
    @FXML private TableView<Tweet> listaTweets_TV;
    @FXML private ObservableList<Tweet> tweets;
    @FXML private int posicionTweetEnTabla;

    //Inner Classes
    TwitterBot.Feed feed = TwitterBot.getInstance().getBOT().new Feed();

    public void initialize(){
        //Botones desactivados
        listaTweets_TV.setVisible(false);
        retweetBT.setVisible(false);
        likeBT.setVisible(false);
    }

    @FXML public void tweetear() throws IOException {
        System.out.println("Cargando ventana para twittear...");

        Parent root = FXMLLoader.load(getClass().getResource("/Interfaz/Twittear.fxml"));
        Scene scene = tweetearBT.getScene();

        root.translateXProperty().set(scene.getWidth());
        StackPane parentContainer = (StackPane) scene.getRoot();
        parentContainer.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateXProperty(),0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.5),kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(event -> {
            parentContainer.getChildren().remove(mainAP);
        });
        timeline.play();
    }

    @FXML public void timeline(){
        listaTweets_TV.setVisible(true);
        retweetBT.setVisible(true);
        likeBT.setVisible(true);
        //Inicializar la tableView
        usuarioCL.setCellValueFactory(new PropertyValueFactory<Tweet,String>("nombre"));
        tweetCL.setCellValueFactory(new PropertyValueFactory<Tweet,String>("mensaje"));
        tweets = FXCollections.observableArrayList();
        listaTweets_TV.setItems(tweets);
        ArrayList<Tweet> listaTweets = feed.ObtenerTweets();
        for (Tweet tweet: listaTweets) {
            Tweet newTweet = new Tweet(tweet.getMensaje(),tweet.getId(),tweet.getNombre());
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

        Parent root = FXMLLoader.load(getClass().getResource("/Interfaz/Follow.fxml"));
        Scene scene = tweetearBT.getScene();

        root.translateXProperty().set(scene.getWidth());
        StackPane parentContainer = (StackPane) scene.getRoot();
        parentContainer.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateXProperty(),0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.5),kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(event -> {
            parentContainer.getChildren().remove(mainAP);
        });
        timeline.play();
    }
    @FXML public void directMessage() throws IOException {
        System.out.println("Cargando ventana para mensajes directos...");

        Parent root = FXMLLoader.load(getClass().getResource("/Interfaz/MensajeDirecto.fxml"));
        Scene scene = directBT.getScene();

        root.translateXProperty().set(scene.getWidth());
        StackPane parentContainer = (StackPane) scene.getRoot();
        parentContainer.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateXProperty(),0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.5),kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(event -> {
            parentContainer.getChildren().remove(mainAP);
        });
        timeline.play();
    }
    @FXML public void cerrarSesion() throws IOException {
        TwitterBot.getInstance().getBOT().isGuardado = false;
        adminSesion.getInstance().Serializar(TwitterBot.getInstance().getBOT());
        System.out.println("Cerrando sesión...");

        Scene scene = cerrar_sesionBT.getScene();

        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.seconds(0.5));
        StackPane rootPane = (StackPane) scene.getRoot();
        fadeTransition.setNode(rootPane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/Interfaz/InicioSesion.fxml"));
                    Scene scene1 = new Scene(root);
                    Stage stage = (Stage) rootPane.getScene().getWindow();
                    stage.setScene(scene1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        fadeTransition.play();
    }
}
