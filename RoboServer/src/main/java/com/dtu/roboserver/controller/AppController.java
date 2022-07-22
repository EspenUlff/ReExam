package com.dtu.roboserver.controller;

import com.dtu.model.Board;
import com.dtu.model.CommandCardField;
import com.dtu.model.Player;
import com.dtu.Config;
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

    @PostMapping(value = "/newgame/{players}")
    public ResponseEntity<UUID> NewGame(@PathVariable int players) {
        Board board = new Board(8, 8);
        GameController gameController = new GameController(board);
        UUID id = UUID.randomUUID();
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
    public ResponseEntity<Board> saveGame(@PathVariable UUID id){
        return null;
    }

    @DeleteMapping (value = "/game/{id}")
    public ResponseEntity<String> endGame(@PathVariable UUID id){
        return null;
    }


}
