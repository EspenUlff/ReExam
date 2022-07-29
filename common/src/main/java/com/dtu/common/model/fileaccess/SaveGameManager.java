package com.dtu.common.model.fileaccess;

import com.dtu.common.model.Board;

import java.io.*;

public class SaveGameManager {

    public static void saveGameToFile(Board board, String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(board);
        oos.close();
    }

    public static Board loadGameFromFile(String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fin = new FileInputStream(fileName);
        ObjectInputStream ois = new ObjectInputStream(fin);
        Board board = (Board) ois.readObject();
        ois.close();
        return board;
    }
}
