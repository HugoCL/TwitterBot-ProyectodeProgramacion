package Interfaz;

import Motor.TwitterBot;
import com.jfoenix.controls.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import twitter4j.TwitterException;

import javax.xml.soap.Text;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class MensajeDirectoController {

    //inicialiar bot
    private TwitterBot bot = TwitterBot.getInstance().getBOT();

    private ArrayList<String> followers;
    private ObservableList<String> listView;

    @FXML private JFXButton regresarBT;
    @FXML private JFXListView<String> followersLV;
    @FXML private JFXTextArea messageTA;
    @FXML private JFXTextField seguidorTA;
    @FXML private JFXButton enviar_mensajeBT;

    @FXML private AnchorPane directMessageAP;

    public void initialize(){
        seguidorTA.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) { Busqueda(); }
        });
        //Caracteres de mensaje
        KeyFrame frame = new KeyFrame(Duration.millis(100), e -> Caracteres());
        Timeline timeline = new Timeline(frame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        followers = bot.new Usuario().getFollowers();
        if (followers.isEmpty()) {seguidorTA.setDisable(true); seguidorTA.setText("NO TIENES SEGUIDORES"); messageTA.setDisable(true);}
    }

    public void Busqueda() {
        listView = FXCollections.observableArrayList();
        String busqueda = seguidorTA.getText();
        boolean tf;
        for (String comparar: followers) {
            tf = true;
            for (int i = 0; tf && busqueda.length() > i; i++) {
                if (comparar.length() < busqueda.length() || busqueda.charAt(i) != comparar.charAt(i))
                    tf = false;
            }
            if (tf && busqueda.length() != 0) listView.add(comparar);
            if (listView.size() == 10) break;
        }
        followersLV.setItems(listView);
    }

    public void Caracteres(){
        if (!messageTA.getText().isEmpty()) {
            enviar_mensajeBT.setDisable(false);
        }
        else {
            enviar_mensajeBT.setDisable(true);
        }
    }

    @FXML public void ObtenerUsuario() {
        String usuario;
        if (followersLV.getSelectionModel().getSelectedItem() != null) {
            usuario = followersLV.getSelectionModel().getSelectedItem();
            seguidorTA.setText(usuario);
            listView = FXCollections.observableArrayList();
            listView.add(usuario);
            followersLV.setItems(listView);
        }
    }

    @FXML public void enviarMensaje() throws TwitterException {
        String arroba = followersLV.getSelectionModel().getSelectedItem();
        String mensaje = messageTA.getText();
        TwitterBot.Messages mensajes = bot.new Messages();
        mensajes.EnviarMD(arroba,mensaje);
        messageTA.setText("");
    }
    @FXML public void regresar() throws IOException {
        System.out.println("Cargando ventana principal...");
        Transiciones.Slide.getInstance().right("/Interfaz/EscenaPrincipal.fxml", regresarBT, directMessageAP);
    }
}
