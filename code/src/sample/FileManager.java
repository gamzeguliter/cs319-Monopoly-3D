package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.*;
import org.json.*;
import sample.entities.Board;
import sample.squares.ColorGroup;
import sample.squares.Square;
import sample.squares.SquareGenerator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class FileManager {

    private static final String configFileName = "boardconfig.json";

    // check if folder exists? this should not be called if folder does not exists anyway
    public static Board readBoardFromFolder(String boardName) {
        String boardPath = getFolderPathFromBoardName(boardName);

        String boardConfigJsonText = readTextFromFile(boardPath + "/" + configFileName);
        JSONObject boardConfigJSON = new JSONObject(boardConfigJsonText);
        System.out.println(boardConfigJSON.toString());

        // check if folder exists, if not there is error in file system
        if (Files.notExists(Paths.get(boardPath))) {
            // the program should never enter here
            System.out.println("ERROR: could not find board to read: " + boardName);
            return null;
        }

        // read and set bord properties
        Board board = new Board(boardName);
        board.setCurrency(boardConfigJSON.getString("currency"));
        board.setMortgageRate(boardConfigJSON.getInt("mortgageRate"));
        board.setRentRate(boardConfigJSON.getInt("rentRate"));

        // generate color groups and give it to board
        JSONArray colorGroupsJSON = boardConfigJSON.getJSONArray("colorGroups");
        ColorGroup[] colorGroups = new ColorGroup[colorGroupsJSON.length()];
        for (int i = 0; i < colorGroups.length; i++) {
            colorGroups[i] = new ColorGroup(colorGroupsJSON.getJSONObject(i));
        }
        board.setColorGroups(new ArrayList<>(Arrays.asList(colorGroups)));

        // generate squares and give it to board
        JSONArray squaresJSON = boardConfigJSON.getJSONArray("squares");
        Square[] squares = new Square[squaresJSON.length()];
        for (int i = 0; i < squares.length; i++) {
            squares[i] = SquareGenerator.getSquareFromJSON(squaresJSON.getJSONObject(i), board);
        }
        board.setSquares(squares);

        return board;
    }

    public static void writeBoardToFolder(Board board) {
        String boardPath = getFolderPathFromBoardName(board.getName());

        // generate the folder if it does not already exist
        if (Files.notExists(Paths.get(boardPath))) {
            File file = new File(boardPath);
            boolean bool = file.mkdir();
            if(bool){
                System.out.println("Directory created successfully");
            }else{
                System.out.println("Couldn’t create specified directory");
            }
        }

        // store board properties
        JSONObject boardConfigJSON = new JSONObject();
        boardConfigJSON.put("name", board.getName());
        boardConfigJSON.put("mortgageRate", board.getMortgageRate());
        boardConfigJSON.put("rentRate", board.getRentRate());
        boardConfigJSON.put("currency", board.getCurrency());

        // store color groups as an array
        ArrayList<ColorGroup> colorGroups = board.getColorGroups();
        JSONArray colorGroupsJSON = new JSONArray();
        for (ColorGroup colorGroup : colorGroups) {
            colorGroupsJSON.put(colorGroup.getJSON());
        }
        boardConfigJSON.put("colorGroups", colorGroupsJSON);

        // store squares as an array
        Square[] squares = board.getSquares();
        JSONArray squaresJSON = new JSONArray();
        for (Square square : squares) {
            squaresJSON.put(square.getJSON());
        }
        boardConfigJSON.put("squares", squaresJSON);

        writeTextToFile(boardPath + "/" + configFileName, boardConfigJSON.toString());
        System.out.println("Succesfully wrote board to file");
    }

    public static Image getImage(String relativePath, int width, int height) {
        InputStream stream;
        try {
            stream = new FileInputStream(System.getProperty("user.dir") + "/" + relativePath);
            return new Image(stream, width, height, false, false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveIconsOnBoard(ArrayList<Image> playerIcons, Image boardIcon, String boardName) {
        String boardPath = getFolderPathFromBoardName(boardName);
        String iconsPath = boardPath + "/icons";

        // generate the folder if it does not already exist
        if (Files.notExists(Paths.get(iconsPath))) {
            File file = new File(iconsPath);
            boolean bool = file.mkdir();
            if(bool){
                System.out.println("Directory created successfully");
            }else{
                System.out.println("Couldn’t create specified directory");
            }
        }

        for (int i = 0; i < playerIcons.size(); i++) {
            Image icon = playerIcons.get(i);
            writeImageToPath(icon, iconsPath + "/" + i + ".png");
            System.out.println(iconsPath + "/" + i + ".png");
        }

        writeImageToPath(boardIcon, boardPath + "/board_icon.png");
    }

    public static ArrayList<Image> getPlayerIcons(String boardName, int width, int height) {
        ArrayList<Image> playerIcons = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            Image icon = getImage("/boards/" + boardName + "/icons/" + i + ".png", width, height);
            if (icon == null)
                icon = generateWhiteImage(width, height);

            playerIcons.add(icon);
        }

        return playerIcons;
    }

    public static Image getBoardIcon(String boardName) {
        Image boardIcon = getImage("/boards/" + boardName + "/board_icon.png", 100, 100);
        if (boardIcon == null) {
            boardIcon = generateWhiteImage(100, 100);
        }
        return boardIcon;
    }

    // helper methods

    public static Image generateWhiteImage(int width, int height) {
        WritableImage img = new WritableImage(width, height);
        PixelWriter pw = img.getPixelWriter();

        // Should really verify 0.0 <= red, green, blue, opacity <= 1.0
        int alpha = 255;
        int r = 255;
        int g = 255;
        int b = 255;

        int pixel = (alpha << 24) | (r << 16) | (g << 8) | b ;
        int[] pixels = new int[width * height];
        Arrays.fill(pixels, pixel);

        pw.setPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), pixels, 0, width);
        return img ;
    }

    private static void writeImageToPath(Image img, String path) {
        File file = new File(path);
        BufferedImage bImage = SwingFXUtils.fromFXImage(img, null);
        try {
            ImageIO.write(bImage, "png", file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getFolderPathFromBoardName(String name) {
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
}
