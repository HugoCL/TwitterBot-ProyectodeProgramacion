package TestZone;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class motorBot {

    private Twitter twitter;

    public void inicializarBot(){
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey("y3rodATEKk9OopeZb3bJ49k7L");
        cb.setOAuthConsumerSecret("eCkLQgglSpvdD7nUiU6hoH2hoWYEWASAAMRWkfuTyqnhUxLfr0");
        cb.setOAuthAccessToken("1160958881268473856-azR9gn8ajjf1EqlURcy6xjo4LxmjkJ");
        cb.setOAuthAccessTokenSecret("UemukyzySFXAi9jBK9t3TO91tYZfT7cVsxSsPFGnu1i3n");
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
    }

    public String publicarTwitter(String tweet) throws TwitterException {
        Status status = twitter.updateStatus(tweet);
        return status.getText();
    }
}
