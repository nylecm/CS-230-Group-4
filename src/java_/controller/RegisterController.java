package java_.controller;

import java_.util.security.RegisterHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Register controller handles users registering their accounts. This includes their username, email and password.
 *
 * @author nylecm
 */
public class RegisterController implements Initializable {

    @FXML
    private VBox mainBox;

    @FXML
    private Label registerStatusLabel;

    @FXML
    private TextField username;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField passwordRepeat;

    private static final String MAIN_MENU_PATH = "../../view/layout/mainMenu.fxml";
    private static final String URANUS_BACKGROUND_PATH = "src/view/res/img/space_uranus.png";

    private static final String USERNAME_PATTERN = "^(?=[a-zA-Z0-9._]{3,20}$)(?!.*[_.]{2})[^_.].*[^_.]$";
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    private static final String PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$";

    private static final String USERNAME_INVALID_MSG = "Username invalid!";
    private static final String EMAIL_INVALID_MSG = "Email invalid!";
    private static final String PASSWORD_INVALID_MSG = "Password invalid!";
    private static final String PASSWORD_NO_MATCH_MSG = "Passwords don't match!";
    private static final String ACCOUNT_REGISTERED_MSG = "Account Registered!";
    private static final String FILE_NOT_FOUND_OR_ERROR_ACCESSING_IT_MSG = "Error, users file not found, or error accessing it!";
    public static final String DUPLICATE_USER_NAME_MSG = "Duplicate user name!";

    /**
     * Registers a user with the credentials they have entered on the interface.
     *
     * @param event The event of the register button being clicked.
     */
    @FXML
    private void onRegisterButtonClicked(ActionEvent event) {
        String username = this.username.getText();
        String email = this.email.getText();
        String password = this.password.getText();
        String passwordRepeat = this.passwordRepeat.getText();
        if (validate(username, email, password, passwordRepeat)) {
            try {
                RegisterHandler.register(username, email, password);
                registerStatusLabel.setText(ACCOUNT_REGISTERED_MSG);
            } catch (IOException e) {
                registerStatusLabel.setText(FILE_NOT_FOUND_OR_ERROR_ACCESSING_IT_MSG);
            } catch (IllegalArgumentException e2) {
                registerStatusLabel.setText(e2.getMessage());
            }
            this.username.setText("");
            this.email.setText("");
            this.password.setText("");
            this.passwordRepeat.setText("");
        }
    }

    /**
     * Validates the credentials the user has entered to register with.
     * The username, password and email must match their retrospective patterns.
     *
     * @param username       The username the user is registering with.
     * @param email          The email the user is registering with.
     * @param password       The password the user is registering with.
     * @param passwordRepeat The repeat of the password previously entered. Both should match.
     * @return Returns true if the user has entered valid credentials to register with. It is false otherwise.
     */
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

    /**
     * Validates the username of the user, ensuring it matches the correct pattern.
     *
     * @param username The username to be validated.
     * @return True if the username is valid.
     */
    private boolean validateUsername(String username) {
        return username.matches(USERNAME_PATTERN);
    }

    /**
     * Validates the email of the user, ensuring it matches the correct pattern.
     *
     * @param email The email to be validated.
     * @return True if the email is valid.
     */
    private boolean validateEmail(String email) {
        return email.matches(EMAIL_PATTERN);
    }

    /**
     * Validates the password of the user, ensuring it matches the correct pattern.
     *
     * @param password The password to be validated.
     * @return True if the password is valid.
     */
    private boolean validatePassword(String password) {
        return password.matches(PASSWORD_PATTERN);
    }

    /**
     * Validates the repeated password of the user, ensuring it matches the original password entered
     *
     * @param repeatPassword The username to be validated.
     * @param password       The original password entered by the user.
     * @return True if the repeatPassword matches the original password entered.
     */
    private boolean validateRepeatPassword(String password, String repeatPassword) {
        return password.equals(repeatPassword);
    }

    /**
     * Returns the user to the main meny
     *
     * @param e The action of the back button being clicked.
     * @throws IOException If the main meny file path is incorrect.
     */
    @FXML
    private void onBackButtonClicked(ActionEvent e) throws IOException {
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane mainMenu = FXMLLoader.load(getClass().getResource(MAIN_MENU_PATH));
        currentStage.setScene(new Scene(mainMenu));
    }

    /**
     * Initialises the register interface, giving it a background and setting its size.
     *
     * @param location  The location (not used).
     * @param resources The resources (not used).
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BackgroundImage backgroundImage = null;
        try {
            backgroundImage = new BackgroundImage(new Image(String.valueOf
                    (new File(URANUS_BACKGROUND_PATH).toURI().toURL())), BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize
                    (0, 0, false, false, false, true));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        mainBox.setBackground(new Background(backgroundImage));
    }
}
