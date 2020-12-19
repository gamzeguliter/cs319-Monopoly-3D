package sample.screens;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import sample.Board;
import sample.GameEngine;
import sample.Player;
import sample.ScreenManager;
import sample.squares.Property;
import sample.squares.SquareType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class GameScreen extends Screen {

    // properties
    private Scene scene;
    GridPane boardPane;

    ArrayList<Text> playerTexts;
    Text turnText;
    Button btnRollDice;
    Button btnEndTurn;
    Button btnResign;
    GameEngine gameEngine;
    Font font = Font.font("Source Sans Pro", 20);
    int position;
    Parent gameScreen = FXMLLoader.load(getClass().getResource("GameScreen.fxml"));
    DialogPane diceScreen = FXMLLoader.load(getClass().getResource("diceScreen.fxml"));
    DialogPane propertyScreen = FXMLLoader.load(getClass().getResource("propertyScreen.fxml"));
    DialogPane auctionOrSellScreen = FXMLLoader.load(getClass().getResource("auctionOrSellScreen.fxml"));
    DialogPane jokerScreen = FXMLLoader.load(getClass().getResource("jokerScreen.fxml"));
    DialogPane chanceAndCommunityChestScreen = FXMLLoader.load(getClass().getResource("chanceAndCommunityChestScreen.fxml"));

    // constructors
    public GameScreen(ScreenManager screenManager) throws IOException {
        super(screenManager);
        gameEngine = new GameEngine();
        boardPane = getSquares();
        setScene();
    }

    public GameScreen(ScreenManager screenManager, Board board, ArrayList<Player> players) throws IOException {
        super(screenManager);
        gameEngine = new GameEngine(board, players);
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

    //todo değişecek
    private void initializePlayerTexts(Group group) {
        playerTexts = new ArrayList<>();
        int count = 0;
        for (Player player : gameEngine.getPlayers()) {
            playerTexts.add(getPlayerText(player));
            playerTexts.get(count).setX(800);
            playerTexts.get(count).setY(100 + count * 100);
            group.getChildren().add(playerTexts.get(count));
            count++;
        }
        /*
        playerTexts[0].setX(800);
        playerTexts[0].setY(100);
        playerTexts[1].setX(800);
        playerTexts[1].setY(200);
        playerTexts[2].setX(800);
        playerTexts[2].setY(300);
        playerTexts[3].setX(800);
        playerTexts[3].setY(400);*/

        //group.getChildren().addAll(playerTexts[0], playerTexts[1], playerTexts[2], playerTexts[3]);
    }

    //todo bu method direk getPlayers ile almasa daha iyi olur
    private void updatePlayerTexts() {
        for (int i = 0; i < gameEngine.getPlayers().size(); i++) {
            Player player = gameEngine.getPlayers().get(i);
            playerTexts.get(i).setText(player.getName() + "\nBalance: " + player.getBalance() + "\nPosition: " + player.getPosition());
        }
    }


    //done knotrol edelim
    private void setScene() {
        scene = new Scene(gameScreen);

        VBox vBox = (VBox) gameScreen.getChildrenUnmodifiable().get(1);
        HBox hBox = (HBox) vBox.getChildren().get(3);
        Label turnText = (Label) vBox.getChildren().get(2);
        Button btnRollDice = (Button) hBox.getChildren().get(0);
        Button btnEndTurn = (Button) hBox.getChildren().get(1);

        turnText.setText("Player Turn: " + gameEngine.getCurrentPlayer().getName());
        // initialize buttons
        //btnRollDice = new Button();
        //btnEndTurn = new Button();
        btnResign = new Button("Resign"); //todo eksik
        Font font3 = Font.font("Source Sans Pro", 15);

        //resign button
        btnResign.setOnAction(actionEvent -> {
            createResignDialog();
        });

        //roll dice button
        btnRollDice.setText("Roll Dice");
        btnRollDice.setFont(font3);
        //initialize end turn as disabled
        btnEndTurn.setDisable(true);

        //todo tekrar check buraya
        btnRollDice.setOnAction(event -> {
            createDiceDialog();
            btnEndTurn.setDisable(false); //todo eğer oyunda tekrar hareket varsa hareket pop-upından sonra disable kaldır
            btnRollDice.setDisable(true);
        });

        //end turn button
        Label finalTurnText = turnText;
        btnEndTurn.setOnAction(event -> {
            if(gameEngine.isBankrupt()) {
                createBankruptDialog();
            }
            else {
                gameEngine.nextTurn();
            }
            finalTurnText.setText("Player Turn: " + gameEngine.getCurrentPlayer().getName());
            btnRollDice.setDisable(false);
            btnEndTurn.setDisable(true);
        });

        btnEndTurn.setLayoutX(200);
        btnEndTurn.setLayoutY(120);

        // turn text
        //turnText = new Text();
        turnText.setFont(font);
        turnText.setText("Player Turn: " + gameEngine.getCurrentPlayer().getName()); //changed
        /*turnText.setX(150);
        turnText.setY(200);
        group.getChildren().add(turnText);

        group.getChildren().add(boardPane);*/

        //scene = new Scene(group, width, height);
    }

    private void checkSquare() {
        if(gameEngine.passesStart()) {
            createStartDialog();
            updatePlayerTexts();
        }

        if (gameEngine.getCurrentSquare().getType() == SquareType.PROPERTY) {
            createPropertyDialog(-1);

        } else if (gameEngine.getCurrentSquare().getType() == SquareType.JOKER) {
            createJokerDialog();
        }
        else if(gameEngine.getCurrentSquare().getType() == SquareType.CHANCEANDCOMMUNITYCHEST) {
            createChanceAndChestDialog();
        }
        //start square
        else {
            createStartDialog();
        }
        updateSquares();
        updatePlayerTexts();
    }

    private void createBankruptDialog() {
        Dialog dialog = new Dialog();
        VBox vbox = new VBox();
        Text bankrupt = new Text("YOU LOSE");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        ((Button)okButton).setOnAction(event -> {
            ArrayList<Integer> indexes = gameEngine.sellPlayerProperties();
            updatePlayerTexts();
            for (int i = 0; i < indexes.size(); i++) {
                createAuctionOrSellDialog(indexes.get(i), true);
                updateSquares();
                updatePlayerTexts();
            }
            gameEngine.bankrupt();
            gameEngine.nextTurn();
        });
        vbox.getChildren().addAll(bankrupt);
        dialog.getDialogPane().setContent(vbox);
        dialog.show();
    }

    private void createResignDialog() {
        Dialog dialog = new Dialog();
        VBox vbox = new VBox();
        Text resignConfirmation = new Text("Are you sure you want to resign?");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        ((Button)okButton).setText("Yes");
        ((Button)okButton).setOnAction(event -> {
            dialog.close();
            ArrayList<Integer> indexes = gameEngine.sellPlayerProperties();
            updatePlayerTexts();

            for (int i = 0; i < indexes.size(); i++) {
                createAuctionOrSellDialog(indexes.get(i), true);
                updateSquares();
                updatePlayerTexts();
            }
            gameEngine.resign();
        });
        vbox.getChildren().addAll(resignConfirmation, okButton);
        dialog.getDialogPane().setContent(vbox);
        dialog.show();
    }

    //done
    private void createDiceDialog() {
        Dialog diceDialog = new Dialog();
        diceDialog.setDialogPane(diceScreen);

        VBox vbox = (VBox) diceDialog.getDialogPane().getContent();
        int[] dieResult = gameEngine.rollDice();
        Text die1 = (Text) vbox.getChildren().get(0);
        Text die2 = (Text) vbox.getChildren().get(1);
        die1.setText("Die 1: " + dieResult[0]);
        die2.setText("Die 2: " + dieResult[1]);

        Node okButton = diceDialog.getDialogPane().lookupButton(ButtonType.OK);
        ((Button)okButton).setOnAction(event -> {
            //if(gameEngine.canMove()) -- todo check jail

            gameEngine.movePlayer();
            updateSquares();
            updatePlayerTexts();
            diceDialog.close();
            checkSquare();
        });
        diceDialog.show();
    }

    private void createStartDialog() {
        Dialog dialog = new Dialog();
        VBox vbox = gameEngine.startInfo();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        ((Button)okButton).setText("Collect");
        ((Button)okButton).setOnAction(event -> {
            gameEngine.startAction();
            updateSquares();
            updatePlayerTexts();
            dialog.close();
        });
        vbox.getChildren().add(okButton);
        dialog.getDialogPane().setContent(vbox);
        dialog.showAndWait();
    }

    //done check
    private void createChanceAndChestDialog() {
        Dialog chanceAndCommunityChestDialog = new Dialog();
        chanceAndCommunityChestDialog.setDialogPane(chanceAndCommunityChestScreen);

        Node okButton = chanceAndCommunityChestDialog.getDialogPane().lookupButton(ButtonType.OK);
        ((Button)okButton).setText("Draw");
        ((Button)okButton).setOnAction(event -> {
            gameEngine.drawCard();
            createCardDialog();
            chanceAndCommunityChestDialog.close();
            updateSquares();
            updatePlayerTexts();
        });
        chanceAndCommunityChestDialog.show();
    }

    private void createCardDialog() {
        Dialog dialog = new Dialog();
        VBox vbox = new VBox();
        Text text = new Text(gameEngine.getCardInfo());
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        ((Button)okButton).setText("Do");
        ((Button)okButton).setOnAction(event -> {
            if(gameEngine.implementCard()) {
                dialog.close();
                updateSquares();
                updatePlayerTexts();
                checkSquare();
            }
            else {
                dialog.close();
                updateSquares();
                updatePlayerTexts();
            }
        });
        vbox.getChildren().addAll(text, okButton);
        dialog.getDialogPane().setContent(vbox);
        dialog.show();
    }

    //done check
    private void createJokerDialog() {
        Dialog jokerDialog = new Dialog();
        jokerDialog.setDialogPane(jokerScreen);

        VBox vBox = (VBox) jokerDialog.getDialogPane().getContent();

        VBox jokerContent = gameEngine.getJokerContent();
        String jokerName = gameEngine.getJokerName();

        Text header = (Text) jokerDialog.getDialogPane().getChildren().get(0);
        vBox.getChildren().add(jokerContent);

        header.setText(jokerName);
        jokerDialog.getDialogPane().getChildren().get(1);

        Node okButton = jokerDialog.getDialogPane().lookupButton(ButtonType.OK);
        ((Button)okButton).setText("Sold!");
        ((Button)okButton).setOnAction(event -> {
            if(gameEngine.jokerActions()) {
                jokerDialog.close();
                updateSquares();
                updatePlayerTexts();
                checkSquare();
            }
            else {
                jokerDialog.close();
                updateSquares();
                updatePlayerTexts();
            }
        });

        jokerDialog.showAndWait();
    }

    // done combo box düzeltilecek
    private void createAuctionOrSellDialog(int index, boolean isAuction) {
        Dialog auctionOrSell = new Dialog();
        auctionOrSell.setDialogPane(auctionOrSellScreen);

        VBox mainVBox = (VBox) auctionOrSell.getDialogPane().getContent();
        Text header = (Text) auctionOrSell.getDialogPane().getHeader();

        HBox propertyBox = (HBox) mainVBox.getChildren().get(0);
        HBox playerBox = (HBox) mainVBox.getChildren().get(1);
        HBox priceBox = (HBox) mainVBox.getChildren().get(2);

        Text propertyName = (Text) propertyBox.getChildren().get(0);
        ComboBox playerSelection = (ComboBox) playerBox.getChildren().get(1);
        TextField price = (TextField) priceBox.getChildren().get(1);

        if(index < 0)
            propertyName.setText("Property: " + ((Property)(gameEngine.getCurrentSquare())).getName());
        else {
            propertyName.setText("Property: " + ((Property)(gameEngine.getSquare(index))).getName());
        }

        if(isAuction) {
             header = new Text("Auction Property");
        }
        else
            header = new Text("Sell Property");

        propertyName.setText("Property: " + ((Property)(gameEngine.getCurrentSquare())).getName());

        auctionOrSell.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                //todo return new Pair<>(playerSelection.getText(), price.getText());  PLAYER SELECTION ARTIK BİR COMBO BOX COMBO BOX LISTENER?
            }

            return null;
        });

        //auction.show();
        Optional<Pair<String, String>> result = auctionOrSell.showAndWait();
        result.ifPresent(pair -> {
            //Todo System.out.println(player.getText());
            if(isAuction) {
                //todo gameEngine.auctionProperty(index, player.getText(), Integer.parseInt(amount.getText()));
            }
            else {
                //selling a mortgaged property -- ask the new owner if they want to lift the mortgage
                //todo gameEngine.sellProperty(index, player.getText(), Integer.parseInt(amount.getText()));
            }
            if(gameEngine.soldMortgaged(index)) {
                createMortgageLiftDialog(index);
            }
            updateSquares();
            updatePlayerTexts();
        });
    }

    private void createMortgageLiftDialog(int index) {
        Dialog mortgageLifting = new Dialog();
        VBox vbox = gameEngine.getMortgageLiftingInfo(index);

        //cancel button
        mortgageLifting.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Node closeButton = mortgageLifting.getDialogPane().lookupButton(ButtonType.CLOSE);
        ((Button)closeButton).setText("Lift mortgage later");
        ((Button)closeButton).setOnAction(event -> {
            gameEngine.liftMortgageLater(index);
            mortgageLifting.close();
            updateSquares();
            updatePlayerTexts();
        });

        //ok button
        mortgageLifting.getDialogPane().getButtonTypes().add(ButtonType.OK);
        Node okButton = mortgageLifting.getDialogPane().lookupButton(ButtonType.OK);
        ((Button)okButton).setText("Lift mortgage now");
        ((Button)okButton).setOnAction(event -> {
            gameEngine.unmortgageProperty(index);
            mortgageLifting.close();
            updateSquares();
            updatePlayerTexts();
        });

        vbox.getChildren().addAll(okButton,closeButton);
        mortgageLifting.getDialogPane().setContent(vbox);
        mortgageLifting.show();
    }

    //done konrol edelim
    private void createPropertyDialog(int index) {
        Dialog propertyDialog = new Dialog();
        propertyDialog.setDialogPane(propertyScreen);

        VBox mainVBox = (VBox) propertyDialog.getDialogPane().getContent();
        Text header = (Text) propertyDialog.getDialogPane().getHeader();
        VBox propertyBox = (VBox) mainVBox.getChildren().get(0);
        VBox buttonBox = (VBox) mainVBox.getChildren().get(1);

        propertyBox.getChildren().add(gameEngine.getPropertyContent(index)); //fazladan vbox mu ekledik

        header.setText(gameEngine.getPropertyName(index));

        Property property;
        if(index < 0 ) {
            property = (Property)(gameEngine.getCurrentSquare());
        }
        else {
            property = (Property) (gameEngine.getSquare(index));
        }
        if(property.isOwned()) {
            propertyDialog.getDialogPane().setBackground(new Background(new BackgroundFill(property.getOwner().getColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        }
        else {
            propertyDialog.getDialogPane().setBackground(new Background(new BackgroundFill(Color.rgb(182, 216, 184), CornerRadii.EMPTY, Insets.EMPTY)));
        }
        ArrayList<String> buttonNames = gameEngine.getPropertyButtons(index);
        //add cancel button
        propertyDialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE); //todo bunu koyunca x tuşu ile kapanıyor olmaması lazım
        Node closeButton = propertyDialog.getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.setDisable(true);

        //dialog.getDialogPane().getScene().getWindow().setOnCloseRequest(event -> event.consume());

        for(String buttonName: buttonNames) {
            switch(buttonName) {
                case "cancel":
                    closeButton.setDisable(false);
                    //propertyDialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
                    break;
                case "sell":
                    Button sellBtn = new Button("Sell");
                    sellBtn.setOnAction(event -> {
                        propertyDialog.close();
                        createAuctionOrSellDialog( index, false);
                        updateSquares();
                    });
                    buttonBox.getChildren().add(sellBtn);
                    break;

                case "buy":
                    Button buyBtn = new Button("Buy");
                    buyBtn.setOnAction(event -> {
                        gameEngine.buyProperty();
                        updateSquares();
                        updatePlayerTexts();
                        propertyDialog.close();
                    });
                    buttonBox.getChildren().add(buyBtn);
                    break;

                case "auction":
                    Button auctionBtn = new Button("Auction");
                    auctionBtn.setOnAction(event -> {
                        propertyDialog.close();
                        createAuctionOrSellDialog( index, true);
                        updateSquares();
                    });
                    buttonBox.getChildren().add(auctionBtn);
                    break;

                case "mortgage":
                    Button mortgageBtn = new Button("Mortgage");
                    mortgageBtn.setOnAction(event -> {
                        gameEngine.mortgageProperty(index);
                        propertyDialog.close();
                        updatePlayerTexts();
                    });
                    buttonBox.getChildren().add(mortgageBtn);
                    break;

                case "unmortgage":
                    Button unmortgageBtn = new Button("Unmortgage");
                    unmortgageBtn.setOnAction(event -> {
                        gameEngine.unmortgageProperty(index);
                        updatePlayerTexts();
                        propertyDialog.close();
                    });
                    buttonBox.getChildren().add(unmortgageBtn);
                    break;

                case "add house":
                    Button addHouseBtn = new Button("Add House");
                    addHouseBtn.setOnAction(event -> {
                        gameEngine.addHouse(index);
                        updatePlayerTexts();
                        propertyDialog.close();
                    });
                    buttonBox.getChildren().add(addHouseBtn);
                    break;

                case "sell house":
                    Button sellHouseBtn = new Button("Sell House");
                    sellHouseBtn.setOnAction(event -> {
                        gameEngine.sellHouse(index);
                        updatePlayerTexts();
                        propertyDialog.close();
                    });
                    buttonBox.getChildren().add(sellHouseBtn);
                    break;

                case "add hotel":
                    Button addHotelBtn = new Button("Add hotel");
                    addHotelBtn.setOnAction(event -> {
                        gameEngine.addHotel(index);
                        updatePlayerTexts();
                        propertyDialog.close();
                    });
                    buttonBox.getChildren().add(addHotelBtn);
                    break;

                case "sell hotel":
                    Button sellHotelBtn = new Button("Sell Hotel");
                    sellHotelBtn.setOnAction(event -> {
                        gameEngine.sellHotel(index);
                        updatePlayerTexts();
                        propertyDialog.close();
                    });
                    buttonBox.getChildren().add(sellHotelBtn);
                    break;

                case "pay rent":
                    Button rentBtn = new Button("Pay Rent");
                    rentBtn.setOnAction(event -> {
                        gameEngine.takeRent();
                        updatePlayerTexts();
                        propertyDialog.close();
                    });
                    buttonBox.getChildren().add(rentBtn);
                    break;
            }
        }
        propertyDialog.show();
    }

    //create the board on screen
    private GridPane getSquares() {
        GridPane gridPane = (GridPane) gameScreen.getChildrenUnmodifiable().get(0);

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
                    // create rectangle of correct color for tile
                    StackPane stackPane = (StackPane) gridPane.getChildren().get(pos);
                    Rectangle tile = (Rectangle) stackPane.getChildren().get(0);

                    tile.setOnMouseClicked(event -> {
                        System.out.println("Position " +position );
                    });

                    //set tile colors
                    System.out.println(gameEngine.getSquare(pos).getType());
                    if (gameEngine.getSquare(pos).getType() == SquareType.PROPERTY) {
                        Property property = (Property)(gameEngine.getSquare(pos));
                        tile.setFill(property.getColorGroup().getColor());
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

                    if ((row == 0) | (col == 0) | (row == 10) | (col == 10)) {
                        stackPane.getChildren().addAll(text); //try
                    }
                }
            }
        }
        return gridPane;
    }

    //updates the board tiles on screen
    private void updateSquares() {
        //boardPane.getChildren().clear();
        //GridPane gridPane = (GridPane) gameScreen.getChildrenUnmodifiable().get(0);


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
                    StackPane stackPane = (StackPane) boardPane.getChildren().get(pos);
                    Rectangle tile = (Rectangle) stackPane.getChildren().get(0);

                    //todo add pictures/names to tiles, make player pawns
                    System.out.println(pos);
                    //make the tiles clickable
                    tile.setOnMouseClicked(event -> {
                        position = pos;
                        System.out.println(gameEngine.canClick(pos));
                        if(gameEngine.canClick(pos)) {
                            createPropertyDialog(pos);
                        }
                        System.out.println("Positionnnnn " + position );
                    });

                    //determine square colors
                    if (gameEngine.getSquare(pos).getType() == SquareType.PROPERTY) {
                        Property property = (Property) gameEngine.getSquare(pos);
                        if (property.isOwned() == true) {
                            Player owner = property.getOwner();
                            tile.setFill(owner.getColor());
                        }
                        else
                            tile.setFill(property.getColorGroup().getColor());
                    }
                    else if(gameEngine.getSquare(pos).getType() == SquareType.JOKER){
                        tile.setFill(Color.DARKGOLDENROD);
                    }

                    else if(gameEngine.getSquare(pos).getType() == SquareType.CHANCEANDCOMMUNITYCHEST){
                        tile.setFill(Color.LIME);
                    }
                    else {
                        tile.setFill(Color.BLUEVIOLET);
                    }

                    // find players on tile and set text
                    ArrayList<Integer> playerPositions = gameEngine.getPlayerPositions();
                    String playersOnTile = "";

                    for (int i = 0; i < 40; i++){
                        if (stackPane.getChildren().size() > 1 )
                            stackPane.getChildren().remove(1);
                    }

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
                    Font font2 = Font.font("Source Sans Pro", 15);
                    text.setFont(font2); //size of the player texts

                    if ((row == 0) | (col == 0) | (row == 10) | (col == 10)) {
                        stackPane.getChildren().add(text);
                    }
                }
            }
        }
    }

    public Scene getScene() { return scene; }

}
