package sample;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class EditorScreen {
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
    public EditorScreen() throws FileNotFoundException {
        //editor = new Editor();
        // boardPane = getTiles(); //CHANGE
        setScene();
    }

    // private methods
    private void setScene() throws FileNotFoundException {
        Group group = new Group();
        int width = 1366;
        int height = 768;

        // group.getChildren().add(boardPane);
        Rectangle[] recs = getTiles();
        for(int i=0; i < 41; i++){
            group.getChildren().add(recs[i]);
        }
        scene = new Scene(group, width, height);
    }

    private Rectangle[] getTiles() throws FileNotFoundException {
        //  GridPane gridPane = new GridPane();
        Rectangle[] recs = new Rectangle[41];
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
                    tile.setFill(Color.WHITE);
                    tile.setOnMouseClicked(event -> {
                        System.out.println(pos);
                        Dialog d = new Dialog();
                        d.setHeaderText("Select square type:");
                        ComboBox squareType = new ComboBox();
                        squareType.getItems().addAll(
                                "Property",
                                "Joker",
                                "Chance",
                                "Community Chest"
                        );
                        d.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
                        d.show();
                    });

                    recs[pos]= tile;
                    /*if ((row == 0) | (col == 0) | (row == 10) | (col == 10)) {
                        stp.getChildren().add(0, tile);
                        stp.getChildren().addAll(tile, text);
                        gridPane.add(stp, col, row);
                    }*/
                }
            }
        }

        /*gridPane.setLayoutX(10);
        gridPane.setLayoutY(300);*/
        Rectangle middleOne = new Rectangle();
        middleOne.setX(recs[0].getX() + recs[0].getWidth());
        middleOne.setY(recs[0].getY() + recs[0].getHeight());
        middleOne.setStroke(Color.BLACK);
        middleOne.setFill(Color.WHITE);
        middleOne.setHeight(540);
        middleOne.setWidth(540);

        /* image yok
        Image image = new Image(new FileInputStream("C:\\Users\\User\\Documents\\cs319-Monopoly-3D\\code\\src\\sample\\ask.jpeg"));

        //Setting the image view
        ImagePattern imagePattern = new ImagePattern(image);

        EventHandler<MouseEvent> eventHandler =
                new EventHandler<javafx.scene.input.MouseEvent>() {
                    @Override
                    public void handle(javafx.scene.input.MouseEvent e) {
                        middleOne.setFill(imagePattern);
                    }
                };

        //Adding the event handler
        middleOne.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, eventHandler);
        recs[40]= middleOne;
        return recs;
    }
    */


    /*
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
                    tile.setOnMouseClicked(event -> {
                        System.out.println(pos);
                    });

                    if ((row == 0) | (col == 0) | (row == 10) | (col == 10)) {
                        stp.getChildren().add(0, tile);
                        //stp.getChildren().addAll(tile, text);
                    }
                    boardPane.add(stp, col, row);
                }
            }
        }*/
        //middleOne.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, eventHandler);
        recs[40]= middleOne;
        return recs;
    }


    // public methods

    public Scene getScene() { return scene; }
}
