package java_.controller;

import java_.game.controller.GameService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameWonController implements Initializable {


    @FXML
    private Label playerWhoWon;

    private static final String WON_MSG = " won!";
    private static final String MAIN_MENU_PATH = "../../view/layout/mainMenu.fxml";

    public void onBackToMainMenuButtonClicked(ActionEvent e) throws IOException {
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane mainMenu = (Pane) FXMLLoader.load(getClass().getResource(MAIN_MENU_PATH));
        currentStage.setScene(new Scene(mainMenu));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int playerNumber = GameService.getInstance().getCurrentPlayerNum();
        playerWhoWon.setText(GameService.getInstance().getPlayerService().getPlayer(playerNumber).getUsername() + WON_MSG);
    }
}
