package com.dtu.common.model.fieldTypes;

import com.dtu.common.controller.IGameController;
import com.dtu.common.model.FieldAction;
import com.dtu.common.model.Space;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;


public class RotateGear extends FieldAction implements Serializable {

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