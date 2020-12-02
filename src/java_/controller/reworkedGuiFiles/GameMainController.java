package java_.controller.reworkedGuiFiles;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class GameMainController implements Initializable {

    @FXML
    private GameHeaderController gameHeaderController;

    @FXML
    private GameFooterController gameFooterController;

    @FXML
    private GameBoardController gameBoardController;

    @FXML
    private GameSidebarController gameSidebarController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameHeaderController.injectMainController(this);
        gameFooterController.injectMainController(this);
        gameBoardController.injectMainController(this);
        gameSidebarController.injectMainController(this);

    }
}
