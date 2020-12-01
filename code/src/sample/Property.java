package sample;

public class Property extends Tile {

    // properties
    // TODO: these are default for now for testing, implement a way to set/get them
    int buyValue = 50;
    int sellValue = 25;
    int rentValue = 10;

    Player owner;

    public Property() {
        super("property");
        owner = null;
    }

    public Player getOwner() { return owner; }
    public void setOwner(Player player) { owner = player; }
}
