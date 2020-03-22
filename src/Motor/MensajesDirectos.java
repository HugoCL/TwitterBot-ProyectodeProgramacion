package Motor;

import twitter4j.DirectMessage;
import twitter4j.DirectMessageList;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.ArrayList;

public class MensajesDirectos {
    /**
     * Inicio patrón de diseño Singleton
     */
    private static MensajesDirectos INSTANCE = null;

    /**
     * Constructor privado
     */
    private MensajesDirectos(){construirConversacion();}

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

    private void construirConversacion() {
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

    public ArrayList<Chat> getChats() {
        return chats;
    }
}
