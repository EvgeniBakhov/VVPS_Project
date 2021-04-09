import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Entity;
import util.FileReader;

import java.util.List;

public class Main extends Application {
    public static void main(String[] args) {
        List<Entity> entities = FileReader.extractEntitiesFromFile("resource/Logs_Course A_StudentsActivities.xlsx");
        System.out.println(entities.get(8).getTime());
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
        stage.setTitle("VVPS Project");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
