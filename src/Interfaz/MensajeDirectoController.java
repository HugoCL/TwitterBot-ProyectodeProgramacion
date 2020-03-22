package Interfaz;

import Motor.*;
import Transiciones.Dialog;
import com.jfoenix.controls.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import twitter4j.DirectMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class MensajeDirectoController {

    private ArrayList<String> followers;
    private ObservableList<Label> listView;
    private VBox chatBox;

    @FXML private JFXButton regresarBT;
    @FXML private JFXButton mostrarBT;
    @FXML private JFXListView<Label> followersLV;
    @FXML private JFXTextArea messageTA;
    @FXML private JFXTextField seguidorTA;
    @FXML private JFXButton enviar_mensajeBT;
    @FXML private ScrollPane container = new ScrollPane();

    @FXML private AnchorPane directMessageAP;
    private boolean enviar;
    private String anterior;
    private Timeline timeline;
    private MensajesDirectos md;

    public void initialize(){
        try{
            md = MensajesDirectos.getInstance();
            Date fechaConsulta = new Date();

            if (md.getFechaAccion() == null){
                new HashtagActions().analizarHashtagActionsMD();
                md.setFechaAccion(new Date());
                md.responderMD();
            }
            else if((fechaConsulta.getTime() - md.getFechaAccion().getTime()) >= 2 * 60 * 1000){
                new HashtagActions().analizarHashtagActionsMD();
                md.setFechaAccion(new Date());
                md.responderMD();
            }
            else{
                System.out.println("Debes esperar para analizar más Tweets");
            }
        }catch(Exception e){
            System.out.println("Error con twitter.");
        }
        followers = new Usuario().getFollowers();
        enviar_mensajeBT.setGraphic(new ImageView(new Image("/Imagenes/sendMessage.png",20,20,false, true)));
        timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> caracteres()), new KeyFrame(Duration.millis(100), e -> busqueda()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        //Caracteres de mensaje
        enviar_mensajeBT.setDisable(true);
        if (followers.isEmpty()) {
            seguidorTA.setDisable(true);
            seguidorTA.setText("NO TIENES SEGUIDORES");
            messageTA.setDisable(true);
        }
        container.getStyleClass().add(0,"container-style");
    }

    private void busqueda() {
        listView = FXCollections.observableArrayList();
        String busqueda = seguidorTA.getText();
        boolean tf;
        if (!busqueda.equalsIgnoreCase(anterior)){
            for (String comparar: followers) {
                tf = true;
                for (int i = 0; busqueda.length() > i; i++)
                    if (comparar.length() < busqueda.length() || busqueda.toLowerCase().charAt(i) != comparar.toLowerCase().charAt(i)) {
                        tf = false;
                        break;
                    }
                if (tf && busqueda.length() != 0){
                    Circle circle = new Circle(10);
                    circle.setFill(new ImagePattern(new Image(Usuario.getUser(comparar).getMiniProfileImageURL())));
                    anterior = busqueda;
                    Label lbl = new Label(comparar);
                    lbl.setGraphic(circle);
                    listView.add(lbl);
                }
                if (listView.size() == 10) break;
            }
            followersLV.setItems(listView);
        }
    }

    private void caracteres(){
        if (!messageTA.getText().isEmpty() && enviar) enviar_mensajeBT.setDisable(false);
        else enviar_mensajeBT.setDisable(true);
    }

    @FXML public void obtenerUsuario() {
        Label usuario;
        if (followersLV.getSelectionModel().getSelectedItem() != null) {
            usuario = followersLV.getSelectionModel().getSelectedItem();
            seguidorTA.setText(usuario.getText());
            listView = FXCollections.observableArrayList();
            listView.add(usuario);
            followersLV.setItems(listView);
        }
    }

    @FXML public void enviarMensaje(){
        String arroba = seguidorTA.getText();
        if (arroba != null){
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
        else {
            Dialog.getInstance().info(enviar_mensajeBT,"Seleccione usuario a enviar mensaje",directMessageAP);
        }
    }

    private void makeChat(int indice) {
        ArrayList<HBox> messages = new ArrayList<>();
        chatBox = new VBox(5);
        chatBox.setMinWidth(430);
        chatBox.getStyleClass().add("chat-style");
        container.setContent(chatBox);
        container.setVvalue(1);
        if (!md.getChats().isEmpty() && indice != -1) {
            System.out.println("Indice dado->"+indice);
            System.out.println("Maximo indice->"+md.getChats().size());
            Chat chat = md.getChats().get(indice);
            ArrayList<DirectMessage> mensajes = chat.getConversacion();
            for (int i = mensajes.size()-1; i >= 0; i--) {
                HBox hbox;
                Label espacio = new Label();
                espacio.setMinWidth(100);
                Label aux = makeLabel(mensajes.get(i).getText());
                if (chat.getUser() == mensajes.get(i).getSenderId()) {
                    aux.setAlignment(Pos.CENTER_LEFT);
                    aux.getStyleClass().add("left-message");
                    hbox = new HBox(5, aux, espacio);
                    hbox.setAlignment(Pos.CENTER_LEFT);
                }
                else{
                    aux.setAlignment(Pos.CENTER_RIGHT);
                    aux.getStyleClass().add("right-message");
                    hbox = new HBox(5, espacio, aux);
                    hbox.setAlignment(Pos.CENTER_RIGHT);
                }
                messages.add(hbox);
            }
            chatBox.getChildren().addAll(messages);
        } else
            Dialog.getInstance().info(mostrarBT,"No hay chat reciente",directMessageAP);
    }

    private Label makeLabel(String i) {
        Label label = new Label(i);
        label.getStyleClass().add("text");
        label.setWrapText(true);
        label.setMinHeight(30);
        label.setMinWidth(30);
        label.setMaxWidth(220);
        return label;
    }

    @FXML public void mostrarChat() {
        long id = Usuario.getIDUsuario(seguidorTA.getText());
        if (id != -1){
            enviar = true;
            int i = md.idCoversation(id);
            makeChat(i);
            return;
        }
        Dialog.getInstance().info(mostrarBT,"Seleccione algún usuario",directMessageAP);
    }

    @FXML public void regresar() throws IOException {
        timeline.stop();
        Transiciones.Slide.getInstance().right("/Interfaz/EscenaPrincipal.fxml", regresarBT, directMessageAP);
    }
}
