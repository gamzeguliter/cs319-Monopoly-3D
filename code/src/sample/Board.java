package sample;

public class Board {

    // properties
    Tile[] tiles;

    // constructors
    // TODO: implement constructor with filemanager

    // test constructor
    public Board() {
        tiles = testTiles();
    }

    // private methods

    private Tile[] testTiles() {
        Tile[] tiles = new Tile[40];
        for (int i = 0; i < 40; i++) {
            tiles[i] = new Property();
        }
        return tiles;
    }
}
