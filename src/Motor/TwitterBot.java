package Motor;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterBot {

    private Twitter twitter;

    public void inicializarBot() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey("y3rodATEKk9OopeZb3bJ49k7L");
        cb.setOAuthConsumerSecret("eCkLQgglSpvdD7nUiU6hoH2hoWYEWASAAMRWkfuTyqnhUxLfr0");
        cb.setOAuthAccessToken("1160958881268473856-azR9gn8ajjf1EqlURcy6xjo4LxmjkJ");
        cb.setOAuthAccessTokenSecret("UemukyzySFXAi9jBK9t3TO91tYZfT7cVsxSsPFGnu1i3n");
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
    }

    class Messages{
        public String PublicarTweet(String Tweet) throws TwitterException {
            Status status = twitter.updateStatus(Tweet);
            return status.getText();
        }
        public void EnviarMD(String arroba, String texto) throws TwitterException {
            DirectMessage MD = twitter.sendDirectMessage(arroba, texto);
            System.out.println("Se ha enviado un mensaje directo a @"+arroba+" El mensaje fue: "+MD.getText());
        }
    }
    class Feed{
        public String ObtenerMensajes() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        public void Like() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        public void Retweet() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }
}
