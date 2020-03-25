package Motor;

import twitter4j.DirectMessage;
import twitter4j.DirectMessageList;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MensajesDirectos {
    /**
     * Inicio patrón de diseño Singleton
     */
    private static MensajesDirectos INSTANCE = null;

    private Date fechaAccion;

    private FileReader saludos;

    /**
     * Constructor privado
     */
    private MensajesDirectos(){
        cargarData();

        construirConversacion();
    }

    private void cargarData() {
        File archivo = new File("saludos.in");
        try {
            saludos = new FileReader(archivo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Método para evitar multi-hilos
     */
    private synchronized static void createInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MensajesDirectos();
        }
    }

    /**
     * Obtener la instancia única de la clase.
     * @return devuelve la instancia única de la clase TwitterBot
     */
    public static MensajesDirectos getInstance() {
        if (INSTANCE == null) createInstance();
        return INSTANCE;
    }
    private static Twitter twitter = TwitterBot.getInstance().getBOT().getTwitter();

    private static ArrayList<Chat> chats;

    public void responderMD() {
        Messages mensajes = new Messages();
        for (Chat chat: chats) {
            try {
                DirectMessage lastMessage = chat.getConversacion().get(0);
                if (lastMessage.getSenderId() != twitter.getId()) {
                    if ((TwitterBot.getInstance().getToxicity(lastMessage.getText()) * 100) >= 70.0)
                        mensajes.EnviarMD(twitter.showUser(lastMessage.getSenderId()).getScreenName(), "Chupalo");
                    else if (respuesta(lastMessage))
                        mensajes.EnviarMD(twitter.showUser(lastMessage.getSenderId()).getScreenName(), "Hola");
                    else
                        mensajes.EnviarMD(twitter.showUser(lastMessage.getSenderId()).getScreenName(), "Recibido");
                }
            } catch (Exception e) {
                System.out.println("error");
            }
        }
    }

    private boolean respuesta(DirectMessage lastMessage) {
        try {
            BufferedReader br = new BufferedReader(saludos);
            String linea;
            while((linea=br.readLine()) != null){
                char[] chars = linea.toCharArray();
                linea = "";
                for (int i = 0; i < chars.length; i++) {
                    chars[i]-=3;
                    linea += chars[i]+"";
                }
                Pattern pattern = Pattern.compile("(.*)(?i)"+ linea + "(.*)");
                Matcher matcher = pattern.matcher(lastMessage.getText());
                if(matcher.find()){
                    return true;
                }
            }
        }catch (Exception e) {
            System.out.println("Error");
        }
        return false;
    }


    public void construirConversacion() {
        DirectMessageList list;
        chats = new ArrayList<>();
        try {
            list = twitter.getDirectMessages(50);
            long id;
            for (DirectMessage md: list) {

                if (md.getSenderId() == twitter.getId()) id = md.getRecipientId();
                else id = md.getSenderId();

                int i = idCoversation(id);

                if (i != -1)    chats.get(i).getConversacion().add(md);
                else {Chat chat = new Chat(id); chat.getConversacion().add(md); chats.add(chat); }
            }
        } catch (TwitterException e) {
            System.out.println("No hay chat disponible");
        }
    }

    public int idCoversation(long id) {
        for (int i = 0; i < chats.size(); i++)
            if (chats.get(i).getUser() == id) return i;
        return -1;
    }

    public void setFechaAccion (Date fechaAccion) {
        this.fechaAccion = fechaAccion;
    }

    public Date getFechaAccion() {
        return fechaAccion;
    }

    public ArrayList<Chat> getChats() {
        return chats;
    }
}
