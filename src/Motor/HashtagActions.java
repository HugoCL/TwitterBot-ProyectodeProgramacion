package Motor;

import twitter4j.*;

import java.util.ArrayList;

public class HashtagActions {

    private static Twitter twitter = TwitterBot.getInstance().getBOT().getTwitter();


    public void HashTagActions(ArrayList<Tweet> tweets) {
        Feed feed = new Feed();
        try {
            for (Tweet tweet: tweets)
                for (HashtagEntity hashtagEntity : twitter.showStatus(tweet.getId()).getHashtagEntities())
                    if(hashtagEntity.getText().equalsIgnoreCase("seguir")){
                        Status statusFollow = twitter.showStatus(tweet.getId());
                        String name = statusFollow.getUser().getScreenName();
                        try {
                            if (!twitter.showFriendship(twitter.getScreenName(), name).isSourceFollowingTarget() && !twitter.getScreenName().equals(name)){
                                twitter.createFriendship(name);
                                if (!twitter.showFriendship(twitter.getScreenName(), name).isSourceFollowingTarget()){
                                    StatusUpdate statusUpdate = new StatusUpdate("Acción automatizada -> Espere respuesta de @"+name);
                                    statusUpdate.setInReplyToStatusId(statusFollow.getId());
                                    twitter.updateStatus(statusUpdate);
                                }
                                StatusUpdate statusUpdate = new StatusUpdate("Acción automatizada -> Se sigue correctamente a @"+name);
                                statusUpdate.setInReplyToStatusId(statusFollow.getId());
                                twitter.updateStatus(statusUpdate);
                            }
                            else if(twitter.getScreenName().equals(name)){
                                StatusUpdate statusUpdate = new StatusUpdate("Acción automatizada -> ERROR: No se pudo realizar la acción por # (No puedes seguirte a ti mismo)");
                                statusUpdate.setInReplyToStatusId(statusFollow.getId());
                                twitter.updateStatus(statusUpdate);
                            }
                            else{
                                StatusUpdate statusUpdate = new StatusUpdate("Acción automatizada -> Ya se siguia a @"+name);
                                statusUpdate.setInReplyToStatusId(statusFollow.getId());
                                twitter.updateStatus(statusUpdate);
                            }
                        } catch (TwitterException e) {
                            try {
                                if (!twitter.showFriendship(twitter.getScreenName(), name).isSourceFollowingTarget()){
                                    StatusUpdate statusUpdate = new StatusUpdate("Acción automatizada -> Espere respuesta de @"+name);
                                    statusUpdate.setInReplyToStatusId(statusFollow.getId());
                                    twitter.updateStatus(statusUpdate);
                                }
                            } catch (TwitterException ex) {
                                StatusUpdate statusUpdate = new StatusUpdate("Acción automatizada -> No se encuentra a @"+name);
                                statusUpdate.setInReplyToStatusId(statusFollow.getId());
                                twitter.updateStatus(statusUpdate);
                            }
                        }
                    }
                    else if (hashtagEntity.getText().equalsIgnoreCase("darlike")){
                        Status statusLike = twitter.showStatus(tweet.getId());
                        try {
                            if(!twitter.showStatus(statusLike.getId()).isFavorited()) {
                                twitter.createFavorite(statusLike.getId());
                                StatusUpdate statusUpdate = new StatusUpdate("Se ha dado like exitosamente @"+statusLike.getUser().getScreenName());
                                statusUpdate.setInReplyToStatusId(statusLike.getId());
                                twitter.updateStatus(statusUpdate);
                            } else{
                                twitter.destroyFavorite(statusLike.getId());
                                StatusUpdate statusUpdate = new StatusUpdate("Se ha quitado el like exitosamente @"+statusLike.getUser().getScreenName());
                                statusUpdate.setInReplyToStatusId(statusLike.getId());
                                twitter.updateStatus(statusUpdate);
                            }
                        } catch (TwitterException e) {
                            StatusUpdate statusUpdate = new StatusUpdate("Ups, ocurrió un error al intentar realizar el like @"+statusLike.getUser().getScreenName());
                            statusUpdate.setInReplyToStatusId(statusLike.getId());
                            twitter.updateStatus(statusUpdate);
                        }
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
