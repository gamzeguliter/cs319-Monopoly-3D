package sample.screens;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Pair;
import sample.ScreenManager;
import sample.squares.*;
import sample.Editor;
import sample.GameEngine;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class EditorScreen extends Screen{
    // properties
    private Scene scene;
    GridPane boardPane;
    Editor editor;
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
    public EditorScreen(ScreenManager screenManager) throws IOException {
        super(screenManager);
        editor = new Editor();
        position = 0;

        gameEngine = new GameEngine();
        setScene();
    }

    // private methods
    private void setScene() {
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

        VBox v = (VBox)editorScreen.getChildrenUnmodifiable().get(1);

        HBox h = (HBox)v.getChildren().get(1);
        TextField mortgageRate =(TextField) h.getChildren().get(1);
        mortgageRate.setText(""+editor.board.getMortgageRate());

        HBox h2 = (HBox)v.getChildren().get(2);
        TextField currency =(TextField) h2.getChildren().get(1);
        currency.setText(editor.board.getCurrency());


        HBox h3 = (HBox)v.getChildren().get(3);
        TextField rentRate =(TextField) h3.getChildren().get(1);
        rentRate.setText(""+editor.board.getMortgageRate());

        Button cancel  = (Button) v.getChildren().get(9);
        Button save    = (Button) v.getChildren().get(8);
        cancel.setCancelButton(true);

        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if(!mortgageRate.getText().isEmpty())
                editor.board.setMortgageRate(Integer.parseInt(mortgageRate.getText()));

                if(!rentRate.getText().isEmpty())
                editor.board.setRentRate(Integer.parseInt(rentRate.getText()));

                if(!currency.getText().isEmpty())
                editor.board.setCurrency(currency.getText());
            }
        });

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

                Optional<ButtonType> result = squareTypeDialog.showAndWait();
                if (result.get() == ButtonType.NEXT & joker.isSelected()){
                    if(  squares2[position].getType() != SquareType.JOKER){
                        if (squares2[position].getType() == SquareType.PROPERTY) {
                            ColorGroup temp = ((Property) squares2[position]).getColorGroup();
                            temp.removeProperty((Property) squares2[position]);
                        }
                        editor.createNewJoker(position, 0, 0, 0, "Joker");
                    }
                    openJokerDialog(squares);
                }
                else if (result.get() == ButtonType.NEXT & property.isSelected()){
                    if(  squares2[position].getType() != SquareType.PROPERTY){
                        if(editor.board.getColorGroups().size() == 0){

                            ColorGroup temp = new ColorGroup("color group"); //might be deleted
                            editor.createNewProperty(position,"ankara",temp,100,50,80);
                        }
                        else{
                            int x = ((int) (Math.random() * editor.board.getColorGroups().size()));
                            ColorGroup cg = editor.board.getColorGroups().get(x);
                            editor.createNewProperty(position,"ankara",cg,100,50,80);
                        }}
                    openPropertyDialog(squares);
                }

                else if (( chance.isSelected() || communityChest.isSelected())&& result.get()== ButtonType.NEXT && squares2[finalPosition].getType() != SquareType.CHANCEANDCOMMUNITYCHEST ) {
                    // removing property from its ColorGroup's arraylist
                    if( squares2[finalPosition].getType() == SquareType.PROPERTY )
                    {
                        ColorGroup temp = ((Property) squares2[finalPosition]).getColorGroup();
                        temp.removeProperty((Property)squares2[finalPosition]);
                    }
                    editor.createNewChestCommunity(finalPosition);
                    System.out.println(editor.getSquare(finalPosition).getType());
                    update();
                }
            });
        }

        // @yiğit burdan ulaşabiliyorsun altta pawnImagex leri ve background u buttonlara bağlayabilirsin delete board olmayacaksa haber ver silerim
        GridPane boardPane = (GridPane) editorScreen.getChildrenUnmodifiable().get(0);
        StackPane stackPane = (StackPane) boardPane.getChildren().get(40);
        ImageView background = (ImageView) stackPane.getChildren().get(0);

        VBox vBox1 = (VBox) editorScreen.getChildrenUnmodifiable().get(1);
        HBox hBox1 = (HBox) vBox1.getChildren().get(5);

        VBox pawnBox1 = (VBox) hBox1.getChildren().get(0);
        VBox pawnBox2 = (VBox) hBox1.getChildren().get(1);
        VBox pawnBox3 = (VBox) hBox1.getChildren().get(2);
        VBox pawnBox4 = (VBox) hBox1.getChildren().get(3);

        ImageView pawnImage1 = (ImageView) pawnBox1.getChildren().get(0);
        Button upload1 = (Button) pawnBox1.getChildren().get(1);

        ImageView pawnImage2 = (ImageView) pawnBox2.getChildren().get(0);
        Button upload2 = (Button) pawnBox2.getChildren().get(1);

        ImageView pawnImage3 = (ImageView) pawnBox3.getChildren().get(0);
        Button upload3 = (Button) pawnBox3.getChildren().get(1);

        ImageView pawnImage4 = (ImageView) pawnBox4.getChildren().get(0);
        Button upload4= (Button) pawnBox4.getChildren().get(1);

        HBox hBox2 = (HBox) vBox1.getChildren().get(7);

        Button uploadBoard = (Button) hBox2.getChildren().get(0);
        Button deleteBoard = (Button) hBox2.getChildren().get(1);
    }
    //the property dialog
    private void openPropertyDialog(Node[]squares) {

        Dialog propertyEditDialog = new Dialog();
        propertyEditDialog.setDialogPane(propertyEditDP);

        VBox vBox = (VBox) propertyEditDialog.getDialogPane().getContent();

        HBox nameBox = (HBox) vBox.getChildren().get(0);
        HBox priceBox = (HBox) vBox.getChildren().get(1);
        HBox colorBox = (HBox) vBox.getChildren().get(2);

        TextField propertyName = (TextField) nameBox.getChildren().get(1);
        TextField propertyPrice = (TextField) priceBox.getChildren().get(1);
        Button color = (Button) colorBox.getChildren().get(1);
        propertyName.setText(((Property)editor.getProperty(position)).getName());
        propertyPrice.setText(((Property)editor.getProperty(position)).getBuyingPrice()+"");


        //selecting the color group
        color.setOnMouseClicked(event -> {
            //adding a new color
            Dialog selectColorDialog = new Dialog();
            selectColorDialog.setDialogPane(selectColorGroupDP);
            VBox vbox = (VBox)selectColorDialog.getDialogPane().getContent();
            HBox hbox =(HBox) vbox.getChildren().get(1);

            Button add = (Button)hbox.getChildren().get(0);
            ComboBox combo_box =(ComboBox)vbox.getChildren().get(0);
            ArrayList<String> choices = new ArrayList<>();



            ArrayList<ColorGroup> tempColorGroup = editor.board.getColorGroups();
            for(int i =0; i < editor.board.getColorGroups().size(); i++){
                choices.add(tempColorGroup.get(i).getGroupName());
                System.out.println(tempColorGroup.get(i).getGroupName());
            }
          combo_box.setItems(FXCollections.observableArrayList(choices));

            // Create action event
            EventHandler<ActionEvent> event1 =
                    new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent e)
                        {
                            ColorGroup cg = editor.getColorGroup((String)combo_box.getValue());
                            editor.changeColorGroupForProperty(cg,position);
                            System.out.println(((Property) editor.getSquare(position)).getColorGroup());
                            System.out.println("hi" + combo_box.getValue());
                        }
                    };

            // Set on action
            combo_box.setOnAction(event1);

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
                result.ifPresent(pair -> {
                    for(int i = 0; i < editor.board.getColorGroups().size(); i++){
                        System.out.println(editor.board.getColorGroups().get(i));
                    }
                    System.out.println("name of the color group=" + colorGroupName.getText());
                    Color c = colorPicker.getValue();
                    editor.createColorGroupForProperty(c,colorGroupName.getText(),position );
                    System.out.println("New Color's "+colorPicker.getValue()+"");
                    for(int i = 0; i < editor.board.getColorGroups().size(); i++){
                        System.out.println(editor.board.getColorGroups().get(i));
                    }

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


        update();
    }

    //opening joker edit window
    private void openJokerDialog(Node[] squares) {
        Dialog jokerEditDialog = new Dialog();
        jokerEditDialog.setDialogPane(jokerEditDP);
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

        jokerSquareName.setText(((Joker)editor.getSquare(position)).getName());
        money.setText(((Joker)editor.getSquare(position)).getMoney()+"");
        actionAmount.setText(((Joker)editor.getSquare(position)).getMovement()+"");

         Button btOk = (Button) jokerEditDialog.getDialogPane().lookupButton(ButtonType.OK);
        btOk.addEventFilter(
                ActionEvent.ACTION,
                event -> {

               if(Integer.parseInt(actionAmount.getText()) > 5 || Integer.parseInt(actionAmount.getText()) < 0 ||Integer.parseInt(money.getText()) > 100
                               || Integer.parseInt(money.getText()) < -100  ){
                           if(wait.isSelected()){
                           Alert alert = new Alert(Alert.AlertType.ERROR);
                           alert.setTitle("Error in creating Joker");
                           alert.setHeaderText(null);
                           alert.setContentText("Oops,your values are too high or low. Please try again.");
                           alert.getDialogPane().setStyle(
                                   " -fx-background-color: rgb(182, 216, 184); -fx-font: 'Source Sans Pro'; -fx-font-family: 'Source Sans Pro'; -fx-font-size: 13;"
                           );
                          // check.set(true);
                           alert.showAndWait();
                           event.consume();

                       }// end if
                   }

                if(Integer.parseInt(actionAmount.getText()) > 5 || Integer.parseInt(actionAmount.getText()) < -5  ||Integer.parseInt(money.getText()) > 100
                                || Integer.parseInt(money.getText()) < -100  ){

                            if (move.isSelected()){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error in creating Joker");
                            alert.setHeaderText(null);
                            alert.setContentText("Oops,your values are too high or low. Please try again.");
                            alert.getDialogPane().setStyle(
                                    " -fx-background-color: rgb(182, 216, 184); -fx-font: 'Source Sans Pro'; -fx-font-family: 'Source Sans Pro'; -fx-font-size: 13;"
                            );
                           // check.set(true);
                            alert.showAndWait();
                            event.consume();
                        }// end if

                    }

                }
        );


        jokerEditDialog.setResultConverter((button) -> {
            if (button == ButtonType.OK) {
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
                        results[0] + " " + results[1] + " " + results[2]);

                editor.setNameForJoker((String)results[0],position);
                editor.setMoneyForJoker((Integer)results[2],position);
                // System.out.println("name" +   ((Joker) editor.getSquare(position)).getName());
                //  System.out.println("money"+ ((Joker) editor.getSquare(position)).getMoney());

                if(move.isSelected()){
                    editor.setMovementForJoker((Integer)results[1],position);
                    // System.out.println("movement" + ((Joker) editor.getSquare(position)).getMovement());
                }
                if(wait.isSelected()){
                    editor.setJailTimeForJoker((Integer)results[1],position);
                    System.out.println("waiting time" + ((Joker) editor.getSquare(position)).getSuspendedTourNo());
                }
                else{
                    System.out.println("movement" + ((Joker) editor.getSquare(position)).getMovement());
                    System.out.println("waiting time" + ((Joker) editor.getSquare(position)).getSuspendedTourNo());

                }

            });



        update();
    }

    public void fillColors(Square[] squares,Rectangle tile,int pos ){


        if (squares[pos].getType() == SquareType.PROPERTY) {

            tile.setFill(((Property)squares[pos]).getColorGroup().getColor());
        }
        else if(squares[pos].getType() == SquareType.JOKER){
            tile.setFill(Color.DARKGOLDENROD);
        }

        else if(editor.getSquare(pos).getType() == SquareType.CHANCEANDCOMMUNITYCHEST){
            tile.setFill(Color.LIME);
        }
        else {
            tile.setFill(Color.BLUEVIOLET);
        }

    }

public Scene getScene() { return scene; }
public void update( ){
        Node [] squares = new Node[40];
        Square[] squares2 = editor.board.getSquares();
        for (int i = 0; i < 40; i++) {
            GridPane boardPane = (GridPane) editorScreen.getChildrenUnmodifiable().get(0);
            StackPane stackPane = (StackPane) boardPane.getChildren().get(i);
            squares[i] = stackPane.getChildren().get(0);
            fillColors(squares2,(Rectangle)squares[i],i); /// paint inside of the squares
        }

    //// This is the right half of the editor screen
    VBox v = (VBox)editorScreen.getChildrenUnmodifiable().get(1);


    HBox h = (HBox)v.getChildren().get(1);
    TextField mortgageRate =(TextField) h.getChildren().get(1);


    HBox h2 = (HBox)v.getChildren().get(2);
    TextField currency =(TextField) h2.getChildren().get(1);


    HBox h3 = (HBox)v.getChildren().get(3);
    TextField rentRate =(TextField) h3.getChildren().get(1);


    Button cancel  = (Button) v.getChildren().get(5);
    Button save    = (Button) v.getChildren().get(4);
    cancel.setCancelButton(true);


    save.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {

            if(!mortgageRate.getText().isEmpty())
                editor.board.setMortgageRate(Integer.parseInt(mortgageRate.getText()));

            if(!rentRate.getText().isEmpty())
                editor.board.setRentRate(Integer.parseInt(rentRate.getText()));

            if(!currency.getText().isEmpty())
                editor.board.setCurrency(currency.getText());

        }
    });
    /// end of the right half

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


                Optional<ButtonType> result = squareTypeDialog.showAndWait();

                if (result.get() == ButtonType.NEXT & joker.isSelected()){
                    if(  squares2[position].getType() != SquareType.JOKER){
                        if (squares2[position].getType() == SquareType.PROPERTY) {
                            ColorGroup temp = ((Property) squares2[position]).getColorGroup();
                            temp.removeProperty((Property) squares2[position]);
                        }

                        editor.createNewJoker(position, 0, 0, 0, "Joker");

                    }

                    openJokerDialog(squares);
                }
                else if (result.get() == ButtonType.NEXT & property.isSelected()){
                    if(  squares2[position].getType() != SquareType.PROPERTY){
                    if(editor.board.getColorGroups().size() == 0){

                        ColorGroup temp = new ColorGroup("color group"); //might be deleted
                        editor.createNewProperty(position,"ankara",temp,100,50,80);
                    }
                    else{
                        int x = ((int) (Math.random() * editor.board.getColorGroups().size()));
                        ColorGroup cg = editor.board.getColorGroups().get(x);
                        editor.createNewProperty(position,"ankara",cg,100,50,80);
                    }}
                    openPropertyDialog(squares);
                }

                else if (( chance.isSelected() || communityChest.isSelected())&& result.get()== ButtonType.NEXT && squares2[finalPosition].getType() != SquareType.CHANCEANDCOMMUNITYCHEST ) {
                    // removing property from its ColorGroup's arraylist
                    if( squares2[finalPosition].getType() == SquareType.PROPERTY )
                    {
                        ColorGroup temp = ((Property) squares2[finalPosition]).getColorGroup();
                        temp.removeProperty((Property)squares2[finalPosition]);
                    }
                    editor.createNewChestCommunity(finalPosition);
                 // System.out.println(editor.getSquare(finalPosition).getType());
                    update();
                }
            });
        }

    }


}

