import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

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
    private PasswordField repeatPassword;

    @FXML
    private void register(ActionEvent event) {
        String username = this.username.getText();
        String email = this.password.getText();
        String password = this.password.getText();
        String repeatPassword = this.repeatPassword.getText();

        boolean usernameValid;
        boolean emailValid;
        boolean passwordValid;
        boolean repeatPasswordValid;
    }

    private boolean validatePassword


}
