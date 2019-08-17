package Motor;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;

public class TwitterBot {

    private Twitter twitter;

    private String consumerKey = "y3rodATEKk9OopeZb3bJ49k7L",
            consumerSecret = "eCkLQgglSpvdD7nUiU6hoH2hoWYEWASAAMRWkfuTyqnhUxLfr0",
            twitterAccessToken = "1160958881268473856-azR9gn8ajjf1EqlURcy6xjo4LxmjkJ",
            twitterAccessTokenSecret = "UemukyzySFXAi9jBK9t3TO91tYZfT7cVsxSsPFGnu1i3n",
            Tweet_ID = "1161699831909429248";

    public void inicializarBot() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey(consumerKey);
        cb.setOAuthConsumerSecret(consumerSecret);

        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();

        AccessToken acceso = new AccessToken(twitterAccessToken, twitterAccessTokenSecret);

        twitter.setOAuthAccessToken(acceso);

        ArrayList<Status> statuses = new ArrayList<>();
        int pageno = 1;

        while(true) {
            try {
                System.out.println("getting tweets");
                int size = statuses.size(); // actual tweets count we got
                Paging page = new Paging(pageno, 200);
                statuses.addAll(twitter.getUserTimeline("Ramos Overflow - BOT", page));
                System.out.println("total got : " + statuses.size());

                if (statuses.size() == size) {
                    break;
                } // we did not get new tweets so we have done the job
                pageno++;

            } catch (
                    TwitterException e) {
                System.err.print("Failed to search tweets: " + e.getMessage());
            }
        }

        try {
            for (Status t: statuses) {
                System.out.println(t.getUser().getName() + ": " + t.getText()+ " " + t.getId());
                twitter.retweetStatus(t.getId());
            }
        } catch (TwitterException e) {
            System.err.print("Failed to search tweets: " + e.getMessage());
        }
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
