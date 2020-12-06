package java_.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;


/**
 * Class that controls the functions of CustomiseProfile.fxml
 *
 * @author Waleed Ashraf
 */
@Deprecated
public class CustomiseProfileController {

    @FXML
    private VBox rootPane;

    @FXML
    private Label nameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label coinLabel;

    private static final String MAIN_MENU_PATH = "../../view/layout/mainMenu.fxml";
    private static final String CUSTOMISE_PROFILE_EDIT_PATH = "../../view/layout/CustomiseProfileEdit.fxml";
    private static final String USERS_FILE_PATH = "users.txt";


    @FXML
    private void onBackButtonClicked(ActionEvent e) throws IOException {
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane mainMenu = (Pane) FXMLLoader.load(getClass().getResource(MAIN_MENU_PATH));
        currentStage.setScene(new Scene(mainMenu));
    }

    /**
     * Function that handles the processes that follow after button pressed for editing user details
     *
     * @param e The action of the button being clicked
     */
    @FXML
    private void handleEditButtonAction(ActionEvent e) throws IOException { //fixme
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane edit = (Pane) FXMLLoader.load(getClass().getResource(CUSTOMISE_PROFILE_EDIT_PATH));
        currentStage.setScene(new Scene(edit));
    }

    /**
     * Function that handles processes following a data refresh.
     *
     * @param event The action of the button being clicked.
     */
    @FXML
    private void handleRefreshData(ActionEvent event) {
        /*try { todo...
            Scanner x = new Scanner(new File(USERS_FILE_PATH));
            while(x.hasNext()) {
                String name = x.nextLine();
                String email = x.nextLine();
                String coins = x.nextLine();
                nameLabel.setText(name);
                emailLabel.setText(email);
                coinLabel.setText(coins);
            }
        } catch (Exception e) {
            S*///ystem.out.println("");
    }
}



