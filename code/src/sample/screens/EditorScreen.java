package sample.screens;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Pair;
import sample.squares.ColorGroup;
import sample.Editor;
import sample.GameEngine;
import sample.Results;

import java.io.FileNotFoundException;
import java.util.Optional;

public class EditorScreen {
    // properties
    private Scene scene;
    GridPane boardPane;
    Editor editor;
    GridPane recs;
    GameEngine gameEngine;
    int position;

    Font font = Font.font("Source Sans Pro", 20);


    // constructors
    public EditorScreen() throws FileNotFoundException {
        editor = new Editor();
        position = 0;
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

                    tile.setFill(Color.WHITE);
                    tile.setOnMouseClicked(event -> {
                        position = pos;
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
                                    editor.createNewChestCommunity(pos);
                                }
                                else if (rb == joker){
                                    // todo - > default values for now , can be changed later by the players
                                    editor.createNewJoker(pos,0,0,0,"Joker");

                                }
                                else if (rb == property){
                                    //todo ->  default values for now , can be changed later by the players
                                    ColorGroup temp = new ColorGroup("temp"); //might be deleted
                                    editor.createNewProperty(pos,"ankara",temp,100,100,180,50,50);

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

    //TODO create methods for style
    // make color group dialog

    //the property dialog
    private void openPropertyDialog() {
        String colorGroup = "Blue";
        Font font = new Font("Source Sans Pro", 20);
        Font fonth = new Font("Source Sans Pro", 30);
        Font fonts = new Font("Source Sans Pro", 10);
        Dialog mainPropertyDialog = new Dialog();
        mainPropertyDialog.getDialogPane().setBackground(new Background(new BackgroundFill(Color.rgb(182, 216, 184), CornerRadii.EMPTY, Insets.EMPTY)));

        Text header = new Text("Property Square");
        header.setFont(fonth);
        HBox hb1 = new HBox();
        hb1.getChildren().addAll(header);

        Label label1 = new Label("Name:");
        label1.setFont(font);
        TextField propertyName = new TextField();
        propertyName.setFont(fonts);
        propertyName.setBackground(new Background(new BackgroundFill(Color.rgb(203, 227, 199), CornerRadii.EMPTY, Insets.EMPTY)));
        propertyName.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID, new CornerRadii(2),BorderStroke.MEDIUM)));
        HBox hb2 = new HBox();
        hb2.getChildren().addAll(label1, propertyName);
        hb2.setSpacing(10);

        Label label2 = new Label("Price:");
        label2.setFont(font);
        TextField propertyPrice = new TextField();
        propertyPrice.setFont(fonts);
        propertyPrice.setBackground(new Background(new BackgroundFill(Color.rgb(203, 227, 199), CornerRadii.EMPTY, Insets.EMPTY)));
        propertyPrice.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID, new CornerRadii(2),BorderStroke.MEDIUM)));
        HBox hb4 = new HBox();
        hb4.getChildren().addAll(label2, propertyPrice);
        hb4.setSpacing(10);

        Label label3 = new Label("Color Group: " + colorGroup);
        label3.setFont(font);
        Button color = new Button("Select");
        color.setFont(font);

        //selecting the color group
        color.setOnMouseClicked(event -> {
            Dialog colorGroupDialog = new Dialog();
            colorGroupDialog.getDialogPane().setBackground(new Background(new BackgroundFill(Color.rgb(182, 216, 184), CornerRadii.EMPTY, Insets.EMPTY)));

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
                Dialog addColorGroupDialog = new Dialog();
                addColorGroupDialog.getDialogPane().setBackground(new Background(new BackgroundFill(Color.rgb(182, 216, 184), CornerRadii.EMPTY, Insets.EMPTY)));

                Text header3 = new Text("New Color Group");
                header3.setFont(fonth);
                HBox hb7 = new HBox();
                hb7.getChildren().addAll(header3);

                Label label5 = new Label("Name:");
                label5.setFont(font);
                TextField colorGroupName = new TextField();
                colorGroupName.setFont(fonts);
                colorGroupName.setBackground(new Background(new BackgroundFill(Color.rgb(203, 227, 199), CornerRadii.EMPTY, Insets.EMPTY)));
                colorGroupName.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID, new CornerRadii(2),BorderStroke.MEDIUM)));
                HBox hb8 = new HBox();
                hb8.getChildren().addAll(label5, colorGroupName);
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


                addColorGroupDialog.getDialogPane().setContent(vbox4);
                addColorGroupDialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
                addColorGroupDialog.setResultConverter(button -> {
                    if (button == ButtonType.OK) {

                        return new Pair<>(colorGroupName.getText(), colorPicker.getValue());
                    }
                    return null;
                });

                ///  getting the user inputs for the name and the color of the color group
                Optional<Pair<String, String>> result = addColorGroupDialog.showAndWait();

                // TODO TODO TODO -> color group !!!!!!!!!!
                result.ifPresent(pair -> {
                    System.out.println("name of the color group=" + colorGroupName.getText());
                    Color c = colorPicker.getValue();
                    System.out.println("New Color's RGB = "+c.getRed()+" "+c.getGreen()+" "+c.getBlue());


                });

            });

            HBox hb6 = new HBox();
            hb6.getChildren().addAll(add, label4);
            hb6.setSpacing(10);

            VBox vbox3 = new VBox();
            vbox3.getChildren().addAll(hb3, hb6);

            colorGroupDialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
            ((Button) colorGroupDialog.getDialogPane().lookupButton(ButtonType.CANCEL)).setFont(font);
            ((Button) colorGroupDialog.getDialogPane().lookupButton(ButtonType.OK)).setFont(font);

            colorGroupDialog.getDialogPane().setContent(vbox3);
            colorGroupDialog.show();
        });

        HBox hb5 = new HBox();
        hb5.getChildren().addAll(label3, color);
        hb5.setSpacing(10);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(hb1, hb2, hb4,  hb5);

        mainPropertyDialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
        ((Button) mainPropertyDialog.getDialogPane().lookupButton(ButtonType.CANCEL)).setFont(font);
        ((Button) mainPropertyDialog.getDialogPane().lookupButton(ButtonType.OK)).setFont(font);

        // getting user input while editing

        mainPropertyDialog.getDialogPane().setContent(vbox);

        mainPropertyDialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                return new Pair<>(propertyName.getText(), propertyPrice.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = mainPropertyDialog.showAndWait();

        result.ifPresent(pair -> {
            System.out.println("name of the property=" + propertyName.getText() + ", amount of the price=" + propertyPrice.getText());

            // todo ->  processing user input : color group is left, checking the corner cases for the unchanged boxes
            editor.setBuyingPriceForProperty(Integer.parseInt(propertyPrice.getText()), position);
            editor.setNameForProperty(propertyName.getText() , position);

        });

    }

    //opening joker edit window
    private void openJokerDialog() {
        Font font = new Font("Source Sans Pro", 20);
        Font fonth = new Font("Source Sans Pro", 30);
        Dialog jokerMainDialog = new Dialog();
        jokerMainDialog.getDialogPane().setBackground(new Background(new BackgroundFill(Color.rgb(182, 216, 184), CornerRadii.EMPTY, Insets.EMPTY)));

        Text jokerHeader = new Text("Joker Square");
        jokerHeader.setFont(fonth);
        HBox hb1 = new HBox();
        hb1.getChildren().addAll(jokerHeader);

        Label label1 = new Label("Name:");
        label1.setFont(font);
        TextField jokerSquareName = new TextField();
        HBox hb2 = new HBox();
        hb2.getChildren().addAll(label1, jokerSquareName);
        hb2.setSpacing(10);

        Label header2 = new Label("Movement:");
        header2.setFont(font);
        HBox hb3 = new HBox();
        hb3.getChildren().addAll(header2);

        //group of radio buttons
        final ToggleGroup actions = new ToggleGroup();

        RadioButton move = new RadioButton("Move ... squares");
        move.setToggleGroup(actions);
        move.setFont(font);
        move.setSelected(true);

        RadioButton wait = new RadioButton("Wait ... turns");
        wait.setFont(font);
        wait.setToggleGroup(actions);

        RadioButton none = new RadioButton("No movement");
        none.setFont(font);
        none.setToggleGroup(actions);

        VBox vbox2 = new VBox(10);
        vbox2.setPadding(new Insets(10));
        vbox2.getChildren().addAll(move, wait, none);

        Label label2 = new Label("Amount:");
        label2.setFont(font);
        TextField actionAmount = new TextField();
        HBox hb4 = new HBox();
        hb4.getChildren().addAll(label2, actionAmount);
        hb4.setSpacing(10);

        Label label3 = new Label("Money:");
        label3.setFont(font);
        TextField money = new TextField();
        HBox hb5 = new HBox();
        hb5.getChildren().addAll(label3, money);
        hb5.setSpacing(10);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(hb1, hb2, hb3, vbox2, hb4,  hb5);

        jokerMainDialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
        ((Button) jokerMainDialog.getDialogPane().lookupButton(ButtonType.CANCEL)).setFont(font);
        ((Button) jokerMainDialog.getDialogPane().lookupButton(ButtonType.OK)).setFont(font);

        jokerMainDialog.getDialogPane().setContent(vbox);

        /// getting user input
        jokerMainDialog.setResultConverter((button) -> {
            if (button == ButtonType.OK) {
                return new Results(jokerSquareName.getText(), Integer.parseInt(actionAmount.getText()),
                        Integer.parseInt(money.getText()));
            }
            return null;
        });

        Optional<Results> optionalResult = jokerMainDialog.showAndWait();
        optionalResult.ifPresent((Results results) -> {
            System.out.println(
                    results.name + " " + results.amount + " " + results.money); // todo -> ratio buttons are left out

            // todo ->  processing user input
            editor.setNameForJoker(results.name,position);
            editor.setMoneyForJoker(results.money,position);
            if(move.isSelected()){
                editor.setMovementForJoker(results.amount,position);
            }
            if(wait.isSelected()){
                editor.setSuspentionForJoker(results.amount,position);
            }

        });
        //jokerMainDialog.show();
    }


public void changeTheSquare(Rectangle s){

}
    // public methods

    public Scene getScene() { return scene; }
}

