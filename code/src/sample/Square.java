package sample;

public class Square {
    private SquareType type;

    public Square(SquareType type) {
        this.type = type;
    }

    public void setType(SquareType newType) {
        this.type = newType;
    }

    public SquareType getType() { return type; }
}
