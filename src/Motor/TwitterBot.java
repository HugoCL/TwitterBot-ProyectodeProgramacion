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
     * @throws IOException Excepcion por problemas con archivos del programa
     */
    public String OAuthURL() throws TwitterException{
        try {
            //Se obtienen los tokens para solicitar autorizacion
            rtoken = twitter.getOAuthRequestToken();
            return (rtoken.getAuthorizationURL());
        } catch (IllegalStateException ie) {
            if (!twitter.getAuthorization().isEnabled()) {
                System.out.println("No se han configurado los tokens de OAuth");
                System.exit(-1);
            }
        }
        return null;
    }

    public void OAuthInicio(String PIN){
        AccessToken atoken = null;
        //Ciclo que se realiza mientras no exista un Token que permita usar una cuenta
        while (atoken == null) {

            // Bloque try-catch en el que se comprueba si el PIN es correcto, para luego obtener el Token de OAuth
            try {
                if (PIN.length() > 0) {
                    atoken = twitter.getOAuthAccessToken(rtoken, PIN);
                } else {
                    System.out.println("No se ingresó ningún PIN, intente nuevamente");
                    rtoken = twitter.getOAuthRequestToken();
                }
            } catch (TwitterException e) {
                if (401 == e.getStatusCode()) {
                    System.out.println("Ocurrió un error al intentar obtener el token de acceso");
                } else {
                    System.out.println("Ocurrió un problema al intentar obtener el token de acceso por un" +
                            "error en la entrada");
                    e.printStackTrace();
                }
            }
        }
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

        public void PublicarTweet(String Tweet) throws TwitterException {
            Status status = twitter.updateStatus(Tweet);
            status.getText();
        }

        /***
         * Método que publica tweets de texto simple e imagenes
         * @param Tweet mensaje de texto
         * @param rutaImagen imagen o video a subir
         */
        public int PublicarTweetImagen (String Tweet, File rutaImagen){
            Pattern patronImage = Pattern.compile("^[^\n]+.jp(e)?g|.png|.gif$");
            Pattern patronVideo = Pattern.compile("^[^\n]+.mp4|.mov$");

            try{
                StatusUpdate nuevoTweet = new StatusUpdate(Tweet);
                nuevoTweet.setMedia(rutaImagen);
                twitter.updateStatus(nuevoTweet);
                System.out.println("Tweet con imagen publicado correctamente");
                return 0;
            }
            catch (Exception e){
                if (patronImage.matcher(rutaImagen.getName()).find()) {
                    System.out.println("tamaño de la imagen superado");
                    return 1;
                }
                if (patronVideo.matcher(rutaImagen.getName()).find()) {
                    System.out.println("tamaño del video superado");
                    return 2;
                }
                e.printStackTrace();
                System.out.println("Ocurrió un error al intentar publicar el Tweet. Revise el tipo de archivo.");
                return 3;
            }
        }

        /***
         * Metodo que envia MD a personas usando su @.
         * @param arroba Nombre de usuario al que se le enviara el MD. No se debe incluir el @
         * @param texto String con el texto a enviar
         * @throws TwitterException Excepcion por si ocurre un problema interno con Twitter
         */
        public void EnviarMD(String arroba, String texto) throws TwitterException {
            try {
                DirectMessage MD = twitter.sendDirectMessage(arroba, texto);
                System.out.println("Se ha enviado un mensaje directo a @" + arroba + " El mensaje fue: " + MD.getText());
            }catch (Exception e){
                System.out.println("Error al enviar mensaje a: @"+arroba+", verifique el ");
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

            System.out.println("Obteniendo tweets...");

            while (true) {
                try {
                    int size = tweets.size();
                    Paging page = new Paging(pageno++, 100);
                    for (Status status: twitter.getHomeTimeline(page)){
                        tweets.add(new Tweet(status.getText(), status.getId(), status.getUser().getName()));
                    }
                    if (tweets.size() == size)
                        break;
                }catch(TwitterException e) {
                    e.printStackTrace();
                    System.err.println("Refresh muy frecuente, intente nuevamente más tarde.");
                    if (tweets.size() != 0)     return null;
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
        public void Like(long like){
            try {
                twitter.createFavorite(like);
                System.out.println("Like exitoso.");
            } catch (TwitterException e) {
                System.err.println("Error: No se puede dar like");
            }
        }

        /***
         * Permite retweetear todos los tweets en el timline de la cuenta ingresada
         * @param tweet contiene el tweet a dar retweet
         * @throws TwitterException
         */
        public void Retweet(long tweet){
            try {
                if (!twitter.showStatus(tweet).isRetweetedByMe()) {
                    twitter.retweetStatus(tweet);
                    System.out.println("Retweet exitoso.");
                } else
                    System.out.println("Tweet ya tweteado");
            } catch (TwitterException e) {
                System.err.println("No se encontro Tweet");
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
        public ArrayList<Followers> getFollowers() {
            ArrayList<Followers> followers = new ArrayList<>();
            long cursor = -1;
            IDs ids;
            try {
                do{
                    ids = twitter.getFollowersIDs(cursor);
                    for (long UserId: ids.getIDs()) {
                        followers.add(new Followers(twitter.showUser(UserId).getScreenName()));
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
        public int Follow(String name) {
            try {
                if (!twitter.showFriendship(twitter.getScreenName(), name).isSourceFollowingTarget())  { twitter.createFriendship(name); return 0; }
                else  {  System.out.println("Ya sigue al usuario"); return 1; }

            } catch (TwitterException e) {
                System.err.println("Error al buscar usuario: " + name);
                return 2;
            }
        }
        public String getNombreUsuario() throws TwitterException {
            return twitter.getScreenName();
        }
    }
}