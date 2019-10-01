package Motor;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.ArrayList;

public class Feed {
    private Twitter twitter = TwitterBot.getInstance().getBOT().getTwitter();
    private ArrayList<Tweet> backupTweets = new ArrayList<>();
    private ArrayList<Tweet> tweets = new ArrayList<>();
    /***
     * Permite la obtención de los tweets del timeline de la cuenta ingresada
     * @return Lista con los tweets.
     */
    public ArrayList<Tweet> ObtenerTweets() {
        int pageno = 1;
        boolean exito = false;
        while (true) {
            try {
                int size = tweets.size();
                Paging page = new Paging(pageno++, 100);
                if (pageno == 2) tweets.clear();

                for (Status status : twitter.getHomeTimeline(page)) {
                    tweets.add(new Tweet(status.getText(), status.getId(), status.getUser().getName(),
                            status.getUser().getMiniProfileImageURL()));
                }
                if (tweets.size() == size){
                    exito = true;
                    break;
                }
            } catch (TwitterException e) {
                if (e.getErrorCode() == 88) {
                    return backupTweets;
                }
            }
        }
        if (!backupTweets.isEmpty() && exito) {
            backupTweets.clear();
        }
        backupTweets = new ArrayList<>(tweets);
        return tweets;
    }

    /***
     * @return Deuelve lista con los tweets del timeline.
     */
    public ArrayList<Tweet> getTweets() {
        return tweets;
    }

    /***
     * Permite agregar a favoritos todos los tweets del timeline de la cuenta asociada
     * @param like contiene el tweet a dar like
     * @throws TwitterException
     */
    public String Like(long like){
        try {
            if(!twitter.showStatus(like).isFavorited()) {
                twitter.createFavorite(like);
                return "Like exitoso";
            } else{
                twitter.destroyFavorite(like);
                return "Tweet ya likeado";
            }
        } catch (TwitterException e) {
            return "ERROR: No se encontro Tweet";
        }
    }

    /***
     * Permite retweetear todos los tweets en el timline de la cuenta ingresada
     * @param tweet contiene el tweet a dar retweet
     * @throws TwitterException
     */
    public String Retweet(long tweet){
        try {
            if (!twitter.showStatus(tweet).isRetweetedByMe()) {
                twitter.retweetStatus(tweet);
                return "Retweet exitoso";
            } else{
                twitter.unRetweetStatus(tweet);
                return "Tweet ya retweeteado";
            }
        } catch (TwitterException e) {
            return "ERROR: No se encontro Tweet";
        }
    }
}
