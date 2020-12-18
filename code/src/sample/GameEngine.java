package sample;

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
    boolean playerPassedStart;

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
        playerPassedStart = false;
    }


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
        Property property = (Property)getCurrentSquare();

        //owned by the current player
        if(property.isOwned() && property.getOwner() == getCurrentPlayer()) {

            //property is mortgaged
            if(property.isMortgaged()) {
                if(getCurrentPlayer().getBalance() >= property.getMortgageLiftingPrice()) {
                    buttons.add("unmortgage");
                }
                buttons.add("sell");
                buttons.add("cancel");
            }
            //property is not mortgaged
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
                if(group.canAddHouse(property, getCurrentPlayer())) {
                    buttons.add("add house");
                }
                //for sell house button:
                if(group.canSellHouse(property, getCurrentPlayer())) {
                    buttons.add("sell house");
                }
                //for add hotel button:
                if(group.canAddHotel(property, getCurrentPlayer())) {
                    buttons.add("add hotel");
                }
                //for sell hotel button:
                if(group.canSellHotel(property)) {
                    buttons.add("sell hotel");
                }
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

    public Square getCurrentSquare() {
        return board.squares[currentPlayer.getPosition()];
    }

    public Square getSquare(int index) {
        return board.squares[index];
    }

    public void buyProperty() {
        Property property = (Property)getCurrentSquare();
        property.buyProperty(currentPlayer);
        if(property.getColorGroup().isComplete(currentPlayer)) {
            property.getColorGroup().completeRentUpdate();
        }
    }

    //todo sell sırasında ya da auction sırasında alan olursa isComplete check
    public boolean sellProperty(int index,String playerName, int amount) {
        if(index < 0) { //sell current square
            Property property = (Property)getCurrentSquare();
            for(Player player:players) {
                if(player.getName().equals(playerName)){
                    property.setOwner(player);
                    player.pay(amount);
                    player.buyProperty(property);
                    currentPlayer.gain(amount);
                    currentPlayer.sellProperty(property);
                    return true;
                }
            }
        }
        //todo else for picking the square
        return false;
    }

    public boolean auctionProperty(String playerName, int amount) {
        Property property = (Property)getCurrentSquare();
        for(Player player:players) {
            if(player.getName().equals(playerName)){
                property.setOwner(player);
                player.pay(amount);
                player.buyProperty(property);
                return true;
            }
        }
        return false;
    }

    public void addHouse(int index) {
        if(index < 0) {
            Property property = (Property)getCurrentSquare();
            property.addHouse();
        }
        //todo else for picking the square
    }

    public void sellHouse(int index) {
        if(index < 0) {
            Property property = (Property)getCurrentSquare();
            property.sellHouse();
        }
        //todo else for picking the square
    }

    public void addHotel(int index) {
        if(index < 0) {
            Property property = (Property)getCurrentSquare();
            property.addHotel();
        }
        //todo else for picking the square
    }

    public void sellHotel(int index) {
        if(index < 0) {
            Property property = (Property)getCurrentSquare();
            property.addHotel();
        }
        //todo else for picking the square
    }

    public void mortgageProperty(int index) {
        if(index < 0) {
            Property property = (Property)getCurrentSquare();
            getCurrentPlayer().gain(property.getMortgagePrice());
            property.mortgageProperty();
        }
        //todo else for picking the square
    }

    //todo
    public void unmortgageProperty(int index) {
        if(index < 0) {
            Property property = (Property)getCurrentSquare();
            getCurrentPlayer().pay(property.getMortgageLiftingPrice());
            property.unmortgageProperty();
        }
        //todo else for picking the square
    }

    //todo ??
    public void nextTurn() {
        turn++;
        currentPlayer = players.get(turn % 4);
        playerPassedStart = false;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

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
        if(position + roll1 + roll2 > 40) {
            playerPassedStart = true;
        }
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
        System.out.println(drawnCard.getAction() + "----" +drawnCard.getPrompt());
    }

    public String getCardInfo() {
        if(((ChanceAndCommunityChest)getCurrentSquare()).isChance()) {
            switch(drawnCard.getAction()) {
                case ("Go to a place"):
                    boolean propertyPresent = false;
                    //we want to move player, same index shouldn't come
                    for (int i = 0; i < 40; i++) {
                        if (board.getSquares()[i].getType() == SquareType.PROPERTY) {
                            propertyPresent = true;
                            break;
                        }
                    }
                    if (propertyPresent) {
                        double random = Math.random();
                        int index = (int) (random * 39);
                        System.out.println("index " + index);
                        while (board.getSquares()[index].getType() != SquareType.PROPERTY) {
                            System.out.println("inside");
                            random = Math.random();
                            index = (int) (random * 39);
                            System.out.println("index " + index);
                        }
                        drawnCard.setPromptInfo(((Property)(board.getSquares()[index])).getName());
                        drawnCard.amount = index;
                    } else {
                        drawnCard.setPromptInfo("Start");
                        drawnCard.amount = 0;
                    }
                    break;
                case ("Earn money"):
                    drawnCard.setPromptInfo(" " + board.currency);
                    break;
                case ("Pay money for house and hotel"):
                    drawnCard.setPromptInfo("Pay 25 " + board.currency + " for each house and 100 " +
                            board.currency + " for each hotel you own");
                    break;
                case ("Pay money"):
                    drawnCard.setPromptInfo(" "+ board.currency);
                    break;
                case ("Pay each player"):
                    drawnCard.setPromptInfo(" " + board.currency + " to each player");
                    break;
            }
        }
        else {
            switch (drawnCard.getAction()) {
                case ("Pay money"):
                    drawnCard.setPromptInfo(" " + board.currency);
                    break;
                case ("Earn x from each player"):
                    drawnCard.setPromptInfo(" " + board.currency + " from each player");
                    break;
                case ("Earn money"):
                    drawnCard.setPromptInfo(" " + board.currency);
                    break;
                case ("Pay money for house and hotel"):
                    //house -- 40
                    //hotel -- 110
                    drawnCard.setPromptInfo("Pay 40 " + board.currency + " for each house and 110 " +
                            board.currency + " for each hotel you own");
                    break;
            }
        }
        System.out.println(drawnCard.getPrompt() + "----" +drawnCard.getPromptInfo());
        return drawnCard.getPrompt() + drawnCard.getPromptInfo();
    }

    //implements the actions on the chance/community chest cards
    public void implementCard() {
        //chance card -- do chance implementations
        if(((ChanceAndCommunityChest)getCurrentSquare()).isChance()) {
            switch(drawnCard.getAction()) {
                case("Go to a place"):
                    currentPlayer.setPosition(drawnCard.amount);
                    break;
                case("Go to nearest joker"):
                    System.out.println("*****JOKER CHECK");
                    int distance = 42;
                    for(int i = 0; i < 40; i ++) {
                        if(board.getSquares()[i].getType() == SquareType.JOKER)
                        {
                            int newDist = 0;
                            //advance to start first and then move to i
                            if(i < getCurrentPlayer().getPosition()) {
                                newDist = 40 - getCurrentPlayer().getPosition() + i;
                            }
                            else {
                                newDist = i - getCurrentPlayer().getPosition();
                            }
                            if(newDist < distance)
                            {
                                distance = newDist;
                            }
                        }
                    }
                    if(distance != 42) {
                        currentPlayer.setPosition(getCurrentPlayerPosition() + distance % 40);
                    }
                    System.out.println("JOKER DIST: "+ distance);
                    break;
                case ("Earn money"):
                    currentPlayer.gain(drawnCard.amount);
                    break;

                case("Go back places"):
                    currentPlayer.setPosition(getCurrentPlayerPosition() - drawnCard.amount % 40);
                    break;
                case("Pay money for house and hotel"):
                    //house -- 25
                    //hotel -- 100
                    int amount = 0;
                    for(Property property : currentPlayer.ownedProperties) {
                        if(property.isHotel()) {
                            amount += 100;
                        }
                        else
                            amount += property.getNoOfHouses() * 25;
                    }
                    currentPlayer.pay(amount);
                    break;
                case("Pay money"):
                    currentPlayer.pay(drawnCard.amount);
                    break;
                case("Pay each player"):
                    for(Player player : players) {
                        if(!player.isOut()) {
                            //todo soru: oyuncunun parası yetmezse?
                            if (currentPlayer.getBalance() < drawnCard.amount) {
                                break;
                            } else {
                                currentPlayer.pay(drawnCard.amount);
                                player.gain(drawnCard.amount);
                            }
                        }
                    }
                    break;
                case("Go to go"):
                    currentPlayer.setPosition(0);
                    break;
            }
        }
        else {
            switch (drawnCard.getAction()) {
                case ("Advance to Go"):
                    currentPlayer.setPosition(drawnCard.amount);
                    break;
                case ("Pay money"):
                    currentPlayer.pay(drawnCard.amount);
                    break;
                case ("Earn x from each player"):
                    for (Player player : players) {
                        if (!player.isOut()) {
                            if (player.getBalance() < drawnCard.amount) {
                                currentPlayer.gain(player.getBalance());
                                player.setBalance(0);
                            } else {
                                player.pay(drawnCard.amount);
                                currentPlayer.gain(drawnCard.amount);
                            }
                        }
                    }
                    break;
                case ("Earn money"):
                    currentPlayer.gain(drawnCard.amount);
                    break;
                case ("Pay money for house and hotel"):
                    //house -- 40
                    //hotel -- 110
                    int amount = 0;
                    for (Property property : currentPlayer.ownedProperties) {
                        if (property.isHotel()) {
                            amount += 110;
                        } else
                            amount += property.getNoOfHouses() * 40;
                    }
                    currentPlayer.pay(amount);
                    break;
            }
        }
    }

    //gives each player money as they pass through the start square
    public void startAction() {
        Start start = (Start)board.getSquares()[0];
        currentPlayer.gain(start.getMoney());
    }

    //creates vbox to pass to GameScreen for start square information
    public VBox startInfo() {
        Start start = (Start)board.getSquares()[0];
        VBox vBox = new VBox();
        Text startText = new Text("START SQUARE");
        Text startInfo = new Text("Collect " + start.getMoney() + " dollars!");
        vBox.getChildren().addAll(startText, startInfo);
       return vBox;
    }

    //todo vakit kalırsa player starta gelince dur pop-up sonra tekrar ilerlet
    //checks if player passed the start square
    public boolean passesStart() {
        return playerPassedStart;
    }
}
