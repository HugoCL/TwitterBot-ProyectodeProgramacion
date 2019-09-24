package Motor;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class CustomCell extends ListCell<String>{


    private JFXButton actionBtn;
    private Label name = new Label();
    private GridPane pane ;
    private boolean tf = false;
    public CustomCell() {
        super();
        setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

            }
        });
        name.getStyleClass().add("label");

        actionBtn = new JFXButton();
        actionBtn.getStyleClass().add("GrayHeart-buttton");
        actionBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Action: "+getItem());
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
        pane = new GridPane();
        pane.add(name, 0, 0);
        pane.add(actionBtn, 1, 1);
        setText(null);
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        setEditable(false);
        if (item != null) {
            name.setText(item);
            setGraphic(pane);
        } else {
            setGraphic(null);
        }
    }
}
