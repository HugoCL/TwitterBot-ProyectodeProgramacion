package Interfaz;

import Motor.*;
import Transiciones.Dialog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import twitter4j.TwitterException;

import java.io.IOException;
import java.util.ArrayList;

public class EscenaPrincipalController {

    @FXML private AnchorPane mainAP;
    @FXML private AnchorPane secondAP;

    @FXML private JFXButton tweetearBT;
    @FXML private JFXButton followBT;
    @FXML private JFXButton directBT;
    @FXML private JFXButton timelineBT;
    @FXML private JFXButton cerrar_sesionBT;

    @FXML private Text usernameTX;

    @FXML private ScrollPane scroll = new ScrollPane();

    @FXML private JFXSpinner spinner;

    private Thread hilo;
    private Thread visivilidades;
    private static ArrayList<Tweet> serializados;
    private static boolean isSerializado;

    private static ArrayList<Tweet> listaTweets;

    private static VBox vbox;
    private static VBox aux;
    private static boolean inicioCarga, finCarga;

    //Classes
    private Feed feed = new Feed();
    private boolean ejecutar;
    private boolean excuteV;

    public void initialize() throws TwitterException {

        //Obtener nombre de usuario
        usernameTX.setText(new Usuario().getNombreUsuario());
        ejecutar = true;
        excuteV = true;
        vbox = new VBox(4);
        aux = new VBox();
        //Botones desactivados
        secondAP.setVisible(false);
        //Cargar botones
        tweetearBT.setGraphic(new ImageView(new Image("/Imagenes/tweet.png",50,50,false, true)));
        timelineBT.setGraphic(new ImageView(new Image("/Imagenes/home.png",50,50,false, true)));
        followBT.setGraphic(new ImageView(new Image("/Imagenes/follow.png",50,50,false, true)));
        directBT.setGraphic(new ImageView(new Image("/Imagenes/message.png",50,50,false, true)));
        cerrar_sesionBT.setGraphic(new ImageView(new Image("/Imagenes/logout.png",30,30,false, true)));
        scroll.getStyleClass().add("scroll");
        visivilidades = new Thread(()->{
            while (excuteV){
                if (!finCarga){
                    spinner.setVisible(true);
                    timelineBT.setDisable(true);
                }else{
                    spinner.setVisible(false);
                    timelineBT.setDisable(false);
                }
            }
        });
        visivilidades.start();
        if(!inicioCarga){
            hilo = new Thread(this::cargarScroll);
            hilo.start();
        }
    }

    @FXML public void tweetear() throws IOException {
        excuteV = false;
        Transiciones.Slide.getInstance().left("/Interfaz/Twittear.fxml",tweetearBT,mainAP);
    }

    @FXML public void timeline() {
        if (vbox.getChildren().size() != 0){
            scroll.setContent(vbox);
            scroll.setVisible(true);
            botonesMain(true);
            secondAP.setVisible(true);
        }
        else {
            Dialog.getInstance().info(timelineBT,"No hay últimos mensajes, Intentelo más tarde",mainAP);
        }
    }

    @FXML public void cerrarTimeline() throws IOException {
        if (isSerializado){
            AdminBackup.getInstance().serializar(serializados);
        }
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
        timelineBT.setDisable(bool);
        followBT.setDisable(bool);
        directBT.setDisable(bool);
    }

    private void cargarScroll() {
        spinner.setVisible(true);
        timelineBT.setDisable(true);
        inicioCarga = true;
        while(ejecutar){
            aux = new VBox();
            listaTweets = new ArrayList<>();
            try {
                listaTweets = feed.ObtenerTweets();
                System.out.println(listaTweets.size());
                if (listaTweets.size() != 0){
                    isSerializado = false;
                    for (int i = 0; ejecutar && i < listaTweets.size(); i++) {
                        //new Messages().screenNameRespuesta(tweet.getScreenName(), tweet.getId());
                        aux.getChildren().add(CellVBox.crearGridPane(listaTweets.get(i), mainAP, vbox, scroll));
                    }
                }
                else{
                    serializados = AdminBackup.getInstance().deserializar();
                    isSerializado = true;
                    if (serializados != null && serializados.size() != 0){
                        for (int i = 0; ejecutar && i < serializados.size(); i++) {
                            aux.getChildren().add(CellVBox.crearGridPane(serializados.get(i), mainAP, vbox, scroll));
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            vbox.getChildren().addAll(aux.getChildren());
            finCarga = true;
            try {
                Thread.sleep(300000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}