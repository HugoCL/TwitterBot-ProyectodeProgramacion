package Motor;

import twitter4j.TwitterException;

import java.util.Scanner;

public class Main3 {
    public static void main(String[] args) throws TwitterException {
        boolean exito = false;
        TwitterBot bot = new TwitterBot();
        bot.inicializarBot();
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

    }
}
