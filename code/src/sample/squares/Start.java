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

    public int getMoney() {
            return money;
        }

    public void setMoney(int money) {
        this.money = money;
    }

    @Override
    public JSONObject getJson() {
        return null;
    }

    @Override
    public void extractPropertiesFromJson(JSONObject json) {

    }
}
