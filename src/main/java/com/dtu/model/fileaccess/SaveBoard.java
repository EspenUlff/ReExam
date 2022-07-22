package com.dtu.model.fileaccess;

import com.dtu.model.Board;
import com.dtu.model.Player;
import com.dtu.model.Space;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveBoard {
    private static final String BOARDSFOLDER = "boards";
    private static final String JSON_EXT = "json";

    public static void saveBoard(Board board, String name) {
        BoardTemplate template = new BoardTemplate();
        template.width = board.width;
        template.height = board.height;

        for (int i=0; i<board.width; i++) {
            for (int j=0; j<board.height; j++) {
                Space space = board.getSpace(i,j);
                if (!space.getWalls().isEmpty() || !space.getActions().isEmpty()) {
                    SpaceTemplate spaceTemplate = new SpaceTemplate();
                    spaceTemplate.x = space.x;
                    spaceTemplate.y = space.y;
                    spaceTemplate.actions.addAll(space.getActions());
                    spaceTemplate.walls.addAll(space.getWalls());
                    template.spaces.add(spaceTemplate);
                }
            }
        }

        for (Player player: board.getPlayers()) {
            template.players.add(new PlayerTemplate(player.getName(), player.getColor(), player.getSpace().x, player.getSpace().y, player.getHeading()));
        }

        ClassLoader classLoader = LoadBoard.class.getClassLoader();
        // TODO: this is not very defensive, and will result in a NullPointerException
        //       when the folder "resources" does not exist! But, it does not need
        //       the file "simpleCards.json" to exist!
        String filename =
                classLoader.getResource(BOARDSFOLDER).getPath() + "/" + name + "." + JSON_EXT;

        // In simple cases, we can create a Gson object with new:
        //
        // Gson gson = new Gson();
        //
        // But, if you need to configure it, it is better to create it from
        // a builder (here, we want to configure the JSON serialisation with
        // a pretty printer):
        GsonBuilder simpleBuilder = new GsonBuilder().
                //registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>()).
                //registerTypeAdapter(PlayerTemplate.class, new Adapter<PlayerTemplate>()).
                        setPrettyPrinting();
        Gson gson = simpleBuilder.create();



        FileWriter fileWriter = null;
        JsonWriter writer = null;
        try {
            File file = new File(filename);
            file.createNewFile();
            fileWriter = new FileWriter(file);
            writer = gson.newJsonWriter(fileWriter);
            String thisJson = gson.toJson(template, template.getClass());
            System.out.println(thisJson);
            writer.close();
        } catch (IOException e1) {
            e1.printStackTrace();
            if (writer != null) {
                try {
                    writer.close();
                    fileWriter = null;
                } catch (IOException e2) {}
            }
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e2) {}
            }
        }
    }
}
