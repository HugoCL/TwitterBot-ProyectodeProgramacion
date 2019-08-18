package Motor;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import java.io.*;


public class TwitterBot implements Serializable {

    private Twitter twitter;
    private String accessToken;
    private String accessTokenS;

    public void inicializarBot() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey("y3rodATEKk9OopeZb3bJ49k7L");
        cb.setOAuthConsumerSecret("eCkLQgglSpvdD7nUiU6hoH2hoWYEWASAAMRWkfuTyqnhUxLfr0");
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
    }

    public void OAuth() throws TwitterException, IOException {
        try{
            RequestToken rtoken = twitter.getOAuthRequestToken();
            System.out.println("Obteniendo Request Token para autorización");
            System.out.println("DEBUGEO");
            System.out.println("Request token: "+rtoken.getToken());
            System.out.println("Request token secreto: "+rtoken.getTokenSecret());
            AccessToken atoken = null;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (atoken == null){
                System.out.println("Abre el siguiente enlace en el navegador para autorizar el bot");
                System.out.println(rtoken.getAuthorizationURL());
                System.out.println("Si se muestra, ingresa el PIN para autorizar la app");
                String PIN = br.readLine();

                try{
                    if (PIN.length() > 0){
                        atoken = twitter.getOAuthAccessToken(rtoken, PIN);
                    }
                    else{
                        System.out.println("No se ingresó ningún PIN, intente nuevamente");
                        rtoken = twitter.getOAuthRequestToken();
                    }
                }
                catch (TwitterException e){
                    if (401 == e.getStatusCode()){
                        System.out.println("Ocurrió un error al intentar obtener el token de acceso");
                    }
                    else{
                        System.out.println("Ocurrió un problema al intentar obtener el token de acceso por un" +
                                "error en la entrada");
                        e.printStackTrace();
                    }
                }
            }
            accessToken = atoken.getToken();
            accessTokenS = atoken.getTokenSecret();
            System.out.println("Se obtuvo el token de acceso!");
            System.out.println("DEBUGEO");
            System.out.println("Token de acceso: "+accessToken);
            System.out.println("Token de acceso secreto: "+accessTokenS);

        }
        catch (IllegalStateException ie){
            if (!twitter.getAuthorization().isEnabled()){
                System.out.println("No se han configurado los tokens de OAuth");
                System.exit(-1);
            }
        }

    }


    class Messages{
        public String PublicarTweet(String Tweet) throws TwitterException {
            Status status = twitter.updateStatus(Tweet);
            return status.getText();
        }
        public void EnviarMD(String arroba, String texto) throws TwitterException {
            DirectMessage MD = twitter.sendDirectMessage(arroba, texto);
            System.out.println("Se ha enviado un mensaje directo a @"+arroba+" El mensaje fue: "+MD.getText());
        }
    }
    class Feed{
        public String ObtenerMensajes() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        public void Like() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        public void Retweet() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }
}
