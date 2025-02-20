package Motor;

import Transiciones.Dialog;
import com.jfoenix.controls.JFXButton;
import javafx.scene.layout.AnchorPane;
import twitter4j.*;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * Clase interna que posee los metodos que realizan las funciones de mensajeria: Tweets y Mensajes Directos
 */
public class Messages {

    private static Twitter twitter = TwitterBot.getInstance().getBOT().getTwitter();
    private static File archivo = null;
    private static FileReader fr = null;
    private static BufferedReader br = null;


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

    public boolean isSpam(String mensaje){
        float tox = 100;
        try {
            tox *= TwitterBot.getInstance().getToxicity(mensaje);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        try {
            if (tox < 70.0) {
                archivo = new File ("spam.in");
                fr = new FileReader (archivo);
                br = new BufferedReader(fr);

                // Lectura del fichero
                String linea;
                while((linea=br.readLine()) != null){
                    char[] chars = linea.toCharArray();
                    linea = "";
                    for (int i = 0; i < chars.length; i++) {
                        chars[i]-=3;
                        linea += chars[i]+"";
                    }
                    Pattern pattern = Pattern.compile("(.*)(?i)"+ linea + "(.*)");
                    Matcher matcher = pattern.matcher(mensaje);
                    if(matcher.find()){
                        return true;
                    }
                }
            } else {
                return true;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if( null != fr ){
                    fr.close();
                }
            }catch (Exception e2){
                e2.printStackTrace();
            }
        }
        return false;
    }
    public static void isSpam(long id) {
        Date fechaAnalisis = null;

        float tox = 100;
        Status tweet ;

        try {
            tweet = twitter.showStatus(id);

            if (Exists("TimeStampSpam.out")) {
                FileInputStream fileInputStream = new FileInputStream("TimeStampSpam.out");
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                fechaAnalisis = (Date) objectInputStream.readObject();
            }

            if (fechaAnalisis == null){
                System.out.println("null");
                Calendar fecha = new GregorianCalendar();
                fecha.add(Calendar.DAY_OF_MONTH, -14);
                fechaAnalisis = fecha.getTime();
            }

            if (tweet.getCreatedAt().compareTo(fechaAnalisis) > 0){
                tox *= TwitterBot.getInstance().getToxicity(tweet.getText());
                if (tox >= 70.0) {
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM HH:mm:ss");
                    Date date2 = new Date();
                    StatusUpdate statusUpdate = new StatusUpdate("@" + tweet.getUser().getScreenName() + " Eres Spam. Acción realizada con fecha: "+dateFormat.format(date2));
                    statusUpdate.setInReplyToStatusId(id);
                    twitter.updateStatus(statusUpdate);
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream("TimeStampSpam.out");
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                        objectOutputStream.writeObject(tweet.getCreatedAt());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }

            archivo = new File ("spam.in");
            fr = new FileReader (archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String linea;
            while((linea=br.readLine()) != null && tweet.getCreatedAt().compareTo(fechaAnalisis) > 0){
                char[] chars = linea.toCharArray();
                linea = "";
                for (int i = 0; i < chars.length; i++) {
                    chars[i]-=3;
                    linea += chars[i]+"";
                }
                Pattern pattern = Pattern.compile("(.*)(?i)"+ linea + "(.*)");
                Matcher matcher = pattern.matcher(tweet.getText());
                if(matcher.find()){
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM HH:mm:ss");
                    Date date2 = new Date();
                    StatusUpdate statusUpdate = new StatusUpdate("@" + tweet.getUser().getScreenName() + " Eres Spam. Acción realizada con fecha: "+dateFormat.format(date2));
                    statusUpdate.setInReplyToStatusId(id);
                    twitter.updateStatus(statusUpdate);
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream("TimeStampSpam.out");
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                        objectOutputStream.writeObject(tweet.getCreatedAt());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
            fr.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if( null != fr ){
                    fr.close();
                }
            }catch (Exception e2){
                e2.printStackTrace();
            }
        }
    }

    private static boolean Exists(String timeStampSpam) {
        File file = new File(timeStampSpam);
        return file.exists();
    }
}
