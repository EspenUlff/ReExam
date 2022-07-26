package com.dtu.roboclient.controller;

import com.dtu.common.Config;
import com.dtu.common.controller.GameController;
import com.dtu.common.model.Board;
import com.dtu.common.model.Player;
import com.dtu.common.model.fileaccess.SaveBoard;
import com.dtu.roboclient.RoboRally;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

public class AppController {

    public static final String BaseURI = "http://localhost:8080";
    RoboRally roboRally;
    GameController gameController;

    public AppController(RoboRally roboRally) {
        this.roboRally = roboRally;
    }

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public void newGame() {
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(Config.PLAYER_NUMBER_OPTIONS.get(0), Config.PLAYER_NUMBER_OPTIONS);
        dialog.setTitle("Player number");
        dialog.setHeaderText("Select number of players");
        Optional<Integer> result = dialog.showAndWait();

        if (result.isPresent()) {
            if (gameController != null) {
                // The UI should not allow this, but in case this happens anyway.
                // give the user the option to save the game or abort this operation!
                if (!stopGame()) {
                    return;
                }
            }

            // XXX the board should eventually be created programmatically or loaded from a file
            //     here we just create an empty board with the required number of players.
            Board board = new Board(Config.DEFAULT_BOARD_WIDTH,Config.DEFAULT_BOARD_HEIGHT);
            gameController = new GameController(board);
            int no = result.get();
            for (int i = 0; i < no; i++) {
                Player player = new Player(board, Config.PLAYER_COLORS[i], "Player " + (i + 1));
                board.addPlayer(player);
                player.setSpace(board.getSpace(i % board.width, i));
            }
            // XXX: V2
            // board.setCurrentPlayer(board.getPlayer(0));


            gameController.startProgrammingPhase();

            roboRally.createBoardView(gameController);
        }
    }

    //TODO: brug samme UUID til at gemme spillet
    public void saveGame(UUID gameID) {
        SaveBoard.saveBoard(gameController.board, gameID.toString());
    }


    /**
     * Stop playing the current game, giving the user the option to save
     * the game or to cancel stopping the game. The method returns true
     * if the game was successfully stopped (with or without saving the
     * game); returns false, if the current game was not stopped. In case
     * there is no current game, false is returned.
     *
     * @return true if the current game was stopped, false otherwise
     */
    public boolean stopGame() {
        if (gameController != null) {

            // here we save the game (without asking the user).
            saveGame();

            gameController = null;
            roboRally.createBoardView(null);
            return true;
        }
        return false;
    }

    public void exit() {
        // TODO needs to be implemented - low priority
        if (gameController != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Exit RoboRally?");
            alert.setContentText("Are you sure you want to exit RoboRally?");
            Optional<ButtonType> result = alert.showAndWait();

            if (!result.isPresent() || result.get() != ButtonType.OK) {
                return; // return without exiting the application
            }
        }

        // If the user did not cancel, the RoboRally application will exit
        // after the option to save the game
        if (gameController == null || stopGame()) {
            Platform.exit();
        }
    }

    //TODO: get feedback
    public boolean isGameRunning() {return gameController != null;}

    public String[] getBoardList(){
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BaseURI+"/boards"))
                .build();
        try {
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            Gson gson = new Gson();
            String[] boards = gson.fromJson(response.body(), String[].class);

            return boards;
        } catch (InterruptedException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;

    }
}
