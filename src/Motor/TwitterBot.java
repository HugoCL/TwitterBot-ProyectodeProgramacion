package Motor;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLOutput;

public class TwitterBot {

    private Twitter twitter;

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
                        atoken = twitter.getOAuthAccessToken(rtoken);
                    }
                }
                catch (TwitterException e){
                    if (401 == e.getStatusCode()){
                        System.out.println("Ocurrió un error al intentar obtener el token de acceso");
                    }
                    else{
                        e.printStackTrace();
                    }
                }

            }
            System.out.println("Se obtuvo el token de acceso!");
            System.out.println("DEBUGEO");
            System.out.println("Token de acceso: "+atoken.getToken());
            System.out.println("Token de acceso secreto: "+atoken.getTokenSecret());
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
