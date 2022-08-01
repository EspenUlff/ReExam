package com.dtu.common.model.fileaccess;

import com.dtu.common.Config;
import com.dtu.common.model.Board;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;

public class SaveGameManager {
    private static final Path appData = Path.of(System.getenv("APPDATA"),"Roborally", Config.GAMESFOLDER);

    public static void saveGameToFile(Board board, String fileName) throws IOException {
        var file = Path.of(appData.toString(), fileName).toFile();
        file.getParentFile().mkdirs();

        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(board);
        oos.close();
    }

    public static Board loadGameFromFile(String fileName) throws IOException, ClassNotFoundException {
        var file = Path.of(appData.toString(), fileName).toFile();

        FileInputStream fin = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fin);
        Board board = (Board) ois.readObject();
        ois.close();
        return board;
    }
    public static Board loadGameFromFileNoExcept(String fileName){
            try {
                return loadGameFromFile(fileName);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
    }

    public static boolean deleteGame(String fileName) {
        var file = Path.of(appData.toString(), fileName).toFile();

        return file.delete();
    }

    public static ArrayList<String> getSaveNames() {
        File[] saveNames = new File(String.valueOf(appData)).listFiles();

        ArrayList<String> saves = new ArrayList<>();
        for (File saveName : saveNames) {
            String name = saveName.getName();
            saves.add(name);

        }
        return saves;
    }
}
