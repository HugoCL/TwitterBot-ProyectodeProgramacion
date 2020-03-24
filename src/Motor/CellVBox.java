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
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class CellVBox extends Thread{
    private static Twitter twitter = TwitterBot.getInstance().getBOT().getTwitter();
    private static Feed feed = new Feed();
    private static Pattern patron = Pattern.compile("^[#|@][^#@]+");

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
                EscenaPrincipalController.getVbox().getChildren().remove(pane);
        });


        Label name = new Label(item.getNombre());
        name.getStyleClass().add("label");

        GridPane cuerpo = new GridPane();
        if (item.getImagenes()[0] != null){
            cuerpo.setPrefSize(scroll.getPrefWidth()-35, 150);
        }else cuerpo.setPrefSize(scroll.getPrefWidth()-35, 70);

        TextFlow mensaje = new TextFlow();
        destacarHashtag(item,mensaje);
        mensaje.setPrefSize(scroll.getPrefWidth()-35, 70);
        mensaje.getStyleClass().add("text");

        HBox visualizador = new HBox();
        obtenerImagenes(item,visualizador);
        cuerpo.add(mensaje,0,0);
        cuerpo.add(visualizador,0,1);

        Circle imagen = new Circle(15);
        imagen.setFill(new ImagePattern(new Image(item.getImagen())));
        imagen.getStyleClass().add("imagen");

        Label separador = new Label();
        Label separador2 = new Label();

        pane.add(imagen, 0, 0);
        pane.add(name, 1, 0);
        pane.add(separador, 0, 1, 3, 1);
        pane.add(cuerpo, 0, 2, 3, 1);
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
            if (patron.matcher(token.getText()).matches()){
                if (palabra.charAt(palabra.length()-1) == ':'){
                    palabra = palabra.substring(0, palabra.length()-1);
                }
                if (palabra.charAt(0) == '#' || (palabra.charAt(0) == '@' && Usuario.getUser(palabra.substring(1)) != null))
                    token.setFill(Color.web("#3e85c3"));
            }
            else
                token.setFill(Color.BLACK);
            mensaje.getChildren().add(token);
        }
    }
    private static void obtenerImagenes(Tweet item, HBox visualizador){
        if (item.getImagenes() != null){
            ImageView img1 = new ImageView();
            ImageView img2 = new ImageView();
            ImageView img3 = new ImageView();
            ImageView img4 = new ImageView();
            if (item.getImagenes()[0] != null){
                Image image1 = new Image(item.getImagenes()[0], 80,80,false, true);
                img1.setImage(image1);
            }
            if (item.getImagenes()[1] != null){
                Image image2 = new Image(item.getImagenes()[1], 80,80,false, true);
                img2.setImage(image2);
            }
            if (item.getImagenes()[2] != null){
                Image image3 = new Image(item.getImagenes()[2], 80,80,false, true);
                img3.setImage(image3);
            }
            if (item.getImagenes()[3] != null){
                Image image4 = new Image(item.getImagenes()[3], 80,80,false, true);
                img4.setImage(image4);
            }
            visualizador.getChildren().add(img1);
            visualizador.getChildren().add(img2);
            visualizador.getChildren().add(img3);
            visualizador.getChildren().add(img4);
        }
    }
}
