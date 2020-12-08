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
    private ArrayList<ColorGroup> colorGroups;

    public GameEngine() {
        board = new Board();
        //temporary players to test
        players = new ArrayList<Player>(); //added
        players.add(new Player("player1", Color.LIMEGREEN, 1000));
        players.add(new Player("player2", Color.TURQUOISE, 1000));
        players.add(new Player("player3", Color.MAROON, 1000));
        players.add(new Player("player4", Color.SILVER, 1000));
        turn = 0;
        currentPlayer = players.get(0);
        colorGroups = board.getColorGroups();
    }

    public void updateGame() {

    }

    public void checkSquare() {
        if(currentPlayer.isSuspended()) {
            //todo
        }
        else {
            //Joker square actions
            if(getCurrentSquare().getType() == SquareType.JOKER) {
                Joker joker = (Joker)getCurrentSquare();
                Player player = getCurrentPlayer();
                if(joker.isMoneyAction()) {
                    player.gain(joker.getMoney()); //getMoney return negative if the amount is to be reduced
                }
                if(joker.isMovementAction()) {
                    //todo joker square de position update edilse de ekranda direk moveluyor duraklaması lazım
                    player.setPosition(player.getPosition() + joker.getMovement());
                }
                else if(joker.isSuspended()) {
                    player.suspend(joker.getSuspendedTourNo());
                }
            }
            //Chance and community chest actions
            else if(getCurrentSquare().getType() == SquareType.CHANCEANDCOMMUNITYCHEST) {
                //todo - draw card
            }
            //Start square -- adds money to players who pass it
            else if(getCurrentSquare().getType() == SquareType.START) {
                getCurrentPlayer().gain(((Start)getCurrentSquare()).getMoney());
                //todo add money
            }
            //Property square
            else {
                Property property = (Property)getCurrentSquare();
                if(property.isOwned()) {
                    takeRent();
                }
                //todo else buy button enabled --> buy no enabled if player balance is smaller
            }
        }
    }

    public boolean isBuyDisabled() {
        if(getCurrentSquare().getType() != SquareType.PROPERTY) {
            System.out.println("Not property");
            return true;
        }
        else if(((Property)getCurrentSquare()).isOwned() ||
                ((Property)getCurrentSquare()).getBuyingPrice() > getCurrentPlayer().getBalance()) {
            return true;
        }
        return false;
    }
/*
    public boolean isSellDisabled() {
        //check if it is owned,
        //if there is any buildings on the color group
<<<<<<< HEAD
    return true;}

    public boolean isAddHouseDisabled() {
        //check if improvable
return true;
    }

    public boolean isSellHouseDisabled() {
 return true;
    }

    public boolean isAddHotelDisabled() {
return true;
=======
        return false;
    }

    public boolean isAddHouseDisabled() {
        //check if improvable
        return false;
    }

    public boolean isSellHouseDisabled() {
        return false;
    }

    public boolean isAddHotelDisabled() {
        return false;
>>>>>>> a6c467dc49d44bb5e8d0e1cd1801b1ea022ceac1
    }

    public boolean isSellHotelDisabled() {
        return false;
    }

    public boolean isMortgagePropertyDisabled() {
        return false;
    }

    public boolean isUnmortgagePropertyDisabled() {
        return false;
    }


*/
    public void initializeGame() {

    }

    public int playerNumber() {
        int playerNum = 0;
        for(Player player: players) {
            if (!player.isOut()){
                playerNum++;
            }
        }
        return playerNum;
    }

    public int getTurn()
    {
        return turn % playerNumber();
    }

    public ArrayList<String> getPlayerNames() {
        ArrayList<String> playernames = new ArrayList<String>();
        for(Player player : players) {
            playernames.add(player.getName());
        }
        return playernames;
    }

    //todo texti durdur -
    public void resign(Player player) {
        player.resign();
    }


    /*
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
    } //todo sonra silinecek

    public ArrayList<Integer> getPlayerPositions() {
        ArrayList<Integer> positions = new ArrayList<Integer>();
        for(Player player : players) {
            positions.add(player.getPosition());
        }
        return positions;
    }

    // TOdo diagramda var ama burada nasıl olacak bilemedik
    public Square getCurrentSquare() {
        return board.squares[currentPlayer.getPosition()];
    }

    public Square getSquare(int index) {
        return board.squares[index];
    }

    public boolean buyProperty() {
        return ((Property)getCurrentSquare()).buyProperty(getCurrentPlayer());
    }

    //todo
    public boolean sellProperty(int index, ColorGroup group) {
        return true;
    }


    public boolean addHouse() {
        //SORU: player istediği zaman istediği kareye ev kurabiliyor mu - öyleyse current square olmayacak!!!
        Property property = (Property)getCurrentSquare();
        ColorGroup group = property.getColorGroup();
        return group.addHouse(property, currentPlayer);
        /*for(ColorGroup group : colorGroups) {
            if(property.getColorGroup() == group.getGroupName()) { //TEKRAR BAK: string comparison with == ?
                return group.addHouse(property, currentPlayer);
            }
        }
        return false;*/
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

    //todo rent i kendi check edecek sistem yaratmalı -- check if mortgaged
    public void takeRent() {
        Property property = (Property)getCurrentSquare();
        Player owner = property.getOwner();
        Player payer = getCurrentPlayer();
        payer.pay(property.getRent());
        owner.gain(property.getRent());
    }

    public int rollDice() {
        int min = 1;
        int max = 6;
        int roll1 = min + (int)(Math.random() * ((max - min) + 1));
        int roll2 = min + (int)(Math.random() * ((max - min) + 1));
        int position = currentPlayer.getPosition();
        position = (position + roll1 + roll2) % 40;
        currentPlayer.setPosition(position); //todo if the player is suspended condition -- double roll
        return roll1 + roll2;
    }

    public int getCurrentPlayerPosition() {
        return currentPlayer.getPosition();
    }

}
