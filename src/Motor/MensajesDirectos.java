package Motor;

import twitter4j.DirectMessage;
import twitter4j.DirectMessageList;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.ArrayList;

public class MensajesDirectos {
    private static Twitter twitter = TwitterBot.getInstance().getBOT().getTwitter();

    private static ArrayList<Chat> chats;

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

        for (Chat chat : chats){
            System.out.println("---------------------");
            ArrayList<DirectMessage> conversacion = chat.getConversacion();
            for (int i = conversacion.size()-1; i >= 0; i--) System.out.println(conversacion.get(i).getText());
        }
    }

    private int idCoversation(long id) {
        for (int i = 0; i < chats.size(); i++)
            if (chats.get(i).getUser() == id) return i;
        return -1;
    }
}
