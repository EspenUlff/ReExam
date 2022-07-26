package com.dtu.common.controller;

import com.dtu.common.model.*;
import org.jetbrains.annotations.NotNull;

public interface IGameController {

    Board getBoard();
    void moveToSpace (@NotNull Player player, @NotNull Space space, @NotNull Heading heading) throws GameController.ImpossibleMoveException;
    void moveCurrentPlayerToSpace(@NotNull Space space);
    void startProgrammingPhase();
    void finishProgrammingPhase();
    void executePrograms();
    void executeStep();
    void moveForward(@NotNull Player player, int amount);
    void moveForward(@NotNull Player player, int amount, Heading heading) throws GameController.ImpossibleMoveException;
    void turnRight(@NotNull Player player);
    void turnLeft(@NotNull Player player);
    boolean moveCards(@NotNull CommandCardField source, @NotNull CommandCardField target);

    void cardOption(Command option);
}
