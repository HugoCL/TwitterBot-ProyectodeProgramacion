package Motor;

import twitter4j.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/***
 * Clase interna que posee los metodos que realizan las funciones de mensajeria: Tweets y Mensajes Directos
 */
public class Messages {

    private static Twitter twitter = TwitterBot.getInstance().getBOT().getTwitter();

    String deleteTweet(long id) {
        try{
            twitter.destroyStatus(id);
            return "Mensaje Eliminado";
        } catch (TwitterException e) {
            return "No se pudo eliminar mensaje\n" + e.getErrorMessage();
        }
    }

    /***
     * Metodo que publica Tweets de texto simple
     * @param Tweet String con el Tweet a publicar
     */


    public String PublicarTweet(String Tweet){
        try{
            twitter.updateStatus(Tweet);
            return "Tweet publicado correctamente";
        }catch (TwitterException e) {
            System.out.println(e.getStatusCode());
            return "ERROR: Tweet duplicado";
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
        catch (TwitterException e){
            if(e.getErrorCode() == 187) return "Tweet Duplicado";
            return "Tamaño de la imagen superado";
        }
    }

    public String PublicarTweetVideo (String Tweet, File rutaVideo){
        try{
            StatusUpdate nuevoTweet = new StatusUpdate(Tweet);
            InputStream is;
            try {
                is = new FileInputStream(rutaVideo);
            } catch (FileNotFoundException e) {
                return "Archivo invalido. Revise la ruta y/o el archivo";
            }
            UploadedMedia um = twitter.uploadMediaChunked(rutaVideo.getName(), is);
            nuevoTweet.setMediaIds(um.getMediaId());
            twitter.updateStatus(nuevoTweet);
            System.out.println("Video y Tweet publicado correctamente");
        }
        catch (TwitterException te){
            System.out.println(te.getErrorCode());
            if (te.getErrorCode() == 187)
                return "El texto del Tweet ya se ha publicado anteriormente.";
            return "Video sobrepasa peso(15MB) o duracion(140s)";
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

    public static Status getTweet(long id) {
        try {
            return twitter.showStatus(id);
        } catch (TwitterException e) {
            return null;
        }
    }
}
