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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    //Text turnText;
    Button btnRollDice;
    Button btnEndTurn;
    Button btnResign;
    GameEngine gameEngine;
    Label turnText;
    Font font = Font.font("Source Sans Pro", 20);
    int position;
    AnchorPane gameScreen = FXMLLoader.load(getClass().getResource("GameScreen.fxml"));
    DialogPane diceScreen = FXMLLoader.load(getClass().getResource("diceScreen.fxml"));
    DialogPane propertyScreen = FXMLLoader.load(getClass().getResource("propertyScreen.fxml"));
    DialogPane auctionOrSellScreen = FXMLLoader.load(getClass().getResource("auctionOrSellScreen.fxml"));
    DialogPane jokerScreen = FXMLLoader.load(getClass().getResource("jokerScreen.fxml"));
    DialogPane chanceAndCommunityChestScreen = FXMLLoader.load(getClass().getResource("chanceAndCommunityChestScreen.fxml"));
    DialogPane mortgageLiftingScreen = FXMLLoader.load(getClass().getResource("mortgageLiftingScreen.fxml"));
    DialogPane jailFinishScreen = FXMLLoader.load(getClass().getResource("jailFinishScreen.fxml"));
    DialogPane cardScreen = FXMLLoader.load(getClass().getResource("cardScreen.fxml"));
    DialogPane startScreen = FXMLLoader.load(getClass().getResource("startScreen.fxml"));
    DialogPane resignScreen = FXMLLoader.load(getClass().getResource("resignScreen.fxml"));
    DialogPane gameOverScreen = FXMLLoader.load(getClass().getResource("gameOverScreen.fxml"));
    DialogPane bankruptScreen = FXMLLoader.load(getClass().getResource("bankruptScreen.fxml"));

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
        return t;
    }

    //todo değişecek
    private void getPlayerTexts() {
        VBox vBox = (VBox) gameScreen.getChildrenUnmodifiable().get(1);
        VBox vBox2 = (VBox) vBox.getChildren().get(1);
        HBox player1 = (HBox) vBox2.getChildren().get(0);
        HBox player2 = (HBox) vBox2.getChildren().get(1);
        HBox player3 = (HBox) vBox2.getChildren().get(2);
        HBox player4 = (HBox) vBox2.getChildren().get(3);

        ArrayList<HBox> playerBoxes = new ArrayList<HBox>();
        playerBoxes.add(player1);
        playerBoxes.add(player2);
        playerBoxes.add(player3);
        playerBoxes.add(player4);

        for(int i = 0; i < 4; i ++) {
            if(i < gameEngine.getPlayers().size()) {
                Player player = gameEngine.getPlayers().get(i);
                HBox currentBox = playerBoxes.get(i);
                currentBox.setVisible(true);

                Rectangle color = (Rectangle) currentBox.getChildren().get(1);
                color.setFill(player.getColor());

                VBox infoBox = (VBox) currentBox.getChildren().get(0);

                Label playerLabel = (Label) infoBox.getChildren().get(0);
                Label moneyLabel = (Label) infoBox.getChildren().get(1);

                playerLabel.setText("Player: " + player.getName());
                moneyLabel.setText("Money: " + player.getBalance());
            }
            else {
                HBox currentBox = playerBoxes.get(i);
                currentBox.setVisible(false);
            }
        }
    }

    /*

    //değişecek
    //todo bu method direk getPlayers ile almasa daha iyi olur
    private void updatePlayerTexts(VBox vBox) {
        HBox player1 = (HBox) vBox.getChildren().get(0);
        HBox player2 = (HBox) vBox.getChildren().get(1);
        HBox player3 = (HBox) vBox.getChildren().get(2);
        HBox player4 = (HBox) vBox.getChildren().get(3);

        ArrayList<HBox> playerBoxes = new ArrayList<HBox>();
        playerBoxes.add(player1);
        playerBoxes.add(player2);
        playerBoxes.add(player3);
        playerBoxes.add(player4);

        int count = 0;
        for (Player player : gameEngine.getPlayers()) {
            HBox currentBox = playerBoxes.get(count);
            VBox infoBox = (VBox) currentBox.getChildren().get(0);

            Label playerLabel = (Label) infoBox.getChildren().get(0);
            Label moneyLabel = (Label) infoBox.getChildren().get(1);

            playerLabel.setText("Player" + (count + 1) + ": " + player.getName());
            moneyLabel.setText("Money: " + player.getBalance());
            count++;
        }
    }

     */

    private void updateTurnText() {
        turnText.setText("Player Turn: " + gameEngine.getCurrentPlayer().getName());
    }

    //done kontrol edelim
    private void setScene() {
        scene = new Scene(gameScreen);

        VBox vBox = (VBox) gameScreen.getChildrenUnmodifiable().get(1);
        VBox vBox2 = (VBox) vBox.getChildren().get(1);
        HBox hBox = (HBox) vBox.getChildren().get(3);
        turnText = (Label) vBox.getChildren().get(2);
        btnRollDice = (Button) hBox.getChildren().get(0);
        btnEndTurn = (Button) hBox.getChildren().get(1);
        HBox hBox2 = (HBox) vBox2.getChildren().get(4);
        btnResign = (Button) hBox2.getChildren().get(0);

        getPlayerTexts();

        turnText.setText("Player Turn: " + gameEngine.getCurrentPlayer().getName());

        //roll dice button
        btnRollDice.setText("Roll Dice");
        //initialize end turn as disabled
        btnEndTurn.setDisable(true);

        //todo tekrar check buraya
        btnRollDice.setOnAction(event -> {
            if(gameEngine.getCurrentPlayer().isInJail()) {
                jailFinishDialog();
            }
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
                if(gameEngine.nextTurn()){
                    createGameOverDialog();
                }
            }
            finalTurnText.setText("Player Turn: " + gameEngine.getCurrentPlayer().getName());
            getPlayerTexts();
            btnRollDice.setDisable(false);
            btnEndTurn.setDisable(true);
        });

        //resign button
        btnResign.setOnAction(actionEvent -> {
            createResignDialog();
        });

        // turn text
        //turnText.setText("Player Turn: " + gameEngine.getCurrentPlayer().getName()); //changed

    }

    private void checkSquare() {
        if(gameEngine.passesStart()) {
            createStartDialog();
            getPlayerTexts();
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
        getPlayerTexts();
    }

    //done
    private void createBankruptDialog() {
        Dialog bankruptDialog = new Dialog();
        bankruptDialog.setDialogPane(bankruptScreen);

        VBox vbox = new VBox();
        Text bankrupt = new Text("YOU LOSE");

        Node okButton = bankruptDialog.getDialogPane().lookupButton(ButtonType.OK);
        ((Button)okButton).setOnAction(event -> {
            ArrayList<Integer> indexes = gameEngine.sellPlayerProperties();
            getPlayerTexts();
            for (int i = 0; i < indexes.size(); i++) {
                createAuctionOrSellDialog(indexes.get(i), true);
                updateSquares();
                getPlayerTexts();
            }
            gameEngine.bankrupt();
            if(gameEngine.nextTurn()) {
                createGameOverDialog();
            }
        });
        vbox.getChildren().addAll(bankrupt);
        bankruptDialog.getDialogPane().setContent(vbox);
        bankruptDialog.show();
    }

    //done check
    private void createGameOverDialog() {
        Dialog gameOverDialog = new Dialog();
        gameOverDialog.setDialogPane(gameOverScreen);

        VBox vbox = new VBox();
        Text winner = new Text(gameEngine.getWinner() + "WINS!!!!");

        Node okButton = gameOverDialog.getDialogPane().lookupButton(ButtonType.OK);
        ((Button)okButton).setText("Go back to the main page");
        ((Button)okButton).setOnAction(event -> {
            gameOverDialog.close();
            screenManager.changeScreen(new MainMenuScreen(screenManager));
        });
        vbox.getChildren().addAll(winner);
        gameOverDialog.getDialogPane().setContent(vbox);
        gameOverDialog.show();
    }

    private void exitConfirmationDialog() {
        //todo @öykü -- cancel butonu
        //are you sure you want to exit?
        //yes -> take to main page
    }

    //done
    private void createResignDialog() {
        Dialog resignDialog = new Dialog();
        resignDialog.setDialogPane(resignScreen);

        VBox vbox = new VBox();
        Text resignConfirmation = new Text("Are you sure you want to resign?");

        Node okButton = resignDialog.getDialogPane().lookupButton(ButtonType.OK);
        ((Button)okButton).setText("Yes");
        ((Button)okButton).setOnAction(event -> {
            resignDialog.close();
            ArrayList<Integer> indexes = gameEngine.sellPlayerProperties();

            for (int i = 0; i < indexes.size(); i++) {
                createAuctionOrSellDialog(indexes.get(i), true);
                updateSquares();
                getPlayerTexts();
            }
            gameEngine.resign();
            gameEngine.nextTurn();
            updateSquares();
            getPlayerTexts();
            updateTurnText();
            if(gameEngine.checkWin()) {
                createGameOverDialog();
            }
            btnEndTurn.setDisable(true);
            btnRollDice.setDisable(false);
        });

        vbox.getChildren().addAll(resignConfirmation);
        resignDialog.getDialogPane().setContent(vbox);
        resignDialog.show();
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
            if(gameEngine.playerInJail()) {
                if(dieResult[0] == dieResult[1]) {
                    gameEngine.releasePlayer(true);
                    gameEngine.movePlayer();
                    updateSquares();
                    getPlayerTexts();
                    diceDialog.close();
                    checkSquare();
                }
                else {
                    jailFinishDialog();
                }
            }
            else {
                gameEngine.movePlayer();
                updateSquares();
                getPlayerTexts();
                diceDialog.close();
                checkSquare();
            }
        });
        diceDialog.show();
    }

    //done
    private void createStartDialog() {
        Dialog startDialog = new Dialog();
        startDialog.setDialogPane(startScreen);
        VBox vbox = gameEngine.startInfo();
        Node okButton = startDialog.getDialogPane().lookupButton(ButtonType.OK);
        ((Button)okButton).setText("Collect");
        ((Button)okButton).setOnAction(event -> {
            gameEngine.startAction();
            updateSquares();
            getPlayerTexts();
            startDialog.close();
        });
        vbox.getChildren().add(okButton);
        startDialog.getDialogPane().setContent(vbox);
        startDialog.showAndWait();
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
            getPlayerTexts();
        });
        chanceAndCommunityChestDialog.show();
    }

    //done
    private void createCardDialog() {
        Dialog cardDialog = new Dialog();
        cardDialog.setDialogPane(cardScreen);

        VBox vbox = new VBox();
        Text text = new Text(gameEngine.getCardInfo());
        vbox.getChildren().add(text);

        Node okButton = cardDialog.getDialogPane().lookupButton(ButtonType.OK);
        ((Button)okButton).setText("Do");
        ((Button)okButton).setOnAction(event -> {
            if(gameEngine.implementCard()) {
                cardDialog.close();
                updateSquares();
                getPlayerTexts();
                checkSquare();
            }
            else {
                cardDialog.close();
                updateSquares();
                getPlayerTexts();
            }
        });
        cardDialog.getDialogPane().setContent(vbox);
        cardDialog.show();
    }

    //done check
    //todo jail update
    private void createJokerDialog() {
        Dialog jokerDialog = new Dialog();
        jokerDialog.setDialogPane(jokerScreen);

        VBox vBox = (VBox) jokerDialog.getDialogPane().getContent();

        VBox jokerContent = gameEngine.getJokerContent();
        String jokerName = gameEngine.getJokerName();

        System.out.println(jokerDialog.getDialogPane().getChildren());
        System.out.println(jokerDialog.getDialogPane());
        System.out.println(jokerDialog.getDialogPane().getChildren().get(0));

        Text header = (Text) jokerDialog.getDialogPane().getChildren().get(0);

        vBox.getChildren().add(jokerContent);

        header.setText(jokerName);
        jokerDialog.getDialogPane().getChildren().get(1);

        Node okButton = jokerDialog.getDialogPane().lookupButton(ButtonType.OK);
        ((Button)okButton).setText("Do");
        ((Button)okButton).setOnAction(event -> {
            if(gameEngine.jokerActions()) {
                jokerDialog.close();
                updateSquares();
                getPlayerTexts();
                checkSquare();
            }
            else {
                if(gameEngine.getCurrentPlayer().isInJail())
                {
                    jailFinishDialog();
                }
                jokerDialog.close();
                updateSquares();
                getPlayerTexts();
            }
        });

        jokerDialog.showAndWait();
    }

    //done
    private void jailFinishDialog() {
        Dialog jailFinish = new Dialog();
        jailFinish.setDialogPane(jailFinishScreen);

        VBox vBox = new VBox();
        Text text = new Text(gameEngine.jokerText());
        vBox.getChildren().add(text);

        Node okButton = jailFinish.getDialogPane().lookupButton(ButtonType.OK);
        ((Button)okButton).setText("Do");
        ((Button)okButton).setOnAction(event -> {
            gameEngine.releasePlayer(false);
            jailFinish.close();
            updateSquares();
            getPlayerTexts();
        });
        jailFinish.getDialogPane().setContent(vBox);
        jailFinish.show();
    }

    // todo done combo box düzeltilecek
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
            getPlayerTexts();
        });
    }

    //done kontrol edelim
    private void createMortgageLiftDialog(int index) {
        Dialog mortgageLifting = new Dialog();
        mortgageLifting.setDialogPane(mortgageLiftingScreen);
        VBox vbox = gameEngine.getMortgageLiftingInfo(index);

        //cancel button

        Node closeButton = mortgageLifting.getDialogPane().lookupButton(ButtonType.CLOSE);
        ((Button)closeButton).setText("Lift mortgage later");
        ((Button)closeButton).setOnAction(event -> {
            gameEngine.liftMortgageLater(index);
            mortgageLifting.close();
            updateSquares();
            getPlayerTexts();
        });

        //ok button
        Node okButton = mortgageLifting.getDialogPane().lookupButton(ButtonType.OK);
        ((Button)okButton).setText("Lift mortgage now");
        ((Button)okButton).setOnAction(event -> {
            gameEngine.unmortgageProperty(index);
            mortgageLifting.close();
            updateSquares();
            getPlayerTexts();
        });

        mortgageLifting.getDialogPane().setContent(vbox);
        mortgageLifting.show();
    }

    private void createPropertyDialog(int index) {
        Dialog propertyDialog = new Dialog();
        propertyDialog.setDialogPane(propertyScreen);

        VBox mainVBox = (VBox) propertyDialog.getDialogPane().getContent();

        VBox deneme = (VBox) propertyDialog.getDialogPane().getContent();
        System.out.println(deneme.getChildren());

        System.out.println(propertyDialog.getDialogPane());
        System.out.println(propertyDialog.getDialogPane().getChildren());
        System.out.println(propertyDialog.getDialogPane().getContent());


        Text header = (Text) propertyDialog.getDialogPane().getChildren();
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
                        getPlayerTexts();
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
                        getPlayerTexts();
                    });
                    buttonBox.getChildren().add(mortgageBtn);
                    break;

                case "unmortgage":
                    Button unmortgageBtn = new Button("Unmortgage");
                    unmortgageBtn.setOnAction(event -> {
                        gameEngine.unmortgageProperty(index);
                        getPlayerTexts();
                        propertyDialog.close();
                    });
                    buttonBox.getChildren().add(unmortgageBtn);
                    break;

                case "add house":
                    Button addHouseBtn = new Button("Add House");
                    addHouseBtn.setOnAction(event -> {
                        gameEngine.addHouse(index);
                        getPlayerTexts();
                        propertyDialog.close();
                    });
                    buttonBox.getChildren().add(addHouseBtn);
                    break;

                case "sell house":
                    Button sellHouseBtn = new Button("Sell House");
                    sellHouseBtn.setOnAction(event -> {
                        gameEngine.sellHouse(index);
                        getPlayerTexts();
                        propertyDialog.close();
                    });
                    buttonBox.getChildren().add(sellHouseBtn);
                    break;

                case "add hotel":
                    Button addHotelBtn = new Button("Add hotel");
                    addHotelBtn.setOnAction(event -> {
                        gameEngine.addHotel(index);
                        getPlayerTexts();
                        propertyDialog.close();
                    });
                    buttonBox.getChildren().add(addHotelBtn);
                    break;

                case "sell hotel":
                    Button sellHotelBtn = new Button("Sell Hotel");
                    sellHotelBtn.setOnAction(event -> {
                        gameEngine.sellHotel(index);
                        getPlayerTexts();
                        propertyDialog.close();
                    });
                    buttonBox.getChildren().add(sellHotelBtn);
                    break;

                case "pay rent":
                    Button rentBtn = new Button("Pay Rent");
                    rentBtn.setOnAction(event -> {
                        gameEngine.takeRent();
                        getPlayerTexts();
                        propertyDialog.close();
                    });
                    buttonBox.getChildren().add(rentBtn);
                    break;
            }
        }
        propertyDialog.showAndWait();
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
                    ArrayList<Image> pawns = new ArrayList<>();

                    for (int i = 0; i < playerPositions.size(); i++) {
                        int position = playerPositions.get(i);
                        if (col >= row) {
                            if (sum == position) {
                                playersOnTile = playersOnTile + gameEngine.getPlayerNames().get(i) + "\n";
                                pawns.add(gameEngine.getPlayers().get(i).getIcon());
                            }
                        } else {
                            if (40 - sum == position) {
                                playersOnTile = playersOnTile + gameEngine.getPlayerNames().get(i) + "\n";
                                pawns.add(gameEngine.getPlayers().get(i).getIcon());
                            }
                        }
                    }

                    Text text = new Text(playersOnTile);
                    Font font2 = Font.font("Source Sans Pro", 10);
                    text.setFont(font2); //size of the player texts

                    if ((row == 0) | (col == 0) | (row == 10) | (col == 10)) {
                        for(Image image : pawns) {
                            ImageView view = new ImageView(image);
                            stackPane.getChildren().add(view);
                        }
                       // stackPane.getChildren().addAll(text); //try
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
                    //make the tiles clickable
                    tile.setOnMouseClicked(event -> {
                        position = pos;
                        System.out.println(gameEngine.canClick(pos));
                        if(gameEngine.canClick(pos)) {
                            createPropertyDialog(pos);
                        }
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
                    ArrayList<Image> pawns = new ArrayList<>();

                    for (int i = 0; i < 40; i++){
                        if (stackPane.getChildren().size() > 1 )
                            stackPane.getChildren().remove(1);
                    }

                    for (int i = 0; i < playerPositions.size(); i++) {
                        int position = playerPositions.get(i);
                        if (col >= row) {
                            if (sum == position) {
                                playersOnTile = playersOnTile + gameEngine.getPlayerNames().get(i) + "\n";
                                pawns.add(gameEngine.getPlayers().get(i).getIcon());
                            }
                        } else {
                            if (40 - sum == position) {
                                playersOnTile = playersOnTile + gameEngine.getPlayerNames().get(i) + "\n";
                                pawns.add(gameEngine.getPlayers().get(i).getIcon());
                            }

                        }
                    }

                    Text text = new Text(playersOnTile);
                    Font font2 = Font.font("Source Sans Pro", 15);
                    text.setFont(font2); //size of the player texts

                    if ((row == 0) | (col == 0) | (row == 10) | (col == 10)) {
                        for(Image image : pawns) {
                            ImageView view = new ImageView(image);
                            stackPane.getChildren().add(view);
                        }
                       // stackPane.getChildren().add(text);
                    }
                }
            }
        }
    }

    public Scene getScene() { return scene; }

}
