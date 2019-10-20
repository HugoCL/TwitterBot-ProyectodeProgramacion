package Motor;

import twitter4j.*;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public void analizarHashtagActions(Status status) throws TwitterException {
        /***
         *  Actions y valores
         *  Primer indice (Follow): 0 es el estado inicial, 1 para acción exitosa, -1 para error de la API y 2 para esperar
         *  del user. Tambien se usará el -2 para indicar que la persona ya es seguida y -3 para error de usuario.
         *  Segundo indice (Like): 0 para estado inicial, 1 para acción exitosa, -1 para error de la API y -2 para
         *  indicar que el Tweet ya tenía like
         *  Tercer indice (RT): 0 para estodo inicial, 1 para acción exitosa, -1 para indicar error de la API y -2 para indicar
         *  que el tweet ya tenía retweet
         */
        Date fechaAnalisis = null;
        try {
            FileInputStream fileInputStream = new FileInputStream("TimeStamp#Actions.out");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            fechaAnalisis = (Date) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No se encontró el archivo serializado");
        }

        if ((fechaAnalisis == null || fechaAnalisis.compareTo(status.getCreatedAt()) < 0)){
            int[] actions = {0,0,0};
            try{
                for (HashtagEntity hashtagEntity : twitter.showStatus(status.getId()).getHashtagEntities()) {
                    if (hashtagEntity.getText().equalsIgnoreCase("seguir")) {
                        String name = status.getUser().getScreenName();
                        try {
                            if (!twitter.showFriendship(twitter.getScreenName(), name).isSourceFollowingTarget() && !twitter.getScreenName().equals(name)) {
                                twitter.createFriendship(name);
                                if (!twitter.showFriendship(twitter.getScreenName(), name).isSourceFollowingTarget()) {
                                    actions[0] = 2;
                                } else {
                                    actions[0] = 1;
                                }
                            } else if (twitter.getScreenName().equals(name)) {
                                actions[0] = -1;
                            } else {
                                actions[0] = -2;
                            }
                        } catch (TwitterException e) {
                            try {
                                if (!twitter.showFriendship(twitter.getScreenName(), name).isSourceFollowingTarget()) {
                                    actions[0] = 2;
                                }
                            } catch (TwitterException ex) {
                                actions[0] = -3;
                            }
                        }
                    } else if (hashtagEntity.getText().equalsIgnoreCase("darlike")) {
                        boolean tweetIDDisponible = false;
                        System.out.println("No entro aún al if");
                        Pattern pattern = Pattern.compile("(?i)#DarLike\\s[0-9]+");
                        Matcher matcher = pattern.matcher(status.getText());
                        String stringParsed = "TEXTEREGEXTESTING";
                        while(matcher.find()){
                            stringParsed = matcher.group(0);
                        }
                        if(status.getText().contains(stringParsed)){
                            tweetIDDisponible = true;
                        }
                        try {
                            if (!tweetIDDisponible){
                                if (!twitter.showStatus(status.getId()).isFavorited()) {
                                    twitter.createFavorite(status.getId());
                                    actions[1] = 1;
                                }
                                else {
                                    actions[1] = -2;
                                }
                            }
                            else{
                                Pattern pattern2 = Pattern.compile("\\d+");
                                Matcher matcher2 = pattern2.matcher(stringParsed);
                                String IDTarget = "9999999999999999";
                                while(matcher2.find()){
                                    IDTarget = matcher2.group(0);
                                    System.out.println(IDTarget);
                                }
                                if (!twitter.showStatus(Long.parseLong(IDTarget)).isFavorited()) {
                                    twitter.createFavorite(Long.parseLong(IDTarget));
                                    System.out.println("LE DI LIKE");
                                    actions[1] = 1;
                                }
                                else {
                                    actions[1] = -2;
                                }
                            }
                        } catch (TwitterException e) {
                            actions[1] = -1;
                        }
                    } else if (hashtagEntity.getText().equalsIgnoreCase("difundir")) {
                        try {
                            if (!twitter.showStatus(status.getId()).isRetweetedByMe()) {
                                twitter.retweetStatus(status.getId());
                                actions[2] = 1;
                            } else {
                                actions[2] = -2;
                            }
                        } catch (TwitterException e) {
                            actions[2] = -1;
                        }
                    }
                }
            } catch (TwitterException e){
                System.out.println(e.getErrorMessage());
            }

            String actionReply = "@"+status.getUser().getScreenName()+" ";
            String currentReply;
            if (actions[0]!= 0){
                if (actions[0] == 1){
                    currentReply = "Te hemos seguido";
                    actionReply = actionReply.concat(currentReply);
                }
                else if (actions[0] == 2){
                    currentReply = "Hemos realizado la acción de seguirte, ahora solo debes esperar la respuesta";
                    actionReply = actionReply.concat(currentReply);
                }
                else if (actions[0] == -1){
                    currentReply = "Problemas de la API al intentar seguirte";
                    actionReply = actionReply.concat(currentReply);
                }
                else if (actions[0] == -2){
                    currentReply = "Intentamos seguirte, pero ya te seguiamos anteriormente";
                    actionReply = actionReply.concat(currentReply);
                }
                else {
                    currentReply = "Cuando intentamos seguirte, nos encontramos con que la cuenta no existe (UPS!)";
                    actionReply = actionReply.concat(currentReply);
                }
            }
            boolean continuedAction = false;
            if (actions[1] != 0){
                if (actions[0]!= 0){
                    actionReply = actionReply.concat(", tambien");
                    continuedAction = true;
                }
                if (actions[1] == 1){
                    currentReply = " le dimos like al Tweet que nos enviaste";
                    if (!continuedAction){
                        currentReply = currentReply.substring(0, 1).toUpperCase() + currentReply.substring(1);
                    }
                    actionReply = actionReply.concat(currentReply);
                }
                else if (actions[1] == -1){
                    currentReply = " intentamos darte like, pero ocurrió un error con la API de Twitter";
                    if (!continuedAction){
                        currentReply = currentReply.substring(0, 1).toUpperCase() + currentReply.substring(1);
                    }
                    actionReply = actionReply.concat(currentReply);
                }
                else{
                    currentReply = " intentamos darte like, pero ya le habiamos dado like anteriormente";
                    if (!continuedAction){
                        currentReply = currentReply.substring(0, 1).toUpperCase() + currentReply.substring(1);
                    }
                    actionReply = actionReply.concat(currentReply);
                }
            }
            continuedAction = false;
            if (actions[2] != 0){
                if (actions[1] != 0){
                    actionReply = actionReply.concat(" y además");
                    continuedAction = true;
                }
                if (actions[2] == 1){
                    currentReply = " hemos retwitteado tu Tweet";
                    if (!continuedAction){
                        currentReply = currentReply.substring(0, 1).toUpperCase() + currentReply.substring(1);
                    }
                    actionReply = actionReply.concat(currentReply);
                }
                else if(actions[2] == -1){
                    currentReply = " hemos intentado retwittear tu Tweet, pero tuvimos un problema con la API de Twitter";
                    if (!continuedAction){
                        currentReply = currentReply.substring(0, 1).toUpperCase() + currentReply.substring(1);
                    }
                    actionReply = actionReply.concat(currentReply);
                }
                else {
                    currentReply = " hemos intentado retwittear tu Tweet";
                    if (!continuedAction){
                        currentReply = currentReply.substring(0, 1).toUpperCase() + currentReply.substring(1);
                    }
                    actionReply = actionReply.concat(currentReply);
                }
            }
            if (!actionReply.equals("") && (actions[0] != 0 || actions[1] != 0 || actions[2] != 0)){
                DateFormat dateFormat = new SimpleDateFormat("dd/MM HH:mm:ss");
                Date date2 = new Date();
                actionReply = actionReply.concat(". Acción realizada con fecha: "+dateFormat.format(date2));
                StatusUpdate statusUpdate = new StatusUpdate(actionReply);
                statusUpdate.setInReplyToStatusId(status.getId());
                twitter.updateStatus(statusUpdate);
                Date fechaAnalisis2 = status.getCreatedAt();
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream("TimeStamp#Actions.out");
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                    objectOutputStream.writeObject(fechaAnalisis2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
