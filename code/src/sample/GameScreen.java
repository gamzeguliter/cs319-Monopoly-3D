package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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

    // constructors
    public GameScreen() {
        gameEngine = new GameEngine();
        boardPane = getTiles(); //CHANGE
        setScene();
    }

    // private methods

    private Text getPlayerText(Player player) {
        Text t = new Text();
        t.setFont(new Font(20));
        t.setText(player.getName() + "\nbalance: " + player.getBalance() + "\nposition: " + player.getPosition());
        t.setFill(player.getColor());
        t.setY(50);
        return t;
    }

    private void initializePlayerTexts(Group group) {
        playerTexts = new Text[4];
        int count = 0;
        for(Player player : gameEngine.getPlayers()) {
            playerTexts[count] = getPlayerText(player);
        }
        playerTexts[0].setX(0);
        playerTexts[1].setX(130);
        playerTexts[2].setX(280);
        playerTexts[3].setX(430);

        group.getChildren().addAll(playerTexts[0], playerTexts[1], playerTexts[2], playerTexts[3]);
    }

    private void setScene() {
        Group group = new Group();
        int width = 600;
        int height = 700;

        // initialize player texts
        initializePlayerTexts(group);

        //todo burayı methodlara ayır -- getPlayerInfo falan gibisinden
        // dice on game engine
        // anlamak için square adlarını da bastır

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
            int position = currentPlayer.getPosition();
            position = (position + roll) % 40;
            currentPlayer.setPosition(position);
            updatePlayerText(playerTexts[gameEngine.getTurn()], currentPlayer);
            updateTiles(); // CHANGE

            if(gameEngine.getCurrentSquare().getType() == SquareType.COLORGROUP) {
                Player owner = ((ColorGroup)gameEngine.getCurrentSquare()).propertyOwner(currentPlayer.getPosition());
                if (owner == null) {
                    btnBuy.setDisable(false);
                } else {
                    gameEngine.rent(currentPlayer.getPosition());
                    updatePlayerTexts();
                }
            }
        });
        btnRollDice.setLayoutX(100);
        btnRollDice.setLayoutY(120);

        btnEndTurn = new Button();
        btnEndTurn.setText("End Turn");
        btnEndTurn.setOnAction(event -> {
            turn = (turn + 1) % 4;
            turnText.setText("Player Turn: " + (turn + 1));
            diceRolled = false;

            btnRollDice.setDisable(false);
            Player owner = ((Property) board.tiles[players[turn].getPosition()]).getOwner();
            if (owner != null) {
                btnBuy.setDisable(true);
            }
        });
        btnEndTurn.setLayoutX(200);
        btnEndTurn.setLayoutY(120);

        btnBuy = new Button();
        btnBuy.setText("Buy");
        btnBuy.setOnAction(event -> {
            int balance = players[turn].getBalance();
            balance -= ((Property) board.tiles[players[turn].getPosition()]).buyValue;
            players[turn].setBalance(balance);

            ((Property) board.tiles[players[turn].getPosition()]).setOwner(players[turn]);
            updateTiles();
            updatePlayerText(playerTexts[turn], players[turn]);

            btnBuy.setDisable(true);
        });
        btnBuy.setLayoutX(300);
        btnBuy.setLayoutY(120);

        group.getChildren().addAll(btnRollDice, btnEndTurn, btnBuy);

        // turn text
        turnText = new Text();
        turnText.setFont(new Font(20));
        turnText.setText("Player Turn: 1");
        turnText.setX(150);
        turnText.setY(200);
        group.getChildren().add(turnText);

        // dice text
        diceText = new Text();
        diceText.setFont(new Font(20));
        diceText.setText("Dice roll: 0");
        diceText.setX(300);
        diceText.setY(200);
        group.getChildren().add(diceText);

        group.getChildren().add(boardPane);

        scene = new Scene(group ,width, height);
    }
    }

    private void updatePlayerText(Text text, Player player) {
        text.setText(player.getName() + "\nbalance: " + player.getBalance() + "\nposition: " + player.getPosition());
    }


    private GridPane getTiles() {
        GridPane gridPane = new GridPane();

        for (int col = 0; col < 10; col++) {
            for (int row = 0; row < 4; row++) {
                int pos = 10 * row + col;

                StackPane stp= new StackPane();
                stp.setPadding(new Insets(5,5,5,5));

                // create rectangle of correct color for tile
                Rectangle tile = new Rectangle();
                tile.setHeight(80);
                tile.setWidth(40);
                if (board.squares[pos].getType() == SquareType.COLORGROUP) {
                    Player owner = ((Property) board.tiles[pos]).owner;
                    if (owner != null)
                        tile.setFill(owner.getColor());
                    else
                        tile.setFill(Color.GRAY);
                }

                // find players on tile and set text
                String playersOnTile = "";
                for (Player player : players) {
                    if (player.getPosition() == pos) {
                        System.out.println(pos);
                        playersOnTile += player.getName() + "\n";
                    }
                }
                Text text = new Text(playersOnTile);

                //stp.getChildren().addAll(tile, text);
                stp.getChildren().add(0, tile);
                gridPane.add(stp, col, row);
            }
        }
        gridPane.setLayoutX(10);
        gridPane.setLayoutY(300);
        return gridPane;
    }

    private void updateTiles() {
        boardPane.getChildren().clear();
        for (int col = 0; col < 10; col++) {
            for (int row = 0; row < 4; row++) {
                int pos = 10*row + col;

                StackPane stp= new StackPane();
                stp.setPadding(new Insets(5,5,5,5));

                // create rectangle of correct color for tile
                Rectangle tile = new Rectangle();
                tile.setHeight(80);
                tile.setWidth(40);
                if (board.tiles[pos].type.equals("property")) {
                    Player owner = ((Property) board.tiles[pos]).owner;
                    if (owner != null)
                        tile.setFill(owner.getColor());
                    else
                        tile.setFill(Color.GRAY);
                }

                // find players on tile and set text
                String playersOnTile = "";
                for (Player player : players) {
                    if (player.getPosition() == pos) {
                        System.out.println(pos);
                        playersOnTile += player.getName() + "\n";
                    }
                }
                Text text = new Text(playersOnTile);
                stp.getChildren().addAll(tile, text);

                boardPane.add(stp, col, row);
            }
        }
    }


    // public methods

    public Scene getScene() { return scene; }
}
