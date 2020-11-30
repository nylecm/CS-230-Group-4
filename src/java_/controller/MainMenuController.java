package java_.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java_.util.MessageOfTheDayService;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    public VBox mainBox;

    @FXML
    private Label messageOfTheDay;

    private void setMessageOfTheDay() throws IOException {
        messageOfTheDay.setText(MessageOfTheDayService.getMessage());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BackgroundFill backgroundFill = null;
        try {
            backgroundFill = new BackgroundFill(new ImagePattern(new Image(String.valueOf(new File("src/view/res/img/space_uranus.png").toURL()))), CornerRadii.EMPTY, Insets.EMPTY);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        mainBox.setBackground(new Background(backgroundFill));

        try {
            setMessageOfTheDay();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onNewGameButtonClicked(ActionEvent e) throws IOException {
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane newGame = (Pane) FXMLLoader.load(getClass().getResource("../../view/layout/newGame.fxml"));
        currentStage.setScene(new Scene(newGame));
    }

    @FXML
    private void onLoadGameButtonClicked(ActionEvent e) throws IOException {
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane loadGame = (Pane) FXMLLoader.load(getClass().getResource("../../view/layout/loadGame.fxml"));
        currentStage.setScene(new Scene(loadGame));
    }

    @FXML
    private void onCustomiseProfileButtonClicked(ActionEvent e) throws IOException {
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane customiseProfile = (Pane) FXMLLoader.load(getClass().getResource("../../view/layout/customiseProfile.fxml")); //todo change to login
        currentStage.setScene(new Scene(customiseProfile));
    }

    @FXML
    private void onLeaderboardButtonClicked(ActionEvent e) throws IOException {
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane newGame = (Pane) FXMLLoader.load(getClass().getResource("../../view/layout/leaderboard.fxml"));
        currentStage.setScene(new Scene(newGame));
    }

    @FXML
    private void onSettingsButtonClicked(ActionEvent e) throws IOException {
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane newGame = (Pane) FXMLLoader.load(getClass().getResource("../../view/layout/settings.fxml"));
        currentStage.setScene(new Scene(newGame));
    }

    @FXML
    private void onRegisterButtonClicked(ActionEvent e) throws IOException {
        Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane newGame = (Pane) FXMLLoader.load(getClass().getResource("../../view/layout/register.fxml"));
        currentStage.setScene(new Scene(newGame));
    }
    @FXML
    private void onQuitButtonClicked(ActionEvent e) {
        System.exit(0);
    }
}
