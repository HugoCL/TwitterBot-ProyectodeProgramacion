package Interfaz;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/Interfaz/InicioSesion.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Twitter Bot - Ramos Overflow");
        primaryStage.getIcons().add(new Image("/Imagenes/Icono.png"));
        primaryStage.resizableProperty().setValue(Boolean.FALSE);
        primaryStage.show();
    }


    public static void main(String[] args){
        launch(args);
    }
}
