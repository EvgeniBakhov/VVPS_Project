package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ErrorController implements Initializable {

    @FXML
    private Button okButton;

    @FXML
    private Label errorMessage;

    @FXML
    void closeWindow(ActionEvent event) {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setErrorMessage(String errorText) {
        this.errorMessage.setText(errorText);
    }
}
