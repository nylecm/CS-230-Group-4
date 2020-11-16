package res;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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

    @FXML
    private void onBackButtonClicked(ActionEvent e) throws IOException {
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane mainMenu = (Pane) FXMLLoader.load(getClass().getResource("../mainMenu.fxml"));
        currentStage.setScene(new Scene(mainMenu));
    }

    /**
     * Function that handles the processes that follow after button pressed for editing user details
     * @param event The action of the button being clicked
     */
    @FXML
    private void handleEditButtonAction(ActionEvent e) throws IOException { //fixme
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane edit = (Pane) FXMLLoader.load(getClass().getResource("CustomiseProfileEdit.fxml"));
        currentStage.setScene(new Scene(edit));
    }

    /**
     * Function that handles processes following a data refresh.
     * @param event The action of the button being clicked.
     */
    @FXML
    private void handleRefreshData(ActionEvent event) {
        try {
            Scanner x = new Scanner(new File("users.txt"));
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
