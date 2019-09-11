package Motor;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
import java.util.regex.Pattern;

import java.io.*;
import java.util.ArrayList;

/***
 * Clase motor del Bot. Contiene todos los metodos que cumplen las funcionalidades del enunciado
 */
public class TwitterBot implements Serializable {

    /**
     * Inicio patrón de diseño Singleton
     */
    private static TwitterBot INSTANCE = null;

    /**
     * Constructor privado
     */
    private TwitterBot(){
        isGuardado = false;
    }

    /**
     * Método para evitar multi-hilos
     */
    private synchronized static void createInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TwitterBot();
        }
    }

    /**
     * Obtener la instancia única de la clase.
     * @return devuelve la instancia única de la clase TwitterBot
     */
    public static TwitterBot getInstance() {
        if (INSTANCE == null) createInstance();
        return INSTANCE;
    }

    /**
     * Atributo que tiene el acceso a la API de twitter que sirve para realizar las consultas.
     */
    private Twitter twitter;

    /**
     * Métodos para guardar y obtener si la sesión está iniciada.
     */
    private boolean isGuardado;
    public void setSesion(boolean isGuardado){this.isGuardado = isGuardado;}
    public boolean getSesion(){return isGuardado;}

    /**
     * Métodos para guardar y obtener el pin de sesión.
     */
    private String pin;
    public void setPin(String pin){this.pin = pin;}
    public String getPin(){return pin;}

    /**
     * Token utilizado para los métodos de iniciar sesión mediate el OAuth
     */
    private RequestToken rtoken;

    /**
     * Metodos para guardar y obtener el bot junto a sus caracteristicas.
     */
    private TwitterBot BOT;
    public void setBOT(TwitterBot BOT){this.BOT = BOT;}
    public TwitterBot getBOT(){return BOT;}

    /**
     *
     */
    public void inicializarBot() {

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);

        // Consumer Key del Bot otorgado por la API de Twitter
        cb.setOAuthConsumerKey("y3rodATEKk9OopeZb3bJ49k7L");
        cb.setOAuthConsumerSecret("eCkLQgglSpvdD7nUiU6hoH2hoWYEWASAAMRWkfuTyqnhUxLfr0");

        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
    }

    /***
     * Metodo que permite iniciar sesion con cualquier cuenta de Twitter gracias a la autorizacion mediante OAuth.
     * @throws TwitterException Excepcion por problemas tecnicos de Twitter
     */
    public String OAuthURL() {
        try {
            //Se obtienen los tokens para solicitar autorizacion
            rtoken = twitter.getOAuthRequestToken();
            return (rtoken.getAuthorizationURL());
        } catch (TwitterException ie) {
            if (!twitter.getAuthorization().isEnabled()) {
                System.out.println("No se han configurado los tokens de OAuth");
                System.exit(-1);
            }
        }
        return null;
    }

    public String OAuthInicio(String PIN){
        // Bloque try-catch en el que se comprueba si el PIN es correcto, para luego obtener el Token de OAuth
        try {
            if (PIN.length() > 0) {
                twitter.getOAuthAccessToken(rtoken, PIN);
            } else {
                return "PIN no ingresado\nVuelva a copiar el enlace de autentificación";
            }
        } catch (TwitterException e) {
            if (401 == e.getStatusCode()) {
                return "ERROR: PIN Incorrecto\nVuelva a copiar el enlace de autentificación";
            }
        }
        return "PIN Correcto";
    }

    /***
     * Clase interna que posee los metodos que realizan las funciones de mensajeria: Tweets y Mensajes Directos
     */
    public class Messages {

        /***
         * Metodo que publica Tweets de texto simple
         * @param Tweet String con el Tweet a publicar
         * @throws TwitterException Excepcion por si ocurre un problema interno con Twitter
         */

        public String PublicarTweet(String Tweet){
            try{
                twitter.updateStatus(Tweet);
                return "Tweet publicado correctamente";
            }catch (TwitterException e) {
                System.out.println(e.getStatusCode());
                return "ERROR:\nTweet duplicado";
            }
        }

        /***
         * Método que publica tweets de texto simple e imagenes
         * @param Tweet mensaje de texto
         * @param rutaImagen imagen o video a subir
         */
        public String PublicarTweetImagen (String Tweet, File rutaImagen){

            try{
                StatusUpdate nuevoTweet = new StatusUpdate(Tweet);
                nuevoTweet.setMedia(rutaImagen);
                twitter.updateStatus(nuevoTweet);
                return "Tweet publicado correctamente";
            }
            catch (Exception e){
                return "Tamaño de la imagen superado";
            }
        }

        public String PublicarTweetVideo (String Tweet, File rutaVideo){
            try{
                StatusUpdate nuevoTweet = new StatusUpdate(Tweet);
                InputStream is = new FileInputStream(rutaVideo);
                UploadedMedia um = twitter.uploadMediaChunked(rutaVideo.getName(), is);
                nuevoTweet.setMediaIds(um.getMediaId());
                twitter.updateStatus(nuevoTweet);
                System.out.println("Video y Tweet publicado correctamente");
            }
            catch (TwitterException e) {
                return "Tamaño o duración del video superado";
            }
            catch (Exception e){
                return "Ruta no enontrada";
            }
            return "Video y Tweet Publicado correctamente";
        }

        /***
         * Metodo que envia MD a personas usando su @.
         * @param arroba Nombre de usuario al que se le enviara el MD. No se debe incluir el @
         * @param texto String con el texto a enviar
         */
        public String EnviarMD(String arroba, String texto){
            try {
                twitter.sendDirectMessage(arroba, texto);
                return "Mensaje enviado correctamente";
            }catch (Exception e){
                return "Usuario no encontrado o no seleccionado";
            }
        }
    }

    /***
     * Segunda clase interna que se encarga de las funciones relacionadas a contenidos externos
     */
    public class Feed {
        private ArrayList<Tweet> tweets = new ArrayList<>();
        /***
         * Permite la obtención de los tweets del timeline de la cuenta ingresada
         * @return Lista con los tweets.
         */
        public ArrayList<Tweet> ObtenerTweets() {
            int pageno = 1;

            while (true) {
                try {
                    int size = tweets.size();
                    Paging page = new Paging(pageno++, 100);
                    if (pageno == 2) tweets.clear();

                    for (Status status: twitter.getHomeTimeline(page)){
                        tweets.add(new Tweet(status.getText(), status.getId(), status.getUser().getName()));
                    }
                    if (tweets.size() == size)
                        break;
                }catch(TwitterException e) {
                    if (tweets.size() != 0){
                        return null;
                    }
                    return tweets;
                }
            }
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
                twitter.createFavorite(like);
                return "Like exitoso";
            } catch (TwitterException e) {
                return "Tweet ya likeado";
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
                    return "Tweet ya retweeteado";
                }
            } catch (TwitterException e) {
                return "ERROR:\nNo se encontro Tweet";
            }
        }
    }

    /***
     * Clase interna que se encarga de las funcionalidades referentes a los usuarios
     */
    public class Usuario {

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
        public String Follow(String name) {
            try {
                if (!twitter.showFriendship(twitter.getScreenName(), name).isSourceFollowingTarget() && !twitter.getScreenName().equals(name)){
                    twitter.createFriendship(name);
                    if (!twitter.showFriendship(twitter.getScreenName(), name).isSourceFollowingTarget())
                        return "Espere respuesta de \n@"+name;
                    return "Se sigue correctamente a\n @"+name;
                }
                else if(twitter.getScreenName().equals(name))
                    return "ERROR: \nNo puedes seguirte a ti mismo";
                else  return "ERROR: Ya sigue al usuario:\n@"+name;

            } catch (TwitterException e) {
                try {
                    if (!twitter.showFriendship(twitter.getScreenName(), name).isSourceFollowingTarget())
                        return "Espere respuesta de \n@"+name;
                } catch (TwitterException ex) {
                    return "No se encuentra al usuario:\n@" + name;
                }
            }
            return "";
        }

        public String getNombreUsuario() throws TwitterException {
            return twitter.getScreenName();
        }
    }
}