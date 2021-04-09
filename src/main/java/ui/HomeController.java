package ui;

import enums.StatisticsType;
import handlers.ErrorHandler;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    public static final String ABOUT_ERROR_TEXT = "Cannot load about file. Please check that all files are downloaded correctly and restart the program.";
    public static final String ABOUT_TITLE = "About";
    ObservableList statTypes = FXCollections.observableArrayList();

    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem exitMenuItem;

    @FXML
    private Button chooseFileButton;

    @FXML
    private Button runButton;

    @FXML
    private ChoiceBox<?> statTypeChoiceBox;

    @FXML
    private BarChart<?, ?> barChart;

    @FXML
    void chooseFile(ActionEvent event) {

    }

    @FXML
    void exitProgram(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void showAbout(ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("about.fxml"));
            Stage stage = new Stage();
            stage.setTitle(ABOUT_TITLE);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            ErrorHandler.handleException(ABOUT_ERROR_TEXT);
        }
    }

    @FXML
    void runAlgorithm(ActionEvent event) {
        barChart.getData().clear();
        //delete this.
        Map<String, Integer> test = new HashMap<>();
        test.put("Login", 10000);
        test.put("Update profile", 4000);
        test.put("Add course", 1100);
        test.put("Update Wiki page", 6340);
        test.put("Upload photo", 1235);
        test.put("Register", 234);
        loadDataToChart(test, "Absolute frequency");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
    }

    public void loadDataToChart(Map<String, Integer> data, String title) {
        for(Map.Entry<String, Integer> entry: data.entrySet()) {
            XYChart.Series series = new XYChart.Series<>();
            series.setName(entry.getKey());
            XYChart.Data<String, Integer> chartData = new XYChart.Data<>("", entry.getValue());
            series.getData().add(chartData);
            barChart.getData().add(series);
        }
        barChart.setTitle(title);
    }

    private void loadData() {
        statTypes.addAll(
                StatisticsType.ABS_FREQUENCY.toString(),
                StatisticsType.RELATIVE_FREQUENCY.toString(),
                StatisticsType.MEDIAN.toString(),
                StatisticsType.SCOPE);
        statTypeChoiceBox.setItems(statTypes);
    }
}