package Motor;

import twitter4j.HashtagEntity;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.ArrayList;

public class HashtagActions {

    private static Twitter twitter = TwitterBot.getInstance().getBOT().getTwitter();


    public void HashTagActions(ArrayList<Tweet> tweets) {
        Usuario user = new Usuario();
        Feed feed = new Feed();
        try {
            for (Tweet tweet: tweets)
                for (HashtagEntity hashtagEntity : twitter.showStatus(tweet.getId()).getHashtagEntities())
                    if(hashtagEntity.getText().equalsIgnoreCase("seguir")){
                        Status statusFollow = twitter.showStatus(tweet.getId());
                        user.follow(statusFollow.getUser().getScreenName());
                    }
                    else if (hashtagEntity.getText().equalsIgnoreCase("darlike")){
                        Status statusLike = twitter.showStatus(tweet.getId());
                        feed.like(statusLike.getId());
                    }
                    else if (hashtagEntity.getText().equalsIgnoreCase("retwittear")){
                        Status statusRetweet = twitter.showStatus(tweet.getId());
                        feed.retweet(statusRetweet.getId());
                    }
        } catch (TwitterException e) {
            System.out.println(e.getErrorMessage());
        }
    }
}
