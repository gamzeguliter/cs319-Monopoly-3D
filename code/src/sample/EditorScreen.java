package sample;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

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
    public EditorScreen() {
        //editor = new Editor();
        boardPane = getTiles(); //CHANGE
        setScene();
    }

    // private methods
    private void setScene() {
        Group group = new Group();
        int width = 1366;
        int height = 768;

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
                    tile.setX(col * 10);
                    System.out.println(tile.getX());
                    tile.setY((row * 10) - 1000);
                    tile.setStroke(Color.BLACK);
                    tile.setFill(Color.WHITE);
                    tile.setOnMouseClicked(event -> {
                        System.out.println(pos);
                        Dialog d = new Dialog();
                        d.setContentText(event.getX() + " y- " + event.getY());
                        d.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
                        d.show();
                    });

                    if ((row == 0) | (col == 0) | (row == 10) | (col == 10)) {
                        stp.getChildren().add(0, tile);
                        //stp.getChildren().addAll(tile, text);
                        gridPane.add(stp, col, row);
                    }
                }
            }
        }
        gridPane.setLayoutX(10);
        gridPane.setLayoutY(10);
        return gridPane;
    }

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
                        Dialog d = new Dialog();
                        d.show();
                    });

                    if ((row == 0) | (col == 0) | (row == 10) | (col == 10)) {
                        stp.getChildren().add(0, tile);
                        //stp.getChildren().addAll(tile, text);
                    }
                    boardPane.add(stp, col, row);
                }
            }
        }
    }

     */
    // public methods

    public Scene getScene() { return scene; }
}
