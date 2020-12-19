package sample;

import org.json.*;
import sample.squares.*;
import sample.squares.Property;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class FileManager {

    private static final String jsonName = "/boardconfig.json";

    private static String getFolderFromName(String name) {
        return System.getProperty("user.dir") + "/boards/" + name;
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

        // check if folder exists, if not there is error in file system
        if (Files.notExists(Paths.get(root))) {
            System.out.println("ERROR: could not find board to read: " + boardName);
        }

        Board board = new Board(boardName);

        JSONObject jo = new JSONObject(boardConfigJsonText);

        JSONArray colorGroupsJSON = jo.getJSONArray("colorGroups");
        ColorGroup[] colorGroups = new ColorGroup[colorGroupsJSON.length()];
        for (int i = 0; i < colorGroups.length; i++) {
            colorGroups[i] = new ColorGroup(colorGroupsJSON.getJSONObject(i));
        }
        board.setColorGroups(new ArrayList<>(Arrays.asList(colorGroups)));

        JSONArray squaresJSON = jo.getJSONArray("squares");
        Square[] squares = new Square[squaresJSON.length()];
        for (int i = 0; i < squares.length; i++) {
            JSONObject squareJSON = squaresJSON.getJSONObject(i);
            String type = squareJSON.getString("type");

            switch (type) {
                case "ChanceAndCommunityChest" -> squares[i] = new ChanceAndCommunityChest(squareJSON);
                case "Joker" -> squares[i] = new Joker(squareJSON);
                case "Property" -> squares[i] = new Property(squareJSON, board);
                case "Start" -> squares[i] = new Start(squareJSON);
                default -> System.out.println("ERROR: Type of square was invalid: " + type);
            }
        }
        board.setSquares(squares);

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
        jo.put("name", board.getName());
        jo.put("mortgageRate", board.getMortgageRate());
        jo.put("rentRate", board.getRentRate());
        jo.put("currency", board.getCurrency());

        ArrayList<ColorGroup> colorGroups = board.getColorGroups();
        JSONArray colorGroupsJSON = new JSONArray();
        for (ColorGroup colorGroup : colorGroups) {
            colorGroupsJSON.put(colorGroup.getJSON());
        }
        jo.put("colorGroups", colorGroupsJSON);

        Square[] squares = board.getSquares();
        JSONArray squaresJSON = new JSONArray();
        for (Square square : squares) {
            squaresJSON.put(square.getJSON());
        }
        jo.put("squares", squaresJSON);

        writeTextToFile(root + jsonName, jo.toString());
    }
}
