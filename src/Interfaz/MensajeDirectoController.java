package Interfaz;

import Motor.Chat;
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
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import twitter4j.DirectMessage;

import java.io.IOException;
import java.util.ArrayList;

public class MensajeDirectoController {

    private ArrayList<String> followers;
    private ObservableList<String> listView;
    private VBox chatBox;

    @FXML private JFXButton regresarBT;
    @FXML private JFXListView<String> followersLV;
    @FXML private JFXTextArea messageTA;
    @FXML private JFXTextField seguidorTA;
    @FXML private JFXButton enviar_mensajeBT;
    @FXML private ScrollPane container = new ScrollPane();

    @FXML private AnchorPane directMessageAP;

    private Timeline timeline;
    private MensajesDirectos md;

    public void initialize(){
        followers = new Usuario().getFollowers();
        enviar_mensajeBT.setGraphic(new ImageView(new Image("/Imagenes/sendMessage.png",20,20,false, true)));
        timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> caracteres()), new KeyFrame(Duration.millis(100), e -> busqueda()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        md = new MensajesDirectos();
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
        if (respuesta.equals("Mensaje enviado correctamente")){
            Label aux = makeLabel(mensaje);
            aux.setAlignment(Pos.CENTER_RIGHT);
            Label espacio = new Label();
            espacio.setMinWidth(100);
            HBox hbox = new HBox(5, espacio, aux);
            hbox.setAlignment(Pos.CENTER_RIGHT);
        }
        Dialog.getInstance().info(enviar_mensajeBT,respuesta,directMessageAP);
        messageTA.setText("");
    }

    private void makeChat(int indice) {
        ArrayList<HBox> messages = new ArrayList<>();
        chatBox = new VBox(5);
        chatBox.setMinWidth(440);
        container.setContent(chatBox);
        Chat chat = md.getChats().get(indice);
        ArrayList<DirectMessage> mensajes = chat.getConversacion();
        for (int i = mensajes.size()-1; i >= 0; i--) {
            HBox hbox;
            Label espacio = new Label();
            espacio.setMinWidth(100);
            Label aux = makeLabel(mensajes.get(i).getText());
            if (chat.getUser() == mensajes.get(i).getSenderId()) { aux.setAlignment(Pos.CENTER_LEFT); hbox = new HBox(5, aux, espacio); hbox.setAlignment(Pos.CENTER_LEFT);}
            else            { aux.setAlignment(Pos.CENTER_RIGHT); hbox = new HBox(5, espacio, aux); hbox.setAlignment(Pos.CENTER_RIGHT);}
            messages.add(hbox);
        }
        chatBox.getChildren().addAll(messages);
    }

    private Label makeLabel(String i) {
        Label label = new Label(i);
        label.getStyleClass().add("text");
        label.setWrapText(true);
        label.setMinHeight(30);
        label.setMinWidth(30);
        label.setMaxWidth(190);
        return label;
    }

    @FXML public void mostrarChat() {
        long id = Usuario.getIDUsuario(seguidorTA.getText());
        if (id != -1){
            int i = md.idCoversation(id);
            makeChat(i);
            return;
        }
        System.out.println("No existe usuario");
    }

    @FXML public void regresar() throws IOException {
        timeline.stop();
        Transiciones.Slide.getInstance().right("/Interfaz/EscenaPrincipal.fxml", regresarBT, directMessageAP);
    }
}
