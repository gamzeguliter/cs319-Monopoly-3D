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

    /// creates a new Chest and Community square while editing
    public void createNewChestCommunity(int index){

             board.squares[index] = new ChanceAndCommunityChest();

    }

    /// creates a new Joker square while editing
    public void createNewJoker(int index,int movement, int money,int suspendedTourNo){
            // default values for now
        board.squares[index] = new Joker(movement, money,suspendedTourNo);
    }

   /// creates a new property square while editing

    public void createNewProperty(int index,String name,ColorGroup colorGroup, int buyingPrice,int sellingPrice, int mortgagePrice,
                                  int housePrice,int rent) {

        board.squares[index] = new Property(name, colorGroup, buyingPrice, sellingPrice, mortgagePrice,
                housePrice, rent);

        colorGroup.addProperty((Property) board.squares[index]);

    }

     public Property getProperty(int index){
        return (Property)board.squares[index];
     }

}
