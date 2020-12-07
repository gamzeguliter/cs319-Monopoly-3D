package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.util.Optional;

import static sample.SquareType.CHANCEANDCOMMUNITYCHEST;

public class EditorScreen {
    // properties
    private Scene scene;
    GridPane boardPane;
     Editor  editor ;
    Text[] playerTexts;
    Text turnText;
    Text diceText;
GridPane recs;
    Button btnRollDice;
    Button btnEndTurn;
    Button btnBuy;

    GameEngine gameEngine;
    private boolean diceRolled;

    Font font = Font.font("Source Sans Pro", 20);


    // constructors
    public EditorScreen() throws FileNotFoundException {
        editor = new Editor();

        gameEngine = new GameEngine();
        setScene();
    }

    // private methods
    private void setScene() throws FileNotFoundException {
        Group group = new Group();
        int width = 1366;
        int height = 768;
         recs = getTiles();


        group.getChildren().add(recs);
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
                    //tile.setFill(Color.ORCHID);

                    tile.setFill(Color.WHITE);
                    tile.setOnMouseClicked(event -> {


                        System.out.println(pos);
                        Font font = new Font("Source Sans Pro", 20);
                        Dialog d = new Dialog();
                        d.getDialogPane().setBackground(new Background(new BackgroundFill(Color.rgb(182, 216, 184), CornerRadii.EMPTY, Insets.EMPTY)));
                        Text header = new Text("Select square type:");
                        header.setFont(font);

                        //group of radio buttons
                        final ToggleGroup group = new ToggleGroup();

                        RadioButton property = new RadioButton("Property");
                        property.setToggleGroup(group);
                        property.setFont(font);
                        property.setSelected(true);

                        RadioButton joker = new RadioButton("Joker");
                        joker.setFont(font);
                        joker.setToggleGroup(group);

                        RadioButton chance = new RadioButton("Chance");
                        chance.setFont(font);
                        chance.setToggleGroup(group);

                        RadioButton communityChest = new RadioButton("Community Chest");
                        communityChest.setFont(font);
                        communityChest.setToggleGroup(group);

                        VBox vbox = new VBox(10);
                        vbox.setPadding(new Insets(10));

                        vbox.getChildren().addAll(header, property, joker, chance, communityChest);

                        d.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.NEXT);
                        ((Button) d.getDialogPane().lookupButton(ButtonType.CANCEL)).setFont(font);
                        ((Button) d.getDialogPane().lookupButton(ButtonType.NEXT)).setFont(font);
                        ((Button) d.getDialogPane().lookupButton(ButtonType.NEXT)).setDefaultButton(false);


                        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
                        {
                            public void changed(ObservableValue<? extends Toggle> ob,
                                                Toggle o, Toggle n)
                            {
                                RadioButton rb = (RadioButton)group.getSelectedToggle();
                             if(rb == chance)
                                editor.createNewSquare(CHANCEANDCOMMUNITYCHEST,pos);
                            }

                        });





                        //todo next buttonu bir sonraki dialog pane e geçmeli
                        d.getDialogPane().lookupButton(ButtonType.NEXT).setOnMouseClicked(event2 -> {
                            System.out.println("clicked");

                            openSecondDialog();
                        });
                        d.getDialogPane().setContent(vbox);

                        Optional<ButtonType> result = d.showAndWait();

                        if (result.get() == ButtonType.NEXT){
                            System.out.println("here");

                            openSecondDialog();
                        }
                    });

                    if ((row == 0) | (col == 0) | (row == 10) | (col == 10)) {
                        //stp.getChildren().add(0, tile);
                        stp.getChildren().addAll(tile);
                        gridPane.add(stp, col, row);
                    }

                }




            }
        }
        gridPane.setLayoutX(10);
        gridPane.setLayoutY(300);
        return gridPane;
    }



    private void openSecondDialog() {
        System.out.println("yes");
        Font font = new Font("Source Sans Pro", 20);
        Dialog d2 = new Dialog();
        d2.getDialogPane().setBackground(new Background(new BackgroundFill(Color.rgb(182, 216, 184), CornerRadii.EMPTY, Insets.EMPTY)));
        Text header = new Text("Joker Square");
        header.setFont(font);

        //group of radio buttons
        final ToggleGroup group2 = new ToggleGroup();

        RadioButton property = new RadioButton("Property");
        property.setToggleGroup(group2);
        property.setFont(font);
        property.setSelected(true);

        RadioButton joker = new RadioButton("Joker");
        joker.setFont(font);
        joker.setToggleGroup(group2);

        RadioButton chance = new RadioButton("Chance");
        chance.setFont(font);
        chance.setToggleGroup(group2);

        RadioButton communityChest = new RadioButton("Community Chest");
        communityChest.setFont(font);
        communityChest.setToggleGroup(group2);

        VBox vbox2 = new VBox(10);
        vbox2.setPadding(new Insets(10));

        vbox2.getChildren().addAll(header, property, joker, chance, communityChest);

        d2.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.NEXT);
        ((Button) d2.getDialogPane().lookupButton(ButtonType.CANCEL)).setFont(font);
        ((Button) d2.getDialogPane().lookupButton(ButtonType.NEXT)).setFont(font);
        System.out.println(vbox2.getChildren());
        d2.getDialogPane().setContent(vbox2);
        d2.show();
    }

public void changeTheSquare(Rectangle s){

}
    // public methods

    public Scene getScene() { return scene; }
}
