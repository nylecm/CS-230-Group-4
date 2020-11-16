package res;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Class that controls the functions of CustomiseProfile.fxml
 * @author Waleed Ashraf
 */
public class CustomiseProfileController {

    @FXML private VBox rootPane;
    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label coinLabel;

    /**
     * Function that handles the processes that follow after button pressed for editing user details
     * @param event The action of the button being clicked
     */
    @FXML
    private void handleEditButtonAction(ActionEvent event) throws IOException {
        VBox pane = (VBox) FXMLLoader.load(getClass().getResource("../../CS-230-Group-4/src/res/CustomiseProfileEdit.fxml")); //loads new pane(VBox in this case) onto current scene
        rootPane.getChildren().setAll(pane);
    }

    /**
     * Function that handles processes following a data refresh.
     * @param event The action of the button being clicked.
     */
    @FXML
    private void handleRefreshData(ActionEvent event) {
        try {
            Scanner x = new Scanner(new File("../../CS-230-Group-4/src/res/users.txt"));
            while(x.hasNext()) {
                String name = x.nextLine();
                String email = x.nextLine();
                String coins = x.nextLine();
                nameLabel.setText(name);
                emailLabel.setText(email);
                coinLabel.setText(coins);
            }
        } catch (Exception e) {
            System.out.println("");
        }
    }


}