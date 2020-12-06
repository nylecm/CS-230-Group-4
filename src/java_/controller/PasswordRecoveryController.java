package java_.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.Properties;

@Deprecated
public class PasswordRecoveryController {

    @FXML
    private TextField email;

    @FXML
    private Button sendCodeButton;

    @FXML
    private TextField code;

    @FXML
    private Button applyCodeButton;

    @FXML
    private TextField newPassword;

    @FXML
    private TextField repeatPassword;

    @FXML
    private Button setNewPassword;

    @FXML
    private void onSendCodeButtonClicked(ActionEvent event) {
        sendCodeButton.setText("Resend Code");
        code.setDisable(false);
        applyCodeButton.setDisable(false);
    }

    private void sendEmailWithCode(String email) {
    }

    @FXML
    private void onApplyCodeButtonClicked(ActionEvent event) {
        String code = this.code.getText();
    }
}
