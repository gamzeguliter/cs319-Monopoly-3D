package sample.squares;

import org.json.JSONObject;

public abstract class Square {
    private SquareType type;

    public Square(SquareType type) {
        this.type = type;
    }

    public void setType(SquareType newType) {
        this.type = newType;
    }

    public SquareType getType() { return type; }

    public abstract JSONObject getJson();
    public abstract void extractPropertiesFromJson(JSONObject json);
}
