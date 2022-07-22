package com.dtu.roboserver.controller;

import com.dtu.controller.IGameController;
import com.dtu.model.FieldAction;
import com.dtu.model.Heading;
import com.dtu.model.Space;
import org.jetbrains.annotations.NotNull;


public class RotateGear extends FieldAction {

    private Heading heading;

    public Heading getHeading() {
        return heading;
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    @Override
    public boolean doAction(@NotNull IGameController gameController, @NotNull Space space, int direction) {
        if (direction == 1){
            gameController.turnRight(space.getPlayer());
        }
        if (direction == 2){
            gameController.turnLeft(space.getPlayer());
        }
        return false;
    }
}