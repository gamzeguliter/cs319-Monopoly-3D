package sample.squares;

import org.json.JSONObject;
import sample.squares.Square;
import sample.squares.SquareType;

public class ChanceAndCommunityChest extends Square {

    private String name;

    public ChanceAndCommunityChest() {
        super(SquareType.CHANCEANDCOMMUNITYCHEST);
    }

    public ChanceAndCommunityChest(JSONObject jo) {
        super(SquareType.CHANCEANDCOMMUNITYCHEST);
        extractPropertiesFromJson(jo);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public JSONObject getJson() {
        JSONObject jo = new JSONObject();
        jo.put("type", "ChanceAndCommunityChest");
        jo.put("name", name);
        return jo;
    }

    @Override
    public void extractPropertiesFromJson(JSONObject jo) {
        if (jo == null) {
            System.out.println("ERROR: JSONObject passed to ChanceAndCommunityChest was null");
        }

        String type = jo.getString("type");
        if (!type.equals("ChanceAndCommunityChest")) {
            System.out.println("ERROR: ChanceAndCommunityChest initialized with wrong type of JSONObject: " + type);
        }

        name = jo.getString("name");
    }
}
