package Interfaz;

import Motor.MensajesDirectos;
import Motor.Messages;
import Motor.Usuario;
import Transiciones.Dialog;
import com.jfoenix.controls.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;

public class MensajeDirectoController {

    private ArrayList<String> followers;
    private ObservableList<String> listView;

    @FXML private JFXButton regresarBT;
    @FXML private JFXListView<String> followersLV;
    @FXML private JFXTextArea messageTA;
    @FXML private JFXTextField seguidorTA;
    @FXML private JFXButton enviar_mensajeBT;

    @FXML private AnchorPane directMessageAP;

    private Timeline timeline;
    public void initialize(){
        followers = new Usuario().getFollowers();
        enviar_mensajeBT.setGraphic(new ImageView(new Image("/Imagenes/sendMessage.png",20,20,false, true)));
        timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> caracteres()), new KeyFrame(Duration.millis(100), e -> busqueda()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        new MensajesDirectos().construirConversacion();
        //Caracteres de mensaje
        enviar_mensajeBT.setDisable(true);
        if (followers.isEmpty()) {
            seguidorTA.setDisable(true);
            seguidorTA.setText("NO TIENES SEGUIDORES");
            messageTA.setDisable(true);
        }
    }

    public void busqueda() {
        listView = FXCollections.observableArrayList();
        String busqueda = seguidorTA.getText();
        boolean tf;
        for (String comparar: followers) {
            tf = true;
            for (int i = 0; tf && busqueda.length() > i; i++) {
                if (comparar.length() < busqueda.length() || busqueda.toLowerCase().charAt(i) != comparar.toLowerCase().charAt(i))
                    tf = false;
            }
            if (tf && busqueda.length() != 0) listView.add(comparar);
            if (listView.size() == 10) break;
        }
        followersLV.setItems(listView);
    }

    public void caracteres(){
        if (!messageTA.getText().isEmpty()) {enviar_mensajeBT.setDisable(false);}
        else {enviar_mensajeBT.setDisable(true);}
    }

    @FXML public void obtenerUsuario() {
        String usuario;
        if (followersLV.getSelectionModel().getSelectedItem() != null) {
            usuario = followersLV.getSelectionModel().getSelectedItem();
            seguidorTA.setText(usuario);
            listView = FXCollections.observableArrayList();
            listView.add(usuario);
            followersLV.setItems(listView);
        }
    }

    @FXML public void enviarMensaje(){
        String arroba = followersLV.getSelectionModel().getSelectedItem();
        String mensaje = messageTA.getText();
        Messages mensajes = new Messages();
        String respuesta = mensajes.EnviarMD(arroba,mensaje);
        Dialog.getInstance().info(enviar_mensajeBT,respuesta,directMessageAP);
        messageTA.setText("");
    }
    @FXML public void regresar() throws IOException {
        timeline.stop();
        Transiciones.Slide.getInstance().right("/Interfaz/EscenaPrincipal.fxml", regresarBT, directMessageAP);
    }
}
