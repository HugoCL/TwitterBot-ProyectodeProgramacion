package Motor;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/***
 * Clase motor del Bot. Contiene todos los metodos que cumplen las funcionalidades del enunciado
 */
public class TwitterBot implements Serializable {
    private static TwitterBot INSTANCE = null;
    RequestToken rtoken;
    public boolean serializacionSesion;
    public String PIN;
    // Constructor privado

    private TwitterBot(){
        serializacionSesion = false;
    }
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple
    private synchronized static void createInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TwitterBot();
        }
    }
    public static TwitterBot getInstance() {
        if (INSTANCE == null) createInstance();
        return INSTANCE;
    }
    private Twitter twitter;

    /**
     * Metodos para guardar y obtener el bot junto a sus caracteristicas.
     */
    private TwitterBot BOT;
    public void setBOT(TwitterBot BOT){
        this.BOT = BOT;
    }
    public TwitterBot getBOT(){
        return BOT;
    }
    /***
     * Metodo que inicializa los parametros iniciales del Bot obtenidos de la API de Twitter y crea la instancia del Bot
     */
    public TwitterBot cargarBot() throws TwitterException, IOException {
        TwitterBot bot;
        adminSesion adm = adminSesion.getInstance();
        TwitterBot botSerializado = adm.desSerializar();
        if (botSerializado == null){
            bot = TwitterBot.getInstance();
            bot.inicializarBot();
            System.out.println(bot.OAuthURL());
            Scanner scan = new Scanner(System.in);
            String pin = scan.nextLine();
            bot.OAuthInicio(pin);
            adm.Serializar(bot);
        }
        else{
            bot = botSerializado;
        }
        return bot;
    }

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
    public String OAuthURL() throws TwitterException, IOException {
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

        // Prints de DEBUG para comprobar Token de Acceso
        String accessToken = atoken.getToken();
        String accessTokenS = atoken.getTokenSecret();
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
         * Metodo que envia MD a personas usando su @.
         * @param arroba Nombre de usuario al que se le enviara el MD. No se debe incluir el @
         * @param texto String con el texto a enviar
         * @throws TwitterException Excepcion por si ocurre un problema interno con Twitter
         */
        public void EnviarMD(String arroba, String texto) throws TwitterException {
            DirectMessage MD = twitter.sendDirectMessage(arroba, texto);
            System.out.println("Se ha enviado un mensaje directo a @" + arroba + " El mensaje fue: " + MD.getText());
        }
    }

    /***
     * Segunda clase interna que se encarga de las funciones relacionadas a contenidos externos
     */

    class Feed {

        /***
         * Permite la obtención de los tweets del timeline de la cuenta ingresada
         * @return Lista con los tweets.
         */
        public ArrayList<Status> ObtenerMensajes() {
            ArrayList<Status> statuses = new ArrayList<>();
            System.out.println("Obteniendo tweets");
            for (int pageno = 1; true; pageno++) {
                try {
                    int size = statuses.size(); // actual tweets count we got
                    Paging page = new Paging(pageno, 200);
                    statuses.addAll(twitter.getHomeTimeline(page));

                    if (statuses.size() == size) {
                        System.out.println("total obtenido: " + statuses.size());
                        break;
                    }
                } catch (TwitterException e) {
                    System.err.print("Failed to search tweets: " + e.getMessage());
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.err.println(e.getMessage());
                }
            }
            return statuses;
        }

        /***
         * Permite agregar a faoritos todos los tweets del timeline de la cuenta asociada
         * @throws TwitterException
         */
        public void Like() throws TwitterException {
            ArrayList<Status> statuses = ObtenerMensajes();

            for (Status t : statuses) {
                System.out.println(t.getUser().getName() + ": " + t.getText());
                if (!t.isFavorited()) {
                    twitter.createFavorite(t.getId());
                }
            }
        }

        /***
         * Permite retweetear todos los tweets en el timline de la cuenta ingresada
         * @throws TwitterException
         */
        public void Retweet() throws TwitterException {
            ArrayList<Status> statuses = ObtenerMensajes();

            for (Status t : statuses) {
                System.out.println(t.getUser().getName() + ": " + t.getText());
                if (!t.isRetweeted()) {
                    twitter.retweetStatus(t.getId());
                }
            }
        }
    }
}
