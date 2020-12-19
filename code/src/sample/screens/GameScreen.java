package sample.screens;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Pair;
import sample.GameEngine;
import sample.Player;
import sample.ScreenManager;
import sample.squares.Property;
import sample.squares.SquareType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class GameScreen extends Screen {

    // properties
    private Scene scene;
    GridPane boardPane;

    ArrayList<Text> playerTexts;
    Text turnText;
    Button btnRollDice;
    Button btnEndTurn;
    Button btnResign;
    GameEngine gameEngine;
    Font font = Font.font("Source Sans Pro", 20);
    int position;

    // constructors
    public GameScreen(ScreenManager screenManager) {
        super(screenManager);
        gameEngine = new GameEngine();
        boardPane = getSquares();
        setScene();
    }


    private Text getPlayerText(Player player) {
        Text t = new Text();
        t.setFont(font);
        t.setText(player.getName() + "\nBalance: " + player.getBalance() + "\nPosition: " + player.getPosition());
        t.setFill(player.getColor());
        t.setY(50);
        //t.setFont(new Font(20));
        return t;
    }

    //todo değişecek
    private void initializePlayerTexts(Group group) {
        playerTexts = new ArrayList<>();
        int count = 0;
        for (Player player : gameEngine.getPlayers()) {
            playerTexts.add(getPlayerText(player));
            playerTexts.get(count).setX(800);
            playerTexts.get(count).setY(100 + count * 100);
            group.getChildren().add(playerTexts.get(count));
            count++;
        }
        /*
        playerTexts[0].setX(800);
        playerTexts[0].setY(100);
        playerTexts[1].setX(800);
        playerTexts[1].setY(200);
        playerTexts[2].setX(800);
        playerTexts[2].setY(300);
        playerTexts[3].setX(800);
        playerTexts[3].setY(400);*/

        //group.getChildren().addAll(playerTexts[0], playerTexts[1], playerTexts[2], playerTexts[3]);
    }

    //todo bu method direk getPlayers ile almasa daha iyi olur
    private void updatePlayerTexts() {
        for (int i = 0; i < gameEngine.getPlayers().size(); i++) {
            Player player = gameEngine.getPlayers().get(i);
            playerTexts.get(i).setText(player.getName() + "\nBalance: " + player.getBalance() + "\nPosition: " + player.getPosition());
        }
    }


    private void setScene() {
        Group group = new Group();
        int width = 1366;
        int height = 768;

        // initialize player texts
        initializePlayerTexts(group);

        // initialize buttons
        btnRollDice = new Button();
        btnEndTurn = new Button();
        btnResign = new Button("Resign");
        Font font3 = Font.font("Source Sans Pro", 15);

        //resign button
        btnResign.setOnAction(actionEvent -> {
            createResignDialog();
        });

        //roll dice button
        btnRollDice.setText("Roll Dice");
        btnRollDice.setFont(font3);
        //initialize end turn as disabled
        btnEndTurn.setDisable(true);

        btnRollDice.setOnAction(event -> {
            createDiceDialog();
            btnEndTurn.setDisable(false);
            btnRollDice.setDisable(true);
        });

        btnRollDice.setLayoutX(100);
        btnRollDice.setLayoutY(120);

        //end turn button
        btnEndTurn.setText("End Turn");
        btnEndTurn.setFont(font3);
        btnEndTurn.setOnAction(event -> {
            if(gameEngine.isBankrupt()) {
                createBankruptDialog();
            }
            else {
                gameEngine.nextTurn();
            }
            turnText.setText("Player Turn: " + gameEngine.getCurrentPlayer().getName());
            btnRollDice.setDisable(false);
            btnEndTurn.setDisable(true);
        });

        btnEndTurn.setLayoutX(200);
        btnEndTurn.setLayoutY(120);

        group.getChildren().addAll(btnRollDice, btnEndTurn, btnResign);

        // turn text
        turnText = new Text();
        turnText.setFont(font);
        turnText.setText("Player Turn: " + gameEngine.getCurrentPlayer().getName()); //changed
        turnText.setX(150);
        turnText.setY(200);
        group.getChildren().add(turnText);

        group.getChildren().add(boardPane);

        scene = new Scene(group, width, height);
    }

    private void checkSquare() {
        if(gameEngine.passesStart()) {
            createStartDialog();
            updatePlayerTexts();
        }

        if (gameEngine.getCurrentSquare().getType() == SquareType.PROPERTY) {
            createPropertyDialog(-1);

        } else if (gameEngine.getCurrentSquare().getType() == SquareType.JOKER) {
            createJokerDialog();
        }
        else if(gameEngine.getCurrentSquare().getType() == SquareType.CHANCEANDCOMMUNITYCHEST) {
            createChanceAndChestDialog();
        }
        //start square
        else {
            createStartDialog();
        }
        updateSquares();
        updatePlayerTexts();
    }

    private void createBankruptDialog() {
        Dialog dialog = new Dialog();
        VBox vbox = new VBox();
        Text bankrupt = new Text("YOU LOSE");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        ((Button)okButton).setOnAction(event -> {
            ArrayList<Integer> indexes = gameEngine.sellPlayerProperties();
            updatePlayerTexts();
            for (int i = 0; i < indexes.size(); i++) {
                createAuctionOrSellDialog(indexes.get(i), true);
                updateSquares();
                updatePlayerTexts();
            }
            gameEngine.bankrupt();
            gameEngine.nextTurn();
        });
        vbox.getChildren().addAll(bankrupt);
        dialog.getDialogPane().setContent(vbox);
        dialog.show();
    }

    private void createResignDialog() {
        Dialog dialog = new Dialog();
        VBox vbox = new VBox();
        Text resignConfirmation = new Text("Are you sure you want to resign?");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        ((Button)okButton).setText("Yes");
        ((Button)okButton).setOnAction(event -> {
            dialog.close();
            ArrayList<Integer> indexes = gameEngine.sellPlayerProperties();
            updatePlayerTexts();

            for (int i = 0; i < indexes.size(); i++) {
                createAuctionOrSellDialog(indexes.get(i), true);
                updateSquares();
                updatePlayerTexts();
            }
            gameEngine.resign();
        });
        vbox.getChildren().addAll(resignConfirmation, okButton);
        dialog.getDialogPane().setContent(vbox);
        dialog.show();
    }

    private void createDiceDialog() {
        Dialog dialog = new Dialog();
        VBox vbox = new VBox();
        int[] dieResult = gameEngine.rollDice();
        Text die1 = new Text("Die 1: " + dieResult[0]);
        Text die2 = new Text("Die 2: " + dieResult[1]);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        ((Button)okButton).setOnAction(event -> {
            //if(gameEngine.canMove()) -- todo check jail

            gameEngine.movePlayer();
            updateSquares();
            updatePlayerTexts();
            dialog.close();
            checkSquare();
        });
        vbox.getChildren().addAll(die1, die2, okButton);
        dialog.getDialogPane().setContent(vbox);
        dialog.show();
    }

    private void createStartDialog() {
        Dialog dialog = new Dialog();
        VBox vbox = gameEngine.startInfo();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        ((Button)okButton).setText("Collect");
        ((Button)okButton).setOnAction(event -> {
            gameEngine.startAction();
            updateSquares();
            updatePlayerTexts();
            dialog.close();
        });
        vbox.getChildren().add(okButton);
        dialog.getDialogPane().setContent(vbox);
        dialog.showAndWait();
    }

    private void createChanceAndChestDialog() {
        Dialog dialog = new Dialog();
        VBox vbox = new VBox();
        Text text = new Text("Draw Card");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        ((Button)okButton).setText("Draw");
        ((Button)okButton).setOnAction(event -> {
            gameEngine.drawCard();
            createCardDialog();
            dialog.close();
            updateSquares();
            updatePlayerTexts();
        });
        vbox.getChildren().addAll(text, okButton);
        dialog.getDialogPane().setContent(vbox);
        dialog.show();
    }

    private void createCardDialog() {
        Dialog dialog = new Dialog();
        VBox vbox = new VBox();
        Text text = new Text(gameEngine.getCardInfo());
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        ((Button)okButton).setText("Do");
        ((Button)okButton).setOnAction(event -> {
            if(gameEngine.implementCard()) {
                dialog.close();
                updateSquares();
                updatePlayerTexts();
                checkSquare();
            }
            else {
                dialog.close();
                updateSquares();
                updatePlayerTexts();
            }
        });
        vbox.getChildren().addAll(text, okButton);
        dialog.getDialogPane().setContent(vbox);
        dialog.show();
    }

    private void createJokerDialog() {
        Dialog jokerDialog = new Dialog();
        VBox vbox = gameEngine.getJokerContent();
        jokerDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        Node okButton = jokerDialog.getDialogPane().lookupButton(ButtonType.OK);
        ((Button)okButton).setOnAction(event -> {
            if(gameEngine.jokerActions()) {
                jokerDialog.close();
                updateSquares();
                updatePlayerTexts();
                checkSquare();
            }
            else {
                jokerDialog.close();
                updateSquares();
                updatePlayerTexts();
            }
        });

        jokerDialog.getDialogPane().setContent(vbox);
        jokerDialog.showAndWait();
    }


    private void createAuctionOrSellDialog(int index, boolean isAuction) {
        Dialog auction = new Dialog();
        VBox vbox = new VBox();
        Text header;
        Text propertyName = new Text();
        if(index < 0)
            propertyName.setText("Property: " + ((Property)(gameEngine.getCurrentSquare())).getName());
        else {
            propertyName.setText("Property: " + ((Property)(gameEngine.getSquare(index))).getName());
        }
        if(isAuction) {
             header = new Text("Auction Property");
        }
        else {
            header = new Text("Sell Property");
        }
        HBox hbox = new HBox();
        Text playerName = new Text("Enter player: ");
        TextField player = new TextField();
        hbox.getChildren().addAll(playerName, player);

        HBox hbox2 = new HBox();
        Text amountPrompt = new Text("Enter amount: ");
        TextField amount = new TextField();
        hbox2.getChildren().addAll(amountPrompt, amount);

        auction.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
        vbox.getChildren().addAll(header, propertyName, hbox, hbox2);

        auction.getDialogPane().setContent(vbox);
        auction.setResultConverter(button -> {
            if (button == ButtonType.OK) {

                return new Pair<>(player.getText(), amount.getText());
            }
            return null;
        });

        auction.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);

        //auction.show();
        Optional<Pair<String, String>> result = auction.showAndWait();
        result.ifPresent(pair -> {
            System.out.println(player.getText());
            if(isAuction) {
                gameEngine.auctionProperty(index, player.getText(), Integer.parseInt(amount.getText()));
            }
            else {
                //selling a mortgaged property -- ask the new owner if they want to lift the mortgage
                gameEngine.sellProperty(index, player.getText(), Integer.parseInt(amount.getText()));
            }
            if(gameEngine.soldMortgaged(index)) {
                createMortgageLiftDialog(index);
            }
            updateSquares();
            updatePlayerTexts();
        });
    }

    private void createMortgageLiftDialog(int index) {
        Dialog mortgageLifting = new Dialog();
        VBox vbox = gameEngine.getMortgageLiftingInfo(index);

        //cancel button
        mortgageLifting.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Node closeButton = mortgageLifting.getDialogPane().lookupButton(ButtonType.CLOSE);
        ((Button)closeButton).setText("Lift mortgage later");
        ((Button)closeButton).setOnAction(event -> {
            gameEngine.liftMortgageLater(index);
            mortgageLifting.close();
            updateSquares();
            updatePlayerTexts();
        });

        //ok button
        mortgageLifting.getDialogPane().getButtonTypes().add(ButtonType.OK);
        Node okButton = mortgageLifting.getDialogPane().lookupButton(ButtonType.OK);
        ((Button)okButton).setText("Lift mortgage now");
        ((Button)okButton).setOnAction(event -> {
            gameEngine.unmortgageProperty(index);
            mortgageLifting.close();
            updateSquares();
            updatePlayerTexts();
        });

        vbox.getChildren().addAll(okButton,closeButton);
        mortgageLifting.getDialogPane().setContent(vbox);
        mortgageLifting.show();
    }

    private void createPropertyDialog(int index) {
        Dialog propertyDialog = new Dialog();
        Text header = new Text(gameEngine.getPropertyName(index));
        VBox vbox = gameEngine.getPropertyContent(index);
        vbox.getChildren().add(header);
        Property property;
        if(index < 0 ) {
            property = (Property)(gameEngine.getCurrentSquare());
        }
        else {
            property = (Property) (gameEngine.getSquare(index));
        }
        if(property.isOwned()) {
            propertyDialog.getDialogPane().setBackground(new Background(new BackgroundFill(property.getOwner().getColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        }
        else {
            propertyDialog.getDialogPane().setBackground(new Background(new BackgroundFill(Color.rgb(182, 216, 184), CornerRadii.EMPTY, Insets.EMPTY)));
        }
        ArrayList<String> buttonNames = gameEngine.getPropertyButtons(index);
        //add cancel button
        propertyDialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE); //todo bunu koyunca x tuşu ile kapanıyor olmaması lazım
        Node closeButton = propertyDialog.getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.setDisable(true);

        //dialog.getDialogPane().getScene().getWindow().setOnCloseRequest(event -> event.consume());

        for(String buttonName: buttonNames) {
            switch(buttonName) {
                case "cancel":
                    closeButton.setDisable(false);
                    //propertyDialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
                    break;
                case "sell":
                    Button sellBtn = new Button("Sell");
                    sellBtn.setOnAction(event -> {
                        propertyDialog.close();
                        createAuctionOrSellDialog( index, false);
                        updateSquares();
                    });
                    vbox.getChildren().add(sellBtn);
                    break;

                case "buy":
                    Button buyBtn = new Button("Buy");
                    buyBtn.setOnAction(event -> {
                        gameEngine.buyProperty();
                        updateSquares();
                        updatePlayerTexts();
                        propertyDialog.close();
                    });
                    vbox.getChildren().add(buyBtn);
                    break;

                case "auction":
                    Button auctionBtn = new Button("Auction");
                    auctionBtn.setOnAction(event -> {
                        propertyDialog.close();
                        createAuctionOrSellDialog( index, true);
                        updateSquares();
                    });
                    vbox.getChildren().add(auctionBtn);
                    break;

                case "mortgage":
                    Button mortgageBtn = new Button("Mortgage");
                    mortgageBtn.setOnAction(event -> {
                        gameEngine.mortgageProperty(index);
                        propertyDialog.close();
                        updatePlayerTexts();
                    });
                    vbox.getChildren().add(mortgageBtn);
                    break;

                case "unmortgage":
                    Button unmortgageBtn = new Button("Unmortgage");
                    unmortgageBtn.setOnAction(event -> {
                        gameEngine.unmortgageProperty(index);
                        updatePlayerTexts();
                        propertyDialog.close();
                    });
                    vbox.getChildren().add(unmortgageBtn);
                    break;

                case "add house":
                    Button addHouseBtn = new Button("Add House");
                    addHouseBtn.setOnAction(event -> {
                        gameEngine.addHouse(index);
                        updatePlayerTexts();
                        propertyDialog.close();
                    });
                    vbox.getChildren().add(addHouseBtn);
                    break;

                case "sell house":
                    Button sellHouseBtn = new Button("Sell House");
                    sellHouseBtn.setOnAction(event -> {
                        gameEngine.sellHouse(index);
                        updatePlayerTexts();
                        propertyDialog.close();
                    });
                    vbox.getChildren().add(sellHouseBtn);
                    break;

                case "add hotel":
                    Button addHotelBtn = new Button("Add hotel");
                    addHotelBtn.setOnAction(event -> {
                        gameEngine.addHotel(index);
                        updatePlayerTexts();
                        propertyDialog.close();
                    });
                    vbox.getChildren().add(addHotelBtn);
                    break;

                case "sell hotel":
                    Button sellHotelBtn = new Button("Sell Hotel");
                    sellHotelBtn.setOnAction(event -> {
                        gameEngine.sellHotel(index);
                        updatePlayerTexts();
                        propertyDialog.close();
                    });
                    vbox.getChildren().add(sellHotelBtn);
                    break;

                case "pay rent":
                    Button rentBtn = new Button("Pay Rent");
                    rentBtn.setOnAction(event -> {
                        gameEngine.takeRent();
                        updatePlayerTexts();
                        propertyDialog.close();
                    });
                    vbox.getChildren().add(rentBtn);
                    break;
            }
        }
        propertyDialog.getDialogPane().setContent(vbox);
        //propertyDialog.showAndWait();
        propertyDialog.show();
    }

    //create the board on screen
    private GridPane getSquares() {
        GridPane gridPane = new GridPane();

        for (int col = 0; col < 11; col++) {
            for (int row = 0; row < 11; row++) {
                int sum = row + col;
                int pos;

                if ( (row == 0) | (col == 0) | (row == 10) | (col == 10)) {
                    if (col >= row) {
                        pos = sum;
                    } else {
                        pos = 40 - sum;
                    }
                }
                else
                    pos = 40 + row + col;

                if (pos < 40) {
                    StackPane stp = new StackPane();
                    stp.setPadding(new Insets(1, 1, 1, 1));

                    // create rectangle of correct color for tile
                    Rectangle tile = new Rectangle();
                    if ((row == col) | (row == 0 & col == 10) | (col == 0 & row == 10)){
                        tile.setHeight(60);
                        tile.setWidth(60);
                    }
                    else if (row == 10 | row == 0){
                        tile.setHeight(60);
                        tile.setWidth(40);
                    }
                    else{
                        tile.setHeight(40);
                        tile.setWidth(60);
                    }
                    tile.setX(col * 10);
                    tile.setY(row * 10);
                    tile.setStroke(Color.BLACK);

                    tile.setOnMouseClicked(event -> {
                        System.out.println("Position " +position );
                    });

                    //set tile colors
                    System.out.println(gameEngine.getSquare(pos).getType());
                    if (gameEngine.getSquare(pos).getType() == SquareType.PROPERTY) {
                        Property property = (Property)(gameEngine.getSquare(pos));
                        tile.setFill(property.getColorGroup().getColor());
                    }
                    else if(gameEngine.getSquare(pos).getType() == SquareType.JOKER){
                        tile.setFill(Color.DARKGOLDENROD);
                    }
                    else if(gameEngine.getSquare(pos).getType() == SquareType.CHANCEANDCOMMUNITYCHEST){
                        tile.setFill(Color.LIME);
                    }
                    else { //start square
                        tile.setFill(Color.BLUEVIOLET);
                    }

                    // find players on tile and set text
                    ArrayList<Integer> playerPositions = gameEngine.getPlayerPositions();
                    String playersOnTile = "";

                    for (int i = 0; i < playerPositions.size(); i++) {
                        int position = playerPositions.get(i);
                        if (col >= row) {
                            if (sum == position) {
                                playersOnTile = playersOnTile + gameEngine.getPlayerNames().get(i) + "\n";
                            }
                        } else {
                            if (40 - sum == position) {
                                playersOnTile = playersOnTile + gameEngine.getPlayerNames().get(i) + "\n";
                            }
                        }
                    }

                    Text text = new Text(playersOnTile);
                    Font font2 = Font.font("Source Sans Pro", 10);
                    text.setFont(font2); //size of the player texts

                    if ((row == 0) | (col == 0) | (row == 10) | (col == 10)) {
                        stp.getChildren().addAll(tile, text);
                        gridPane.add(stp, col, row);
                    }
                }
            }
        }
        gridPane.setLayoutX(10);
        gridPane.setLayoutY(300);
        return gridPane;
    }

    //updates the board tiles on screen
    private void updateSquares() {
        boardPane.getChildren().clear();

        for (int col = 0; col < 11; col++) {
            for (int row = 0; row < 11; row++) {
                int sum = row + col;
                int pos;

                if ( (row == 0) | (col == 0) | (row == 10) | (col == 10)) {
                    if (col >= row) {
                        pos = sum;
                    } else {
                        pos = 40 - sum;
                    }
                }
                else
                    pos = 40 + row + col;

                if (pos < 40) {
                    StackPane stp = new StackPane();
                    stp.setPadding(new Insets(1, 1, 1, 1));

                    Rectangle tile = new Rectangle();
                    if ((row == col) | (row == 0 & col == 10) | (col == 0 & row == 10)){
                        tile.setHeight(60);
                        tile.setWidth(60);
                    }
                    else if (row == 10 | row == 0){
                        tile.setHeight(60);
                        tile.setWidth(40);
                    }
                    else{
                        tile.setHeight(40);
                        tile.setWidth(60);
                    }
                    tile.setX(col * 10);
                    tile.setY(row * 10);
                    tile.setStroke(Color.BLACK);

                    //todo add pictures/names to tiles, make player pawns

                    //make the tiles clickable
                    tile.setOnMouseClicked(event -> {
                        position = pos;
                        System.out.println(gameEngine.canClick(pos));
                        if(gameEngine.canClick(pos)) {
                            createPropertyDialog(pos);
                        }
                        System.out.println("Positionnnnn " + position );
                    });

                    //determine square colors
                    if (gameEngine.getSquare(pos).getType() == SquareType.PROPERTY) {
                        Property property = (Property) gameEngine.getSquare(pos);
                        if (property.isOwned() == true) {
                            Player owner = property.getOwner();
                            tile.setFill(owner.getColor());
                        }
                        else
                            tile.setFill(property.getColorGroup().getColor());
                    }
                    else if(gameEngine.getSquare(pos).getType() == SquareType.JOKER){
                        tile.setFill(Color.DARKGOLDENROD);
                    }

                    else if(gameEngine.getSquare(pos).getType() == SquareType.CHANCEANDCOMMUNITYCHEST){
                        tile.setFill(Color.LIME);
                    }
                    else {
                        tile.setFill(Color.BLUEVIOLET);
                    }

                    // find players on tile and set text
                    ArrayList<Integer> playerPositions = gameEngine.getPlayerPositions();
                    String playersOnTile = "";

                    for (int i = 0; i < playerPositions.size(); i++) {
                        int position = playerPositions.get(i);
                        if (col >= row) {
                            if (sum == position) {
                                playersOnTile = playersOnTile + gameEngine.getPlayerNames().get(i) + "\n";
                            }
                        } else {
                            if (40 - sum == position) {
                                playersOnTile = playersOnTile + gameEngine.getPlayerNames().get(i) + "\n";
                            }
                        }
                    }

                    Text text = new Text(playersOnTile);
                    Font font2 = Font.font("Source Sans Pro", 10);
                    text.setFont(font2); //size of the player texts

                    if ((row == 0) | (col == 0) | (row == 10) | (col == 10)) {
                        //stp.getChildren().add(0, tile);
                        stp.getChildren().addAll(tile, text);
                    }
                    boardPane.add(stp, col, row);
                }
            }
        }
    }

    public Scene getScene() { return scene; }

}
