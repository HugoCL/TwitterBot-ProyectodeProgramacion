package Interfaz;

import Motor.TwitterBot;
import Motor.adminSesion;
import com.jfoenix.controls.JFXButton;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.text.TableView;
import java.io.IOException;
import java.net.URL;

public class EscenaPrincipalController {

    @FXML private AnchorPane mainAP;

    @FXML private JFXButton tweetearBT;
    @FXML private JFXButton retweetearBT;
    @FXML private JFXButton followBT;
    @FXML private JFXButton directBT;
    @FXML private JFXButton cerrar_sesionBT;

    public void initialize(){
        //Botones desactivados
        retweetearBT.setDisable(true);
        followBT.setDisable(true);
    }

    @FXML public void tweetear() throws IOException {
        System.out.println("Cargando ventana para twittear...");

        Parent root = FXMLLoader.load(getClass().getResource("/Interfaz/Twittear.fxml"));
        Scene scene = tweetearBT.getScene();

        root.translateXProperty().set(scene.getWidth());
        StackPane parentContainer = (StackPane) scene.getRoot();
        parentContainer.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateXProperty(),0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.5),kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(event -> {
            parentContainer.getChildren().remove(mainAP);
        });
        timeline.play();
    }

    @FXML public void retweetear(){

    }

    @FXML public void follow() throws IOException {
        System.out.println("Cargando ventana para realizar follow...");

        Parent root = FXMLLoader.load(getClass().getResource("/Interfaz/Follow.fxml"));
        Scene scene = tweetearBT.getScene();

        root.translateXProperty().set(scene.getWidth());
        StackPane parentContainer = (StackPane) scene.getRoot();
        parentContainer.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateXProperty(),0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.5),kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(event -> {
            parentContainer.getChildren().remove(mainAP);
        });
        timeline.play();
    }
    @FXML public void directMessage() throws IOException {
        //Creación de ventana
        FXMLLoader loader = new FXMLLoader();

        URL location = MensajeDirectoController.class.getResource("MensajeDirecto.fxml");
        loader.setLocation(location);
        AnchorPane newBP = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(newBP);
        stage.setScene(scene);
        stage.setTitle("Twitter Bot - Ramos Overflow");
        stage.getIcons().add(new Image("Imagenes/Icono.png"));
        stage.show();
    }
    @FXML public void cerrarSesion() throws IOException {
        TwitterBot.getInstance().getBOT().isGuardado = false;
        adminSesion.getInstance().Serializar(TwitterBot.getInstance().getBOT());
        System.out.println("Cerrando sesión...");

        Scene scene = cerrar_sesionBT.getScene();

        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.seconds(0.5));
        StackPane rootPane = (StackPane) scene.getRoot();
        fadeTransition.setNode(rootPane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/Interfaz/InicioSesion.fxml"));
                    Scene scene1 = new Scene(root);
                    Stage stage = (Stage) rootPane.getScene().getWindow();
                    stage.setScene(scene1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        fadeTransition.play();
    }
}
