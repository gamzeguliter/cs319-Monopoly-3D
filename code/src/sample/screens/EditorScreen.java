package sample.screens;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Pair;
import org.w3c.dom.css.Rect;
import sample.squares.ColorGroup;
import sample.Editor;
import sample.GameEngine;
import sample.Results;
import sample.squares.Property;
import sample.squares.Square;
import sample.squares.SquareType;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

public class EditorScreen{
    // properties
    private Scene scene;
    GridPane boardPane;
    Editor editor;
    Text[] playerTexts;
    Text turnText;
    Text diceText;
    GridPane recs;

    GameEngine gameEngine;
    int position;
    @FXML DialogPane jokerSquareEdit;
    @FXML DialogPane toggleSquareType;
    @FXML DialogPane propertySquareEdit;
    @FXML DialogPane selectColorGroup;
    @FXML DialogPane addColorGroup;

    Font font = Font.font("Source Sans Pro", 20);
    Parent editorScreen = FXMLLoader.load(getClass().getResource("EditorScreen.fxml"));
    DialogPane propertyEditDP = FXMLLoader.load(getClass().getResource("propertyEditScreen.fxml"));
    DialogPane addColorGroupDP = FXMLLoader.load(getClass().getResource("addColorGroup.fxml"));
    DialogPane selectColorGroupDP = FXMLLoader.load(getClass().getResource("selectColorGroup.fxml"));
    DialogPane jokerEditDP = FXMLLoader.load(getClass().getResource("jokerEditScreen.fxml"));
    DialogPane toggleSquareTypeDP = FXMLLoader.load(getClass().getResource("toggleSquareType.fxml"));

    // constructors
    public EditorScreen() throws IOException {
        editor = new Editor();
        position = 0;
        gameEngine = new GameEngine();
        setScene();
    }

    // private methods
    private void setScene() throws IOException {
        //recs = getTiles();
        scene = new Scene(editorScreen);
        setControls();

    }

