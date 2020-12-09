package sample;

public abstract class Tile {

    // properties
    String type;

    // constructors
    public Tile(String type) {
        this.type = type;
    }

    // getters and setters
    String getType() { return type; }
    void setType(String type) { this.type = type; }
}
