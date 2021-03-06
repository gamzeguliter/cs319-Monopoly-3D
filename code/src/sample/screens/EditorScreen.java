package sample.screens;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.scene.text.Text;
import javafx.util.Pair;
import sample.entities.Board;
import sample.ScreenManager;
import sample.FileManager;
import sample.squares.*;
import sample.EditorManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static javafx.scene.paint.Color.rgb;

public class EditorScreen extends Screen {
    // properties
    private Scene scene;
    GridPane boardPane;
    EditorManager editorManager;
    int position;
    @FXML
    DialogPane jokerSquareEdit;
    @FXML
    DialogPane toggleSquareType;
    @FXML
    DialogPane propertySquareEdit;
    @FXML
    DialogPane selectColorGroup;
    @FXML
    DialogPane addColorGroup;

    ArrayList<Image> playerIcons;
    Image boardIcon;

    Font font = Font.font("Source Sans Pro", 20);
    Parent editorScreen = FXMLLoader.load(getClass().getResource("../layouts/editorScreen.fxml"));
    DialogPane propertyEditDP = FXMLLoader.load(getClass().getResource("../layouts/propertyEditScreen.fxml"));
    DialogPane addColorGroupDP = FXMLLoader.load(getClass().getResource("../layouts/addColorGroup.fxml"));
    DialogPane selectColorGroupDP = FXMLLoader.load(getClass().getResource("../layouts/selectColorGroup.fxml"));
    DialogPane jokerEditDP = FXMLLoader.load(getClass().getResource("../layouts/jokerEditScreen.fxml"));
    DialogPane toggleSquareTypeDP = FXMLLoader.load(getClass().getResource("../layouts/toggleSquareType.fxml"));

    // constructors
    public EditorScreen(ScreenManager screenManager) throws IOException {
        super(screenManager);
        EditorScreen.this.editorManager = new EditorManager();
        position = 0;
        setScene();
    }

    public EditorScreen(ScreenManager screenManager, Board board) throws IOException {
        super(screenManager);
        EditorScreen.this.editorManager = new EditorManager(board);
        position = 0;

        playerIcons = FileManager.getPlayerIcons(board.getName(), 100, 100);
        boardIcon = FileManager.getImage("/boards/" + board.getName() + "/board_icon.png", 575, 575);
        if (boardIcon == null) {
            boardIcon = FileManager.generateWhiteImage(575, 575);
        }
        setScene();
    }

    private void setScene() {
        //recs = getTiles();
        scene = new Scene(EditorScreen.this.editorScreen);
        setControls();

    }

