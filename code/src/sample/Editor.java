package sample;
import javafx.scene.paint.Color;

public class Editor {

    private Board board;

    public Editor(){
     board = new Board();
    }
    public Square getSquare(int index) {
        return board.squares[index];
    }

    public SquareType getSquareType(int index) {
        return board.squares[index].getType();
    }
    public void createNewSquare(SquareType type,int index){
        if(type == SquareType.CHANCEANDCOMMUNITYCHEST)
             board.squares[index] = new ChanceAndCommunityChest();

    }

}
