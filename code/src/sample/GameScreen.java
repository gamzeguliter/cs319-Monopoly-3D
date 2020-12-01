package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameScreen {

    // properties
    private Player[] players;
    private Scene scene;
    int turn;
    boolean diceRolled;

    Text[] playerTexts;
    Text turnText;
    Text diceText;

    // constructors
    public GameScreen() {
        players = new Player[4];
        for (int i = 1; i <= 4; i++) {
            players[i-1] = new Player("player" + i, 0, 100);
        }

        turn = 0;
        diceRolled = false;
        setScene();
    }

    // private methods
    private void setScene() {
        Group group = new Group();
        int width = 600;
        int height = 600;

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
        Button btnRollDice = new Button();
        btnRollDice.setText("Roll Dice");
        btnRollDice.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                int roll = rollDice();
                diceRolled = true;
                btnRollDice.setDisable(true);
                diceText.setText("Dice roll: " + roll);

                Player currentPlayer = players[turn];
                int position = currentPlayer.getPosition();
                position = (position + roll) % 40;
                currentPlayer.setPosition(position);
                updatePlayerText(playerTexts[turn], currentPlayer);
            }
        });
        btnRollDice.setLayoutX(100);
        btnRollDice.setLayoutY(120);

        Button btnEndTurn = new Button();
        btnEndTurn.setText("End Turn");
        btnEndTurn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                turn = (turn + 1) % 4;
                turnText.setText("Player Turn: " + (turn + 1));
                diceRolled = false;
                btnRollDice.setDisable(false);
            }
        });
        btnEndTurn.setLayoutX(300);
        btnEndTurn.setLayoutY(120);
        group.getChildren().addAll(btnRollDice, btnEndTurn);

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

        scene = new Scene(group ,width, height);
    }

    private Text getPlayerText(Player player) {
        Text t = new Text();
        t.setFont(new Font(20));
        t.setText(player.getName() + "\nbalance: " + player.getBalance() + "\nposition: " + player.getPosition());
        t.setY(50);
        return t;
    }

    private void updatePlayerText(Text text, Player player) {
        text.setText(player.getName() + "\nbalance: " + player.getBalance() + "\nposition: " + player.getPosition());
    }

    public static int rollDice() {
        int min = 1;
        int max = 6;
        int roll1 = min + (int)(Math.random() * ((max - min) + 1));
        int roll2 = min + (int)(Math.random() * ((max - min) + 1));
        return roll1 + roll2;
    }

    // public methods

    public Scene getScene() { return scene; }
}
