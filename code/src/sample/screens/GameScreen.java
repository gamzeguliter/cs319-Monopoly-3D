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
import sample.squares.Property;
import sample.squares.SquareType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class GameScreen extends Screen {

    // properties
    private Scene scene;
    GridPane boardPane;

    Text[] playerTexts;
    Text turnText;
    Text diceText;
    Button btnRollDice;
    Button btnEndTurn;
    GameEngine gameEngine;
    Font font = Font.font("Source Sans Pro", 20);
    int position;
    // constructors
    public GameScreen() {
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

    private void initializePlayerTexts(Group group) {
        playerTexts = new Text[4];
        int count = 0;
        for (Player player : gameEngine.getPlayers()) {
            playerTexts[count] = getPlayerText(player);
            count++;
        }
        playerTexts[0].setX(800);
        playerTexts[0].setY(100);
        playerTexts[1].setX(800);
        playerTexts[1].setY(200);
        playerTexts[2].setX(800);
        playerTexts[2].setY(300);
        playerTexts[3].setX(800);
        playerTexts[3].setY(400);

        group.getChildren().addAll(playerTexts[0], playerTexts[1], playerTexts[2], playerTexts[3]);
    }

    //todo bu method direk getPlayers ile almasa daha iyi olur
    private void updatePlayerTexts() {
        for(int i = 0; i < gameEngine.getPlayers().size(); i++) {
            if(gameEngine.getPlayers().get(i).isOut()) {
                playerTexts[i].setDisable(true);
            }
            else {
                Player player = gameEngine.getPlayers().get(i);
                playerTexts[i].setText(player.getName() + "\nBalance: " + player.getBalance() + "\nPosition: " + player.getPosition());
            }
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
        Font font3 = Font.font("Source Sans Pro", 15);

        //roll dice button
        btnRollDice.setText("Roll Dice");
        btnRollDice.setFont(font3);
        //initialize end turn as disabled
        btnEndTurn.setDisable(true);

        btnRollDice.setOnAction(event -> {
            btnEndTurn.setDisable(false); //todo eğer oyunda tekrar hareket varsa hareket pop-upından sonra disable kaldır

            int roll = gameEngine.rollDice();
            diceText.setText("Dice roll: " + roll);
            btnRollDice.setDisable(true);

            updateSquares();
            updatePlayerTexts();

            if(gameEngine.passesStart()) {
                createStartDialog();
                updatePlayerTexts();
            }

            if (gameEngine.getCurrentSquare().getType() == SquareType.PROPERTY) {
                createPropertyDialog();

            } else if (gameEngine.getCurrentSquare().getType() == SquareType.JOKER) {
                createJokerDialog();
            }
            else if(gameEngine.getCurrentSquare().getType() == SquareType.CHANCEANDCOMMUNITYCHEST) {
                createChanceAndChestDialog();
            }
            //start square
            //todo start square üstünden geçip gidiyorsa da çıkarmalısın
            else {
                createStartDialog();
            }
            updateSquares();
            updatePlayerTexts();
        });

        btnRollDice.setLayoutX(100);
        btnRollDice.setLayoutY(120);

        //end turn button
        btnEndTurn.setText("End Turn");
        btnEndTurn.setFont(font3);
        btnEndTurn.setOnAction(event -> {
            gameEngine.nextTurn();
            turnText.setText("Player Turn: " + gameEngine.getCurrentPlayer().getName());
            btnRollDice.setDisable(false);
            btnEndTurn.setDisable(true);
        });

        btnEndTurn.setLayoutX(200);
        btnEndTurn.setLayoutY(120);

        group.getChildren().addAll(btnRollDice, btnEndTurn);

        // turn text
        turnText = new Text();
        turnText.setFont(font);
        turnText.setText("Player Turn: " + gameEngine.getCurrentPlayer().getName()); //changed
        turnText.setX(150);
        turnText.setY(200);
        group.getChildren().add(turnText);

        // dice text
        diceText = new Text();
        diceText.setFont(font);
        diceText.setText("Dice roll: 0");
        diceText.setX(150);
        diceText.setY(250);
        group.getChildren().add(diceText);

        group.getChildren().add(boardPane);

        scene = new Scene(group, width, height);
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
        dialog.showAndWait();
    }

    private void createCardDialog() {
        Dialog dialog = new Dialog();
        VBox vbox = new VBox();
        Text text = new Text(gameEngine.getCardInfo());
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        ((Button)okButton).setText("Do");
        ((Button)okButton).setOnAction(event -> {
            gameEngine.implementCard();
            dialog.close();
            updateSquares();
            updatePlayerTexts();
        });
        vbox.getChildren().add(okButton);
        dialog.getDialogPane().setContent(vbox);
        dialog.showAndWait();
    }

    private void createJokerDialog() {
        Dialog jokerDialog = new Dialog();
        VBox vbox = gameEngine.getJokerContent();
        jokerDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        Node okButton = jokerDialog.getDialogPane().lookupButton(ButtonType.OK);
        ((Button)okButton).setOnAction(event -> {
            gameEngine.jokerActions();
            jokerDialog.close();
            updateSquares();
            updatePlayerTexts();
        });

        jokerDialog.getDialogPane().setContent(vbox);
        jokerDialog.showAndWait();
    }

    private void createAuctionOrSellDialog(int index, boolean isAuction) {
        Dialog auction = new Dialog();
        VBox vbox = new VBox();
        Text header;
        if(isAuction) {
             header = new Text("Auction Property");
        }
        else
            header = new Text("Sell Property");

        Text propertyName = new Text("Property: " + ((Property)(gameEngine.getCurrentSquare())).getName());
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

        //auction.show();
        Optional<Pair<String, String>> result = auction.showAndWait();
        result.ifPresent(pair -> {
            System.out.println(player.getText());
            if(isAuction) {
                gameEngine.auctionProperty(player.getText(), Integer.parseInt(amount.getText()));
            }
            else
                gameEngine.sellProperty(index, player.getText(), Integer.parseInt(amount.getText()));
            updateSquares();
            updatePlayerTexts();
        });

    }

    private void createPropertyDialog() {
        Dialog propertyDialog = new Dialog();
        VBox vbox = gameEngine.getPropertyContent();
        Property property = (Property)(gameEngine.getCurrentSquare());
        if(((Property)gameEngine.getCurrentSquare()).isOwned()) {
            propertyDialog.getDialogPane().setBackground(new Background(new BackgroundFill(property.getOwner().getColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        }
        else {
            propertyDialog.getDialogPane().setBackground(new Background(new BackgroundFill(Color.rgb(182, 216, 184), CornerRadii.EMPTY, Insets.EMPTY)));
        }
        ArrayList<String> buttonNames = gameEngine.getPropertyButtons();
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
                        createAuctionOrSellDialog( -1, false);
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
                        createAuctionOrSellDialog( 0, true);
                        updateSquares();
                    });
                    vbox.getChildren().add(auctionBtn);
                    break;

                case "mortgage":
                    Button mortgageBtn = new Button("Mortgage");
                    mortgageBtn.setOnAction(event -> {
                        gameEngine.mortgageProperty(-1);
                        propertyDialog.close();
                        updatePlayerTexts();
                    });
                    vbox.getChildren().add(mortgageBtn);
                    break;

                case "unmortgage":
                    Button unmortgageBtn = new Button("Unmortgage");
                    unmortgageBtn.setOnAction(event -> {
                        gameEngine.unmortgageProperty(-1);
                        updatePlayerTexts();
                        propertyDialog.close();
                    });
                    vbox.getChildren().add(unmortgageBtn);
                    break;

                case "add house":
                    Button addHouseBtn = new Button("Add House");
                    addHouseBtn.setOnAction(event -> {
                        gameEngine.addHouse(-1);
                        updatePlayerTexts();
                        propertyDialog.close();
                    });
                    vbox.getChildren().add(addHouseBtn);
                    break;

                case "sell house":
                    Button sellHouseBtn = new Button("Sell House");
                    sellHouseBtn.setOnAction(event -> {
                        gameEngine.sellHouse(-1);
                        updatePlayerTexts();
                        propertyDialog.close();
                    });
                    vbox.getChildren().add(sellHouseBtn);
                    break;

                case "add hotel":
                    Button addHotelBtn = new Button("Add hotel");
                    addHotelBtn.setOnAction(event -> {
                        gameEngine.addHotel(-1);
                        updatePlayerTexts();
                        propertyDialog.close();
                    });
                    vbox.getChildren().add(addHotelBtn);
                    break;

                case "sell hotel":
                    Button sellHotelBtn = new Button("Sell Hotel");
                    sellHotelBtn.setOnAction(event -> {
                        gameEngine.sellHotel(-1);
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
                    //tile.setFill(Color.ORCHID);
                    tile.setOnMouseClicked(event -> {
                        position = pos;
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
                        //stp.getChildren().add(0, tile);
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

    //new updateBoard same as getTiles?
    private void updateSquares() { //board pane vs grid pane?
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
                        position = pos;
                        System.out.println("Position " +position );
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
    // public methods

    public Scene getScene() { return scene; }

    public Scene drawScene() throws IOException {
        setScene();
        return scene;
    }




}
