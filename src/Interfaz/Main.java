package Interfaz;

import Motor.TwitterBot;
import Motor.adminSesion;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import twitter4j.TwitterException;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("InicioSesion.fxml"));
        primaryStage.setTitle("Twitter Bot - Ramos Overflow");
        primaryStage.setScene(new Scene(root));
        primaryStage.getIcons().add(new Image("/Imagenes/Icono.png"));
        primaryStage.show();
    }


    public static void main(String[] args){
        launch(args);
    }
}
