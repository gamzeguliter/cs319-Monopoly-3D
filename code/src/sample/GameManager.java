package sample;

import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import sample.entities.Board;
import sample.entities.Card;
import sample.entities.Player;
import sample.squares.*;

import java.util.ArrayList;

public class GameManager {

    private Board board;
    private ArrayList<Player> players;
    private int turn;
    private Player currentPlayer;
    Card drawnCard;
    boolean playerPassedStart;
    private int diceResult;

    public GameManager() {
        board = new Board();
        //temporary players to test
        players = new ArrayList<Player>(); //added
        players.add(new Player("player1", Color.LIMEGREEN, 1000));
        players.add(new Player("player2", Color.TURQUOISE, 1000));
        players.add(new Player("player3", Color.MAROON, 1000));
        players.add(new Player("player4", Color.SILVER, 1000));
        turn = 0;
        currentPlayer = players.get(0);
        drawnCard = null;
        playerPassedStart = false;
        diceResult = 0;
    }

    //constructor that takes board and players as paramaters
    public GameManager(Board board, ArrayList<Player> players) {
        this.board = board;
        this.players = players;
        turn = 0;
        currentPlayer = players.get(0);
        drawnCard = null;
        playerPassedStart = false;
        diceResult = 0;
    }


    public VBox getPropertyContent(int index) {
        Property property;
        if(index < 0) {
            property = (Property) getCurrentSquare();
        }
        else
        {
            property = (Property)(board.getSquares()[index]);
        }

        VBox content = defaultPropertyInfo(property);
        if (property.isOwned()) {
            Text owner = new Text("Owner: " + property.getOwner().getName());
            content.getChildren().add(owner);
        }
        return content;
    }


    public ArrayList<String> getPropertyButtons(int index) {
        ArrayList<String> buttons = new ArrayList<String>();

        //square is a property
        Property property;
        if(index < 0) {
            property = (Property)getCurrentSquare();
        }
        else
        {
            property = (Property)(board.getSquares()[index]);
        }

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
                System.out.println(group.canAddHouse(property, getCurrentPlayer()));
                //for add house button:
                if(group.canAddHouse(property, getCurrentPlayer())) {
                    buttons.add("add house");
                }
                //for sell house button:
                if(group.canSellHouse(property, getCurrentPlayer())) {
                    System.out.println(group.canSellHouse(property, getCurrentPlayer()));
                    buttons.add("sell house");
                }
                //for add hotel button:
                if(group.canAddHotel(property, getCurrentPlayer())) {
                    System.out.println(group.canAddHotel(property, getCurrentPlayer()));
                    buttons.add("add hotel");
                }
                //for sell hotel button:
                if(group.canSellHotel(property)) {
                    System.out.println(group.canSellHotel(property));
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
            buttons.add("buy");
            buttons.add("auction");
        }
        return buttons;
    }

    public VBox getJokerContent() {
        VBox vbox = new VBox();
        Joker joker = (Joker)getCurrentSquare();
        if(joker.isMoneyAction()) {
            Text money;
            if(joker.getMoney() < 0) {
                money = new Text("Pay " + Math.abs(joker.getMoney()) + " " + board.currency);

            }
            else {
                money = new Text("Collect " + joker.getMoney() + " " + board.currency);
            }
            vbox.getChildren().add(money);
        }
        if(joker.isMovementAction()) {
            Text movement;
            if(joker.getMovement() < 0) {
                movement = new Text("Go " + joker.getMovement() + " squares back.");

            }
            else {
                movement = new Text("Go " + joker.getMovement() + " squares forward.");
            }
            vbox.getChildren().add(movement);
        }
        else if(joker.isSuspended()) {
            Text jail = new Text("Go to jail for " + joker.getSuspendedTourNo() + " turns.");
            vbox.getChildren().add(jail);
        }
        return vbox;
    }

    //implements the joker actions and returns true if the player is moved
    public boolean jokerActions() {
        boolean isMoved = false;
        Joker joker = (Joker)getCurrentSquare();
        Player player = getCurrentPlayer();
        if(joker.isMoneyAction()) {
            player.gain(joker.getMoney()); //getMoney return negative if the amount is to be reduced
        }
        if(joker.isMovementAction()) {
            isMoved = true;
            player.setPosition(player.getPosition() + joker.getMovement());
        }
        else if(joker.isSuspended()) {
            //the player should go to jail for + 2 turns because next turn decreases it and we need to know if
            //the player is released yet
            player.setJailTime(joker.getSuspendedTourNo() + 2);
        }
        return isMoved;
    }

