package Motor;

import Interfaz.EscenaPrincipalController;
import Transiciones.Dialog;
import com.jfoenix.controls.JFXButton;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.StringTokenizer;

public class CellVBox extends Thread{
    private static Twitter twitter = TwitterBot.getInstance().getBOT().getTwitter();
    private static Feed feed = new Feed();

    public static GridPane crearGridPane(Tweet item, AnchorPane mainAP, ScrollPane scroll) {
        GridPane pane = new GridPane();
        pane.setPrefWidth(scroll.getPrefWidth()-16);
        pane.setAlignment(Pos.CENTER);
        JFXButton like_BT = new JFXButton();
        like_BT.setGraphic(new ImageView(new Image("Imagenes/heart.png", 20,20,false,true)));
        like_BT.getStyleClass().add("GrayHeart-buttton");
        like_BT.setOnAction(event -> {
            String respuesta = feed.like(item.getId());
            if(respuesta.equals("Like exitoso")){
                like_BT.getStyleClass().set(2, "RedHeart-buttton");
                Dialog.getInstance().info(like_BT,respuesta,mainAP);
            }
            else{
                like_BT.getStyleClass().set(2, "GrayHeart-buttton");
                Dialog.getInstance().info(like_BT,respuesta,mainAP);
            }
        });

        JFXButton retweet_BT = new JFXButton();
        retweet_BT.setGraphic(new ImageView(new Image("Imagenes/retweet.png", 20,20,false, true)));
        retweet_BT.getStyleClass().add("RetweetGray-button");

        retweet_BT.setOnAction(event -> {
            String respuesta = feed.retweet(item.getId());
            if (respuesta.equals("Retweet exitoso")){
                retweet_BT.getStyleClass().set(2, "RetweetGreen-button");
                Dialog.getInstance().info(retweet_BT,respuesta,mainAP);
            }
            else{
                retweet_BT.getStyleClass().set(2, "RetweetGray-button");
                Dialog.getInstance().info(retweet_BT,respuesta,mainAP);
            }
        });

        revisarLikeRetweet(item.getId(), retweet_BT, like_BT);

        JFXButton delete = new JFXButton();
        delete.setGraphic(new ImageView(new Image("Imagenes/delete.png", 20,20,false, true)));
        try {
            if (!item.getScreenName().equals(twitter.getScreenName()))
                delete.setDisable(true);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        delete.setOnAction(event -> {
            String respuesta = new Messages().deleteTweet(item.getId());
            Dialog.getInstance().info(delete, respuesta, mainAP);
            if (respuesta.equals("Mensaje Eliminado"))
                EscenaPrincipalController.getVbox().getChildren().remove(pane);
        });


        Label name = new Label(item.getNombre());
        name.getStyleClass().add("label");

        TextFlow mensaje = new TextFlow();
        destacarHashtag(item,mensaje);
        //mensaje.setEditable(false);
        mensaje.setPrefSize(scroll.getPrefWidth()-35, 70);
        //mensaje.setWrapText(true);
        mensaje.getStyleClass().add("text");

        Circle imagen = new Circle(15);
        imagen.setFill(new ImagePattern(new Image(item.getImagen())));
        imagen.getStyleClass().add("imagen");

        Label separador = new Label();
        Label separador2 = new Label();

        pane.add(imagen, 0, 0);
        pane.add(name, 1, 0);
        pane.add(separador, 0, 1, 3, 1);
        pane.add(mensaje, 0, 2, 3, 1);
        pane.add(separador2, 0, 3, 3, 1);
        pane.add(like_BT, 0, 4);
        pane.add(retweet_BT, 1, 4);
        pane.add(delete, 2, 4);
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

    private static void destacarHashtag(Tweet item, TextFlow mensaje){
        StringTokenizer Tok = new StringTokenizer (item.getMensaje());
        while (Tok.hasMoreElements()) {
            Text token = new Text();
            String palabra = Tok.nextToken();
            token.setText(palabra+" ");
            if (palabra.charAt(0) == '#' || palabra.charAt(0) == '@'){
                token.setFill(Color.web("#3e85c3"));
            }
            else{
                token.setFill(Color.BLACK);
            }
            mensaje.getChildren().add(token);
        }
    }
}
