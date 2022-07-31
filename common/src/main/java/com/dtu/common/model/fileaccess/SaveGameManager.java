package com.dtu.common.model.fileaccess;

import com.dtu.common.Config;
import com.dtu.common.model.Board;

import java.io.*;
import java.nio.file.Path;

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
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
    }
}
