package Motor;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterBot {

    private Twitter twitter;

    public void inicializarBot() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey("C42lxF6YuqqwGqp0pDF6HswTs");
        cb.setOAuthConsumerSecret("ePgINxmWcmT4nIjdDdCd5DpwahSqUOHmhkONbGFrUt53Hb2tRA");
        cb.setOAuthAccessToken("3090225852-lxL0g5SGAD2XSHOyu1CJXhc4Yg78comkAaPYZjL");
        cb.setOAuthAccessTokenSecret("V1u62wAEOucjDYGIM3YxWWRTFyjEJSMo3vZNbO0kNjQHN");
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
}
