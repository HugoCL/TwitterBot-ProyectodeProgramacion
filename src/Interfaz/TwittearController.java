package Interfaz;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwittearController {

    private Twitter twitter;

    @FXML private Button publicar_tweetBT;
    @FXML private TextArea tweet_TA;
    @FXML private Label caracteres_LB;

    public void initialize(){
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey("C42lxF6YuqqwGqp0pDF6HswTs");
        cb.setOAuthConsumerSecret("ePgINxmWcmT4nIjdDdCd5DpwahSqUOHmhkONbGFrUt53Hb2tRA");
        cb.setOAuthAccessToken("3090225852-lxL0g5SGAD2XSHOyu1CJXhc4Yg78comkAaPYZjL");
        cb.setOAuthAccessTokenSecret("V1u62wAEOucjDYGIM3YxWWRTFyjEJSMo3vZNbO0kNjQHN");
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();

        //Inicio de Ventana
        publicar_tweetBT.setDisable(true);

        //Caracteres
        KeyFrame frame = new KeyFrame(Duration.millis(100), e -> Caracteres());
        Timeline timeline = new Timeline(frame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    private void Caracteres() {
        if (!tweet_TA.getText().isEmpty()) {
            if (tweet_TA.getText().length() >= 280){
                caracteres_LB.setTextFill(Color.web("#ff0000"));
                publicar_tweetBT.setDisable(true);
            }
            else {
                caracteres_LB.setTextFill(Color.web("#000000"));
                publicar_tweetBT.setDisable(false);
            }
            caracteres_LB.setText(tweet_TA.getText().length()+"/280");
        }
        else    caracteres_LB.setText("0/280");

    }
    @FXML public String publicarTwitter() throws TwitterException {
        String tweet = tweet_TA.getText();
        Status status = twitter.updateStatus(tweet);
        System.out.println(tweet);
        return status.getText();
    }
}
