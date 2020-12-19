package sample.squares;

import org.json.JSONObject;
import sample.squares.Square;
import sample.squares.SquareType;

public class Start extends Square {

    private int money;

    public Start(int money) {
        super(SquareType.START);
        this.money = money;
    }

    public Start(JSONObject jo) {
        super(SquareType.START);
        extractPropertiesFromJSON(jo);
    }

    public int getMoney() {
            return money;
        }

    public void setMoney(int money) {
        this.money = money;
    }

    @Override
    public JSONObject getJSON() {
        JSONObject jo = new JSONObject();
        jo.put("type", "Start");
        jo.put("money", money);
        return jo;
    }

    @Override
    public void extractPropertiesFromJSON(JSONObject jo) {
        if (jo == null) {
            System.out.println("ERROR: JSONObject passed to Start was null");
        }

        String type = jo.getString("type");
        if (!type.equals("Start")) {
            System.out.println("ERROR: Start initialized with wrong type of JSONObject: " + type);
        }

        money = jo.getInt("money");
    }
}
