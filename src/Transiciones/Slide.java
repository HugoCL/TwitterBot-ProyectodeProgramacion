package Transiciones;

import com.jfoenix.controls.JFXButton;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;

public class Slide {
    /**
     * Inicio patrón de diseño Singleton
     */
    private static Slide INSTANCE = null;
    // Constructor privado
    private Slide(){}
    // Método para evitar multi-hilos
    private synchronized static void createInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Slide();
        }
    }
    public static Slide getInstance() {
        if (INSTANCE == null) createInstance();
        return INSTANCE;
    }
    /**
     * Fin patrón de diseño Singleton
     */
    public void right(String ruta, JFXButton boton, AnchorPane anchorPane) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(ruta));
        Scene scene = boton.getScene();

        root.translateXProperty().set(scene.getWidth());
        StackPane parentContainer = (StackPane) scene.getRoot();
        parentContainer.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateXProperty(),0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.5),kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(event -> {
            parentContainer.getChildren().remove(anchorPane);
        });
        timeline.play();
    }
    public void left(String ruta, JFXButton boton, AnchorPane anchorPane) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(ruta));
        Scene scene = boton.getScene();

        root.translateXProperty().set(scene.getWidth());
        StackPane parentContainer = (StackPane) scene.getRoot();
        parentContainer.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateXProperty(),0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.5),kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(event -> {
            parentContainer.getChildren().remove(anchorPane);
        });
        timeline.play();
    }
}
