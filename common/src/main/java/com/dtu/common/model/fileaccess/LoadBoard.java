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
package com.dtu.common.model.fileaccess;

import com.dtu.common.Config;
import com.dtu.common.model.Board;
import com.dtu.common.model.FieldAction;
import com.dtu.common.model.Player;
import com.dtu.common.model.Space;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class LoadBoard {
    private static final String DEFAULTBOARD = "defaultboard";
    private static final String JSON_EXT = "json";

    public static final String savesPath = Path.of(System.getenv("APPDATA"), "Roborally", Config.GAMESFOLDER).toString();

    public static Board loadBoardFromFile(String boardname) {
        return loadBoardFromFile(boardname, Config.BOARDSFOLDER);
    }

    public static Board loadBoardFromFile(String boardname, String path) {
        if (boardname == null) {
            boardname = DEFAULTBOARD;
        }

        ClassLoader classLoader = LoadBoard.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(path + "/" + boardname + "." + JSON_EXT);
        if (inputStream == null) {
            return new Board(Config.DEFAULT_BOARD_WIDTH, Config.DEFAULT_BOARD_HEIGHT);
        }

        // In simple cases, we can create a Gson object with new Gson():
        GsonBuilder simpleBuilder = new GsonBuilder().registerTypeAdapter(Player.class, new Adapter<Player>()).registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>()).registerTypeAdapter(Board.class, new Adapter<Board>());

        Gson gson = simpleBuilder.create();

        Board result;
        // FileReader fileReader = null;
        JsonReader reader = null;
        try {
            // fileReader = new FileReader(filename);
            reader = gson.newJsonReader(new InputStreamReader(inputStream));
            BoardTemplate template = gson.fromJson(reader, BoardTemplate.class);

            result = new Board(template.width, template.height);
            for (PlayerTemplate playerT : template.players) {
                Player player = new Player(result, playerT.color, playerT.name);
                player.setSpace(result.getSpace(playerT.x, playerT.y));
                result.addPlayer(player);
            }

            for (SpaceTemplate spaceTemplate : template.spaces) {
                Space space = result.getSpace(spaceTemplate.x, spaceTemplate.y);
                if (space != null) {
                    space.getActions().addAll(spaceTemplate.actions);
                    space.getWalls().addAll(spaceTemplate.walls);
                }
            }

            reader.close();
            return result;
        } catch (IOException e1) {
            if (reader != null) {
                try {
                    reader.close();
                    inputStream = null;
                } catch (IOException e2) {
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e2) {
                }
            }
        }
        return null;
    }

    public static Board loadBoard(String jsonBoard) {
        GsonBuilder simpleBuilder = new GsonBuilder().
                //registerTypeAdapter(Player.class, new Adapter<Player>()).
                        registerTypeAdapter(FieldAction.class, new FieldActionTypeAdapter());

        Gson gson = simpleBuilder.create();

        //BoardTemplate template = gson.fromJson(jsonBoard, BoardTemplate.class);
        return gson.fromJson(jsonBoard, Board.class);
    }

}