    public String getPropertyName(int index) {
        if(index < 0) {
            return ((Property)getCurrentSquare()).getName();
        }
        else
        {
            return ((Property)getSquare(index)).getName();
        }
    }

    public VBox defaultPropertyInfo(Property property) {
        VBox vbox = new VBox();

        Text mortgage;
        if(property.isMortgaged()) {
            mortgage = new Text("MORTGAGED");
        }
        else {
            mortgage = new Text("Mortgage: " + property.getMortgagePrice());
        }

        Text price = new Text("Price: " + property.getBuyingPrice());

        Text rent = new Text("Rent: " + property.getRent());

        Text rentWithColorGroup = new Text("Rent with color set: " + property.getRent() * 2);

        Text rentOneHouse = new Text("Rent one house: " + property.getRentOneHouse());

        Text rentTwoHouses = new Text("Rent two houses: " + property.getRentTwoHouses());

        Text rentThreeHouses = new Text("Rent three houses: " + property.getRentThreeHouses());

        Text rentFourHouses = new Text("Rent four houses: " + property.getRentFourHouses());

        Text rentHotel = new Text("Rent hotel: " + property.getRentHotel());

        Text housePrice = new Text("House price: " + property.getHousePrice());

        Text hotelPrice = new Text("Hotel price: " + property.getHotelPrice());

        vbox.getChildren().addAll(price, rent, rentWithColorGroup, rentOneHouse, rentTwoHouses, rentThreeHouses, rentFourHouses,
                rentHotel, housePrice, hotelPrice, mortgage);

        if(property.isOwned()) {
            Text noOfHouses = new Text("House number: " + property.getNoOfHouses());
            Text hotelno = new Text("Hotel number: 0");
            if(property.isHotel())
                hotelno = new Text("Hotel number: 1");
            vbox.getChildren().addAll(noOfHouses, hotelno);
        }
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

    public ArrayList<Player> getPlayers() {
        return players;
    }

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

    public void sellProperty(int index, String playerName, int amount) {
        Property property;
        if(index < 0) { //sell current square
            property = (Property)getCurrentSquare();
        }
        else { //sell the clicked square
            property = (Property)board.getSquares()[index];
        }
        for(Player player:players) {
            if(player.getName().equals(playerName)){
                property.setOwner(player);
                player.buyProperty(property, amount);
                currentPlayer.sellProperty(property, amount);
                if(property.getColorGroup().isComplete(player)) {
                    property.getColorGroup().completeRentUpdate();
                }
            }
        }
    }

    //checks if the sold property was mortgaged
    public boolean soldMortgaged(int index) {
        Property property;
        if(index < 0) {
            property = (Property) getCurrentSquare();
        }
        else {
            property = (Property)board.getSquares()[index];
        }
        if(property.isMortgaged()) {
            return true;
        }
        return false;
    }

    public VBox getMortgageLiftingInfo(int index) {
        VBox vbox = new VBox();
        Property property;
        if(index < 0) {
            property = (Property) getCurrentSquare();
        }
        else {
            property = (Property)board.getSquares()[index];
        }
        Text mortgageInfo = new Text("Would you like to lift the mortgage of " + property.getName()  + " by paying " + property.getMortgageLiftingPrice() + " " + board.currency + " ?");
        Text liftLater = new Text("If you choose not to lift it now, you will pay " + property.getMortgagePrice() * 10 / 100  + " " + board.currency + " now " +
                "\nand you will need to pay the same mortgage lifting price" +
                " when you choose to lift it.");
        vbox.getChildren().addAll(mortgageInfo, liftLater);
        return vbox;
    }

    public void liftMortgageLater(int index) {
        Property property = (Property)board.getSquares()[index];
        property.getOwner().pay(property.getMortgagePrice() * 10 / 100 );
    }

    public void auctionProperty(int index, String playerName, int amount) {
        Property property;
        if(index < 0) {
            property = (Property)getCurrentSquare();
        }
        else {
            property = (Property)getSquare(index);
        }
        for(Player player:players) {
            if(player.getName().equals(playerName)){
                property.setOwner(player);
                player.buyProperty(property, amount);
                if(property.getColorGroup().isComplete(player)) {
                    property.getColorGroup().completeRentUpdate();
                }
            }
        }
    }

    public void addHouse(int index) {
        if(index < 0) {
            Property property = (Property)getCurrentSquare();
            property.addHouse();
        }
        else {
            Property property = (Property)board.getSquares()[index];
            property.addHouse();
        }
    }

    public void sellHouse(int index) {
        if(index < 0) {
            Property property = (Property)getCurrentSquare();
            property.sellHouse();
        }
        else {
            Property property = (Property)board.getSquares()[index];
            property.sellHouse();
        }
    }

    public void addHotel(int index) {
        if(index < 0) {
            Property property = (Property)getCurrentSquare();
            property.addHotel();
        }
        else {
            Property property = (Property)board.getSquares()[index];
            property.addHotel();
        }
    }

    public void sellHotel(int index) {
        if(index < 0) {
            Property property = (Property)getCurrentSquare();
            property.addHotel();
        }
        else {
            Property property = (Property)board.getSquares()[index];
            property.sellHotel();
        }
    }

    public void mortgageProperty(int index) {
        Property property;
        if(index < 0) {
            property = (Property)getCurrentSquare();
        }
        else {
            property = (Property)board.getSquares()[index];
        }
        getCurrentPlayer().gain(property.getMortgagePrice());
        property.mortgageProperty();
    }

    public void unmortgageProperty(int index) {
        Property property;
        if(index < 0) {
            property = (Property)getCurrentSquare();
        }
        else {
            property = (Property)board.getSquares()[index];
        }
        getCurrentPlayer().pay(property.getMortgageLiftingPrice());
        property.unmortgageProperty();
    }

    public boolean nextTurn() {
        turn++;
        currentPlayer = players.get(Math.abs(turn) % players.size());
        playerPassedStart = false;
        if(currentPlayer.isInJail()) {
            currentPlayer.setJailTime(currentPlayer.getjailTime() - 1);
        }
        return checkWin(); //return if the game is over
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

    public int[] rollDice() {
        int[] dice = new int[2];
        int min = 1;
        int max = 6;
        int roll1 = min + (int) (Math.random() * ((max - min) + 1));
        int roll2 = min + (int) (Math.random() * ((max - min) + 1));
        dice[0] = roll1;
        dice[1] = roll2;
        diceResult = roll1 + roll2;
        if(currentPlayer.getjailTime() == 1 ) {
            releasePlayer(false);
        }
        return dice;
    }

    public void movePlayer() {
        int position = currentPlayer.getPosition();
        if(position + diceResult > 40) {
            playerPassedStart = true;
        }
        position = (position + diceResult) % 40;
        currentPlayer.setPosition(position);
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
                        drawnCard.setAmount(index);
                    } else {
                        drawnCard.setPromptInfo("Start");
                        drawnCard.setAmount(0);
                    }
                    break;
                case ("Earn money"):
                    drawnCard.setPromptInfo(" " + board.currency);
                    break;
                case ("Pay money for house and hotel"):
                    drawnCard.setPrompt("Make general repairs on all your property");
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
    //returns true if the player is moved
    public boolean implementCard() {
        boolean didMove = false;
        playerPassedStart = false;
        //chance card -- do chance implementations
        if(((ChanceAndCommunityChest)getCurrentSquare()).isChance()) {
            switch(drawnCard.getAction()) {
                case("Go to a place"):
                    didMove = true;
                    if(drawnCard.getAmount() < currentPlayer.getPosition()) {
                        playerPassedStart = true;
                    }
                    currentPlayer.setPosition(drawnCard.getAmount());
                    break;
                case("Go to nearest joker"):
                    didMove = true;
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
                        if( 40 < getCurrentPlayerPosition() + distance) {
                            playerPassedStart = true;
                        }
                        currentPlayer.setPosition(getCurrentPlayerPosition() + distance % 40);
                    }
                    System.out.println("JOKER DIST: "+ distance);
                    break;
                case ("Earn money"):
                    currentPlayer.gain(drawnCard.getAmount());
                    break;

                case("Go back places"):
                    didMove = true;
                    currentPlayer.setPosition(getCurrentPlayerPosition() - drawnCard.getAmount() % 40);
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
                    currentPlayer.pay(drawnCard.getAmount());
                    break;
                case("Pay each player"):
                    for(Player player : players) {
                        currentPlayer.pay(drawnCard.getAmount());
                        player.gain(drawnCard.getAmount());
                    }
                    break;
                case("Go to go"):
                    didMove = true;
                    currentPlayer.setPosition(0);
                    break;
            }
        }
        else {
            switch (drawnCard.getAction()) {
                case ("Advance to Go"):
                    didMove = true;
                    currentPlayer.setPosition(drawnCard.getAmount());
                    break;
                case ("Pay money"):
                    currentPlayer.pay(drawnCard.getAmount());
                    break;
                case ("Earn x from each player"):
                    for (Player player : players) {
                        if (!player.isOut()) {
                            player.pay(drawnCard.getAmount());
                            currentPlayer.gain(drawnCard.getAmount());
                        }
                    }
                    break;
                case ("Earn money"):
                    currentPlayer.gain(drawnCard.getAmount());
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

        return didMove;
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
        Text startInfo = new Text("Collect " + start.getMoney() + " dollars!");
        vBox.getChildren().addAll(startInfo);
       return vBox;
    }

    //checks if player passed the start square
    public boolean passesStart() {
        return playerPassedStart;
    }

    public boolean canClick(int index) {
        System.out.println("here");
        for(Property property : currentPlayer.ownedProperties) {
            System.out.println(property.getName());
            if (property == board.getSquares()[index]) {
                return true;
            }
        }
        return false;
    }

   public ArrayList<Integer> sellPlayerProperties() {
        ArrayList<Integer> propertyIndexes = new ArrayList<>();
        while(currentPlayer.ownedProperties.size() > 0) {
            for(int i = 0; i < 40; i ++) {
                if (getSquare(i).getType() == SquareType.PROPERTY) {
                    if (getSquare(i) == currentPlayer.ownedProperties.get(0)) {
                        propertyIndexes.add(i);
                        Property property = currentPlayer.ownedProperties.get(0);
                        property.reset();
                        currentPlayer.ownedProperties.remove(0);
                        break;
                    }
                }
            }
        }
        currentPlayer.out();
        players.remove(currentPlayer);
        return propertyIndexes;
   }

    public void resign() {
        currentPlayer.out();
        players.remove(currentPlayer);
        turn  = turn - 1;
        currentPlayer = players.get(Math.abs(turn) % players.size());

    }

    /*public void bankrupt() {
        Player bankruptPlayer = currentPlayer;
        currentPlayer = players.get((turn + 1) % players.size());
        bankruptPlayer.out();
        players.remove(bankruptPlayer);
        turn--;
    }*/

    public boolean isBankrupt() {
        return currentPlayer.isBankrupt();
    }

    public String jokerText() {
        return "You are in jail. \n" +
                "Would you like to get out of jail by paying 50 " + board.currency + " ?";
    }

    public void releasePlayer(boolean rollDouble) {
        if (!rollDouble) {
            currentPlayer.pay(50);
        }
        currentPlayer.endJail();
    }

    public boolean playerInJail() {
        return currentPlayer.isInJail();
    }

    public boolean checkWin() {
        if(players.size() <= 1) {
            return true;
        }
        else {
            return false;
        }
    }

    public String getWinner() {
        return players.get(0).getName();
    }

    public ArrayList<String> playersInAuction() {
        ArrayList<String> choices = new ArrayList<>();
        for(Player player : players) {
            if(player != currentPlayer) {
                choices.add(player.getName());
            }
        }
        return choices;
    }

    public boolean lastPlayer() {
        return players.size() <= 2;
    }

    public Image boardImage() {
        Image boardIcon = FileManager.getImage("/boards/" + board.getName() + "/board_icon.png", 575, 575);
        if (boardIcon == null) {
            boardIcon = FileManager.generateWhiteImage(575, 575);
        }
        return boardIcon;
    }

    public String getJokerName(int index) {
        if(index < 0 )
            return ((Joker) getCurrentSquare()).getName();
        return ((Joker) getSquare(index)).getName();
    }
}
