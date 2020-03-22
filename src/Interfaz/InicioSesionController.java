package Interfaz;

import Motor.*;
import Transiciones.Dialog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import twitter4j.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import twitter4j.conf.ConfigurationBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class InicioSesionController {

    private TwitterBot bot;
    private HashtagActions hashtagActions;

    @FXML private JFXPasswordField pinPF;
    @FXML private JFXCheckBox no_cierre_sesionCB;
    @FXML private JFXButton iniciar_sesionBT;
    @FXML private JFXButton copyBT;
    @FXML private JFXButton cerrarBT;
    @FXML private Label infoLB;
    @FXML private String enlace;

    @FXML private AnchorPane inicioSesionAP;

    @FXML private StackPane parentContainer;

    public void initialize() throws TwitterException {
        cerrarBT.setGraphic(new ImageView(new Image("/Imagenes/logout.png",25,25,false, true)));
        copyBT.setGraphic(new ImageView(new Image("/Imagenes/copy.png",35,35,false, true)));
        AdminSesion adm = AdminSesion.getInstance();
        pinPF.setText("");
        TwitterBot botSerializado = adm.desSerializar();
        bot = TwitterBot.getInstance();
        if (botSerializado == null){
            bot.setSesion(false);
            bot.inicializarBot();
        }
        else{
            bot = botSerializado;
        }
        if (bot.getSesion()) {
            pinPF.setEditable(false);
            pinPF.setText(bot.getPin());
            no_cierre_sesionCB.setSelected(true);
            TwitterBot.getInstance().setBOT(bot);
            try{
                infoLB.setText("Sesión iniciada con: \n"+new Usuario().getNombreUsuario());
            }catch (Exception e){
                System.err.print("ERROR: ");System.out.print("Se necesita conexión a internet.");
                System.exit(0);
            }
            infoLB.setVisible(true);
            cerrarBT.setVisible(true);
            copyBT.setVisible(false);
        }else{
            pinPF.setEditable(true);
            infoLB.setVisible(false);
            cerrarBT.setVisible(false);
            copyBT.setVisible(true);
        }
        //FadeIn
        Transiciones.Fade.getInstance().in(parentContainer);

    }

    @FXML public void iniciarSesion() throws IOException, TwitterException {
        String respuesta;
        if (!bot.getSesion()){
            String pin = pinPF.getText();
            respuesta = bot.OAuthInicio(pin);
            if (respuesta.compareTo("PIN Correcto") == 0) {
                if (no_cierre_sesionCB.isSelected()){
                    bot.setSesion(true);
                    bot.setPin(pinPF.getText());
                    AdminSesion.getInstance().serializar(bot);
                }
                TwitterBot.getInstance().setBOT(bot);
            } else {
                Dialog.getInstance().info(iniciar_sesionBT,respuesta,inicioSesionAP);
                this.initialize();
                return;}
        }else {
            if (!no_cierre_sesionCB.isSelected()){
                bot.setSesion(false);
                AdminSesion.getInstance().serializar(bot);
                TwitterBot.getInstance().setBOT(bot);
            }
        }

        ConfigurationBuilder cbTS = new ConfigurationBuilder();
        cbTS.setDebugEnabled(true);
        cbTS.setOAuthConsumerKey("y3rodATEKk9OopeZb3bJ49k7L");
        cbTS.setOAuthConsumerSecret("eCkLQgglSpvdD7nUiU6hoH2hoWYEWASAAMRWkfuTyqnhUxLfr0");
        cbTS.setOAuthAccessToken(bot.getAccessToken().getToken());
        cbTS.setOAuthAccessTokenSecret(bot.getAccessToken().getTokenSecret());
        hashtagActions = new HashtagActions();
        Date fechaAnalisis = null;
        try {
            FileInputStream fileInputStream = new FileInputStream("TimeStamp#Actions.out");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            fechaAnalisis = (Date) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No se encontró el archivo serializado");
        }

        Query query = new Query("@"+bot.getTwitter().getScreenName());
        if (fechaAnalisis == null) {
            Calendar fecha = new GregorianCalendar();
            fecha.add(Calendar.DAY_OF_MONTH, -14);
            int anio = fecha.get(Calendar.YEAR);
            int mes = fecha.get(Calendar.MONTH);
            int dia = fecha.get(Calendar.DAY_OF_MONTH);
            System.out.println(anio+"-"+(mes+1)+"-"+dia);
            query.setSince(anio+"-"+(mes+1)+"-"+(dia));
        } else {
            System.out.println(fechaAnalisis.toString());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            System.out.println("--------------------------");
            System.out.println(sdf.format(fechaAnalisis));
            query.setSince(sdf.format(fechaAnalisis));
        }

        QueryResult result;
        Twitter twitter = TwitterBot.getInstance().getBOT().getTwitter();
        int count = 0;
        do {
            result = twitter.search(query);
            List<Status> tweets = result.getTweets();

            for (int i = 0;count <= 300 && i < tweets.size();i++) {
                hashtagActions.analizarHashtagActions(tweets.get(i));
                count++;
            }
        } while (count <= 300 && (query = result.nextQuery()) != null);

        TwitterStream twitterStream = new TwitterStreamFactory(cbTS.build()).getInstance();
        twitterStream.addListener(new StatusListener() {
            @Override
            public void onStatus(Status status) {
                try {
                    hashtagActions.analizarHashtagActions(status);
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                System.out.println("On deletion status");
            }

            @Override
            public void onTrackLimitationNotice(int i) {
                System.out.println("Limite alcanzado: +i");
            }

            @Override
            public void onScrubGeo(long l, long l1) {
                System.out.println("Scrub geo detectado");
            }

            @Override
            public void onStallWarning(StallWarning stallWarning) {
                System.out.println("Stall warning");
            }

            @Override
            public void onException(Exception e) {
                e.printStackTrace();
            }
        }).filter(setFilterTS());

        //Analizar Hashtags DM
        //hashtagActions.analizarHashtagActionsMD();
        //Transición de escenas
        Transiciones.Slide.getInstance().left("/Interfaz/EscenaPrincipal.fxml",iniciar_sesionBT, inicioSesionAP);
    }

    private FilterQuery setFilterTS() throws TwitterException {
        FilterQuery filterQuery = new FilterQuery();
        filterQuery.track("@"+bot.getTwitter().getScreenName());
        return filterQuery;
    }

    @FXML public void copiar() {
        bot.inicializarBot();
        enlace = bot.OAuthURL();
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(enlace);
        clipboard.setContent(content);

        Dialog.getInstance().info(copyBT,"Enlace copiado",inicioSesionAP);
    }

    @FXML public void cerrarSesion() throws IOException, TwitterException {
        bot.setSesion(false);
        AdminSesion.getInstance().serializar(bot);
        TwitterBot.getInstance().setBOT(bot);
        AdminBackup.getInstance().serializar(new ArrayList<>());
        this.initialize();
    }
}
