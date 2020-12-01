package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;



public class GameScreen {

    // properties
    private Player[] players;
    private Scene scene;
    int turn;
    boolean diceRolled;
    Board board;
    GridPane boardPane;

    Text[] playerTexts;
    Text turnText;
    Text diceText;

    Button btnRollDice;
    Button btnEndTurn;
    Button btnBuy;

    // constructors
    public GameScreen() {
        players = new Player[4];
        for (int i = 1; i <= 4; i++) {
            players[i-1] = new Player("player" + i, Color.GRAY, 100);
        }
        players[0].setColor(Color.RED);
        players[1].setColor(Color.GREEN);
        players[2].setColor(Color.BLUE);
        players[3].setColor(Color.YELLOW);

        board = new Board();
        boardPane = getTiles();
        turn = 0;
        diceRolled = false;
        setScene();
    }

    // private methods
    private void setScene() {
        Group group = new Group();
        int width = 600;
        int height = 700;

        // initialize player texts
        playerTexts = new Text[4];
        playerTexts[0] = getPlayerText(players[0]);
        playerTexts[0].setX(0);
        playerTexts[1] = getPlayerText(players[1]);
        playerTexts[1].setX(130);
        playerTexts[2] = getPlayerText(players[2]);
        playerTexts[2].setX(280);
        playerTexts[3] = getPlayerText(players[3]);
        playerTexts[3].setX(430);
        group.getChildren().addAll(playerTexts[0], playerTexts[1], playerTexts[2], playerTexts[3]);

        // initialize buttons
        btnRollDice = new Button();
        btnRollDice.setText("Roll Dice");
        btnRollDice.setOnAction(event -> {
            int roll = rollDice();
            diceRolled = true;
            btnRollDice.setDisable(true);
            diceText.setText("Dice roll: " + roll);

            Player currentPlayer = players[turn];
            int position = currentPlayer.getPosition();
            position = (position + roll) % 40;
            currentPlayer.setPosition(position);
            updatePlayerText(playerTexts[turn], currentPlayer);
            updateTiles();

            Player owner = ((Property) board.tiles[players[turn].getPosition()]).getOwner();
            if (owner == null) {
                btnBuy.setDisable(false);
            }
            else {
                int rentValue = ((Property) board.tiles[players[turn].getPosition()]).rentValue;
                players[turn].setBalance(players[turn].getBalance() - rentValue);
                owner.setBalance(owner.getBalance() + rentValue);
                updatePlayerTexts();
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

    private Text getPlayerText(Player player) {
        Text t = new Text();
        t.setFont(new Font(20));
        t.setText(player.getName() + "\nbalance: " + player.getBalance() + "\nposition: " + player.getPosition());
        t.setFill(player.getColor());
        t.setY(50);
        return t;
    }

    private void updatePlayerText(Text text, Player player) {
        text.setText(player.getName() + "\nbalance: " + player.getBalance() + "\nposition: " + player.getPosition());
    }

    private void updatePlayerTexts() {
        for (int i = 0; i < 4; i++) {
            updatePlayerText(playerTexts[i], players[i]);
        }
    }

    public static int rollDice() {
        int min = 1;
        int max = 6;
        int roll1 = min + (int)(Math.random() * ((max - min) + 1));
        int roll2 = min + (int)(Math.random() * ((max - min) + 1));
        return roll1 + roll2;
    }

    private GridPane getTiles() {
        GridPane gridPane = new GridPane();

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
