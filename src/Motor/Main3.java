package Motor;

import twitter4j.TwitterException;

public class Main3 {
    public static void main(String[] args) throws TwitterException {
        TwitterBot bot = new TwitterBot();
        bot.inicializarBot();
        TwitterBot.Messages mensajes = bot.new Messages();
        mensajes.EnviarMD("BotOverflow", "Nos vamos a sacar un 7");
    }
}
