import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import security.LoginHandler;

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
}
