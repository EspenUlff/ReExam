package com.dtu.controller;

import com.dtu.model.*;
import org.jetbrains.annotations.NotNull;

public interface IGameController {
    void moveToSpace (@NotNull Player player, @NotNull Space space, @NotNull Heading heading);
    void startProgrammingPhase();
    void finishProgrammingPhase();
    void executePrograms();
    void executeStep();
    void moveForward(@NotNull Player player);
    void fastForward(@NotNull Player player);
    void turnRight(@NotNull Player player);
    void turnLeft(@NotNull Player player);
    boolean moveCards(@NotNull CommandCardField source, @NotNull CommandCardField target);

}
