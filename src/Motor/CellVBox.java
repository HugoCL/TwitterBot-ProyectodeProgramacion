package Motor;

import Transiciones.Dialog;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class CellVBox extends Thread{
    private static Twitter twitter = TwitterBot.getInstance().getBOT().getTwitter();
    private static Feed feed = new Feed();


    public static GridPane crearGridPane(Tweet item, AnchorPane mainAP, VBox vbox) {
        GridPane pane = new GridPane();

        JFXButton like_BT = new JFXButton();
        like_BT.setGraphic(new ImageView(new Image("Imagenes/heart.png", 20,20,false,true)));
        like_BT.getStyleClass().add("GrayHeart-buttton");
        like_BT.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String respuesta = feed.like(item.getId());
                if(respuesta.equals("Like exitoso")){
                    like_BT.getStyleClass().set(2, "RedHeart-buttton");
                    Dialog.getInstance().info(like_BT,respuesta,mainAP);
                }
                else{
                    like_BT.getStyleClass().set(2, "GrayHeart-buttton");
                    Dialog.getInstance().info(like_BT,respuesta,mainAP);
                }
            }
        });

        JFXButton retweet_BT = new JFXButton();
        retweet_BT.setGraphic(new ImageView(new Image("Imagenes/retweet.png", 20,20,false, true)));
        retweet_BT.getStyleClass().add("RetweetGray-button");

        retweet_BT.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String respuesta = feed.retweet(item.getId());
                if (respuesta.equals("Retweet exitoso")){
                    retweet_BT.getStyleClass().set(2, "RetweetGreen-button");
                    Dialog.getInstance().info(retweet_BT,respuesta,mainAP);
                }
                else{
                    retweet_BT.getStyleClass().set(2, "RetweetGray-button");
                    Dialog.getInstance().info(retweet_BT,respuesta,mainAP);
                }
            }
        });

        revisarLikeRetweet(item.getId(), retweet_BT, like_BT);

        JFXButton delete = new JFXButton("Borrar");
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String respuesta = new Messages().deleteTweet(item.getId());
                Dialog.getInstance().info(delete, respuesta, mainAP);
                if (respuesta.equals("Mensaje Eliminado"))
                    vbox.getChildren().remove(pane);
            }
        });

        Label name = new Label(item.getNombre());
        name.getStyleClass().add("label");

        TextArea mensaje = new TextArea(item.getMensaje());
        mensaje.setEditable(false);
        mensaje.setMinSize(325, 100);
        mensaje.setMaxSize(325, 100);
        mensaje.setWrapText(true);
        mensaje.getStyleClass().add("text");

        ImageView imagen = new ImageView(new Image(item.getImagen()));

        pane.add(imagen, 0, 0);
        pane.add(name, 1, 0);
        pane.add(mensaje, 0, 1, 3, 1);
        pane.add(retweet_BT, 0, 2);
        pane.add(like_BT, 1, 2);
        pane.add(delete, 2, 2);
        GridPane.setHalignment(delete, HPos.RIGHT);

        pane.getStyleClass().add("grid");
        return pane;
    }

    private static void revisarLikeRetweet(long id, JFXButton retweet_BT, JFXButton like_BT) {
        try {
            if(twitter.showStatus(id).isRetweetedByMe())
                retweet_BT.getStyleClass().set(2, "RetweetGreen-button");
            if(twitter.showStatus(id).isFavorited())
                like_BT.getStyleClass().set(2, "RedHeart-buttton");
        } catch (TwitterException e) {
            System.out.println("No se encuentra tweet");
        }
    }
}
