package java_.controller;

import java_.util.security.RegisterHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Register controller handles users registering their accounts. This includes their username, email and password.
 */
public class RegisterController implements Initializable {

    private static final String USERNAME_PATTERN = "^(?=[a-zA-Z0-9._]{3,20}$)(?!.*[_.]{2})[^_.].*[^_.]$";

    private static final String EMAIL_PATTERN = "^\\S+@\\S+$"; //Too simple? xxx@xxx.xxxx

    private static final String PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$"; //Might be broken

    @FXML
    private VBox mainBox;

    @FXML
    private Label registerStatusLabel;

    @FXML
    private Button registerButton;

    @FXML
    private Button backButton;

    @FXML
    private TextField username;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField passwordRepeat;

    private final String USERNAME_INVALID_MSG = "Username invalid!"; //TODO MAKE MESSAGES MORE USEFUL
    private final String EMAIL_INVALID_MSG = "Email invalid!";
    private final String PASSWORD_INVALID_MSG = "Password invalid!";
    private final String PASSWORD_NO_MATCH_MSG = "Passwords don't match!";


    @FXML
    private void onRegisterButtonClicked(ActionEvent event) {
        String username = this.username.getText();
        String email = this.email.getText();
        String password = this.password.getText();
        String passwordRepeat = this.passwordRepeat.getText();
        if (validate(username, email, password, passwordRepeat)) {
            try {
                RegisterHandler.register(username, email, password);
                registerStatusLabel.setText("Account Registered!");
                this.username.setText("");
                this.email.setText("");
                this.password.setText("");
                this.passwordRepeat.setText("");
            } catch (IOException e) {
                registerStatusLabel.setText("Error, users file not found, or error accessing it!");
            } catch (IllegalArgumentException e2) { //todo custom exception...
                registerStatusLabel.setText("Duplicate user name!");
            }
        }
    }

    private boolean validate(String username, String email, String password, String passwordRepeat) {
        if (!validateUsername(username)) {
            registerStatusLabel.setText(USERNAME_INVALID_MSG);
            return false;
        }
        if (!validateEmail(email)) {
            registerStatusLabel.setText(EMAIL_INVALID_MSG);
            return false;
        }
        if (!validatePassword(password)) {
            registerStatusLabel.setText(PASSWORD_INVALID_MSG);
            return false;
        }
        if (!validateRepeatPassword(password, passwordRepeat)) {
            registerStatusLabel.setText(PASSWORD_NO_MATCH_MSG);
            return false;
        }
        return true;
    }

    private boolean validateUsername(String username) {
        return username.matches(USERNAME_PATTERN);
    }

    private boolean validateEmail(String email) {
        return email.matches(EMAIL_PATTERN);
    }

    private boolean validatePassword(String password) {
        return password.matches(PASSWORD_PATTERN);
    }

    private boolean validateRepeatPassword(String password, String repeatPassword) {
        return password.equals(repeatPassword);
    }

    @FXML
    private void onBackButtonClicked(ActionEvent e) throws IOException {
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane mainMenu = (Pane) FXMLLoader.load(getClass().getResource("../../view/layout/mainMenu.fxml"));
        currentStage.setScene(new Scene(mainMenu));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BackgroundFill backgroundFill = null;
        try {
            backgroundFill = new BackgroundFill(new ImagePattern(new Image(String.valueOf(new File("src/view/res/img/space_uranus.png").toURL()))), CornerRadii.EMPTY, Insets.EMPTY);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        mainBox.setBackground(new Background(backgroundFill));
    }
}
