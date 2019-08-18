package Motor;

import twitter4j.TwitterException;

import java.io.IOException;

public class Main3 {
    public static void main(String[] args) throws TwitterException, IOException {
        TwitterBot bot = null;
        boolean exito = false;
        adminSesion adm = new adminSesion();
        TwitterBot botSerializado = adm.desSerializar();
        if (botSerializado == null){
            bot = new TwitterBot();
            bot.inicializarBot();
            bot.OAuth();
            adm.Serializar(bot);
        }
        else{
            bot = botSerializado;
        }
        TwitterBot.Messages mensajes = bot.new Messages();
        mensajes.PublicarTweet("Estoy probando la serialización x2");
        /*
        TwitterBot.Messages mensajes = bot.new Messages();
        Scanner entrada = new Scanner(System.in);
        while(!exito){
            System.out.println("Ingrese el Tweet a enviar:");
            String tweet = entrada.nextLine();
            if (tweet.length() > 280){
                System.out.println("Te pasaste del limite de caracteres :(");
            }
            else{
                System.out.println("Enviando...");
                mensajes.PublicarTweet(tweet);
                System.out.println("Se Tweeteo: "+tweet+". Enviado correctamente.");
                exito = true;
            }
        }
        */
    }
}
