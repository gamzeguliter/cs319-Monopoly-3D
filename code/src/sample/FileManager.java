package sample;

import org.json.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class FileManager {

    private static final String jsonName = "\\boardconfig.json";
    private static final String iconsFolderName = "\\icons";

    private static String getFolderFromName(String name) {
        return System.getProperty("user.dir") + "\\boards\\" + name;
    }

    private static String readTextFromFile(String path) {
        String data = "";
        try {
            data = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            System.out.println("Error occurred while reading from file: " + e.getMessage());
        }
        return data;
    }

    private static void writeTextToFile(String path, String text) {
        FileOutputStream fos = null;
        DataOutputStream dos = null;

        try {
            fos = new FileOutputStream(path);
            dos = new DataOutputStream(fos);
            dos.writeBytes(text);
        }
        catch (FileNotFoundException fnfe) {
            System.out.println("File not found" + fnfe);
        }
        catch (IOException ioe) {
            System.out.println("Error while writing to file" + ioe);
        }
        finally {
            try {
                if (dos != null)
                    dos.close();

                if (fos != null)
                    fos.close();
            }
            catch (Exception e) {
                System.out.println("Error while closing streams" + e);
            }
        }
    }

    // check if folder exists? this should not be called if folder does not exists anyway
    public static Board readBoardFromFolder(String boardName) {
        String root = getFolderFromName(boardName);
        String boardConfigJsonText = readTextFromFile(root + jsonName);
        System.out.println(boardConfigJsonText);
        return new Board();
    }

    public static void writeBoardToFolder(Board board) {
        String root = getFolderFromName(board.getName());

        // generate the folder if it does not already exist
        if (Files.notExists(Paths.get(root))) {
            File file = new File(root);
            boolean bool = file.mkdir();
            if(bool){
                System.out.println("Directory created successfully");
            }else{
                System.out.println("Couldn’t create specified directory");
            }
        }

        JSONObject jo = new JSONObject();
        jo.put("name", "jon doe");
        jo.put("age", "22");
        jo.put("city", "chicago");
        String sa = jo.toString();

        String boardConfigJsonText = "{ name: \"John\", age: 31, city: \"New York\" }";
        writeTextToFile(root + jsonName, boardConfigJsonText);
    }
}
