package com.dtu.roboserver.controller;

import com.dtu.model.Board;
import com.dtu.model.Player;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.dtu.Config;

import java.util.HashMap;
import java.util.UUID;

@RestController
public class AppController {
    HashMap <UUID, GameController> games = new HashMap<>();

    @GetMapping (value = "/ping")
    public ResponseEntity<String> Ping() {
        return ResponseEntity.ok().body("Pong");
    }

    @PostMapping (value = "/newgame/{players}")
    public ResponseEntity<UUID> NewGame(@PathVariable int players){
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

    @GetMapping (value = "/list")
    public ResponseEntity<HashMap> list() {
        return ResponseEntity.ok().body(games);
    }


}
