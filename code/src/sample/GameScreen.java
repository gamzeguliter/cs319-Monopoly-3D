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
    Rectangle[] boardPane;

    Text[] playerTexts;
    Text turnText;
    Text diceText;

    Button btnRollDice;
    Button btnEndTurn;
    Button btnBuy;

    GameEngine gameEngine;
    Font font = Font.font("Source Sans Pro", 20);

    // constructors
    public GameScreen() {
        gameEngine = new GameEngine();
        boardPane =  new Rectangle[41];
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
        for(Player player : gameEngine.getPlayers()) {
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

    private void updatePlayerText(Text text, Player player) {
        text.setText(player.getName() + "\nBalance: " + player.getBalance() + "\nPosition: " + player.getPosition());
    }

    private void setScene() {
        Group group = new Group();
        int width = 1366;
        int height = 768;

        // initialize player texts
        initializePlayerTexts(group);

        // initialize buttons
        btnRollDice = new Button();
        Font font3 = Font.font("Source Sans Pro", 15);

        //roll dice button
        btnRollDice.setText("Roll Dice");
        btnRollDice.setFont(font3);
        btnRollDice.setOnAction(event -> {
            int roll = gameEngine.rollDice();
            gameEngine.checkSquare();
            btnRollDice.setDisable(true);
            btnBuy.setDisable(gameEngine.isBuyDisabled());
            diceText.setText("Dice roll: " + roll);
            Player currentPlayer = gameEngine.getCurrentPlayer();
            updatePlayerText(playerTexts[gameEngine.getTurn()], currentPlayer);
            updateSquares();
        });

        btnRollDice.setLayoutX(100);
        btnRollDice.setLayoutY(120);

        //end turn button
        btnEndTurn = new Button();
        btnEndTurn.setText("End Turn");
        btnEndTurn.setFont(font3);
        btnEndTurn.setOnAction(event -> {
            gameEngine.nextTurn();
            turnText.setText("Player Turn: " + gameEngine.getCurrentPlayer().getName());
            btnRollDice.setDisable(false);
        });

        btnEndTurn.setLayoutX(200);
        btnEndTurn.setLayoutY(120);

        //buy button
        btnBuy = new Button();
        btnBuy.setText("Buy");
        btnBuy.setFont(font3);
        System.out.println("isBuyEnabled: " + !gameEngine.isBuyDisabled());
        btnBuy.setDisable(gameEngine.isBuyDisabled());
        btnBuy.setOnAction(event -> {
            gameEngine.buyProperty();
            updateSquares();
            updatePlayerText(playerTexts[gameEngine.getTurn()], gameEngine.getCurrentPlayer());
            btnBuy.setDisable(true);
        });

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

       // group.getChildren().add(boardPane);
        for(int i =0; i < 41; i++ )
            group.getChildren().add(boardPane[i]);

        scene = new Scene(group, width, height);
    }

    private Rectangle[] getSquares() {
        GridPane gridPane = new GridPane();
       Rectangle [] recs = new Rectangle[41];
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
                  /*  StackPane stp = new StackPane();
                    stp.setPadding(new Insets(1, 1, 1, 1));*/

                    // create rectangle of correct color for tile
                    Rectangle tile = new Rectangle();
                    if ( (row == 0 & col == 10) | (col == 0 & row == 10) | (row == col)){
                        tile.setHeight(90);
                        tile.setWidth(90);
                    }
                    else if (row == 10 | row == 0){
                        tile.setHeight(90);
                        tile.setWidth(60);
                    }
                    else{
                        tile.setHeight(60);
                        tile.setWidth(90);
                    }

                    //rect locations
                    if (pos == 0){
                        tile.setX(10);
                        tile.setY(10);
                    }
                    else if (pos <= 10){
                        tile.setX(recs[pos - 1].getX() + recs[pos - 1].getWidth());
                        tile.setY(recs[pos - 1].getY());
                    }
                    else if (pos <= 20){
                        tile.setX(recs[pos - 1].getX());
                        tile.setY(recs[pos - 1].getY() + recs[pos - 1].getHeight());
                    }
                    else if (pos < 30){
                        tile.setX(recs[(pos + 1) % 40].getX() + recs[(pos + 1) % 40].getWidth());
                        tile.setY(recs[(pos + 1) % 40].getY());
                    }
                    else if (pos < 40){
                        tile.setX(recs[(pos + 1) % 40].getX());
                        tile.setY(recs[(pos + 1) % 40].getY() + recs[(pos + 1) % 40].getHeight());
                    }
                    else{
                        tile.setX(recs[0].getX() + recs[0].getHeight());
                        tile.setY(recs[0].getY() + recs[0].getWidth());
                    }

                    //rect colors
                    tile.setStroke(Color.BLACK);
                    //tile.setFill(Color.ORCHID);

                    //set tile colors
                    if (gameEngine.getSquare(pos).getType() == SquareType.PROPERTY) {
                        tile.setFill(Color.MEDIUMSEAGREEN);
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
                   recs[pos]= tile;
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

                   /* if ((row == 0) | (col == 0) | (row == 10) | (col == 10)) {
                        //stp.getChildren().add(0, tile);
                        stp.getChildren().addAll(tile, text);
                        gridPane.add(stp, col, row);
                    }*/
                }
            }
        }
        Rectangle middleOne = new Rectangle();
        middleOne.setX(recs[0].getX() + recs[0].getWidth());
        middleOne.setY(recs[0].getY() + recs[0].getHeight());
        middleOne.setStroke(Color.BLACK);
        middleOne.setFill(Color.WHITE);
        middleOne.setHeight(10);
        middleOne.setWidth(10);
        recs[40]= middleOne;
        gridPane.setLayoutX(10);
        gridPane.setLayoutY(300);

        return recs;
    }


    //new updateBoard same as getTiles?
    private void updateSquares() { //board pane vs grid pane?
       // boardPane.getChildren().clear();

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
                  /*  StackPane stp = new StackPane();
                    stp.setPadding(new Insets(1, 1, 1, 1));*/

                    // create rectangle of correct color for tile
                    Rectangle tile = new Rectangle();
                    if ( (row == 0 & col == 10) | (col == 0 & row == 10) | (row == col)){
                        tile.setHeight(90);
                        tile.setWidth(90);
                    }
                    else if (row == 10 | row == 0){
                        tile.setHeight(90);
                        tile.setWidth(60);
                    }
                    else{
                        tile.setHeight(60);
                        tile.setWidth(90);
                    }

                    //rect locations
                    if (pos == 0){
                        tile.setX(10);
                        tile.setY(10);
                    }
                    else if (pos <= 10){
                        tile.setX(boardPane[pos - 1].getX() + boardPane[pos - 1].getWidth());
                        tile.setY(boardPane[pos - 1].getY());
                    }
                    else if (pos <= 20){
                        tile.setX(boardPane[pos - 1].getX());
                        tile.setY(boardPane[pos - 1].getY() + boardPane[pos - 1].getHeight());
                    }
                    else if (pos < 30){
                        tile.setX(boardPane[(pos + 1) % 40].getX() + boardPane[(pos + 1) % 40].getWidth());
                        tile.setY(boardPane[(pos + 1) % 40].getY());
                    }
                    else if (pos < 40){
                        tile.setX(boardPane[(pos + 1) % 40].getX());
                        tile.setY(boardPane[(pos + 1) % 40].getY() + boardPane[(pos + 1) % 40].getHeight());
                    }
                    else{
                        tile.setX(boardPane[0].getX() + boardPane[0].getHeight());
                        tile.setY(boardPane[0].getY() + boardPane[0].getWidth());
                    }


                    //rect colors
                    tile.setStroke(Color.BLACK);

                    //determine square colors
                    if (gameEngine.getSquare(pos).getType() == SquareType.PROPERTY) {
                        Property property = (Property) gameEngine.getSquare(pos);
                        if (property.isOwned() == true) {
                            Player owner = property.getOwner();
                            tile.setFill(owner.getColor());
                        }
                        else
                            tile.setFill(Color.MEDIUMSEAGREEN);
                    }
                    else if(gameEngine.getSquare(pos).getType() == SquareType.JOKER){
                        tile.setFill(Color.DARKGOLDENROD);
                    }

                    else {
                        tile.setFill(Color.LIME);
                    }

                    boardPane[pos] = tile;

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
                    text.setY(tile.getY()+2);
                    text.setX(tile.getX()+2);
                   /* if ((row == 0) | (col == 0) | (row == 10) | (col == 10)) {
                        //stp.getChildren().add(0, tile);
                        stp.getChildren().addAll(tile, text);
                    }*/
                   // boardPane.add(stp, col, row);

                }
            }
        }
    }
    // public methods

    public Scene getScene() { return scene; }
}
