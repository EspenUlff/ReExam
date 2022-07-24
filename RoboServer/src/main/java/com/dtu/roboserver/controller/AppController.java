package com.dtu.roboserver.controller;

import com.dtu.common.controller.GameController;
import com.dtu.common.model.Board;
import com.dtu.common.model.CommandCardField;
import com.dtu.common.model.Player;
import com.dtu.common.Config;
import com.dtu.common.model.fileaccess.LoadBoard;
import com.dtu.common.model.fileaccess.SaveBoard;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.UUID;

@RestController
public class AppController {
    HashMap<UUID, GameController> games = new HashMap<>();

    @GetMapping(value = "/ping")
    public ResponseEntity<String> Ping() {
        return ResponseEntity.ok().body("Pong");
    }

    @GetMapping(value = "/newgame/{players}")
    public ResponseEntity<UUID> NewGame(@PathVariable int players) {
        Board board = LoadBoard.loadBoardFromFile(null);
        GameController gameController = new GameController(board);
        UUID id = UUID.randomUUID();
        id.toString();
        games.put(id, gameController);

        for (int i = 0; i < players; i++) {
            Player player = new Player(board, Config.PLAYER_COLORS[i], "Player " + (i + 1));
            board.addPlayer(player);
            player.setSpace(board.getSpace(i % board.width, i));
        }

        return ResponseEntity.ok(id);
    }

    @GetMapping(value = "/list")
    public ResponseEntity<HashMap> list() {
        return ResponseEntity.ok().body(games);
    }

    @GetMapping(value = "/game/{id}")
    public ResponseEntity<Board> getGame(@PathVariable UUID id) {
        GameController gC = games.get(id);
        if (gC == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(gC.board);
    }

    @PostMapping(value = "/game/finishprograming/{id}/{playernumber}")
    public ResponseEntity<Board> finishProgramming(@PathVariable UUID id, @PathVariable int playernumber, @RequestBody CommandCardField[] cards, @RequestBody CommandCardField[] programCards){
        GameController gC = games.get(id);
        if (gC == null) return ResponseEntity.notFound().build();
        Player player = gC.board.getPlayer(playernumber-1);
        if (player == null) return ResponseEntity.notFound().build();
        player.setProgram(programCards);
        player.setCards(cards);
        return getGame(id);
    }

    @GetMapping (value = "/game/generatecards/{id}/{playernumber}")
    public ResponseEntity<String> generateCards(@PathVariable UUID id, @PathVariable int playernumber){
        return null;
    }

    @GetMapping (value = "/game/savegame/{id}")
    public ResponseEntity<String> saveGame(@PathVariable UUID id){
        var game = games.get(id);
        if (game == null) return ResponseEntity.notFound().build();
        SaveBoard.saveBoard(game.board, id.toString());
        return ResponseEntity.ok("Saved game");
    }

    @DeleteMapping (value = "/game/{id}")
    public ResponseEntity<String> endGame(@PathVariable UUID id){
        return null;
    }


}
