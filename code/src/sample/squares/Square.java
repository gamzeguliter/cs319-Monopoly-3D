package sample.squares;

import org.json.JSONObject;

public abstract class Square implements JSONable {
    private SquareType type;

    public Square(SquareType type) {
        this.type = type;
    }

    public void setType(SquareType newType) {
        this.type = newType;
    }

    public SquareType getType() { return type; }
}
