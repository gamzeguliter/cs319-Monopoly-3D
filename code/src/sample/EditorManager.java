package sample;
import javafx.scene.paint.Color;
import sample.entities.Board;
import sample.squares.ChanceAndCommunityChest;
import sample.squares.*;

import java.util.ArrayList;

public class EditorManager {

    public Board board;
    private int rentRate;
    private int mortgageRent;
    private String  currency;

    public EditorManager(){
        board = new Board();
    }

    public EditorManager(Board board) {
        this.board = board;
    }

    public Square getSquare(int index) {
        return board.squares[index];
    }

    public SquareType getSquareType(int index) {
        return board.squares[index].getType();
    }

    /// creates a new Chest and Community square while editing
    public void createNewChestCommunity(int index,boolean chance){
        System.out.println("chest and community created");
        board.squares[index] = new ChanceAndCommunityChest(chance);

        System.out.println(board.squares[index].getJSON().toString());
    }

    /// creates a new Joker square while editing
    public void createNewJoker(int index,int movement, int money,int suspendedTourNo,String name){
        // default values for now
        board.squares[index] = new Joker(movement, money,suspendedTourNo,name);
        System.out.println("check" +money + " " + movement);
    }

    /// creates a new property square while editing
    public void createNewProperty(int index, String name, ColorGroup colorGroup, int buyingPrice,int rentRate, int mortgageRate) {

        if(board.getColorGroups().contains(colorGroup) == false){
            board.getColorGroups().add(colorGroup);
        }
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

    public void createColorGroupForProperty(Color color, String name, int index)
    {
        ColorGroup c = new ColorGroup(name);
        c.setColor(color);
        c.addProperty(((Property)board.squares[index]));
        board.colorGroups.add(c);

    }
    public void changeColorGroupForProperty(ColorGroup cg, int index)
    {        cg.addProperty(((Property)board.squares[index]));

    }
    public void setNameForJoker(String name , int index){

        if(board.squares[index].getType() == SquareType.JOKER)
            ((Joker)board.squares[index]).setName(name);
    }
    public ColorGroup getColorGroup(String name){
        ArrayList<ColorGroup> cg =  board.getColorGroups();
        for(int i =0; i < cg.size(); i++){
            if(cg.get(i).getGroupName() == name){
                return cg.get(i);
            }
        }
        return null;
    }

    public void setMovementForJoker(int movement,int index ) {

        if(board.squares[index].getType() == SquareType.JOKER)
            ((Joker)board.squares[index]).setMovement(movement);
    }
    public void setMoneyForJoker(int money,int index)
    {

        if(board.squares[index].getType() == SquareType.JOKER)
            ((Joker)board.squares[index]).setMoney(money);

    }
    public void setJailTimeForJoker(int suspendedTourNo,int index)
    {
        if(board.squares[index].getType() == SquareType.JOKER)
            ((Joker)board.squares[index]).setSuspendedTourNo(suspendedTourNo);

    }




}
