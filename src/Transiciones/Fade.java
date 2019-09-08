package Transiciones;

import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class Fade {
    /**
     * Inicio patrón de diseño Singleton
     */
    private static Fade INSTANCE = null;
    // Constructor privado
    private Fade(){}
    // Método para evitar multi-hilos
    private synchronized static void createInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Fade();
        }
    }
    public static Fade getInstance() {
        if (INSTANCE == null) createInstance();
        return INSTANCE;
    }
    /**
     * Fin patrón de diseño Singleton
     */
    public void in(StackPane parentContainer){
        //Ventana
        parentContainer.setOpacity(0);
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.seconds(0.25));
        fadeTransition.setNode(parentContainer);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }
    public void out(String ruta, JFXButton boton){
        Scene scene = boton.getScene();
        FadeTransition fadeTransition = new FadeTransition();
        fadeTransition.setDuration(Duration.seconds(0.25));
        StackPane rootPane = (StackPane) scene.getRoot();
        fadeTransition.setNode(rootPane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource(ruta));
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
