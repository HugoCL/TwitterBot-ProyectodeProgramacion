package Motor;

import twitter4j.*;

import java.util.ArrayList;

public class HashtagActions {

    private static Twitter twitter = TwitterBot.getInstance().getBOT().getTwitter();


    public void HashTagActions(ArrayList<Tweet> tweets) {
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
                                    StatusUpdate statusUpdate = new StatusUpdate("Espere respuesta de @"+name);
                                    statusUpdate.setInReplyToStatusId(statusFollow.getId());
                                    twitter.updateStatus(statusUpdate);
                                }
                                else {
                                    StatusUpdate statusUpdate = new StatusUpdate("Se está siguiendo correctamente a @" + name);
                                    statusUpdate.setInReplyToStatusId(statusFollow.getId());
                                    twitter.updateStatus(statusUpdate);
                                }
                            }
                            else if(twitter.getScreenName().equals(name)){
                                StatusUpdate statusUpdate = new StatusUpdate("ERROR: No se pudo realizar la acción por # (No puedes seguirte a ti mismo)");
                                statusUpdate.setInReplyToStatusId(statusFollow.getId());
                                twitter.updateStatus(statusUpdate);
                            }
                            else{
                                StatusUpdate statusUpdate = new StatusUpdate("¡Vaya! Ya se siguia a @"+name);
                                statusUpdate.setInReplyToStatusId(statusFollow.getId());
                                twitter.updateStatus(statusUpdate);
                            }
                        } catch (TwitterException e) {
                            try {
                                if (!twitter.showFriendship(twitter.getScreenName(), name).isSourceFollowingTarget()){
                                    StatusUpdate statusUpdate = new StatusUpdate("Espere respuesta de @"+name);
                                    statusUpdate.setInReplyToStatusId(statusFollow.getId());
                                    twitter.updateStatus(statusUpdate);
                                }
                            } catch (TwitterException ex) {
                                StatusUpdate statusUpdate = new StatusUpdate("Ups :( No se encuentra a @"+name);
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
                                StatusUpdate statusUpdate = new StatusUpdate("Ya se había dado like anteriormente a este Tweet @"+statusLike.getUser().getScreenName());
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
                        try {
                            if (!twitter.showStatus(statusRetweet.getId()).isRetweetedByMe()) {
                                twitter.retweetStatus(statusRetweet.getId());
                                StatusUpdate statusUpdate = new StatusUpdate("Se ha retwitteado exitosamente @"+statusRetweet.getUser().getScreenName());
                                statusUpdate.setInReplyToStatusId(statusRetweet.getId());
                                twitter.updateStatus(statusUpdate);
                            } else{
                                StatusUpdate statusUpdate = new StatusUpdate("Ya se había retwitteado este Tweet anteriormente @"+statusRetweet.getUser().getScreenName());
                                statusUpdate.setInReplyToStatusId(statusRetweet.getId());
                                twitter.updateStatus(statusUpdate);
                            }
                        } catch (TwitterException e) {
                            StatusUpdate statusUpdate = new StatusUpdate("Vaya, ocurrió un error al momento de retwittear :( @"+statusRetweet.getUser().getScreenName());
                            statusUpdate.setInReplyToStatusId(statusRetweet.getId());
                            twitter.updateStatus(statusUpdate);
                        }
                    }
        } catch (TwitterException e) {
            System.out.println(e.getErrorMessage());
        }
    }
}
