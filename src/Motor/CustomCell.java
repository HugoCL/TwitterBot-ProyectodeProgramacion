package Motor;
import Transiciones.Dialog;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class CustomCell extends ListCell<Tweet> {
    private JFXButton like_BT;
    private JFXButton retweet_BT;
    private Label name;
    private Label numero;
    private TextArea mensaje;
    private ImageView imagen;
    private GridPane pane ;
    private static Twitter twitter = TwitterBot.getInstance().getBOT().getTwitter();
    private long idTweet;

    public CustomCell(AnchorPane mainAP) {
        super();
        Feed feed = new Feed();
        like_BT = new JFXButton();
        like_BT.setGraphic(new ImageView(new Image("Imagenes/heart.png", 20,20,false,true)));
        like_BT.getStyleClass().add("GrayHeart-buttton");
        like_BT.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(feed.Like(idTweet).equals("Like exitoso")){
                    like_BT.getStyleClass().set(2, "RedHeart-buttton");
                    Dialog.getInstance().info(like_BT,"Like exitoso","OK",mainAP);
                }
                else{
                    like_BT.getStyleClass().set(2, "GrayHeart-buttton");
                    Dialog.getInstance().info(like_BT,"Like quitado","OK",mainAP);
                }
            }
        });

        retweet_BT = new JFXButton();
        retweet_BT.getStyleClass().add("RetweetGray-button");
        retweet_BT.setGraphic(new ImageView(new Image("Imagenes/retweet.png", 20,20,false, true)));

        retweet_BT.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (feed.Retweet(idTweet).equals("Retweet exitoso")){
                    retweet_BT.getStyleClass().set(2, "RetweetGreen-button");
                    Dialog.getInstance().info(like_BT,"Retweet exitoso","OK",mainAP);
                }
                else{
                    retweet_BT.getStyleClass().set(2, "RetweetGray-button");
                    Dialog.getInstance().info(like_BT,"Retweet quitado","OK",mainAP);
                }
            }
        });

        name = new Label();
        name.getStyleClass().add("label");

        numero = new Label("1");
        numero.setMaxSize(15, 15);
        numero.setMinSize(15, 15);

        mensaje = new TextArea();
        mensaje.setEditable(false);
        mensaje.setMaxSize(300, 100);
        mensaje.setWrapText(true);
        mensaje.getStyleClass().add("text");

        imagen = new ImageView();

        pane = new GridPane();


        pane.add(imagen, 0, 0);
        pane.add(name, 1, 0);
        pane.add(mensaje, 0, 1, 2, 1);
        pane.add(new HBox(1, retweet_BT, numero), 0, 2);
        pane.add(new HBox(1, like_BT, numero), 1, 2);
        setText(null);
    }

    @Override
    public void updateItem(Tweet item, boolean empty) {
        super.updateItem(item, empty);
        setEditable(false);
        if (item != null) {
            idTweet = item.getId();
            name.setText(item.getNombre());
            imagen.setImage(new Image(item.getImagen()));
            mensaje.setText(item.getMensaje());
            revisarLikeRetweet(idTweet);
            setGraphic(pane);
        } else {
            setGraphic(null);
        }
    }

    private void revisarLikeRetweet(long id) {
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