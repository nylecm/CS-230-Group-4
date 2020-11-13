import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    private static final String USERNAME_PATTERN = "^(?=[a-zA-Z0-9._]{3,20}$)(?!.*[_.]{2})[^_.].*[^_.]$";

    private static final String EMAIL_PATTERN = "^\\S+@\\S+$"; //Too simple?

    private static final String PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$"; //Might be broken

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

    @FXML
    private void register(ActionEvent event) {
        String username = this.username.getText();
        String email = this.email.getText();
        String password = this.password.getText();
        String passwordRepeat = this.passwordRepeat.getText();
        String output = validate(username, email, password, passwordRepeat) ? "Registration successful!" : "Registration failed!";
        System.out.println(output);
    }

    //Printlns for testing, to be replaced with status texts
    private boolean validate(String username, String email, String password, String passwordRepeat) {
        if (!validateUsername(username)) {
            System.out.println("Username invalid!");
            return false;
        }
        if (!validateEmail(email)) {
            System.out.println("Email invalid!");
            return false;
        }
        if (!validatePassword(password)) {
            System.out.println("Password invalid!");
            return false;
        }
        if (!validateRepeatPassword(password, passwordRepeat)) {
            System.out.println("Passwords don't match!");
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
}
