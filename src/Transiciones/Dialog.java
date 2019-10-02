package Transiciones;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class Dialog {
    /**
     * Inicio patrón de diseño Singleton
     */
    private static Dialog INSTANCE = null;
    // Constructor privado
    private Dialog(){}
    // Método para evitar multi-hilos
    private synchronized static void createInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Dialog();
        }
    }
    public static Dialog getInstance() {
        if (INSTANCE == null) createInstance();
        return INSTANCE;
    }
    /**
     * Fin patrón de diseño Singleton
     */
    @FXML protected JFXDialogLayout dialogLayout = new JFXDialogLayout();
    @FXML protected JFXButton button = new JFXButton();

    protected BoxBlur boxBlur = new BoxBlur(3,3,3);

    public void info(JFXButton boton, String texto, AnchorPane AP){
        AP.setDisable(true);
        Scene scene = boton.getScene();
        StackPane rootpane = (StackPane) scene.getRoot();

        dialogLayout.setPrefWidth(350);
        dialogLayout.setAlignment(Pos.CENTER);
        JFXDialog dialog = new JFXDialog(rootpane,dialogLayout,JFXDialog.DialogTransition.TOP);

        Label label = new Label();
        label.setPrefWidth(300);
        label.setText(texto);
        label.setFont(new Font("Segoe UI",16));
        label.setAlignment(Pos.CENTER);
        label.setWrapText(true);

        dialogLayout.setBody(label);
        Image image = new Image("/Imagenes/backgroung.jpg");
        Background background = new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,BackgroundPosition.DEFAULT,BackgroundSize.DEFAULT));
        dialogLayout.setBackground(background);
        dialog.show();
        dialog.setOnDialogClosed((JFXDialogEvent event1)->{
            AP.setEffect(null);
            AP.setDisable(false);
        });
        AP.setEffect(boxBlur);
    }
}
