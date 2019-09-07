package Interfaz;

import Motor.TwitterBot;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import twitter4j.TwitterException;

import java.io.IOException;
import java.util.ArrayList;

public class MensajeDirectoController {

    //inicialiar bot
    private TwitterBot bot = TwitterBot.getInstance().getBOT();

    private ArrayList<String> followers;

    @FXML private JFXButton regresarBT;
    @FXML private JFXComboBox<String> followersCB;
    @FXML private JFXTextArea messageTA;
    @FXML private JFXTextArea seguidorTA;
    @FXML private JFXButton enviar_mensajeBT;

    @FXML private AnchorPane directMessageAP;

    public void initialize(){
        //Caracteres de mensaje
        KeyFrame frame = new KeyFrame(Duration.millis(100), (e -> {
            Caracteres();
            Busqueda(seguidorTA.getText());
        }));
        Timeline timeline = new Timeline(frame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        followers = bot.new Usuario().getFollowers();
    }
    public void Busqueda(String busqueda) {
        ArrayList<String> resultados = null;
        boolean tf;
        if (busqueda.length() != 0) {
            for (String comparar: followers) {
                tf = true;
                for (int i = 0; tf && busqueda.length() > i; i++) {
                    if (comparar.length() < busqueda.length() && busqueda.charAt(i) == comparar.charAt(i))
                        tf = false;
                }
                if (tf) resultados.add(comparar);
                if (resultados.size() == 10) break;
            }
        }
    }

    public void Caracteres(){
        if (!messageTA.getText().isEmpty()) {
            enviar_mensajeBT.setDisable(false);
        }
        else {
            enviar_mensajeBT.setDisable(true);
        }
    }
    @FXML public void enviarMensaje() throws TwitterException {
        String arroba = followersCB.getValue();
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
