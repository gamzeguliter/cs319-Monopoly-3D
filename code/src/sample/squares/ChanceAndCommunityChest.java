package sample.squares;

import org.json.JSONObject;
import sample.squares.Square;
import sample.squares.SquareType;

public class ChanceAndCommunityChest extends Square {

    private String name;
    private boolean isChance;

    public ChanceAndCommunityChest(boolean chance) {
        super(SquareType.CHANCEANDCOMMUNITYCHEST);
        isChance = chance;
        if (isChance) {
            name = "chance";
        } else {
            name = "community";
        }
    }

    public boolean isChance() {
        return isChance;
    }

    public ChanceAndCommunityChest(JSONObject jo) {
        super(SquareType.CHANCEANDCOMMUNITYCHEST);
        extractPropertiesFromJSON(jo);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public JSONObject getJSON() {
        JSONObject jo = new JSONObject();
        jo.put("type", "ChanceAndCommunityChest");
        jo.put("name", name);
        jo.put("isChance", isChance);
        return jo;
    }

    @Override
    public void extractPropertiesFromJSON(JSONObject jo) {
        if (jo == null) {
            System.out.println("ERROR: JSONObject passed to ChanceAndCommunityChest was null");
        }

        String type = jo.getString("type");
        if (!type.equals("ChanceAndCommunityChest")) {
            System.out.println("ERROR: ChanceAndCommunityChest initialized with wrong type of JSONObject: " + type);
        }

        name = jo.getString("name");
        isChance = jo.getBoolean("isChance");
    }
}
