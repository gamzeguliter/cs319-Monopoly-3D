package sample;

import javafx.scene.paint.Color;

import java.awt.*;
import java.util.ArrayList;

public class GameEngine {

    private Board board;
    //private SpinningWheel wheel;
    private ArrayList<Player> players;
    //filemanager ?
    private int turn;
    private Player currentPlayer;

    public GameEngine() {
        board = new Board();
        //temporary players to test
        players.add(new Player("player1", Color.LIMEGREEN, 1000));
        players.add(new Player("player2", Color.TURQUOISE, 1000));
        players.add(new Player("player3", Color.MAROON, 1000));
        players.add(new Player("player4", Color.SILVER, 1000));
        turn = 0;
        currentPlayer = players.get(0);

    }

    public void update() {

    }

    public void initializeGame() {

    }

    public int getTurn()
    {
        return turn % 4;
    }

    /*public ArrayList<String> getPlayerNames() {
        ArrayList<String> playernames = new ArrayList<String>();
        for(Player player : players) {
            playernames.add(player.getName());
        }
        return playernames;
    }

    public ArrayList<Integer> getPlayerBalances() {
        ArrayList<Integer> playerbalances = new ArrayList<Integer>();
        for(Player player : players) {
            playerbalances.add(player.getBalance());
        }
        return playerbalances;
    }

    public ArrayList<Integer> getPlayerPositions() {
        ArrayList<Integer> playerpositions = new ArrayList<Integer>();
        for(Player player : players) {
            playerpositions.add(player.getPosition());
        }
        return playerpositions;
    } */

    public ArrayList<Player> getPlayers() {
        return players;
    }

    //todo -- board'dan şu anki oyuncunun bulunduğu kare indexini al
    public int getBoardIndex()
    {
        //ağlamasın diye yazdım değiştirilecek
        return currentPlayer.getPosition();
    }

    // TOdo diagramda var ama burada nasıl olacak bilemedik
    public Square getCurrentSquare() {
        return board.squares[currentPlayer.getPosition()];

    }

    public boolean buyProperty(Property property, Player player) {
        if(property.isOwned() || player.getBalance() < property.getBuyingPrice()) {
            return false;
        }
        else {
            property.setOwner(player);
            player.pay(property.getBuyingPrice());
            //TODO UI change color and stuff
            return true;
        }
    }

    //todo
    public boolean sellProperty(int index, ColorGroup group) {
        return true;
    }

    public boolean addHouse(int index, ColorGroup group) {
        if(!group.isComplete(currentPlayer)) {
            return false;
        }
        else {

            if(currentPlayer.getBalance() < group.propertyHousePrice(index)) {
                return false;
            }
            else {
                return group.addHouse(index);
            }
        }
    }

    //todo
    public boolean sellHouse(int index, ColorGroup group) {
        return true;
    }

    //todo
    public boolean addHotel(int index, ColorGroup group) {
        return true;
    }

    //todo
    public boolean sellHotel(int index, ColorGroup group) {
        return true;
    }

    //todo
    public boolean mortgageProperty(int index, ColorGroup group) {
        return true;
    }

    //todo
    public boolean unmortgageProperty(int index, ColorGroup group) {
        return true;
    }

    //todo ??
    public void nextTurn() {
        turn++;
        currentPlayer = players.get(turn % 4);
    }

    //todo
    public int spinWheel() {
        return 0;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean takeRent(Player owner, Player payer, int rent) {
        if(payer.getBalance() >= rent) {
            payer.pay(rent);
            owner.gain(rent);
            return true;
        }
        else {
            return false;
        }
    }
}
