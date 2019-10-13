package Motor;

import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.ArrayList;

/***
 * Clase que se encarga de las funcionalidades referentes a los usuarios
 */
public class Usuario {
    private static Twitter twitter = TwitterBot.getInstance().getBOT().getTwitter();

    /***
     * Devuelve una lista con los usuarios que sigue la cuenta activa.
     * @return Lista de la clase Friend, que contiene la información básica de los usuarios que sigue la cuenta activa
     */
    public ArrayList<String> getFollowers() {
        ArrayList<String> followers = new ArrayList<>();
        long cursor = -1;
        IDs ids;
        try {
            do{
                ids = twitter.getFollowersIDs(cursor);
                for (long UserId: ids.getIDs()) {
                    followers.add(twitter.showUser(UserId).getScreenName());
                }
            }while((cursor = ids.getNextCursor()) != 0);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return followers;
    }

    /***
     * Permite, a través del nombre de usuario, seguir a una cuenta de twitter
     * @param name Cadena con el nombre del usuario a seguir
     */
    public String follow(String name) {
        try {
            if (!twitter.showFriendship(twitter.getScreenName(), name).isSourceFollowingTarget() && !twitter.getScreenName().equals(name)){
                twitter.createFriendship(name);
                if (!twitter.showFriendship(twitter.getScreenName(), name).isSourceFollowingTarget())
                    return "Espere respuesta de @"+name;
                return "Se sigue correctamente a @"+name;
            }
            else if(twitter.getScreenName().equals(name))
                return "ERROR: No puedes seguirte a ti mismo";
            else  return "ERROR: Ya sigue al usuario: @"+name;

        } catch (TwitterException e) {
            try {
                if (!twitter.showFriendship(twitter.getScreenName(), name).isSourceFollowingTarget())
                    return "Espere respuesta de @"+name;
            } catch (TwitterException ex) {
                return "No se encuentra al usuario: @"+name;
            }
        }
        return "";
    }

    public static long getIDUsuario(String name) {
        try {
            return twitter.showUser(name).getId();
        } catch (TwitterException e) {
            return -1;
        }
    }

    public String getNombreUsuario() throws TwitterException {
        return twitter.getScreenName();
    }
}
