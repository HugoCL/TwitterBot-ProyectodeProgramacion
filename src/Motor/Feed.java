package Motor;

import twitter4j.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Feed implements Serializable {
    private Twitter twitter = TwitterBot.getInstance().getBOT().getTwitter();
    private ArrayList<Tweet> backupTweets = new ArrayList<>();
    private ArrayList<Tweet> tweets = new ArrayList<>();
    /***
     * Permite la obtención de los tweets del timeline de la cuenta ingresada
     * @return Lista con los tweets.
     */
    public ArrayList<Tweet> ObtenerTweets() throws IOException {
        int pageno = 1;
        while (true) {
            try {
                int size = tweets.size();
                Paging page = new Paging(pageno++, 50);
                if (pageno == 2) tweets.clear();

                for (Status status : twitter.getHomeTimeline(page)) {
                    String[] imagenesTweet = new String[4];
                    int i=0;
                    if (status.getMediaEntities() != null){
                        for (MediaEntity urlImagen : status.getMediaEntities()){
                            imagenesTweet[i++] = urlImagen.getMediaURL();
                        }
                    }else imagenesTweet = new String[0];

                    tweets.add(new Tweet(status.getText(), status.getId(), status.getUser().getName(), status.getUser().getScreenName(),
                            status.getUser().getMiniProfileImageURL(),imagenesTweet));
                    if(tweets.size() >= 100) break;
                }
                if (tweets.size() == size || tweets.size() >= 100){
                    break;
                }
            } catch (TwitterException e) {
                if (e.getErrorCode() == 88) {
                    return backupTweets;
                }
            }
        }
        if (!backupTweets.isEmpty()) {
            backupTweets.clear();
        }
        backupTweets = new ArrayList<>(tweets);
        AdminBackup.getInstance().serializar(backupTweets);
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
     */
    public String like(long like){
        try {
            if(!twitter.showStatus(like).isFavorited()) {
                twitter.createFavorite(like);
                return "Like exitoso";
            } else{
                twitter.destroyFavorite(like);
                return "Like quitado";
            }
        } catch (TwitterException e) {
            return "ERROR: No se encontro Tweet";
        }
    }

    /***
     * Permite retweetear todos los tweets en el timline de la cuenta ingresada
     * @param tweet contiene el tweet a dar retweet
     */
    public String retweet(long tweet){
        try {
            if (!twitter.showStatus(tweet).isRetweetedByMe()) {
                twitter.retweetStatus(tweet);
                return "Retweet exitoso";
            } else{
                twitter.unRetweetStatus(tweet);
                return "Retweet quitado";
            }
        } catch (TwitterException e) {
            return "ERROR: No se encontro Tweet";
        }
    }

    public Status showStatus(long id) {
        try {
            return twitter.showStatus(id);
        } catch (TwitterException e) {
            return null;
        }
    }
}
