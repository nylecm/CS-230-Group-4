import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.swing.*;

public class LogInController {
    @FXML private Button button;
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private Button logInButton;

    @FXML
    private void handleCalculateButtonAction(ActionEvent event) {
        String x = username.getText();
        String y = password.getText();

        //fixme remove this:
        JOptionPane.showMessageDialog(null, "Hello");
    }
}
