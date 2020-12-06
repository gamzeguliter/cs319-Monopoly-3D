package sample;

public class Board {

    // properties
    Square[] squares;
    ColorGroup[] colorGroups;
    /*
    property array -?
        aynı name'de 2 group a izin verme!
        property'i colorGroupa koyarken, property içindeki colorgroup ismine bak

        property(name, ...., string colorgroup)

       if(colorgroup yoksa yarat, varsa içindeki property array e bu property 'i ekle
     */


    // constructors
    // TODO: implement constructor with filemanager

    // default constructor
    public Board() {
        squares = testSquares();
    }

    //TODO load board constructor


    // private methods
    public SquareType getSquareType(int squareNo) {
        return squares[squareNo].getType();
    }

    private Square[] testSquares() {
        Square[] squares = new Square[40];
        for (int i = 0; i < 40; i++) {
            if(i % 4 == 0)
                squares[i] = new Joker(9, 10, 0);
            else if(i % 4 == 1)
                squares[i] = new ColorGroup("red", i);
            else
                squares[i] = new ChanceAndCommunityChest();
        }
        return squares;
    }
}
