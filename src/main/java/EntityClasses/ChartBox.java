package EntityClasses;

import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class ChartBox {

  public static void display(String name) {
    Stage window = new Stage();

    window.initModality(Modality.APPLICATION_MODAL);
    window.setTitle(name);
    window.setMinWidth(200);

    javafx.scene.control.Label label = new Label();
    label.setText("Welcome in:" + name + " chart");
    javafx.scene.control.Button closeButton = new javafx.scene.control.Button("Close this window");
    closeButton.setOnAction(e -> window.close());

    VBox layout = new VBox(10);
    layout.getChildren().addAll(label,closeButton);
    layout.setAlignment((Pos.CENTER));

    Scene scene = new Scene(layout);
    window.setScene(scene);
    window.showAndWait();


  }
}
