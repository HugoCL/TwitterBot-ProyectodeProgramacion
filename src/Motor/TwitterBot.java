package Motor;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import java.io.Serializable;

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
     *  Metodo que configura la instancia de Twitter, usando las Keys de la API asociados al programa
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
                // SE NECESITA ELIMINAR ESTE SYS.EXIT
                System.exit(-1);
            }
        }
        return null;
    }

    /***
     * Método que permite autorizar al Bot el uso de la cuenta del usuario que inició sesión
     * @param PIN Secuencia de numeros de autorización otorgado por Twitter para confirmar el accecso
     * @return Retorna un String con el estado de la autorización
     */

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

    public Twitter getTwitter() {
        return twitter;
    }
}