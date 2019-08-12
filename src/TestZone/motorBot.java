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
        cb.setOAuthConsumerKey("C42lxF6YuqqwGqp0pDF6HswTs");
        cb.setOAuthConsumerSecret("ePgINxmWcmT4nIjdDdCd5DpwahSqUOHmhkONbGFrUt53Hb2tRA");
        cb.setOAuthAccessToken("3090225852-lxL0g5SGAD2XSHOyu1CJXhc4Yg78comkAaPYZjL");
        cb.setOAuthAccessTokenSecret("V1u62wAEOucjDYGIM3YxWWRTFyjEJSMo3vZNbO0kNjQHN");
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
    }

    public String publicarTwitter(String tweet) throws TwitterException {
        Status status = twitter.updateStatus(tweet);
        return status.getText();
    }
}
