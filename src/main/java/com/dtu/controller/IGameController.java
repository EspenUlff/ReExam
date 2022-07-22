package com.dtu.controller;

import com.dtu.model.CommandCardField;
import com.dtu.model.Heading;
import com.dtu.model.Player;
import com.dtu.model.Space;
import org.jetbrains.annotations.NotNull;

public interface IGameController {
    void moveToSpace (@NotNull Player player, @NotNull Space space, @NotNull Heading heading);
    void moveCurrentPlayerToSpace(@NotNull Space space);
    void startProgrammingPhase();
    void finishProgrammingPhase();
    void executePrograms();
    void executeStep();
    void moveForward(@NotNull Player player, int amount);
    void moveForward(@NotNull Player player, int amount, Heading heading);
    void fastForward(@NotNull Player player);
    void turnRight(@NotNull Player player);
    void turnLeft(@NotNull Player player);
    boolean moveCards(@NotNull CommandCardField source, @NotNull CommandCardField target);

}
