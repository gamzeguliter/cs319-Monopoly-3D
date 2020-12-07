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
                    tile.setY(row * 10);
                    tile.setStroke(Color.BLACK);
                    //tile.setFill(Color.ORCHID);

                    tile.setFill(Color.WHITE);
                    tile.setOnMouseClicked(event -> {
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
                                if(rb == chance) {
                                    editor.createNewSquare(CHANCEANDCOMMUNITYCHEST, pos);
                                }
                                else if (rb == joker){
                                    //todo create joker
                                }
                                else if (rb == property){
                                    //todo create property
                                }
                            }

                        });

                        d.getDialogPane().setContent(vbox);
                        Optional<ButtonType> result = d.showAndWait();

                        if (result.get() == ButtonType.NEXT & joker.isSelected()){
                            openJokerDialog();
                        }
                        else if (result.get() == ButtonType.NEXT & property.isSelected()){
                            openPropertyDialog();
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
        gridPane.setLayoutY(10);
        return gridPane;
    }

    //the property dialog
    private void openPropertyDialog() {
        String colorGroup = "Blue";
        Font font = new Font("Source Sans Pro", 20);
        Font fonth = new Font("Source Sans Pro", 30);
        Font fonts = new Font("Source Sans Pro", 10);
        Dialog d2 = new Dialog();
        d2.getDialogPane().setBackground(new Background(new BackgroundFill(Color.rgb(182, 216, 184), CornerRadii.EMPTY, Insets.EMPTY)));

        Text header = new Text("Property Square");
        header.setFont(fonth);
        HBox hb1 = new HBox();
        hb1.getChildren().addAll(header);

        Label label1 = new Label("Name:");
        label1.setFont(font);
        TextField name = new TextField();
        name.setFont(fonts);
        name.setBackground(new Background(new BackgroundFill(Color.rgb(203, 227, 199), CornerRadii.EMPTY, Insets.EMPTY)));
        name.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID, new CornerRadii(2),BorderStroke.MEDIUM)));
        HBox hb2 = new HBox();
        hb2.getChildren().addAll(label1, name);
        hb2.setSpacing(10);

        Label label2 = new Label("Price:");
        label2.setFont(font);
        TextField amount = new TextField();
        amount.setFont(fonts);
        amount.setBackground(new Background(new BackgroundFill(Color.rgb(203, 227, 199), CornerRadii.EMPTY, Insets.EMPTY)));
        amount.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID, new CornerRadii(2),BorderStroke.MEDIUM)));
        HBox hb4 = new HBox();
        hb4.getChildren().addAll(label2, amount);
        hb4.setSpacing(10);

        Label label3 = new Label("Color Group: " + colorGroup);
        label3.setFont(font);
        Button color = new Button("Select");
        color.setFont(font);

        //selecting the color group
        color.setOnMouseClicked(event -> {
            Dialog d3 = new Dialog();
            d3.getDialogPane().setBackground(new Background(new BackgroundFill(Color.rgb(182, 216, 184), CornerRadii.EMPTY, Insets.EMPTY)));

            Text header2 = new Text("Color Groups");
            header2.setFont(fonth);
            HBox hb3 = new HBox();
            hb3.getChildren().addAll(header2);

            Label label4 = new Label("New Color Group");
            label4.setFont(font);
            Button add = new Button("+");
            add.setFont(fonth);

            //adding a new color
            add.setOnMouseClicked(event2 -> {
                Dialog d4 = new Dialog();
                d4.getDialogPane().setBackground(new Background(new BackgroundFill(Color.rgb(182, 216, 184), CornerRadii.EMPTY, Insets.EMPTY)));

                Text header3 = new Text("New Color Group");
                header3.setFont(fonth);
                HBox hb7 = new HBox();
                hb7.getChildren().addAll(header3);

                Label label5 = new Label("Name:");
                label5.setFont(font);
                TextField name2 = new TextField();
                name2.setFont(fonts);
                name2.setBackground(new Background(new BackgroundFill(Color.rgb(203, 227, 199), CornerRadii.EMPTY, Insets.EMPTY)));
                name2.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID, new CornerRadii(2),BorderStroke.MEDIUM)));
                HBox hb8 = new HBox();
                hb8.getChildren().addAll(label5, name2);
                hb8.setSpacing(10);

                Label label6 = new Label("Color:");
                label6.setFont(font);
                ColorPicker colorPicker = new ColorPicker();
                colorPicker.setBackground(new Background(new BackgroundFill(Color.rgb(203, 227, 199), CornerRadii.EMPTY, Insets.EMPTY)));
                colorPicker.setStyle("-fx-font: 'Source Sans Pro';" + "-fx-font-family: 'Source Sans Pro';" + "-fx-font-size: 10;");
                HBox hb9 = new HBox();
                hb9.getChildren().addAll(label6, colorPicker);
                hb9.setSpacing(10);

                VBox vbox4 = new VBox();
                vbox4.getChildren().addAll(hb7, hb8, hb9);

                d4.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
                ((Button) d4.getDialogPane().lookupButton(ButtonType.CANCEL)).setFont(font);
                ((Button) d4.getDialogPane().lookupButton(ButtonType.OK)).setFont(font);

                d4.getDialogPane().setContent(vbox4);
                d4.show();
            });

            HBox hb6 = new HBox();
            hb6.getChildren().addAll(add, label4);
            hb6.setSpacing(10);

            VBox vbox3 = new VBox();
            vbox3.getChildren().addAll(hb3, hb6);

            d3.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
            ((Button) d3.getDialogPane().lookupButton(ButtonType.CANCEL)).setFont(font);
            ((Button) d3.getDialogPane().lookupButton(ButtonType.OK)).setFont(font);

            d3.getDialogPane().setContent(vbox3);
            d3.show();
        });

        HBox hb5 = new HBox();
        hb5.getChildren().addAll(label3, color);
        hb5.setSpacing(10);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(hb1, hb2, hb4,  hb5);

        d2.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
        ((Button) d2.getDialogPane().lookupButton(ButtonType.CANCEL)).setFont(font);
        ((Button) d2.getDialogPane().lookupButton(ButtonType.OK)).setFont(font);

        d2.getDialogPane().setContent(vbox);
        d2.show();
    }

    //opening joker edit window
    private void openJokerDialog() {
        Font font = new Font("Source Sans Pro", 20);
        Font fonth = new Font("Source Sans Pro", 30);
        Dialog d2 = new Dialog();
        d2.getDialogPane().setBackground(new Background(new BackgroundFill(Color.rgb(182, 216, 184), CornerRadii.EMPTY, Insets.EMPTY)));

        Text header = new Text("Joker Square");
        header.setFont(fonth);
        HBox hb1 = new HBox();
        hb1.getChildren().addAll(header);

        Label label1 = new Label("Name:");
        label1.setFont(font);
        TextField name = new TextField();
        HBox hb2 = new HBox();
        hb2.getChildren().addAll(label1, name);
        hb2.setSpacing(10);

        Label header2 = new Label("Movement:");
        header2.setFont(font);
        HBox hb3 = new HBox();
        hb3.getChildren().addAll(header2);

        //group of radio buttons
        final ToggleGroup group2 = new ToggleGroup();

        RadioButton move = new RadioButton("Move ... squares");
        move.setToggleGroup(group2);
        move.setFont(font);
        move.setSelected(true);

        RadioButton wait = new RadioButton("Wait ... turns");
        wait.setFont(font);
        wait.setToggleGroup(group2);

        RadioButton none = new RadioButton("No movement");
        none.setFont(font);
        none.setToggleGroup(group2);

        VBox vbox2 = new VBox(10);
        vbox2.setPadding(new Insets(10));
        vbox2.getChildren().addAll(move, wait, none);

        Label label2 = new Label("Amount:");
        label2.setFont(font);
        TextField amount = new TextField();
        HBox hb4 = new HBox();
        hb4.getChildren().addAll(label2, amount);
        hb4.setSpacing(10);

        Label label3 = new Label("Money:");
        label3.setFont(font);
        TextField money = new TextField();
        HBox hb5 = new HBox();
        hb5.getChildren().addAll(label3, money);
        hb5.setSpacing(10);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(hb1, hb2, hb3, vbox2, hb4,  hb5);

        d2.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
        ((Button) d2.getDialogPane().lookupButton(ButtonType.CANCEL)).setFont(font);
        ((Button) d2.getDialogPane().lookupButton(ButtonType.OK)).setFont(font);

        d2.getDialogPane().setContent(vbox);
        d2.show();
    }

public void changeTheSquare(Rectangle s){

}
    // public methods

    public Scene getScene() { return scene; }
}