    //setting edit controls
    private void setControls() {
        Node[] squares = new Node[40];
        Square[] squares2 = EditorScreen.this.editorManager.board.getSquares();

        for (int i = 0; i < 40; i++) {
            GridPane boardPane = (GridPane) EditorScreen.this.editorScreen.getChildrenUnmodifiable().get(0);
            String picture = "";
            String name = "";

            //property square
            if (EditorScreen.this.editorManager.getSquare(i).getType() == SquareType.PROPERTY)
                name = ((Property) EditorScreen.this.editorManager.getSquare(i)).getName();

            //joker square
            if (EditorScreen.this.editorManager.getSquare(i).getType() == SquareType.JOKER)
                name = ((Joker) EditorScreen.this.editorManager.getSquare(i)).getName();

            Text text = new Text(name);
            Font font2 = Font.font("Source Sans Pro", 10);
            text.setFont(font2); //size of the player texts
            StackPane stackPane = (StackPane) boardPane.getChildren().get(i);
            stackPane.getChildren().add(text);

            //start square
            if (EditorScreen.this.editorManager.getSquare(i).getType() == SquareType.START ) {
                Image image = FileManager.getImage("src/sample/icons/go.png", 90, 90);
                ImageView imageView = new ImageView(image);
                stackPane.getChildren().add(imageView);
            }

            //chance and community chest square
            if (EditorScreen.this.editorManager.getSquare(i).getType() == SquareType.CHANCEANDCOMMUNITYCHEST ) {
                if (((ChanceAndCommunityChest) EditorScreen.this.editorManager.getSquare(i)).isChance()) {
                    picture += "chance";
                } else {
                    picture += "chest";
                }
                Image image = FileManager.getImage("src/sample/icons/" + picture + ".png", 50, 50);
                ImageView imageView = new ImageView(image);
                stackPane.getChildren().add(imageView);
            }

            squares[i] = stackPane.getChildren().get(0);
            fillColors(squares2, (Rectangle) squares[i], i); /// paint inside of the squares
        }

        VBox v = (VBox) EditorScreen.this.editorScreen.getChildrenUnmodifiable().get(1);

        TextField boardName = (TextField) v.getChildren().get(0);
        boardName.setText(EditorScreen.this.editorManager.board.getName());
        HBox h = (HBox) v.getChildren().get(1);
        TextField mortgageRate = (TextField) h.getChildren().get(1);
        mortgageRate.setText("" + EditorScreen.this.editorManager.board.getMortgageRate());

        HBox h2 = (HBox) v.getChildren().get(2);
        TextField currency = (TextField) h2.getChildren().get(1);
        currency.setText(EditorScreen.this.editorManager.board.getCurrency());

        HBox h3 = (HBox) v.getChildren().get(3);
        TextField rentRate = (TextField) h3.getChildren().get(1);
        rentRate.setText("" + EditorScreen.this.editorManager.board.getRentRate());

        HBox buttons = (HBox) v.getChildren().get(8);

        Button cancel = (Button) buttons.getChildren().get(1);
        Button save = (Button) buttons.getChildren().get(0);

        //cancel button action listener
        cancel.setCancelButton(true);
        cancel.setOnAction(event -> {
            try {
                screenManager.changeScreen(new MainMenuScreen(screenManager));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //save button action listener / event filter
        save.addEventFilter( ActionEvent.ACTION,
                event -> {
                    if(boardName.getText().equalsIgnoreCase("Default board")) {
                        System.out.println("CAN'T SAVEEEE");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error saving board");
                        alert.setHeaderText(null);
                        alert.setContentText("You can't save the board with the name \"Default board\"");
                        alert.getDialogPane().setStyle(
                                " -fx-background-color: rgb(182, 216, 184); -fx-font: 'Source Sans Pro'; -fx-font-family: 'Source Sans Pro'; -fx-font-size: 13;"
                        );
                        alert.showAndWait();
                        event.consume();
                    }
                });

        save.setOnAction(event -> {

            if (!mortgageRate.getText().isEmpty())
                EditorScreen.this.editorManager.board.setMortgageRate(Integer.parseInt(mortgageRate.getText()));

            if (!rentRate.getText().isEmpty())
                EditorScreen.this.editorManager.board.setRentRate(Integer.parseInt(rentRate.getText()));

            if (!currency.getText().isEmpty())
                EditorScreen.this.editorManager.board.setCurrency(currency.getText());

            if (!boardName.getText().isEmpty())
                EditorScreen.this.editorManager.board.setName(boardName.getText());

            System.out.println("before write board");
            FileManager.writeBoardToFolder(editorManager.board);
            System.out.println("after write board");
            FileManager.saveIconsOnBoard(playerIcons, boardIcon, editorManager.board.getName());

            try {
                screenManager.changeScreen(new MainMenuScreen(screenManager));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //make the tiles clickable
        for (int pos = 1; pos < 40; pos++) {
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

                if (EditorScreen.this.editorManager.getSquare(position).getType() == SquareType.CHANCEANDCOMMUNITYCHEST) {
                    if (((ChanceAndCommunityChest) EditorScreen.this.editorManager.getSquare(position)).isChance() == true)
                        chance.setSelected(true);
                    if (((ChanceAndCommunityChest) EditorScreen.this.editorManager.getSquare(position)).isChance() == false)
                        communityChest.setSelected(true);
                }
                if (EditorScreen.this.editorManager.getSquare(position).getType() == SquareType.PROPERTY) {
                    property.setSelected(true);
                }
                if (EditorScreen.this.editorManager.getSquare(position).getType() == SquareType.JOKER) {
                    joker.setSelected(true);
                }
                Optional<ButtonType> result = squareTypeDialog.showAndWait();
                if (result.get() == ButtonType.NEXT & joker.isSelected()) {
                    if (squares2[position].getType() != SquareType.JOKER) {
                        if (squares2[position].getType() == SquareType.PROPERTY) {
                            ColorGroup temp = ((Property) squares2[position]).getColorGroup();
                            temp.removeProperty((Property) squares2[position]);
                        }
                        EditorScreen.this.editorManager.createNewJoker(position, 0, 0, 0, "Joker");

                    }
                    openJokerDialog(squares);

                } else if (result.get() == ButtonType.NEXT & property.isSelected()) {
                    if (squares2[position].getType() != SquareType.PROPERTY) {
                        if (EditorScreen.this.editorManager.board.getColorGroups().size() == 0) {

                            ColorGroup temp = new ColorGroup("color group"); //might be deleted
                            EditorScreen.this.editorManager.createNewProperty(position, "ankara", temp, 100, 50, 80);
                        } else {
                            int x = ((int) (Math.random() * EditorScreen.this.editorManager.board.getColorGroups().size()));
                            ColorGroup cg = EditorScreen.this.editorManager.board.getColorGroups().get(x);
                            EditorScreen.this.editorManager.createNewProperty(position, "ankara", cg, 100, 50, 80);
                        }
                    }
                    openPropertyDialog(squares);
                } else if ((communityChest.isSelected()) && result.get() == ButtonType.NEXT && squares2[finalPosition].getType() != SquareType.CHANCEANDCOMMUNITYCHEST) {
                    // removing property from its ColorGroup's arraylist
                    if (squares2[finalPosition].getType() == SquareType.PROPERTY) {
                        ColorGroup temp = ((Property) squares2[finalPosition]).getColorGroup();
                        temp.removeProperty((Property) squares2[finalPosition]);
                    }
                    EditorScreen.this.editorManager.createNewChestCommunity(finalPosition, false);
                    System.out.println(EditorScreen.this.editorManager.getSquare(finalPosition).getType());
                    update();
                } else if ((chance.isSelected()) && result.get() == ButtonType.NEXT && squares2[finalPosition].getType() != SquareType.CHANCEANDCOMMUNITYCHEST) {
                    // removing property from its ColorGroup's arraylist
                    if (squares2[finalPosition].getType() == SquareType.PROPERTY) {
                        ColorGroup temp = ((Property) squares2[finalPosition]).getColorGroup();
                        temp.removeProperty((Property) squares2[finalPosition]);
                    }
                    EditorScreen.this.editorManager.createNewChestCommunity(finalPosition, true);
                    System.out.println(EditorScreen.this.editorManager.getSquare(finalPosition).getType());
                    update();
                } else if ((chance.isSelected()) && result.get() == ButtonType.NEXT && squares2[finalPosition].getType() == SquareType.CHANCEANDCOMMUNITYCHEST
                        && ((ChanceAndCommunityChest) squares2[finalPosition]).isChance() == false) {
                    // removing property from its ColorGroup's arraylist
                    EditorScreen.this.editorManager.createNewChestCommunity(finalPosition, true);
                    System.out.println(EditorScreen.this.editorManager.getSquare(finalPosition).getType());
                    update();
                } else if ((communityChest.isSelected()) && result.get() == ButtonType.NEXT && squares2[finalPosition].getType() == SquareType.CHANCEANDCOMMUNITYCHEST
                        && ((ChanceAndCommunityChest) squares2[finalPosition]).isChance() == true) {
                    // removing property from its ColorGroup's arraylist
                    EditorScreen.this.editorManager.createNewChestCommunity(finalPosition, false);
                    System.out.println(EditorScreen.this.editorManager.getSquare(finalPosition).getType());
                    update();
                }
            });
        }

        GridPane boardPane = (GridPane) EditorScreen.this.editorScreen.getChildrenUnmodifiable().get(0);
        StackPane stackPane = (StackPane) boardPane.getChildren().get(40);
        ImageView background = (ImageView) stackPane.getChildren().get(0);

        VBox vBox1 = (VBox) EditorScreen.this.editorScreen.getChildrenUnmodifiable().get(1);
        HBox hBox1 = (HBox) vBox1.getChildren().get(5);

        VBox pawnBox1 = (VBox) hBox1.getChildren().get(0);
        VBox pawnBox2 = (VBox) hBox1.getChildren().get(1);
        VBox pawnBox3 = (VBox) hBox1.getChildren().get(2);
        VBox pawnBox4 = (VBox) hBox1.getChildren().get(3);

        //pawn image uploads
        ImageView pawnImage1 = (ImageView) pawnBox1.getChildren().get(0);
        if (playerIcons.size() > 0)
            pawnImage1.setImage(playerIcons.get(0));
        Button upload1 = (Button) pawnBox1.getChildren().get(1);
        upload1.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(scene.getWindow());
            if (file != null) {
                String path = file.toURI().toASCIIString();
                Image image = new Image(path);
                playerIcons.set(0, image);
                pawnImage1.setImage(image);
            }
        });

        ImageView pawnImage2 = (ImageView) pawnBox2.getChildren().get(0);
        if (playerIcons.size() > 1)
            pawnImage2.setImage(playerIcons.get(1));
        Button upload2 = (Button) pawnBox2.getChildren().get(1);
        upload2.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(scene.getWindow());
            if (file != null) {
                String path = file.toURI().toASCIIString();
                Image image = new Image(path);
                playerIcons.set(1, image);
                pawnImage2.setImage(image);
            }
        });

        ImageView pawnImage3 = (ImageView) pawnBox3.getChildren().get(0);
        if (playerIcons.size() > 2)
            pawnImage3.setImage(playerIcons.get(2));
        Button upload3 = (Button) pawnBox3.getChildren().get(1);
        upload3.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(scene.getWindow());
            if (file != null) {
                String path = file.toURI().toASCIIString();
                Image image = new Image(path);
                playerIcons.set(2, image);
                pawnImage3.setImage(image);
            }
        });

        ImageView pawnImage4 = (ImageView) pawnBox4.getChildren().get(0);
        if (playerIcons.size() > 3)
            pawnImage4.setImage(playerIcons.get(3));
        Button upload4 = (Button) pawnBox4.getChildren().get(1);
        upload4.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(scene.getWindow());
            if (file != null) {
                String path = file.toURI().toASCIIString();
                Image image = new Image(path);
                playerIcons.set(3, image);
                pawnImage4.setImage(image);
            }
        });

        HBox hBox2 = (HBox) vBox1.getChildren().get(7);

        //board picture uploads
        Button uploadBoard = (Button) hBox2.getChildren().get(0);
        if (boardIcon != null)
            background.setImage(boardIcon);
        uploadBoard.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(scene.getWindow());
            if (file != null) {
                String path = file.toURI().toASCIIString();
                Image image = new Image(path);
                boardIcon = image;
                background.setImage(image);
            }
        });
    }

    //the property dialog
    private void openPropertyDialog(Node[] squares) {

        Dialog propertyEditDialog = new Dialog();
        propertyEditDialog.setDialogPane(propertyEditDP);

        VBox vBox = (VBox) propertyEditDialog.getDialogPane().getContent();

        HBox nameBox = (HBox) vBox.getChildren().get(0);
        HBox priceBox = (HBox) vBox.getChildren().get(1);
        HBox colorBox = (HBox) vBox.getChildren().get(2);

        TextField propertyName = (TextField) nameBox.getChildren().get(1);
        TextField propertyPrice = (TextField) priceBox.getChildren().get(1);
        Button color = (Button) colorBox.getChildren().get(1);
        propertyName.setText(((Property) EditorScreen.this.editorManager.getProperty(position)).getName());
        propertyPrice.setText(((Property) EditorScreen.this.editorManager.getProperty(position)).getBuyingPrice() + "");

        //selecting the color group
        color.setOnMouseClicked(event -> {
            //adding a new color
            Dialog selectColorDialog = new Dialog();
            selectColorDialog.setDialogPane(selectColorGroupDP);
            VBox vbox = (VBox) selectColorDialog.getDialogPane().getContent();
            HBox hbox = (HBox) vbox.getChildren().get(1);

            Button add = (Button) hbox.getChildren().get(0);
            ComboBox combo_box = (ComboBox) vbox.getChildren().get(0);
            ArrayList<String> choices = new ArrayList<>();

            ArrayList<ColorGroup> tempColorGroup = EditorScreen.this.editorManager.board.getColorGroups();
            for(int i = 0; i < EditorScreen.this.editorManager.board.getColorGroups().size(); i++){
                choices.add(tempColorGroup.get(i).getGroupName());
                System.out.println(tempColorGroup.get(i).getGroupName());
            }
          combo_box.setItems(FXCollections.observableArrayList(choices));

            // Create action event
            EventHandler<ActionEvent> event1 =
                    new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent e)
                        {
                            ColorGroup cg = EditorScreen.this.editorManager.getColorGroup((String)combo_box.getValue());
                            EditorScreen.this.editorManager.changeColorGroupForProperty(cg,position);
                            System.out.println(((Property) EditorScreen.this.editorManager.getSquare(position)).getColorGroup());
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
                    for(int i = 0; i < EditorScreen.this.editorManager.board.getColorGroups().size(); i++){
                        System.out.println(EditorScreen.this.editorManager.board.getColorGroups().get(i));
                    }
                    System.out.println("name of the color group=" + colorGroupName.getText());
                    Color c = colorPicker.getValue();
                    EditorScreen.this.editorManager.createColorGroupForProperty(c,colorGroupName.getText(),position );
                    System.out.println("New Color's "+colorPicker.getValue()+"");
                    for(int i = 0; i < EditorScreen.this.editorManager.board.getColorGroups().size(); i++){
                        System.out.println(EditorScreen.this.editorManager.board.getColorGroups().get(i));
                    }

                });
                // can be deleted

                ArrayList<ColorGroup> tempColorGroup2 = EditorScreen.this.editorManager.board.getColorGroups();

                for(int i = 0; i < EditorScreen.this.editorManager.board.getColorGroups().size(); i++){
                    choices.add(tempColorGroup2.get(i).getGroupName());
                    System.out.println(tempColorGroup2.get(i).getGroupName());
                }
               combo_box.setItems(FXCollections.observableArrayList(choices));
                /// can be deleted

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

            EditorScreen.this.editorManager.setBuyingPriceForProperty(Integer.parseInt(propertyPrice.getText()), position);
            EditorScreen.this.editorManager.setNameForProperty(propertyName.getText() , position);
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

        jokerSquareName.setText(((Joker) EditorScreen.this.editorManager.getSquare(position)).getName());
        money.setText(((Joker) EditorScreen.this.editorManager.getSquare(position)).getMoney()+"");
        actionAmount.setText(((Joker) EditorScreen.this.editorManager.getSquare(position)).getMovement()+"");

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

                EditorScreen.this.editorManager.setNameForJoker((String)results[0],position);
                EditorScreen.this.editorManager.setMoneyForJoker((Integer)results[2],position);
                // System.out.println("name" +   ((Joker) editorManager.getSquare(position)).getName());
                //  System.out.println("money"+ ((Joker) editorManager.getSquare(position)).getMoney());

            if (move.isSelected()) {
                EditorScreen.this.editorManager.setMovementForJoker((Integer) results[1], position);
                // System.out.println("movement" + ((Joker) editorManager.getSquare(position)).getMovement());
            }
            if (wait.isSelected()) {
                EditorScreen.this.editorManager.setJailTimeForJoker((Integer) results[1], position);
                System.out.println("waiting time" + ((Joker) EditorScreen.this.editorManager.getSquare(position)).getSuspendedTourNo());
            } else {
                System.out.println("movement" + ((Joker) EditorScreen.this.editorManager.getSquare(position)).getMovement());
                System.out.println("waiting time" + ((Joker) EditorScreen.this.editorManager.getSquare(position)).getSuspendedTourNo());

            }

        });


        update();
    }

    public void fillColors(Square[] squares,Rectangle tile,int pos ){


        if (squares[pos].getType() == SquareType.PROPERTY) {

            tile.setFill(((Property)squares[pos]).getColorGroup().getColor());
        }
        else if(squares[pos].getType() == SquareType.JOKER){
            tile.setFill(rgb(248, 195, 165));
        }

        else if(EditorScreen.this.editorManager.getSquare(pos).getType() == SquareType.CHANCEANDCOMMUNITYCHEST){
            tile.setFill(rgb(180, 208, 246));
        }
        else {
            tile.setFill(Color.ALICEBLUE);
        }

    }

    public Scene getScene() {
        return scene;
    }

    public void update( ){
        Node [] squares = new Node[40];
        Square[] squares2 = EditorScreen.this.editorManager.board.getSquares();


        for (int i = 0; i < 40; i++) {
            GridPane boardPane = (GridPane) EditorScreen.this.editorScreen.getChildrenUnmodifiable().get(0);
            StackPane stackPane = (StackPane) boardPane.getChildren().get(i);
            squares[i] = stackPane.getChildren().get(0);

            String name = "";
            String picture = "";

            if(stackPane.getChildren().size() > 2) {
                stackPane.getChildren().remove(1);
            }
            stackPane.getChildren().remove(1);

            if (EditorScreen.this.editorManager.getSquare(i).getType() == SquareType.PROPERTY)
                name = ((Property) EditorScreen.this.editorManager.getSquare(i)).getName();
            if (EditorScreen.this.editorManager.getSquare(i).getType() == SquareType.JOKER)
                name = ((Joker) EditorScreen.this.editorManager.getSquare(i)).getName();

            Text text = new Text(name);
            Font font2 = Font.font("Source Sans Pro", 10);
            text.setFont(font2); //size of the player texts
            //StackPane stackPane = (StackPane) boardPane.getChildren().get(i);

            if (EditorScreen.this.editorManager.getSquare(i).getType() == SquareType.START ) {
                Image image = FileManager.getImage("src/sample/icons/go.png", 90, 90);
                ImageView imageView = new ImageView(image);
                stackPane.getChildren().add(imageView);
            }

            if (EditorScreen.this.editorManager.getSquare(i).getType() == SquareType.CHANCEANDCOMMUNITYCHEST ) {
                if (((ChanceAndCommunityChest) EditorScreen.this.editorManager.getSquare(i)).isChance()) {
                    picture += "chance";
                } else {
                    picture += "chest";
                }
                Image image = FileManager.getImage("src/sample/icons/" + picture + ".png", 50, 50);
                ImageView imageView = new ImageView(image);
                stackPane.getChildren().add(imageView);
            }

            fillColors(squares2, (Rectangle) squares[i], i);
            stackPane.getChildren().add(text);
            squares[i] = stackPane.getChildren().get(0);/// paint inside of the squares

        }

    //// This is the right half of the editorManager screen
    VBox v = (VBox) EditorScreen.this.editorScreen.getChildrenUnmodifiable().get(1);


    HBox h = (HBox)v.getChildren().get(1);
    TextField mortgageRate =(TextField) h.getChildren().get(1);


    HBox h2 = (HBox)v.getChildren().get(2);
    TextField currency =(TextField) h2.getChildren().get(1);


    HBox h3 = (HBox)v.getChildren().get(3);
    TextField rentRate =(TextField) h3.getChildren().get(1);

    HBox buttons = (HBox) v.getChildren().get(8);

    Button cancel  = (Button) buttons.getChildren().get(1);
    Button save    = (Button) buttons.getChildren().get(0);
    cancel.setCancelButton(true);

        TextField boardName = (TextField) v.getChildren().get(0);
        cancel.setCancelButton(true);
        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    screenManager.changeScreen(new MainMenuScreen(screenManager));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        save.addEventFilter( ActionEvent.ACTION,
                event -> {
                    if(boardName.getText().equalsIgnoreCase("Default board")) {
                        System.out.println("CAN'T SAVEEEE");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error saving board");
                        alert.setHeaderText(null);
                        alert.setContentText("You can't save the board with the name \"Default board\"");
                        alert.getDialogPane().setStyle(
                                " -fx-background-color: rgb(182, 216, 184); -fx-font: 'Source Sans Pro'; -fx-font-family: 'Source Sans Pro'; -fx-font-size: 13;"
                        );
                        alert.showAndWait();
                        event.consume();
                }
        });

        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!mortgageRate.getText().isEmpty())
                    EditorScreen.this.editorManager.board.setMortgageRate(Integer.parseInt(mortgageRate.getText()));
                if(!rentRate.getText().isEmpty())
                    EditorScreen.this.editorManager.board.setRentRate(Integer.parseInt(rentRate.getText()));
                if(!currency.getText().isEmpty())
                    EditorScreen.this.editorManager.board.setCurrency(currency.getText());
                if(!boardName.getText().isEmpty())
                    EditorScreen.this.editorManager.board.setName(boardName.getText());

                FileManager.writeBoardToFolder(editorManager.board);
                FileManager.saveIconsOnBoard(playerIcons, boardIcon, editorManager.board.getName());
            }
        });
        /// end of the right half
        for (int pos = 0; pos < 40; pos++) {
           // TextField boardName = (TextField) v.getChildren().get(0);
            cancel.setCancelButton(true);
            cancel.setOnAction(event -> {
                try {
                    screenManager.changeScreen(new MainMenuScreen(screenManager));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });


            save.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    if (!mortgageRate.getText().isEmpty())
                        EditorScreen.this.editorManager.board.setMortgageRate(Integer.parseInt(mortgageRate.getText()));

                    if (!rentRate.getText().isEmpty())
                        EditorScreen.this.editorManager.board.setRentRate(Integer.parseInt(rentRate.getText()));

                    if (!currency.getText().isEmpty())
                        EditorScreen.this.editorManager.board.setCurrency(currency.getText());

                    if (!boardName.getText().isEmpty())
                        EditorScreen.this.editorManager.board.setName(boardName.getText());


                    FileManager.writeBoardToFolder(EditorScreen.this.editorManager.board);
                    FileManager.saveIconsOnBoard(playerIcons, boardIcon, editorManager.board.getName());
                    try {
                        screenManager.changeScreen(new MainMenuScreen(screenManager));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            /// end of the right half
        }
    for (int pos = 1; pos < 40; pos++) {
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

                if(EditorScreen.this.editorManager.getSquare(position).getType() == SquareType.CHANCEANDCOMMUNITYCHEST){
                    if(((ChanceAndCommunityChest) EditorScreen.this.editorManager.getSquare(position)).isChance() == true)
                        chance.setSelected(true);
                    if(((ChanceAndCommunityChest) EditorScreen.this.editorManager.getSquare(position)).isChance() == false)
                        communityChest.setSelected(true);
                }
                if(EditorScreen.this.editorManager.getSquare(position).getType() == SquareType.PROPERTY){
                    property.setSelected(true);
                }
                if(EditorScreen.this.editorManager.getSquare(position).getType() == SquareType.JOKER){
                    joker.setSelected(true);
                }
                Optional<ButtonType> result = squareTypeDialog.showAndWait();

                if (result.get() == ButtonType.NEXT & joker.isSelected()){
                    if(  squares2[position].getType() != SquareType.JOKER){
                        if (squares2[position].getType() == SquareType.PROPERTY) {
                            ColorGroup temp = ((Property) squares2[position]).getColorGroup();
                            temp.removeProperty((Property) squares2[position]);
                        }

                        EditorScreen.this.editorManager.createNewJoker(position, 0, 0, 0, "Joker");

                    }

                    openJokerDialog(squares);
                }
                else if (result.get() == ButtonType.NEXT & property.isSelected()){
                    if(  squares2[position].getType() != SquareType.PROPERTY){
                    if(EditorScreen.this.editorManager.board.getColorGroups().size() == 0){

                        ColorGroup temp = new ColorGroup("color group"); //might be deleted
                        EditorScreen.this.editorManager.createNewProperty(position,"ankara",temp,100,50,80);
                    }
                    else{
                        int x = ((int) (Math.random() * EditorScreen.this.editorManager.board.getColorGroups().size()));
                        ColorGroup cg = EditorScreen.this.editorManager.board.getColorGroups().get(x);
                        EditorScreen.this.editorManager.createNewProperty(position,"ankara",cg,100,50,80);
                    }}
                    openPropertyDialog(squares);
                }

                else if (( communityChest.isSelected())&& result.get()== ButtonType.NEXT && squares2[finalPosition].getType() != SquareType.CHANCEANDCOMMUNITYCHEST ) {
                    // removing property from its ColorGroup's arraylist
                    if( squares2[finalPosition].getType() == SquareType.PROPERTY )
                    {
                        ColorGroup temp = ((Property) squares2[finalPosition]).getColorGroup();
                        temp.removeProperty((Property)squares2[finalPosition]);
                    }
                    EditorScreen.this.editorManager.createNewChestCommunity(finalPosition,false);
                    System.out.println(EditorScreen.this.editorManager.getSquare(finalPosition).getType());
                    update();
                }


                else if (( chance.isSelected() )&& result.get()== ButtonType.NEXT && squares2[finalPosition].getType() != SquareType.CHANCEANDCOMMUNITYCHEST ) {
                    // removing property from its ColorGroup's arraylist
                    if( squares2[finalPosition].getType() == SquareType.PROPERTY )
                    {
                        ColorGroup temp = ((Property) squares2[finalPosition]).getColorGroup();
                        temp.removeProperty((Property)squares2[finalPosition]);
                    }
                    EditorScreen.this.editorManager.createNewChestCommunity(finalPosition,true);
                    System.out.println(EditorScreen.this.editorManager.getSquare(finalPosition).getType());
                    update();
                }
                else if (( chance.isSelected() )&& result.get()== ButtonType.NEXT && squares2[finalPosition].getType() == SquareType.CHANCEANDCOMMUNITYCHEST
                        && ((ChanceAndCommunityChest)squares2[finalPosition]).isChance()== false) {
                    // removing property from its ColorGroup's arraylist
                    EditorScreen.this.editorManager.createNewChestCommunity(finalPosition,true);
                    System.out.println(EditorScreen.this.editorManager.getSquare(finalPosition).getType());
                    update();
                }
                else if (( communityChest.isSelected() )&& result.get()== ButtonType.NEXT && squares2[finalPosition].getType() == SquareType.CHANCEANDCOMMUNITYCHEST
                        && ((ChanceAndCommunityChest)squares2[finalPosition]).isChance()== true) {
                    // removing property from its ColorGroup's arraylist
                    EditorScreen.this.editorManager.createNewChestCommunity(finalPosition,false);
                    update();
                }
            });
        }

    }


}

