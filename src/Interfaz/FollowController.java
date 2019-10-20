package Interfaz;

import Motor.Usuario;
import Transiciones.Dialog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.User;

import java.io.IOException;

public class FollowController {

    @FXML private JFXButton followBT;
    @FXML private JFXButton closeBT;
    @FXML private JFXTextField nicknameTF;

    @FXML private AnchorPane followAP;
    @FXML private ObservableList<Label> users_OL;
    @FXML private JFXListView<Label> users_LV;

    @FXML private Circle circle;
    @FXML private Text isProtected;
    @FXML private Text screenName;
    @FXML private Text userName;
    @FXML private JFXButton seguirBT;
    @FXML private AnchorPane userAP;

    public void initialize(){
        userAP.setVisible(false);
    }

    public void buscar() {
        users_OL = FXCollections.observableArrayList();
        if (nicknameTF.getText().isEmpty()){
            System.out.println("Ingrese algún nombre de usuario.");
            Dialog.getInstance().info(followBT,"Ingrese algún nombre de usuario.",followAP);
        }
        else {
            ResponseList<User> users = Usuario.searchUsers(nicknameTF.getText());
            if (users != null){
                for (User user:users) {
                    Circle circle = new Circle(10);
                    circle.setFill(new ImagePattern(new Image(user.getMiniProfileImageURL())));
                    Label label = new Label(user.getScreenName());
                    label.setGraphic(circle);
                    users_OL.add(label);
                }
                users_LV.setItems(users_OL);
            } else
                Dialog.getInstance().info(followBT,"No se encontro ningun usuario",followAP);
        }
    }

    public void mostrar() {
        Label usuario = users_LV.getSelectionModel().getSelectedItem();
        if (usuario != null){
            userAP.setVisible(true);
            circle.setFill(new ImagePattern(new Image(Usuario.getUser(usuario.getText()).getOriginalProfileImageURL())));
            seguirBT.setDisable(false);
            seguirBT.setVisible(true);
            try {
                if (new Usuario().getNombreUsuario().compareTo(usuario.getText()) == 0)
                    seguirBT.setVisible(false);
                else if (Usuario.isFollowing(usuario.getText()))
                    seguirBT.setText("UnFollow");
                else
                    seguirBT.setText("Follow");
            } catch (TwitterException e) {
                e.printStackTrace();
            }

            if (Usuario.getUser(usuario.getText()).isProtected()) {
                if (Usuario.getUser(usuario.getText()).isFollowRequestSent()){
                    seguirBT.setText("Solicitud Enviada");
                    seguirBT.setDisable(true);
                }
                isProtected.setVisible(true);
            }
            else isProtected.setVisible(false);
            screenName.setText("@"+usuario.getText());
            userName.setText(Usuario.getUser(usuario.getText()).getName());
        } else
            Dialog.getInstance().info(followBT,"No se ha seleccionado usuario",followAP);
    }

    public void seguir(){
        String respuesta;
        if (nicknameTF.getText().isEmpty()){
            System.out.println("Ingrese algún nombre de usuario.");
            Dialog.getInstance().info(followBT,"Ingrese algún nombre de usuario.",followAP);
        }
        else {
            Usuario usuario = new Usuario();
            respuesta = usuario.follow(nicknameTF.getText());
            Dialog.getInstance().info(followBT,respuesta,followAP);
            nicknameTF.setText("");
        }
    }
    public void cerrarVentana() throws IOException {
        Transiciones.Slide.getInstance().right("/Interfaz/EscenaPrincipal.fxml",closeBT,followAP);
    }
}
