package ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AboutController implements Initializable {

    public static final String ABOUT_EN_TXT = "src/main/resources/text/about_en.txt";
    @FXML
    private Label captionLabel;

    @FXML
    private Label aboutLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        captionLabel.setWrapText(true);
        loadData();
    }

    private void loadData() {
        File file = new File(ABOUT_EN_TXT);
        try(FileInputStream fis = new FileInputStream(file)) {
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            aboutLabel.setText(new String(data, "UTF-8"));
        } catch (IOException e) {
            System.out.println("Unable to open the file" + ABOUT_EN_TXT);
            aboutLabel.setText("Unable to load about file, sorry. Developed by Yevhen Bakhov 2021.");
        }
    }
}

