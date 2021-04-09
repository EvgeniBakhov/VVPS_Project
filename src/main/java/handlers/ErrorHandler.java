package handlers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.ErrorController;

import java.io.IOException;

public class ErrorHandler {

    public void handleException(String message) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("error.fxml"));
            Parent root = loader.load();
            ErrorController controller = loader.getController();
            controller.setErrorMessage(message);
            Stage stage = new Stage();
            stage.setTitle("Error");
            stage.setScene(new Scene(root));
            stage.show();
        }catch (IOException e) {
            System.out.println("Could not display error message");
        }
    }
}
