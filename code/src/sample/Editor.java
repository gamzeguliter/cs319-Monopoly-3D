package sample;
import javafx.scene.paint.Color;
import sample.squares.ChanceAndCommunityChest;
import sample.squares.*;

import java.awt.*;

public class Editor {

    public Board board;
    private int rentRate;
    private int mortgageRent;
    private String  currency;

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

             board.squares[index] = new ChanceAndCommunityChest(true);

    }

    /// creates a new Joker square while editing
    public void createNewJoker(int index,int movement, int money,int suspendedTourNo,String name){
        // default values for now
        //todo -> buraya bak geri, name de minor bir sorun olabilir
        board.squares[index] = new Joker(movement, money,suspendedTourNo,name);
    }

   /// creates a new property square while editing
    public void createNewProperty(int index, String name, ColorGroup colorGroup, int buyingPrice,int rentRate, int mortgageRate) {

        //todo -> buraya bak geri, name de minor bir sorun olabilir
        board.squares[index] = new Property(name, colorGroup, buyingPrice,rentRate,mortgageRate);
        colorGroup.addProperty((Property) board.squares[index]);
    }
    // Below methods are going to be called in the editor screen in order to process user input
     public Property getProperty(int index){
        return (Property)board.squares[index];
     }


    public void setNameForProperty(String name , int index){
        ((Property)board.squares[index]).setName(name);
    }

    public void setBuyingPriceForProperty(int price,int index ) {
        if(board.squares[index].getType() == SquareType.PROPERTY)
            ((Property)(board.squares[index])).setBuyingPrice(price);

    }
    // todo -> this is a little problematic
    public void createColorGroupForProperty(Color color, String name, int index)
    {
        ColorGroup c = new ColorGroup(name);
        c.addProperty(((Property)board.squares[index]));
        board.colorGroups.add(c);

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
    public void setJailTimeForJoker(int suspendedTourNo,int index)
    {
        ((Joker)board.squares[index]).setSuspendedTourNo(suspendedTourNo);

    }




}
