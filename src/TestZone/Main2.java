package TestZone;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class Main2 {
    public static void main(String[] args) throws TwitterException {
        motorBot motor = new motorBot();
        motor.inicializarBot();
        motor.publicarTwitter("Hola Mundo!");
    }
}
