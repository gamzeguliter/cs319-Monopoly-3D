package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class GameScreen {

    // properties
    private Scene scene;
    GridPane boardPane;

    Text[] playerTexts;
    Text turnText;
    Text diceText;

    Button btnRollDice;
    Button btnEndTurn;
    Button btnBuy;

    GameEngine gameEngine;
    private boolean diceRolled;

    Font font = Font.font("Source Sans Pro", 20);

    // constructors
    public GameScreen() {
        gameEngine = new GameEngine();
        boardPane = getTiles(); //CHANGE
        boardPane.setBackground(new Background(new BackgroundFill(Color.rgb(203, 227, 199), CornerRadii.EMPTY, Insets.EMPTY)));
        setScene();
    }

    // private methods

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
        for(Player player : gameEngine.getPlayers()) {
            playerTexts[count] = getPlayerText(player);
            count++;
        }
        playerTexts[0].setX(10);
        playerTexts[1].setX(160);
        playerTexts[2].setX(310);
        playerTexts[3].setX(460);

        group.getChildren().addAll(playerTexts[0], playerTexts[1], playerTexts[2], playerTexts[3]);
    }

    private void updatePlayerText(Text text, Player player) {
        text.setText(player.getName() + "\nbalance: " + player.getBalance() + "\nposition: " + player.getPosition());
    }

    private void setScene() {
        Group group = new Group();
        int width = 1000;
        int height = 1000;

        // initialize player texts
        initializePlayerTexts(group);

        //todo burayı methodlara ayır -- getPlayerInfo falan gibisinden
        // dice on game engine
        // anlamak için square adlarını da bastır

        // burda board falan yaratmak laızm mı

        // initialize buttons
        btnRollDice = new Button();

        //roll dice
        btnRollDice.setText("Roll Dice");
        btnRollDice.setOnAction(event -> {
            int roll = gameEngine.rollDice();
            diceRolled = true;
            btnRollDice.setDisable(true);
            diceText.setText("Dice roll: " + roll);

            Player currentPlayer = gameEngine.getCurrentPlayer();

            //already updated in roll dice method
            //int position = currentPlayer.getPosition();
            //position = (position + roll) % 40;
            //currentPlayer.setPosition(position);

            updatePlayerText(playerTexts[gameEngine.getTurn()], currentPlayer);
            updateTiles(); // CHANGE

            if (gameEngine.getCurrentSquare().getType() == SquareType.COLORGROUP) {
                Player owner = ((ColorGroup) gameEngine.getCurrentSquare()).propertyOwner(currentPlayer.getPosition());
                if (owner == null) {
                    btnBuy.setDisable(false);
                } else {
                    gameEngine.rent(currentPlayer.getPosition());
                    //updatePlayerTexts();
                }
            }
        });
        btnRollDice.setLayoutX(100);
        btnRollDice.setLayoutY(120);

        btnEndTurn = new Button();
        btnEndTurn.setText("End Turn");
        btnEndTurn.setOnAction(event -> {
            gameEngine.nextTurn();
            turnText.setText("Player Turn: " + gameEngine.getCurrentPlayer().getName());
            diceRolled = false;

            btnRollDice.setDisable(false);
            //Player owner = ((Property) board.tiles[players[turn].getPosition()]).getOwner();
            //if (owner != null) {
            //    btnBuy.setDisable(true);
            //}
        });

        btnEndTurn.setLayoutX(200);
        btnEndTurn.setLayoutY(120);

        btnBuy = new Button();
        btnBuy.setText("Buy");
        /*btnBuy.setOnAction(event -> {
            int balance = players[turn].getBalance();
            balance -= ((Property) board.tiles[players[turn].getPosition()]).buyValue;
            players[turn].setBalance(balance);

            ((Property) board.tiles[players[turn].getPosition()]).setOwner(players[turn]);
            updateTiles();
            updatePlayerText(playerTexts[turn], players[turn]);

            btnBuy.setDisable(true);
        });
        */
        btnBuy.setLayoutX(300);
        btnBuy.setLayoutY(120);

        group.getChildren().addAll(btnRollDice, btnEndTurn, btnBuy);

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

    private GridPane getTiles() {
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
                    tile.setFill(Color.ORCHID);

                    /*
                    if (gameEngine.getSquare(pos).getType() == SquareType.COLORGROUP) {
                        tile.setFill(Color.LIMEGREEN);
                        if (gameEngine.getSquare(pos) == null) {
                            System.out.println("AAAAAA SORUN VAR");
                        } else {
                            ColorGroup colors = (ColorGroup) gameEngine.getSquare(pos);
                            if (colors.propertyOwner(gameEngine.getCurrentPlayer().getPosition()) != null) {
                                Player owner = colors.propertyOwner(gameEngine.getCurrentPlayer().getPosition());
                                if (owner != null)
                                    tile.setFill(owner.getColor());
                                else
                                    tile.setFill(Color.GREY);
                            }
                        }
                    }
                    */

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
    private void updateTiles() { //board pane vs grid pane?
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
                    tile.setFill(Color.ORCHID);

                    /*
                    if (gameEngine.getSquare(pos).getType() == SquareType.COLORGROUP) {
                        tile.setFill(Color.LIMEGREEN);
                        if (gameEngine.getSquare(pos) == null) {
                            System.out.println("AAAAAA SORUN VAR");
                        } else {
                            ColorGroup colors = (ColorGroup) gameEngine.getSquare(pos);
                            if (colors.propertyOwner(gameEngine.getCurrentPlayer().getPosition()) != null) {
                                Player owner = colors.propertyOwner(gameEngine.getCurrentPlayer().getPosition());
                                if (owner != null)
                                    tile.setFill(owner.getColor());
                                else
                                    tile.setFill(Color.GREY);
                            }
                        }
                    }
                    */

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

                    System.out.println(pos + " " + playersOnTile);
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
}
