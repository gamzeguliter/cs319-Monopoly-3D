package sample.squares;

import org.json.JSONObject;
import sample.Board;

public class SquareGenerator {


    public static Square getSquareFromJSON(JSONObject jo, Board board) {
        String type = jo.getString("type");
        System.out.println(type);
        switch (type) {
            case "ChanceAndCommunityChest" -> {return new ChanceAndCommunityChest(jo);}
            case "Joker" -> {return new Joker(jo);}
            case "Property" -> {return new Property(jo, board);}
            case "Start" -> {return new Start(jo);}
            default -> System.out.println("ERROR: Type of square was invalid: " + type);
        }
        return null;
    }
}
