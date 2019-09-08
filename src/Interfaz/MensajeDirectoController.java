package Interfaz;

import Motor.TwitterBot;
import Transiciones.Dialog;
import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import twitter4j.TwitterException;

import java.io.IOException;
import java.util.ArrayList;

public class MensajeDirectoController {

    //inicializar bot
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
        seguidorTA.setOnKeyReleased(event -> Busqueda());
        //Caracteres de mensaje
        enviar_mensajeBT.setDisable(true);
        followers = bot.new Usuario().getFollowers();
        if (followers.isEmpty()) {
            seguidorTA.setDisable(true);
            seguidorTA.setText("NO TIENES SEGUIDORES");
            messageTA.setDisable(true);
        }
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
        if (!messageTA.getText().isEmpty()) {enviar_mensajeBT.setDisable(false);}
        else {enviar_mensajeBT.setDisable(true);}
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
        String respuesta = mensajes.EnviarMD(arroba,mensaje);
        Dialog.getInstance().info(enviar_mensajeBT,respuesta,"OK, revisaré",directMessageAP);
        messageTA.setText("");
    }
    @FXML public void regresar() throws IOException {
        Transiciones.Slide.getInstance().right("/Interfaz/EscenaPrincipal.fxml", regresarBT, directMessageAP);
    }
}
