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

        /***
         * Permite la obtención de los tweets del timeline de la cuenta ingresada
         * @return Lista con los tweets.
         */
        public ArrayList<Status> ObtenerMensajes()  {
            ArrayList<Status> statuses = new ArrayList<>();
            System.out.println("Obteniendo tweets");
            for(int pageno = 1; true; pageno++) {
                try {
                    int size = statuses.size(); // actual tweets count we got
                    Paging page = new Paging(pageno, 200);
                    statuses.addAll(twitter.getHomeTimeline(page));

                    if (statuses.size() == size) {
                        System.out.println("total obtenido: " + statuses.size());
                        break;
                    }
                } catch (TwitterException e) {
                    System.err.print("Failed to search tweets: " + e.getMessage());
                }
                try {
                    Thread.sleep(1000);
                }catch(InterruptedException e) {
                    System.err.println(e.getMessage());
                }
            }
            return statuses;
        }
        public void Like() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /***
         * Permite retweetear los tweets en el timline de la cuenta ingresada
         * @throws TwitterException
         */
        public void Retweet() throws TwitterException {
            ArrayList<Status> statuses = ObtenerMensajes();

            for (Status t: statuses) {
                System.out.println(t.getUser().getName()+": "+t.getText());
                if (!t.isRetweeted()) {
                    twitter.retweetStatus(t.getId());
                }
            }
        }
    }
}
