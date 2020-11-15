package res;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
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

        File file = new File("../../CS-230-Group-4/src/res/users.txt");
        FileWriter fw = new FileWriter(file);
        PrintWriter pw = new PrintWriter(fw);

        pw.println(name);
        pw.println(email);
        pw.println(coins);

        pw.close();

        VBox pane = (VBox) FXMLLoader.load(getClass().getResource("../../CS-230-Group-4/src/res/CustomiseProfile.fxml")); //loads new pane(VBox in this case) onto current scene
        rootPane.getChildren().setAll(pane);
    }
}
