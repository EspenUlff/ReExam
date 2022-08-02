package com.dtu.roboserver.controller;

import com.dtu.common.Config;
import com.dtu.common.controller.GameController;
import com.dtu.common.model.Board;
import com.dtu.common.model.Player;
import com.dtu.common.model.fileaccess.IOUtil;
import com.dtu.common.model.fileaccess.LoadBoard;
import com.dtu.common.model.fileaccess.SaveGameManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class AppController {


    @GetMapping(value = "/ping")
    public ResponseEntity<String> Ping() {
        return ResponseEntity.ok().body("Pong");
    }

    @GetMapping(value = "/newgame/{players}")
    public ResponseEntity<UUID> NewGame(@PathVariable int players) throws IOException {
        Board board = LoadBoard.loadBoardFromFile(null);
        GameController gameController = new GameController(board);

        for (int i = 0; i < players; i++) {
            Player player = new Player(board, Config.PLAYER_COLORS[i], "Player " + (i + 1));
            board.addPlayer(player);
            player.setSpace(board.getSpace(i % board.width, i));
        }

        gameController.startProgrammingPhase();
        UUID id = UUID.randomUUID();
        SaveGameManager.saveGameToFile(board, id.toString());

        return ResponseEntity.ok(id);
    }

    @GetMapping(value = "/list")
    public ResponseEntity<List<String>> list() {
        var savedGames = SaveGameManager.getSaveNames();

        return ResponseEntity.ok().body(savedGames);
    }

    @GetMapping(value = "/boards")
    public ResponseEntity<ArrayList<Board>> boardList() {
        ArrayList<Board> boardArrayList = new ArrayList<>();
        for (var boardname : IOUtil.getBoardNames()) {
            boardArrayList.add(LoadBoard.loadBoardFromFile(boardname));
        }
        return ResponseEntity.ok().body(boardArrayList);
    }

    /*@GetMapping(value = "/game/{id}")
    public ResponseEntity<Board> getGame(@PathVariable UUID id) {
        GameController gC = games.get(id);
        if (gC == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(gC.board);
    }*/

    /*@PostMapping(value = "/game/finishprograming/{id}")
    public ResponseEntity<Board> finishProgramming(@PathVariable UUID id, @RequestBody CommandCardField[] cards, @RequestBody CommandCardField[] programCards) {
        GameController gC = games.get(id);
        if (gC == null) return ResponseEntity.notFound().build();
        Player player = gC.board.getPlayer(playernumber - 1);
        if (player == null) return ResponseEntity.notFound().build();
        player.setProgram(programCards);
        player.setCards(cards);
        return getGame(id);
    }*/

    @PostMapping(value = "/game/savegame/{id}")
    public ResponseEntity<UUID> saveGame(@PathVariable UUID id, @RequestBody Board board) throws IOException {
        SaveGameManager.saveGameToFile(board, id.toString());
        return ResponseEntity.ok(id);
    }

    @GetMapping(value = "/game/loadgame/{id}")
    public ResponseEntity<Board> loadGame(@PathVariable UUID id){
        var game = SaveGameManager.loadGameFromFileNoExcept(id.toString());
        return ResponseEntity.ok(game);
    }

    @DeleteMapping(value = "/game/{id}")
    public ResponseEntity<String> endGame(@PathVariable UUID id) {
        var deleted = SaveGameManager.deleteGame(id.toString());
        if (!deleted){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Deleted");
    }

    @GetMapping(value = "/game/updategame/{id}")
    public ResponseEntity<Board> updateGame(@PathVariable UUID id){
        var game = SaveGameManager.loadGameFromFileNoExcept(id.toString());
        return ResponseEntity.ok(game);
    }


}
