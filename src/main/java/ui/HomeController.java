package ui;

import enums.StatisticsType;
import exception.NullDataException;
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
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Entity;
import service.DataProcessor;
import util.FileReader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class HomeController implements Initializable {

    private static final String ABOUT_ERROR_TEXT = "Cannot load about file. Please check that all files are downloaded correctly and restart the program.";
    private static final String ABOUT_TITLE = "About";
    private static final String EVENT_NAME = "Wiki page updated";
    private static final ErrorHandler errorHandler = new ErrorHandler();

    private ObservableList statTypes = FXCollections.observableArrayList();
    private File logFile;
    private List<Entity> entities;


    @FXML
    private ChoiceBox<?> statTypeChoiceBox;

    @FXML
    private BarChart<?, ?> barChart;

    @FXML
    private ListView<String> resultList;

    @FXML
    void chooseFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open xslx log file");
        logFile = fileChooser.showOpenDialog(null);
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
            errorHandler.handleException(ABOUT_ERROR_TEXT);
        }
    }

    @FXML
    void runAlgorithm(ActionEvent event) {
        barChart.getData().clear();
        if(logFile != null) {
            entities = FileReader.extractEntitiesFromFile(logFile);
            try {
                StatisticsType choice = StatisticsType.valueOf(statTypeChoiceBox.getValue().toString());
                switch (choice) {
                    case ABS_FREQUENCY: calculateAbsFrequency(); break;
                    case RELATIVE_FREQUENCY: calculateRelativeFrequency(); break;
                    case SCOPE: calculateScope(); break;
                    case MEDIAN: calculateMedian(); break;
                }
            } catch (Exception e) {
                errorHandler.handleException("Please, choose what type of data you want \nto be displayed.");
            }
        } else {
            errorHandler.handleException("Please, choose the file with system logs");
        }
    }

    private void calculateScope() throws NullDataException{
        Map<String, Long> data = DataProcessor.findAbsFrequencyForUser(entities, EVENT_NAME);
        Map<String, Long> minMaxValues = DataProcessor.findMinMax(data);
        loadDataWithLongToChart(minMaxValues, "Minimum and maximum");
        long scope = DataProcessor.findScope(entities, EVENT_NAME);
        ObservableList<String> listData = FXCollections.observableArrayList(getScopeTextData(scope));
        loadTextData(listData);
    }

    private List<String> getScopeTextData(long scope) {
        List<String> data = new ArrayList<>();
        data.add("Scope = " + scope);
        return data;
    }

    private void calculateMedian() throws NullDataException {
        double median = DataProcessor.findMedian(entities, EVENT_NAME);
        Map<String, Double> chartData = new HashMap<>();
        chartData.put("Median", median);
        loadDataWithDoubleToChart(chartData, "Median");
        ObservableList<String> listData = FXCollections.observableArrayList(getMedianTextData(median));
        loadTextData(listData);
    }

    private List<String> getMedianTextData(double median) {
        List<String> data = new ArrayList<>();
        data.add("Median = " + median);
        return data;
    }

    private void calculateRelativeFrequency() throws NullDataException{
        Map<String, Double> data = DataProcessor.findRelativeFrequencyForUser(entities, EVENT_NAME);
        loadDataWithDoubleToChart(data, "Relative frequency");
        ObservableList<String> listData = FXCollections.observableList(getRelativeFrequencyTextData(data));
        loadTextData(listData);
    }

    private void calculateAbsFrequency() throws NullDataException {
        Map<String, Long> data = DataProcessor.findAbsFrequencyForUser(entities, EVENT_NAME);
        loadDataWithLongToChart(data, "Absolute frequency");
        ObservableList<String> listData = FXCollections.observableList(getAbsoluteFrequencyTextData(data));
        loadTextData(listData);
    }

    private List<String> getAbsoluteFrequencyTextData(Map<String, Long> data) {
        List<String> list = new ArrayList<>();
        data.entrySet().forEach(e -> list.add("id " + e.getKey() + ": " + e.getValue() + ";\n"));
        return list;
    }

    private List<String> getRelativeFrequencyTextData(Map<String, Double> data) {
        List<String> list = new ArrayList<>();
        data.entrySet().forEach(e -> list.add("id " + e.getKey() + ": " + e.getValue() + "%;\n"));
        return list;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
    }

    private void loadDataWithDoubleToChart(Map<String, Double> data, String title) {
        barChart.getData().clear();
        for(Map.Entry<String, Double> entry: data.entrySet()) {
            XYChart.Series series = new XYChart.Series<>();
            series.setName(entry.getKey());
            XYChart.Data<String, Double> chartData = new XYChart.Data<>("", entry.getValue());
            series.getData().add(chartData);
            barChart.getData().add(series);
        }
        barChart.setTitle(title);
    }

    private void loadDataWithLongToChart(Map<String, Long> data, String title) {
        barChart.getData().clear();
        for(Map.Entry<String, Long> entry: data.entrySet()) {
            XYChart.Series series = new XYChart.Series<>();
            series.setName(entry.getKey());
            XYChart.Data<String, Long> chartData = new XYChart.Data<>("", entry.getValue());
            series.getData().add(chartData);
            barChart.getData().add(series);
        }
        barChart.setTitle(title);
    }

    private void loadTextData(ObservableList<String> result) {
        resultList.setItems(result);
    }

    private void loadData() {
        statTypes.addAll(
                StatisticsType.ABS_FREQUENCY.toString(),
                StatisticsType.RELATIVE_FREQUENCY.toString(),
                StatisticsType.MEDIAN.toString(),
                StatisticsType.SCOPE.toString());
        statTypeChoiceBox.setItems(statTypes);
    }
}
