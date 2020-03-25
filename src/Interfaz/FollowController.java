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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.User;

import java.io.IOException;
import java.util.Objects;

public class FollowController {

    @FXML private JFXButton closeBT;
    @FXML private JFXButton buscarBT;
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
        buscarBT.setGraphic(new ImageView(new Image("Imagenes/lupa.png", 20,20,false,true)));
        userAP.setVisible(false);
    }

    public void buscar() {
        users_OL = FXCollections.observableArrayList();
        if (nicknameTF.getText().isEmpty()){
            System.out.println("Ingrese algún nombre de usuario.");
            Dialog.getInstance().info(seguirBT,"Ingrese algún nombre de usuario.",followAP);
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
                Dialog.getInstance().info(seguirBT,"No se encontro ningun usuario",followAP);
        }
    }

    public void mostrar() {
        Label usuario = users_LV.getSelectionModel().getSelectedItem();
        if (usuario != null){
            userAP.setVisible(true);
            circle.setFill(new ImagePattern(new Image(Objects.requireNonNull(Usuario.getUser(usuario.getText())).getOriginalProfileImageURL())));
            seguirBT.setDisable(false);
            seguirBT.setVisible(true);
            cambioTextoButton(usuario.getText());
        } else
            Dialog.getInstance().info(seguirBT,"No se ha seleccionado usuario",followAP);
    }

    private void cambioTextoButton(String usuario) {
        try {
            if (new Usuario().getNombreUsuario().compareTo(usuario) == 0)
                seguirBT.setVisible(false);
            else if (Usuario.isFollowing(usuario))
                seguirBT.setText("UnFollow");
            else
                seguirBT.setText("Follow");
        } catch (TwitterException e) {
            e.printStackTrace();
        }

        if (Objects.requireNonNull(Usuario.getUser(usuario)).isProtected()) {
            if (Objects.requireNonNull(Usuario.getUser(usuario)).isFollowRequestSent()){
                seguirBT.setDisable(true);
                if (seguirBT.getText().compareTo("Follow") == 0) seguirBT.setText("Solicitud Enviada");
                else seguirBT.setDisable(false);
            }
            isProtected.setVisible(true);
        }
        else isProtected.setVisible(false);
        screenName.setText("@"+usuario);
        userName.setText(Objects.requireNonNull(Usuario.getUser(usuario)).getName());
    }

    public void seguir(){
        String respuesta;
        if (screenName.getText().isEmpty()){
            System.out.println("Ingrese algún nombre de usuario.");
            Dialog.getInstance().info(seguirBT,"Ingrese algún nombre de usuario.",followAP);
        }
        else {
            Usuario usuario = new Usuario();
            respuesta = usuario.follow(screenName.getText().substring(1));
            Dialog.getInstance().info(seguirBT,respuesta,followAP);
            cambioTextoButton(screenName.getText().substring(1));
        }
    }
    public void cerrarVentana() throws IOException {
        Transiciones.Slide.getInstance().right("/Interfaz/EscenaPrincipal.fxml",closeBT,followAP);
    }
}