    private void setControls() {
        Node[] squares = new Node[40];
        Square[] squares2 = editor.board.getSquares();
        for (int i = 0; i < 40; i++) {
            GridPane boardPane = (GridPane) editorScreen.getChildrenUnmodifiable().get(0);
            StackPane stackPane = (StackPane) boardPane.getChildren().get(i);
            squares[i] = stackPane.getChildren().get(0);
            fillColors(squares2,(Rectangle)squares[i],i); /// paint inside of the squres
        }

        for (int pos = 0; pos < 40; pos++) {
            int finalPosition = pos;
            squares[pos].setOnMouseClicked(event -> {
                position = finalPosition;

                Dialog squareTypeDialog = new Dialog();
                squareTypeDialog.setDialogPane(toggleSquareTypeDP);

                VBox vbox = (VBox) squareTypeDialog.getDialogPane().getContent();
                RadioButton property = (RadioButton) vbox.getChildren().get(0);
                RadioButton joker = (RadioButton) vbox.getChildren().get(1);
                RadioButton chance = (RadioButton) vbox.getChildren().get(2);
                RadioButton communityChest = (RadioButton) vbox.getChildren().get(3);

                ToggleGroup group = property.getToggleGroup();

                group.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
                {
                    public void changed(ObservableValue<? extends Toggle> ob,
                                        Toggle o, Toggle n)
                    {
                        ///  todo -> check the type control codes
                        RadioButton rb = (RadioButton)group.getSelectedToggle();

                        if(rb == chance && squares2[finalPosition].getType() != SquareType.CHANCEANDCOMMUNITYCHEST ) {
                            // removing property from its ColorGroup's arraylist
                            if( squares2[finalPosition].getType() == SquareType.PROPERTY )
                            {
                                ColorGroup temp = ((Property) squares2[finalPosition]).getColorGroup();
                                temp.removeProperty((Property)squares2[finalPosition]);
                            }
                            editor.createNewChestCommunity(finalPosition);
                        }


                    }
                });

                Optional<ButtonType> result = squareTypeDialog.showAndWait();

                if (result.get() == ButtonType.NEXT & joker.isSelected()){
                    if(  squares2[position].getType() != SquareType.JOKER){
                        if (squares2[position].getType() == SquareType.PROPERTY) {
                            ColorGroup temp = ((Property) squares2[position]).getColorGroup();
                            temp.removeProperty((Property) squares2[position]);
                        }
                        editor.createNewJoker(position, 0, 0, 0, "Joker");

                    }

                    openJokerDialog();
                }
                else if (result.get() == ButtonType.NEXT & property.isSelected()){
                    if(  squares2[position].getType() != SquareType.PROPERTY){
                        ColorGroup temp = new ColorGroup("color group"); //might be deleted
                        editor.createNewProperty(position,"ankara",temp,100,50,80);
                    }
                    openPropertyDialog();
                }
            });
        }
    }

    /*
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
                                ///  todo -> check the type control codes
                                RadioButton rb = (RadioButton)group.getSelectedToggle();
                                Square[] squares = editor.board.getSquares();

                                if(rb == chance && squares[position].getType() != SquareType.CHANCEANDCOMMUNITYCHEST ) {
                                    // removing property from its ColorGroup's arraylist
                                    if( squares[position].getType() == SquareType.PROPERTY )
                                    {
                                      ColorGroup temp = ((Property) squares[position]).getColorGroup();
                                      temp.removeProperty((Property)squares[position]);
                                    }

                                    editor.createNewChestCommunity(pos);
                                }
                                else if (rb == joker && squares[position].getType() != SquareType.JOKER ){
                                    // todo - > default values for now , can be changed later by the players
                                    // removing property from its ColorGroup's arraylist
                                    if( squares[position].getType() == SquareType.PROPERTY )
                                    {
                                        ColorGroup temp =  ((Property) squares[position]).getColorGroup();
                                        temp.removeProperty((Property)squares[position]);
                                    }

                                    editor.createNewJoker(pos,0,0,0,"Joker");
                               }

                                else if (rb == property && squares[position].getType() != SquareType.PROPERTY ){
                                    //todo ->  default values for now , can be changed later by the players
                                    ColorGroup temp = new ColorGroup("temp"); //might be deleted
                                    editor.createNewProperty(pos,"ankara",temp,100,50,80);
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

     */

    //TODO create methods for style
    // make color group dialog

    //the property dialog
    private void openPropertyDialog() {

        Dialog propertyEditDialog = new Dialog();
        propertyEditDialog.setDialogPane(propertyEditDP);

        VBox vBox = (VBox) propertyEditDialog.getDialogPane().getContent();

        HBox nameBox = (HBox) vBox.getChildren().get(0);
        HBox priceBox = (HBox) vBox.getChildren().get(1);
        HBox colorBox = (HBox) vBox.getChildren().get(2);

        TextField propertyName = (TextField) nameBox.getChildren().get(1);
        TextField propertyPrice = (TextField) priceBox.getChildren().get(1);
        Button color = (Button) colorBox.getChildren().get(1);


        //selecting the color group
        color.setOnMouseClicked(event -> {
            //adding a new color
            Dialog selectColorDialog = new Dialog();
            selectColorDialog.setDialogPane(selectColorGroupDP);
            VBox vbox = (VBox)selectColorDialog.getDialogPane().getContent();
            HBox hbox =(HBox) vbox.getChildren().get(1);
            Button add = (Button)hbox.getChildren().get(0);

            add.setOnMouseClicked(event2 -> {
                Dialog addColorGroupDialog = new Dialog();
                addColorGroupDialog.setDialogPane(addColorGroupDP);
                VBox vbox2 = (VBox)addColorGroupDialog.getDialogPane().getContent();
                HBox hbox2 =(HBox) vbox2.getChildren().get(0);
                TextField colorGroupName = (TextField)hbox2.getChildren().get(1);
                HBox hbox3 =(HBox) vbox2.getChildren().get(1);
                ColorPicker colorPicker = (ColorPicker)hbox3.getChildren().get(1);
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
                    editor.createColorGroupForProperty(c,colorGroupName.getText(),position );
                    System.out.println("New Color's "+colorPicker.getValue()+"");
                });

            });


            selectColorDialog.show();
        });



        propertyEditDialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                Object[] results = new Object[2];
                results[0]= propertyName.getText();
                results[1] = propertyPrice.getText();
                return results;
            }
            return null;
        });

        Optional<Object> result = propertyEditDialog.showAndWait();

        result.ifPresent(pair -> {
            System.out.println("name of the property=" + propertyName.getText() + ", amount of the price=" + propertyPrice.getText());

            // todo ->  processing user input : color group is left, checking the corner cases for the unchanged boxes
            editor.setBuyingPriceForProperty(Integer.parseInt(propertyPrice.getText()), position);
            editor.setNameForProperty(propertyName.getText() , position);
        });
    }

    //opening joker edit window
    private void openJokerDialog() {

        Dialog jokerEditDialog = new Dialog();
        jokerEditDialog.setDialogPane(jokerEditDP);
        /// getting user input
        VBox vBox = (VBox) jokerEditDialog.getDialogPane().getContent();

        HBox jokerBox = (HBox) vBox.getChildren().get(0);
        RadioButton move = (RadioButton) vBox.getChildren().get(2);
        RadioButton wait = (RadioButton) vBox.getChildren().get(3);
        RadioButton none = (RadioButton) vBox.getChildren().get(4);
        HBox actionBox = (HBox) vBox.getChildren().get(5);
        HBox moneyBox = (HBox) vBox.getChildren().get(6);

        TextField jokerSquareName = (TextField) jokerBox.getChildren().get(1);
        TextField actionAmount = (TextField) actionBox.getChildren().get(1);
        TextField money = (TextField) moneyBox.getChildren().get(1);

        jokerEditDialog.setResultConverter((button) -> {
            if (button == ButtonType.OK) {
                // todo pairin içine pair
                Object[] results = new Object[3];
                results[0]= jokerSquareName.getText();
                results[1] =Integer.parseInt(actionAmount.getText());
                results[2]= Integer.parseInt(money.getText());
                return results;
            }
            return null;
        });

        Optional<Object [] > optionalResult = jokerEditDialog.showAndWait();
        optionalResult.ifPresent(results -> {
            System.out.println(
                    results[0] + " " + results[1] + " " + results[2]); // todo -> ratio buttons are left out

            // todo ->  processing user input
            editor.setNameForJoker((String)results[0],position);
            editor.setMoneyForJoker((Integer)results[2],position);
            if(move.isSelected()){
                editor.setMovementForJoker((Integer)results[1],position);
            }
            if(wait.isSelected()){
                editor.setJailTimeForJoker((Integer)results[1],position);
            }


        });

        //todo delete root ?
        //jokerMainDialog.show();
    }

    public void fillColors(Square[] squares,Rectangle tile,int pos ){


        if (squares[pos].getType() == SquareType.PROPERTY) {

            tile.setFill(((Property)squares[pos]).getColorGroup().getColor());
        }
        else if(squares[pos].getType() == SquareType.JOKER){
            tile.setFill(Color.DARKGOLDENROD);
        }

        else if(gameEngine.getSquare(pos).getType() == SquareType.CHANCEANDCOMMUNITYCHEST){
            tile.setFill(Color.LIME);
        }
        else {
            tile.setFill(Color.BLUEVIOLET);
        }


    }

