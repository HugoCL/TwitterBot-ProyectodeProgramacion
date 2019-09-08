package Transiciones;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
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

    public void error(JFXButton boton, String texto, String botonTexto, AnchorPane AP){
        AP.setDisable(true);
        Scene scene = boton.getScene();
        StackPane rootpane = (StackPane) scene.getRoot();

        dialogLayout.setPrefWidth(210);
        JFXDialog dialog = new JFXDialog(rootpane,dialogLayout,JFXDialog.DialogTransition.TOP);

        button.setText(botonTexto);
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent)->{
            dialog.close();
            AP.setDisable(false);
        });

        Label label = new Label();
        label.setText(texto);
        label.setFont(new Font("Segoe UI",12));

        dialogLayout.setHeading(label);
        dialogLayout.setActions(button);
        dialog.show();
        dialog.setOnDialogClosed((JFXDialogEvent event1)->{
            AP.setEffect(null);
            AP.setDisable(false);
        });
        AP.setEffect(boxBlur);
    }
    public void info(JFXButton boton, String texto, AnchorPane AP){

    }
}
