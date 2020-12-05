import java_.controller.NewGameController;
import java_.game.controller.GameService;
import java_.game.player.Player;
import java_.game.player.PlayerPiece;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

public class BackupNewGame extends Application {

    public static void main(String[] args) throws IOException {

        File playerPiece1File = new File("src/view/res/img/player_piece/alien_ufo_1.png");
        PlayerPiece player1pp = new PlayerPiece(playerPiece1File);
        Player player1 = new Player("bob101", player1pp);

        File playerPiece2File = new File("alien_ufo_2.png");
        PlayerPiece player2pp = new PlayerPiece(playerPiece2File);
        Player player2 = new Player("user", player2pp);

        File playerPiece3File = new File("homer.png");
        PlayerPiece player3pp = new PlayerPiece(playerPiece3File);
        Player player3 = new Player("user", player3pp);

        File playerPiece4File = new File("red_temp_pp.png");
        PlayerPiece player4pp = new PlayerPiece(playerPiece4File);
        Player player4 = new Player("nylecm", player4pp);


        Player[] players = new Player[4];
        players[0] = player1;
        players[1] = player2;
        players[2] = player3;
        players[3] = player4;



        String gameboardName = "oberon_1.txt";

        GameService.getInstance().loadNewGame(players, gameboardName);

        Stage currentStage = null;
        Pane game = (Pane) FXMLLoader.load(BackupNewGame.class.getResource("../../view/layout/game.fxml"));
        currentStage.setScene(new Scene(game));








    }


    @Override
    public void start(Stage primaryStage) throws Exception {




        Pane game = (Pane) FXMLLoader.load(BackupNewGame.class.getResource("../../view/layout/game.fxml"));
        primaryStage.setScene(new Scene(game));
        primaryStage.show();



    }
}
