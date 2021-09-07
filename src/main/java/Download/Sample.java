package Download;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Sample implements Initializable {
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

  }

  private Stage stage;
  private Scene scene;
  private Parent root;
  @FXML
  private MenuBar MenuBar;

  @FXML
  public void handlePopular(ActionEvent event) throws IOException {
    Parent tableView = FXMLLoader.load(getClass().getResource("/popular.fxml"));
    Scene tableViewScene = new Scene(tableView);
    //Lines gets the Stage information
    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
    window.setScene(tableViewScene);
    window.show();

  }
  @FXML
  public void handlePopularFromFile(ActionEvent event) throws IOException {
    Parent tableView1;
    tableView1 = FXMLLoader.load(getClass().getResource("/popular.fxml"));
    Scene tableViewScene1= new Scene(tableView1);
    //Lines gets the Stage information
    Stage window1 = (Stage) MenuBar.getScene().getWindow();
    window1.setScene(tableViewScene1);
    window1.show();
  }

  @FXML
  public void close(){
    System.exit(0);
  }


}
