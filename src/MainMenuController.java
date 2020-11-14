import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {
    @FXML
    private Label messageOfTheDay;

    private void setMessageOfTheDay() throws IOException {
        messageOfTheDay.setText(MessageOfTheDayService.getMessage());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            setMessageOfTheDay();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
