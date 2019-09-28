package Motor;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import twitter4j.Twitter;

public class CustomCell extends ListCell<Tweet> {
    private JFXButton actionBtn;
    private Label name;
    private Label numero;
    private TextArea mensaje;
    private Label imagen;
    private GridPane pane ;
    private static Twitter twitter = TwitterBot.getInstance().getBOT().getTwitter();
    private boolean tf = false;

    public CustomCell() {
        super();

        setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

            }
        });

        actionBtn = new JFXButton();
        actionBtn.setMinSize(15, 15);
        actionBtn.setMaxSize(15, 15);
        actionBtn.getStyleClass().add("GrayHeart-buttton");
        actionBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Action: "+getItem().getMensaje());
                System.out.println(actionBtn.getStyleClass().get(2));
                if (!tf){
                    actionBtn.getStyleClass().set(2, "RedHeart-buttton");System.out.println(tf);
                    tf = true;

                }else{
                    actionBtn.getStyleClass().set(2, "GrayHeart-buttton");System.out.println(tf);
                    tf = false;

                }
            }
        });
        name = new Label();
        numero = new Label("1");
        numero.setMaxSize(15, 15);
        numero.setMinSize(15, 15);
        mensaje = new TextArea();
        mensaje.setEditable(false);
        mensaje.setMaxSize(300, 100);
        mensaje.setWrapText(true);
        mensaje.getStyleClass().add("text-area");
        imagen = new Label();
        name.getStyleClass().add("label");
        pane = new GridPane();


        pane.add(imagen, 0, 0);
        pane.add(name, 1, 0);
        pane.add(mensaje, 0, 1, 2, 1);
        pane.add(new HBox(1, new JFXButton("algo"), numero), 0, 2);
        pane.add(new HBox(1, actionBtn, numero), 1, 2);
        setText(null);
    }

    @Override
    public void updateItem(Tweet item, boolean empty) {
        super.updateItem(item, empty);
        setEditable(false);
        if (item != null) {
            name.setText(item.getNombre());
            imagen.setText(item.getId()+"");
            mensaje.setText(item.getMensaje());
            //De esta manera se obitiene la imagen de perfil del usuario
            //twitter.showStatus(item.getId()).getUser().getProfileImageURL();
            setGraphic(pane);
        } else {
            setGraphic(null);
        }
    }
}