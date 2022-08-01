package com.dtu.common.model.fieldTypes;

import com.dtu.common.controller.IGameController;
import com.dtu.common.model.Board;
import com.dtu.common.model.FieldAction;
import com.dtu.common.model.Player;
import com.dtu.common.model.Space;

import java.io.Serializable;

public class Checkpoint extends FieldAction implements Serializable {
    private int checkpointNumber;

    public Checkpoint(int checkpointNumber) {
        this.checkpointNumber = checkpointNumber;
    }

    @Override
    public boolean doAction(IGameController gameController, Space space, int var) {
        Player player = space.getPlayer();
        Board board = gameController.getBoard();

        if (player != null && player.getCheckpointProgress() == this.checkpointNumber - 1 && board.getStep() == 4) {
            player.setCheckpointProgress(checkpointNumber);
        }

        if (player.getCheckpointProgress() == board.getTotalCheckpoints() && board.getWinner() == null) {
            board.setWinner(player);
        }
        return false;
    }

    public int getCheckpointNumber() {
        return checkpointNumber;
    }

    public void setCheckpointNumber(int checkpointNumber) {
        this.checkpointNumber = checkpointNumber;
    }
}
