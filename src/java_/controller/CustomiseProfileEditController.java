package java_.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Class that controls the functions of CustomiseProfileEdit.fxml
 * @author Waleed Ashraf
 */
public class CustomiseProfileEditController {

    private static final String USERS_FILE_PATH = "users.txt";
    private static final String CUSTOMISE_PROFILE_PATH = "../../view/layout/CustomiseProfile.fxml";

    @FXML private VBox rootPane;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField coinsField;

    /**
     * Function that handles what happens after data is submitted.
     * @param event The action of the button being clicked
     */
    @FXML
    private void handleDataSubmit(ActionEvent event) throws IOException {
        String name = nameField.getText();
        String email = emailField.getText();
        int coins = Integer.parseInt(coinsField.getText());

        File file = new File(USERS_FILE_PATH);
        FileWriter fw = new FileWriter(file);
        PrintWriter pw = new PrintWriter(fw);

        pw.println(name);
        pw.println(email);
        pw.println(coins);

        pw.close();

        VBox pane = (VBox) FXMLLoader.load(getClass().getResource(CUSTOMISE_PROFILE_PATH)); //loads new pane(VBox in this case) onto current scene
        rootPane.getChildren().setAll(pane);
    }
}
