package Motor;

import twitter4j.DirectMessage;

import java.util.ArrayList;

public class Chat {

    private long user;

    private ArrayList<DirectMessage> conversacion;

    public Chat(long idUser) {
        user = idUser;
        conversacion = new ArrayList<>();
    }

    public long getUser(){
        return user;
    }

    public ArrayList<DirectMessage> getConversacion() {
        return conversacion;
    }

}
