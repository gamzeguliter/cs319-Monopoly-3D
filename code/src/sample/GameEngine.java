package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import sample.squares.*;

import java.util.ArrayList;

public class GameEngine {

    private Board board;
    //private SpinningWheel wheel;
    private ArrayList<Player> players;
    //filemanager ?
    private int turn;
    private Player currentPlayer;
    private ArrayList<ColorGroup> colorGroups;
    Card drawnCard;

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
        drawnCard = null;
    }

    public void updateGame() {

    }

    /*public void checkSquare() {
        if(currentPlayer.isInJail()) {
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
    }*/

    public VBox getPropertyContent() {
        Property property = (Property) getCurrentSquare();
        VBox content = defaultPropertyInfo(property);
        if (property.isOwned()) {
            Text owner = new Text("Owner: " + property.getOwner().getName());
            content.getChildren().add(owner);
        }
        return content;
    }

    public ArrayList<String> getPropertyButtons() {
        ArrayList<String> buttons = new ArrayList<String>();

        //square is a property
        if(getCurrentSquare().getType() == SquareType.PROPERTY) { //todo tüm squareleri burada yapmayacaksan if kaldır
            Property property = (Property)getCurrentSquare();

            //owned by the current player
            if(property.isOwned() && property.getOwner() == getCurrentPlayer()) {
                if(property.isMortgaged()) {
                    if(getCurrentPlayer().getBalance() >= property.getMortgageLiftingPrice()) {
                        buttons.add("unmortgage");
                    }
                    buttons.add("sell");
                    buttons.add("cancel");
                }
                else {
                   ColorGroup group = property.getColorGroup();

                    //for mortgage button: check if there are any buildings on the color group
                    if(group.canMortgage(property)) {
                        buttons.add("mortgage");
                    }

                    //for sell button: check if there are any buildings on the group
                    if(!group.checkBuildings()) {
                        buttons.add("sell");
                    }

                    //for add house button:
                    //for sell house button:
                    //for add hotel button:
                    //for sell hotel button:
                }
            }
            //the property is not owned by the current player, but it is owned
            else if(property.isOwned()){
                //if the property is mortgaged, only cancel button
                if(property.isMortgaged())
                    buttons.add("cancel");
                else {
                    buttons.add("pay rent");
                }
            }
            //the property is not owned, buy or auction
            else {
                if(currentPlayer.getBalance() >= property.getBuyingPrice()) {
                    buttons.add("buy");
                }
                buttons.add("auction");
            }
        }

        //if owned by someone else and not mortgaged -- pay rent button
        //if owned by someone else and mortgaged, add text mortgsaged and okay button

        //if owned by current player
        //if mortgaged add unmortgage, sell, cancel button --
        // if unmortgage is clicked write unmortgage price and confirmation pane

        //if sell, add sell dialog, after the sell, ask the buyer if they want to lift mortgage
        //if they don't want to-- they pay 10% to bank

        //if owned by current player and unmortgaged
        //sell(check condition), add/sell house(check condition)

        return buttons;
    }

    //todo update texts
    public VBox getJokerContent() {
        VBox vbox = new VBox();
        Joker joker = (Joker)getCurrentSquare();
        if(joker.isMoneyAction()) {
            Text money = new Text("Money: " + joker.getMoney());
            vbox.getChildren().add(money);
        }
        if(joker.isMovementAction()) {
            Text movement = new Text("Movement: " + joker.getMovement());
            vbox.getChildren().add(movement);
        }
        else if(joker.isSuspended()) { //todo -- suspend => jail
            Text jail = new Text("Jail: " + joker.getSuspendedTourNo());
            vbox.getChildren().add(jail);
        }
        return vbox;
    }

    //todo hem checksquares hem jokeractions olarak var
    public void jokerActions() {
        Joker joker = (Joker)getCurrentSquare();
        Player player = getCurrentPlayer();
        if(joker.isMoneyAction()) {
            player.gain(joker.getMoney()); //getMoney return negative if the amount is to be reduced
        }
        if(joker.isMovementAction()) {
            player.setPosition(player.getPosition() + joker.getMovement());
        }
        else if(joker.isSuspended()) {
            player.suspend(joker.getSuspendedTourNo());
        }
    }

    //todo burası amele işi oldu arrayli fişekli bi şeyler yapıp düzeltmek lazım
    public VBox defaultPropertyInfo(Property property) {
        VBox vbox = new VBox();

        Text propertyName = new Text(property.getName());

        Text price = new Text("Price: " + property.getBuyingPrice());

        Text rent = new Text("Rent: " + property.getRent());

        Text rentWithColorGroup = new Text("Rent with color set: " + property.getRent() * 2);

        Text rentOneHouse = new Text("Rent one house " + property.getRentOneHouse());

        Text rentTwoHouses = new Text("Rent two houses " + property.getRentTwoHouses());

        Text rentThreeHouses = new Text("Rent three houses " + property.getRentThreeHouses());

        Text rentFourHouses = new Text("Rent four houses " + property.getRentFourHouses());

        Text rentHotel = new Text("Rent hotel " + property.getRentHotel());

        Text housePrice = new Text("House price " + property.getHousePrice());

        Text hotelPrice = new Text("Hotel price " + property.getHotelPrice());

        Text mortgage = new Text("Mortgage " + property.getMortgagePrice());

        vbox.getChildren().addAll(propertyName, price, rent, rentWithColorGroup, rentOneHouse, rentTwoHouses, rentThreeHouses, rentFourHouses,
                rentHotel, housePrice, hotelPrice, mortgage);
        return vbox;
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

    public void drawCard() {
        ChanceAndCommunityChest square = (ChanceAndCommunityChest)getCurrentSquare();
        if(square.isChance()) {
            drawnCard = board.drawChanceCard();
        }
        else {
            drawnCard = board.drawChestCard();
        }
    }
    public String getCardInfo() {
        return drawnCard.getPrompt();
    }

    //todo
    public void implementCard() {

    }

    public void startAction() {
        Start start = (Start)getCurrentSquare();
        currentPlayer.gain(start.getMoney());
    }

    public VBox startInfo() {
        Start start = (Start)getCurrentSquare();
        VBox vBox = new VBox();
        Text startText = new Text("START SQUARE");
        Text startInfo = new Text("Collect " + start.getMoney() + " dollars!");
        vBox.getChildren().addAll(startText, startInfo);
       return vBox;
    }

    //todo
    public boolean passesStart() {
        return false;
    }

}
