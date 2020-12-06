package java_.controller.reworkedGuiFiles;

import javafx.fxml.FXML;

public class GameHeaderController {

    @FXML
    private GameMainController gameMainController;

    public void injectMainController(GameMainController gameMainController) {
        this.gameMainController = gameMainController;
    }
}
