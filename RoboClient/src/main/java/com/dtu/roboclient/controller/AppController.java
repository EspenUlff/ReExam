package com.dtu.roboclient.controller;

import com.dtu.common.Config;
import com.dtu.common.controller.GameController;
import com.dtu.common.model.fileaccess.IOUtil;
import com.dtu.roboclient.RoboRally;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

public class AppController {

    public static final String BaseURI = "http://localhost:8080";
    RoboRally roboRally;
    //GameController gameController;
    UUID gameID;

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
            gameID = HttpClientSynchronous.NewGameNoExcept(result.get());

            var board = HttpClientSynchronous.GetGameNoExcept(gameID);
            roboRally.createBoardView(new GameController(board));
        }
    }

    public void saveGame() {
        HttpClientSynchronous.saveGameNoExcept(gameID);
    }

    public void loadGame() {
        ChoiceDialog<String> choices = new ChoiceDialog<>(IOUtil.getSaveNames().get(0), IOUtil.getSaveNames());
        choices.setTitle("Load Board");
        choices.setHeaderText("Select a saved game to load");
        Optional<String> result = choices.showAndWait();

        HttpClientSynchronous.loadGameNoExcept(UUID.fromString(result.get()));
        HttpClientSynchronous.GetGameNoExcept(UUID.fromString((result.get())));
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
        if (gameID != null) {
            ChoiceDialog<String> choices = new ChoiceDialog<>("Save Game", "Keep playing");
            choices.setTitle("Save game");
            choices.setHeaderText("Do you want to save and exit the game?");
            Optional<String> result = choices.showAndWait();

            if (result.get().equals("Save Game")) {
                saveGame();

                gameID = null;
                roboRally.createBoardView(null);
                return true;
            }
        }
        return false;
    }

    public void exit() {
        // TODO needs to be implemented - low priority
        if (gameID != null) {
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
        if (gameID == null || stopGame()) {
            Platform.exit();
        }
    }

    public boolean isGameRunning() {
        return gameID != null;
    }
}
