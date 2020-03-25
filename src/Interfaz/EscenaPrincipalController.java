package Interfaz;

import Motor.*;
import Transiciones.Dialog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EscenaPrincipalController {

    @FXML private AnchorPane mainAP;
    @FXML private AnchorPane secondAP;

    @FXML private JFXButton tweetearBT;
    @FXML private JFXButton followBT;
    @FXML private JFXButton directBT;
    @FXML private JFXButton timelineBT;
    @FXML private JFXButton cerrar_sesionBT;
    @FXML private JFXButton hashtagBT;

    @FXML private Text usernameTX;
    @FXML private JFXTextField hashTF;

    @FXML private ScrollPane scroll = new ScrollPane();

    @FXML private JFXSpinner spinner;

    @FXML private static VBox vbox;
    @FXML private static VBox vboxHash;

    private static ArrayList<Node> aux;
    private static ArrayList<Node> auxHashTagNodes;
    private static ArrayList<Tweet> auxTweets;
    private static boolean inicioCarga, finCarga;

    //Classes
    private Feed feed = new Feed();
    private static boolean ejecutar, reloadTimeline;
    private boolean excuteV;

    public void initialize() throws TwitterException {
        auxHashTagNodes = new ArrayList<>();
        //Obtener nombre de usuario
        usernameTX.setText(new Usuario().getNombreUsuario());
        ejecutar = true;
        excuteV = true;
        //Botones desactivados
        secondAP.setVisible(false);
        vboxHash = new VBox(4);
        vboxHash.setVisible(false);
        //Cargar botones
        hashtagBT.setGraphic(new ImageView(new Image("Imagenes/lupa.png", 20,20,false,true)));
        tweetearBT.setGraphic(new ImageView(new Image("/Imagenes/tweet.png",50,50,false, true)));
        timelineBT.setGraphic(new ImageView(new Image("/Imagenes/home.png",50,50,false, true)));
        followBT.setGraphic(new ImageView(new Image("/Imagenes/follow.png",50,50,false, true)));
        directBT.setGraphic(new ImageView(new Image("/Imagenes/message.png",50,50,false, true)));
        cerrar_sesionBT.setGraphic(new ImageView(new Image("/Imagenes/logout.png",25,25,false, true)));
        scroll.getStyleClass().add("scroll");
        Thread visivilidades = new Thread(() -> {
            while (excuteV) {
                if (!finCarga) {
                    spinner.setVisible(true);
                    timelineBT.setDisable(true);
                } else {
                    spinner.setVisible(false);
                    timelineBT.setDisable(false);
                }
            }
        });
        visivilidades.start();
        if(!inicioCarga){
            Thread hilo = new Thread(this::cargarScroll);
            hilo.start();
        }
    }

    @FXML public void tweetear() throws IOException {
        excuteV = false;
        Transiciones.Slide.getInstance().left("/Interfaz/Twittear.fxml",tweetearBT,mainAP);
    }

    @FXML public void timeline() {
        vboxHash.setVisible(false);
        if(reloadTimeline && !aux.isEmpty()){
            vbox = new VBox(4);
            reloadTimeline = false;
            for (Node nodo: aux)
                vbox.getChildren().addAll(nodo);
            aux.clear();
        }
        if (vbox.getChildren().size() != 0){
            vbox.setVisible(true);
            scroll.setContent(vbox);
            scroll.setVisible(true);
            botonesMain(true);
            secondAP.setVisible(true);
        }
        else
            Dialog.getInstance().info(timelineBT,"No hay últimos mensajes, intentelo más tarde",mainAP);
    }

    @FXML public void cerrarTimeline(){
        botonesMain(false);
        secondAP.setVisible(false);
    }

    @FXML public void follow() throws IOException {
        excuteV = false;
        Transiciones.Slide.getInstance().left("/Interfaz/Follow.fxml", followBT, mainAP);

    }

    @FXML public void directMessage() throws IOException {
        excuteV = false;
        Transiciones.Slide.getInstance().left("/Interfaz/MensajeDirecto.fxml", directBT, mainAP);
    }

    @FXML public void cerrarSesion() throws IOException {
        TwitterBot.getInstance().getBOT().setSesion(false);
        AdminSesion.getInstance().serializar(TwitterBot.getInstance().getBOT());
        AdminBackup.getInstance().serializar(new ArrayList<>());
        vbox = new VBox();
        ejecutar = false;
        excuteV = false;
        inicioCarga = false;
        finCarga = false;
        Transiciones.Fade.getInstance().out("/Interfaz/InicioSesion.fxml", cerrar_sesionBT);
    }

    private void botonesMain(boolean bool){
        tweetearBT.setDisable(bool);
        followBT.setDisable(bool);
        directBT.setDisable(bool);
    }

    @FXML
    public void searchHashtag() {
        String cad = null;
        if (!hashTF.getText().isEmpty() && (cad = splitHash(hashTF.getText())) != null) {
            if (!auxTweets.isEmpty()) {
                vboxHash = new VBox(4);
                Pattern pattern = Pattern.compile("(.*?) #" + cad + " (.*?)|^#" + cad + "$|(.*?) #" + cad + "$|#" + cad + " (.*?)");
                for (Tweet tweet : auxTweets){
                    Matcher matcher = pattern.matcher(tweet.getMensaje());
                    if (matcher.find())
                        auxHashTagNodes.add(CellVBox.crearGridPane(tweet, mainAP, scroll));
                }
                if (!auxHashTagNodes.isEmpty()) {
                    for (Node nodo: auxHashTagNodes)
                        vboxHash.getChildren().addAll(nodo);
                    vboxHash.setVisible(true);
                    scroll.setContent(vboxHash);
                    vbox.setVisible(false);
                    auxHashTagNodes.clear();
                } else
                    Dialog.getInstance().info(hashtagBT, "No se encontró hashtag", secondAP);
            } else
                Dialog.getInstance().info(hashtagBT, "No hay mensajes", secondAP);
        } else
            Dialog.getInstance().info(hashtagBT, "No se ingreso parametro", secondAP);
    }

    private String splitHash(String cadena) {
        String aux = null;
        String[] cads = cadena.split("(?=#)|\\s");
        for (String cad : cads) {
            if (cad.length() != 0 && cad.charAt(0) == '#') {
                aux = cad.substring(1);
                break;
            }
        }
        return aux;
    }

    public static VBox getVbox() {
        return vbox;
    }

    private void cargarScroll() {
        System.out.println("inicio");
        spinner.setVisible(true);
        timelineBT.setDisable(true);
        inicioCarga = true;
        vbox = new VBox(4);
        while(ejecutar){
            reloadTimeline = false;
            aux = new ArrayList<>();
            try {
                auxTweets = new ArrayList<>();
                ArrayList<Tweet> listaTweets = feed.ObtenerTweets();
                System.out.println(listaTweets.size());
                if (listaTweets.size() != 0){
                    auxTweets.addAll(listaTweets);
                    AdminBackup.getInstance().serializar(listaTweets);
                    for (int i = 0; ejecutar && i < listaTweets.size(); i++){
                        Messages.isSpam(listaTweets.get(i).getId());
                        aux.add(CellVBox.crearGridPane(listaTweets.get(i), mainAP, scroll));
                    }
                }
                else{
                    ArrayList<Tweet> serializados = AdminBackup.getInstance().deserializar();
                    if (serializados != null && serializados.size() != 0){
                        for (int i = 0; ejecutar && i < serializados.size(); i++) {
                            if (Messages.getTweet(serializados.get(i).getId()) != null)
                                aux.add(CellVBox.crearGridPane(serializados.get(i), mainAP, scroll));
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            reloadTimeline = true;
            finCarga = true;
            for (int i = 0; ejecutar && i < 120000 ;i++){
                try {
                    Thread.sleep(1 );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("fin");
    }
}