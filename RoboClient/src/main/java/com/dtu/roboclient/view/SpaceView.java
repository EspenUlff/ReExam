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
package com.dtu.roboclient.view;

import com.dtu.common.model.Heading;
import com.dtu.common.model.fieldTypes.ConveyorBelt;
import com.dtu.common.model.fieldTypes.RotateGear;
import com.dtu.common.observer.Subject;
import com.dtu.common.model.Player;
import com.dtu.common.model.Space;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import org.jetbrains.annotations.NotNull;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class SpaceView extends StackPane implements ViewObserver {

    final public static int SPACE_HEIGHT = 60; // 60; // 75;
    final public static int SPACE_WIDTH = 60;  // 60; // 75;

    public final Space space;


    public SpaceView(@NotNull Space space) {
        this.space = space;

        // XXX the following styling should better be done with styles
        this.setPrefWidth(SPACE_WIDTH);
        this.setMinWidth(SPACE_WIDTH);
        this.setMaxWidth(SPACE_WIDTH);

        this.setPrefHeight(SPACE_HEIGHT);
        this.setMinHeight(SPACE_HEIGHT);
        this.setMaxHeight(SPACE_HEIGHT);

        if ((space.x + space.y) % 2 == 0) {
            this.setStyle("-fx-background-color: white;");
        } else {
            this.setStyle("-fx-background-color: black;");
        }

        space.attach(this);
        update(space);
    }

    private void renderPlayer() {

        Player player = space.getPlayer();
        if (player != null) {
            Polygon arrow = new Polygon(0.0, 0.0,
                    10.0, 20.0,
                    20.0, 0.0);
            try {
                arrow.setFill(Color.valueOf(player.getColor()));
            } catch (Exception e) {
                arrow.setFill(Color.MEDIUMPURPLE);
            }

            arrow.setRotate((90 * player.getHeading().ordinal()) % 360);
            this.getChildren().add(arrow);
        }
    }

    ImagePattern getImageResource(String path) {
        return new ImagePattern(new Image(getClass().getResource(path).toString()));
    }

    private void renderWalls() {

        var wall = getImageResource("/graphics/Wall.png");

        for (Heading wallHeading : this.space.getWalls()) {
            Rectangle rectangle = new Rectangle(0.0, 0.0, SPACE_WIDTH, SPACE_HEIGHT);

            rectangle.setRotate(wallHeading.ordinal() * 90);
            rectangle.toFront();
            rectangle.setFill(wall);
            this.getChildren().add(rectangle);
        }
    }

    private void renderBG() {
        space.actions.stream()
                .filter(fieldAction -> fieldAction instanceof ConveyorBelt)
                .findFirst()
                .ifPresent(cb -> {
            var conveyorBelt = (ConveyorBelt) cb;
            if (conveyorBelt.power == 1){
                var belt = getImageResource("/graphics/ConveyorBelt_Green.png");
                Rectangle rectangle = new Rectangle(0.0, 0.0, SPACE_WIDTH, SPACE_HEIGHT);

                rectangle.setRotate(conveyorBelt.heading.ordinal() * 90);
                rectangle.toBack();
                rectangle.setFill(belt);
                this.getChildren().add(rectangle);
            }
            if(conveyorBelt.power == 2) {
                var belt = getImageResource("/graphics/ConveyorBelt_Blue.png");
                Rectangle rectangle = new Rectangle(0.0, 0.0, SPACE_WIDTH, SPACE_HEIGHT);

                rectangle.setRotate(conveyorBelt.heading.ordinal() * 90);
                rectangle.toBack();
                rectangle.setFill(belt);
                this.getChildren().add(rectangle);
            }
        });
        space.actions.stream()
                .filter(fieldAction -> fieldAction instanceof RotateGear)
                .findFirst()
                .ifPresent(rGear -> {
                    var rotateGear = (RotateGear) rGear;
                    if (rotateGear.direction == 1){
                        var gear = getImageResource("/graphics/GearRight.png");
                        Rectangle rectangle = new Rectangle(0.0, 0.0, SPACE_WIDTH, SPACE_HEIGHT);

                        rectangle.toBack();
                        rectangle.setFill(gear);
                        this.getChildren().add(rectangle);
                    }
                    if(rotateGear.direction == 2) {
                        var gear = getImageResource("/graphics/GearLeft.png");
                        Rectangle rectangle = new Rectangle(0.0, 0.0, SPACE_WIDTH, SPACE_HEIGHT);

                        rectangle.toBack();
                        rectangle.setFill(gear);
                        this.getChildren().add(rectangle);
                    }
    });
    }


    @Override
    public void updateView(Subject subject) {
        if (subject == this.space) {
            this.getChildren().clear();
            renderBG();
            renderWalls();
            renderPlayer();
        }
    }

}
