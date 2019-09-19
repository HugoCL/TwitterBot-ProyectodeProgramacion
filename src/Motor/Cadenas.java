package Motor;

import twitter4j.HashtagEntity;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Cadenas {
    private static Twitter twitter = TwitterBot.getInstance().getBOT().getTwitter();

    public static ArrayList<Tweet> BuscarTweetsHash (ArrayList<Tweet> tweets) {
        ArrayList<Tweet> aux = new ArrayList<>();
        Pattern patron = Pattern.compile("(.*\\s#.+)|(^#.+)");
        for (Tweet tweet: tweets)
            if (patron.matcher(tweet.getMensaje()).find())
                aux.add(tweet);
        if (!aux.isEmpty())
            ContestarHashTag(aux);
        return aux;
    }

    private static void ContestarHashTag(ArrayList<Tweet> tweets) {
        try {
            for (Tweet tweet: tweets)
                for (HashtagEntity hashtagEntity : twitter.showStatus(tweet.getId()).getHashtagEntities())
                    System.out.println(hashtagEntity.getText());
        } catch (TwitterException e) {
            System.out.println(e.getErrorMessage());
        }
    }
}
