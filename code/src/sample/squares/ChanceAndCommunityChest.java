package sample.squares;

import org.json.JSONObject;
import sample.squares.Square;
import sample.squares.SquareType;

public class ChanceAndCommunityChest extends Square {

    private String name;

    public ChanceAndCommunityChest() {
        super(SquareType.CHANCEANDCOMMUNITYCHEST);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public JSONObject getJson() {
        return null;
    }

    @Override
    public void extractPropertiesFromJson(JSONObject json) {

    }
}
