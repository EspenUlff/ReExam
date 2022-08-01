/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package com.dtu.common.model.fieldTypes;

import com.dtu.common.controller.GameController;
import com.dtu.common.controller.IGameController;
import com.dtu.common.model.FieldAction;
import com.dtu.common.model.Heading;
import com.dtu.common.model.Space;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class ConveyorBelt extends FieldAction implements Serializable {

    public Heading heading;

    public Heading getHeading() {
        return heading;
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    public ConveyorBelt(Heading heading) {
        this.heading = heading;
    }

    @Override
    public boolean doAction(@NotNull IGameController gameController, @NotNull Space space, int amount) {
        try {
            gameController.moveForward(space.getPlayer(), amount, this.heading);
        } catch (GameController.ImpossibleMoveException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
