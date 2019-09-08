package Motor;

import javax.swing.*;
import java.io.File;
import java.util.Scanner;

public class test {
    public static void main(String[] args) {
        adminSesion adm = adminSesion.getInstance();
        TwitterBot botSerializado = adm.desSerializar();
        TwitterBot bot;
        if (botSerializado == null){
            bot = TwitterBot.getInstance();
            bot.setSesion(false);
            bot.inicializarBot();
        }
        else{
            bot = botSerializado;
        }
        TwitterBot.Messages mensajes = bot.new Messages();
        File f = new File("C:\\Users\\Hugo Castro\\Downloads\\3.mp4");
        mensajes.PublicarTweetVideo("oof", f);
    }

}
