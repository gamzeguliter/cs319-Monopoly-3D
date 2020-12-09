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
    public void createNewJoker(int index,int movement, int money,int suspendedTourNo,String name){
            // default values for now
        //todo -> buraya bak geri, name de minor bir sorun olabilir
        board.squares[index] = new Joker(movement, money,suspendedTourNo,name);
    }

   /// creates a new property square while editing

    public void createNewProperty(int index,String name,ColorGroup colorGroup, int buyingPrice,int sellingPrice, int mortgagePrice,
                                  int housePrice,int rent) {

        //todo -> buraya bak geri, name de minor bir sorun olabilir
        board.squares[index] = new Property(name, colorGroup, buyingPrice, sellingPrice, mortgagePrice,
                housePrice, rent);

        colorGroup.addProperty((Property) board.squares[index]);

    }


    // Below methods are going to be called in the editor screen in order to process user input
     public Property getProperty(int index){
        return (Property)board.squares[index];
     }

     // todo  rent is not taken in the user input
    public void setRentForProperty(int rent,int index ) {
        ((Property)board.squares[index]).setRent(rent);

    }
    public void setNameForProperty(String name , int index){
        ((Property)board.squares[index]).setName(name);
    }

    public void setBuyingPriceForProperty(int price,int index ) {
        ((Property)board.squares[index]).setBuyingPrice(price);

    }
    // todo -> this is a little problematic
    public void setColorGroupForProperty()
    {

    }


    public void setNameForJoker(String name , int index){
        ((Joker)board.squares[index]).setName(name);
    }

    public void setMovementForJoker(int movement,int index ) {
        ((Joker)board.squares[index]).setMovement(movement);

    }

    public void setMoneyForJoker(int money,int index)
    {

        ((Joker)board.squares[index]).setMoney(money);

    }
    public void setSuspentionForJoker(int suspendedTourNo,int index)
    {

        ((Joker)board.squares[index]).setSuspendedTourNo(suspendedTourNo);

    }



/// todo
    public void setColorGroupForProperty(String name,int index)
    {


    }


}
