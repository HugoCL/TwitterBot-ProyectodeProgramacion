package Motor;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Cadenas {

    public static ArrayList<Tweet> BuscarTweetsHash (ArrayList<Tweet> tweets) {
        ArrayList<Tweet> aux = null;
        Pattern patron = Pattern.compile("(.*\\s#.+)|(^#.+)");
        for (Tweet tweet: tweets)
            if (patron.matcher(tweet.getMensaje()).find())
                aux.add(tweet);
        return aux;
    }

    public static String[] BuscarTextoHash(String tweet) {
        String[] aux = tweet.split(" ");
        String[] hashs = new String[160];
        int i = 0;
        for (String world: aux)
            if(world.charAt(0) == '#')
                hashs[i++] = world;
        return hashs;
    }

}