public void changeTheSquare(Rectangle s){

}
    // public methods

    public Scene getScene() { return scene; }






    public void update(){
        Node[] squares = new Node[40];
        Square[] squares2 = editor.board.getSquares();
        for (int i = 0; i < 40; i++) {
            GridPane boardPane = (GridPane) editorScreen.getChildrenUnmodifiable().get(0);
            StackPane stackPane = (StackPane) boardPane.getChildren().get(i);
            squares[i] = stackPane.getChildren().get(0);
            fillColors(squares2,(Rectangle)squares[i],i); /// paint inside of the squres
        }

        for (int pos = 0; pos < 40; pos++) {
            int finalPosition = pos;
            squares[pos].setOnMouseClicked(event -> {
                position = finalPosition;

                Dialog squareTypeDialog = new Dialog();
                squareTypeDialog.setDialogPane(toggleSquareTypeDP);

                VBox vbox = (VBox) squareTypeDialog.getDialogPane().getContent();
                RadioButton property = (RadioButton) vbox.getChildren().get(0);
                RadioButton joker = (RadioButton) vbox.getChildren().get(1);
                RadioButton chance = (RadioButton) vbox.getChildren().get(2);
                RadioButton communityChest = (RadioButton) vbox.getChildren().get(3);

                ToggleGroup group = property.getToggleGroup();

                group.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
                {
                    public void changed(ObservableValue<? extends Toggle> ob,
                                        Toggle o, Toggle n)
                    {
                        ///  todo -> check the type control codes
                        RadioButton rb = (RadioButton)group.getSelectedToggle();

                        if(rb == chance && squares2[finalPosition].getType() != SquareType.CHANCEANDCOMMUNITYCHEST ) {
                            // removing property from its ColorGroup's arraylist
                            if( squares2[finalPosition].getType() == SquareType.PROPERTY )
                            {
                                ColorGroup temp = ((Property) squares2[finalPosition]).getColorGroup();
                                temp.removeProperty((Property)squares2[finalPosition]);
                            }
                            editor.createNewChestCommunity(finalPosition);
                        }


                    }
                });

                Optional<ButtonType> result = squareTypeDialog.showAndWait();

                if (result.get() == ButtonType.NEXT & joker.isSelected()){
                    if(  squares2[position].getType() != SquareType.JOKER){
                        if (squares2[position].getType() == SquareType.PROPERTY) {
                            ColorGroup temp = ((Property) squares2[position]).getColorGroup();
                            temp.removeProperty((Property) squares2[position]);
                        }
                        editor.createNewJoker(position, 0, 0, 0, "Joker");

                    }

                    openJokerDialog();
                }
                else if (result.get() == ButtonType.NEXT & property.isSelected()){
                    if(  squares2[position].getType() != SquareType.PROPERTY){
                        ColorGroup temp = new ColorGroup("color group"); //might be deleted
                        editor.createNewProperty(position,"ankara",temp,100,50,80);
                    }
                    openPropertyDialog();
                }
            });
        }
    }










}

