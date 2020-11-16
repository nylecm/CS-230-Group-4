package java_.controller;

import java_.util.security.LoginHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class LogInController {

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button logInButton;

    @FXML
    private void onLoginButtonClicked(ActionEvent event) {
        String username = this.username.getText();
        String password = this.password.getText();
        System.out.println(LoginHandler.login(username, password));
    }

    @FXML
    private void onRegisterButtonClicked(ActionEvent e) throws IOException {
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane register = (Pane) FXMLLoader.load(getClass().getResource("../../view/layout/register.fxml"));
        currentStage.setScene(new Scene(register));
    }

    @FXML
    private void onRegisterButtonScrolled(ActionEvent e) throws IOException {
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane register = (Pane) FXMLLoader.load(getClass().getResource("../../view/layout/register.fxml"));
        currentStage.setScene(new Scene(register));
    }
}
