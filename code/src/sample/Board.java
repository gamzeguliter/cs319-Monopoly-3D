package sample;

public class Board {

    // properties
    Square[] squares;

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
                squares[i] = new Square(SquareType.JOKER);
            else if(i % 4 == 1)
                squares[i] = new Square(SquareType.COLORGROUP);
            else
                squares[i] = new Square(SquareType.CHANCEANDCOMMUNITYCHEST);
        }
        return squares;
    }
}
