package sample;
import javafx.scene.paint.Color;

public class Editor {

    private Board board;
      // will be deleted

    public Editor(){
     board = new Board();

    }
    public Square getSquare(int index) {
        return board.squares[index];
    }

    public SquareType getSquareType(int index) {
        return board.squares[index].getType();
    }

    public void createNewChestCommunity(int index){

             board.squares[index] = new ChanceAndCommunityChest();

    }
    public void createNewJoker(int index,int movement, int money,int suspendedTourNo){
            // default values for now
        board.squares[index] = new Joker(movement, money,suspendedTourNo);
    }
    public void createNewProperty(int index,String name,ColorGroup colorGroup, int buyingPrice,int sellingPrice, int mortgagePrice,
                                  int housePrice,int rent){

            board.squares[index] = new Property( name, colorGroup,  buyingPrice, sellingPrice,  mortgagePrice,
                                    housePrice, rent);

            colorGroup.addProperty((Property)board.squares[index]);
      
    }


}
