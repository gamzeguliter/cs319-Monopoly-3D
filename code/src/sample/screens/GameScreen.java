package sample.screens;

import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import sample.FileManager;
import sample.entities.Board;
import sample.GameManager;
import sample.entities.Player;
import sample.ScreenManager;
import sample.squares.ChanceAndCommunityChest;
import sample.squares.Property;
import sample.squares.SquareType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static javafx.scene.paint.Color.rgb;

public class GameScreen extends Screen {

    // properties
    private Scene scene;
    GridPane boardPane;

    ArrayList<Text> playerTexts;
    //Text turnText;
    Button btnRollDice;
    Button btnEndTurn;
    Button btnResign;
    GameManager gameManager;
    Label turnText;
    Font font = Font.font("Source Sans Pro", 20);
    int position;
    AnchorPane gameScreen = FXMLLoader.load(getClass().getResource("../layouts/GameScreen.fxml"));
    DialogPane diceScreen = FXMLLoader.load(getClass().getResource("../layouts/diceScreen.fxml"));
    DialogPane propertyScreen = FXMLLoader.load(getClass().getResource("../layouts/propertyScreen.fxml"));
    DialogPane auctionOrSellScreen = FXMLLoader.load(getClass().getResource("../layouts/auctionOrSellScreen.fxml"));
    DialogPane jokerScreen = FXMLLoader.load(getClass().getResource("../layouts/jokerScreen.fxml"));
    DialogPane chanceAndCommunityChestScreen = FXMLLoader.load(getClass().getResource("../layouts/chanceAndCommunityChestScreen.fxml"));
    DialogPane mortgageLiftingScreen = FXMLLoader.load(getClass().getResource("../layouts/mortgageLiftingScreen.fxml"));
    DialogPane jailFinishScreen = FXMLLoader.load(getClass().getResource("../layouts/jailFinishScreen.fxml"));
    DialogPane cardScreen = FXMLLoader.load(getClass().getResource("../layouts/cardScreen.fxml"));
    DialogPane startScreen = FXMLLoader.load(getClass().getResource("../layouts/startScreen.fxml"));
    DialogPane resignScreen = FXMLLoader.load(getClass().getResource("../layouts/resignScreen.fxml"));
    DialogPane gameOverScreen = FXMLLoader.load(getClass().getResource("../layouts/gameOverScreen.fxml"));
    DialogPane bankruptScreen = FXMLLoader.load(getClass().getResource("../layouts/bankruptScreen.fxml"));
    DialogPane exitScreen = FXMLLoader.load(getClass().getResource("../layouts/exitScreen.fxml"));

    // constructors
    public GameScreen(ScreenManager screenManager) throws IOException {
        super(screenManager);
        gameManager = new GameManager();
        boardPane = getSquares();
        setScene();
    }

    public GameScreen(ScreenManager screenManager, Board board, ArrayList<Player> players) throws IOException {
        super(screenManager);
        gameManager = new GameManager(board, players);
        boardPane = getSquares();
        StackPane background = (StackPane) boardPane.getChildren().get(40);
        ImageView boardImage = new ImageView(gameManager.boardImage());
        boardImage.setFitWidth(575);
        boardImage.setFitHeight(575);
        background.getChildren().add(boardImage);
        setScene();
    }

    private void getPlayerTexts() {
        VBox vBox = (VBox) gameScreen.getChildrenUnmodifiable().get(1);
        VBox vBox2 = (VBox) vBox.getChildren().get(2);
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
            if(i < gameManager.getPlayers().size()) {
                Player player = gameManager.getPlayers().get(i);
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



    private void updateTurnText() {
        turnText.setText("Player Turn: " + gameManager.getCurrentPlayer().getName());
    }

    private void setScene() {
        scene = new Scene(gameScreen);

        VBox vBox = (VBox) gameScreen.getChildrenUnmodifiable().get(1);
        HBox exitBox = (HBox) vBox.getChildren().get(0);
        Button exitGameBtn = (Button) exitBox.getChildren().get(0);
        VBox vBox2 = (VBox) vBox.getChildren().get(2);
        HBox hBox = (HBox) vBox.getChildren().get(4);
        turnText = (Label) vBox.getChildren().get(3);
        btnRollDice = (Button) hBox.getChildren().get(0);
        btnEndTurn = (Button) hBox.getChildren().get(1);
        HBox hBox2 = (HBox) vBox2.getChildren().get(4);
        btnResign = (Button) hBox2.getChildren().get(0);

        getPlayerTexts();

        turnText.setText("Player Turn: " + gameManager.getCurrentPlayer().getName());

        //roll dice button
        btnRollDice.setText("Roll Dice");
        //initialize end turn as disabled
        btnEndTurn.setDisable(true);


        btnRollDice.setOnAction(event -> {
           /* if(gameEngine.getCurrentPlayer().isInJail()) {
                jailFinishDialog();
            }
           // else {*/
                createDiceDialog();
          //  }
            btnEndTurn.setDisable(false);
            btnRollDice.setDisable(true);
        });

        //end turn button
        //Label finalTurnText = turnText;
        btnEndTurn.setOnAction(event -> {
            if(gameManager.isBankrupt()) {
                createBankruptDialog();
            }
            else {
                if(gameManager.nextTurn()){
                    createGameOverDialog();
                }
            }
            updateTurnText();
            //finalTurnText.setText("Player Turn: " + gameEngine.getCurrentPlayer().getName());
            getPlayerTexts();
            btnRollDice.setDisable(false);
            btnEndTurn.setDisable(true);
        });

        //resign button
        btnResign.setOnAction(actionEvent -> {
            createResignDialog();
        });

        //exit game button
        exitGameBtn.setOnAction(actionEvent -> {
            createExitDialog();
        });

    }

    private void checkSquare() {
        if(gameManager.passesStart()) {
            createStartDialog();
            getPlayerTexts();
        }

        if (gameManager.getCurrentSquare().getType() == SquareType.PROPERTY) {
            createPropertyDialog(-1);

        } else if (gameManager.getCurrentSquare().getType() == SquareType.JOKER) {
            createJokerDialog();
        }
        else if(gameManager.getCurrentSquare().getType() == SquareType.CHANCEANDCOMMUNITYCHEST) {
            createChanceAndChestDialog();
        }
        //start square
        else {
            createStartDialog();
        }
        updateSquares();
        getPlayerTexts();
    }

    private void createExitDialog() {
        Dialog exitDialog = new Dialog();
        exitDialog.setDialogPane(exitScreen);

        VBox vbox = new VBox();
        Text bankrupt = new Text("Are you sure you want to exit the game?");
        Node okButton = exitDialog.getDialogPane().lookupButton(ButtonType.OK);
        ((Button)okButton).setOnAction(event -> {
            exitDialog.close();
            try {
                screenManager.changeScreen(new MainMenuScreen(screenManager));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        vbox.getChildren().addAll(bankrupt);
        exitDialog.getDialogPane().setContent(vbox);
        exitDialog.show();
    }


    //done
    private void createBankruptDialog() {
        Dialog bankrupt = new Dialog();
        bankrupt.setDialogPane(bankruptScreen);

        VBox vbox = new VBox();
        Text bankruptText = new Text("YOU LOST");

        Node okButton = bankrupt.getDialogPane().lookupButton(ButtonType.OK);
        ((Button) okButton).setOnAction(event -> {
            bankrupt.close();
            if(gameManager.lastPlayer()) {
                gameManager.resign();
                createGameOverDialog();
            }
            else {
                ArrayList<Integer> indexes = gameManager.sellPlayerProperties();

                for (int i = 0; i < indexes.size(); i++) {
                    createAuctionOrSellDialog(indexes.get(i), true);
                    updateSquares();
                    getPlayerTexts();
                }
                gameManager.resign();
                gameManager.nextTurn();
                updateSquares();
                getPlayerTexts();
                updateTurnText();
                /*if (gameEngine.checkWin()) {
                    createGameOverDialog();
                }*/
                btnEndTurn.setDisable(true);
                btnRollDice.setDisable(false);
            }
        });

        vbox.getChildren().addAll(bankruptText);
        bankrupt.getDialogPane().setContent(vbox);
        bankrupt.show();
    }



    //done check
    private void createGameOverDialog() {
        Dialog gameOverDialog = new Dialog();
        gameOverDialog.setDialogPane(gameOverScreen);

        VBox vbox = new VBox();
        Text winner = new Text(gameManager.getWinner() + " WINS!!!!");

        Node okButton = gameOverDialog.getDialogPane().lookupButton(ButtonType.OK);
        ((Button)okButton).setText("Go back to the main page");
        ((Button)okButton).setOnAction(event -> {
            gameOverDialog.close();
            try {
                screenManager.changeScreen(new MainMenuScreen(screenManager));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        vbox.getChildren().addAll(winner);
        gameOverDialog.getDialogPane().setContent(vbox);
        gameOverDialog.show();
    }

    private void createResignDialog() {
        Dialog resignDialog = new Dialog();
        resignDialog.setDialogPane(resignScreen);

        VBox vbox = new VBox();
        Text resignConfirmation = new Text("Are you sure you want to resign?");

        Node okButton = resignDialog.getDialogPane().lookupButton(ButtonType.OK);
        ((Button)okButton).setText("Yes");
        ((Button)okButton).setOnAction(event -> {
            resignDialog.close();
            ArrayList<Integer> indexes = gameManager.sellPlayerProperties();

            for (int i = 0; i < indexes.size(); i++) {
                createAuctionOrSellDialog(indexes.get(i), true);
                updateSquares();
                getPlayerTexts();
            }
            gameManager.resign();
            gameManager.nextTurn();
            updateSquares();
            getPlayerTexts();
            updateTurnText();
            if(gameManager.checkWin()) {
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
        int[] dieResult = gameManager.rollDice();
        Text die1 = (Text) vbox.getChildren().get(0);
        Text die2 = (Text) vbox.getChildren().get(1);
        die1.setText("Die 1: " + dieResult[0]);
        die2.setText("Die 2: " + dieResult[1]);

        Node okButton = diceDialog.getDialogPane().lookupButton(ButtonType.OK);
        ((Button)okButton).setOnAction(event -> {
            if(gameManager.playerInJail()) {
                if(dieResult[0] == dieResult[1]) {
                    gameManager.releasePlayer(true);
                    gameManager.movePlayer();
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
                gameManager.movePlayer();
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
        VBox vbox = gameManager.startInfo();
        Node okButton = startDialog.getDialogPane().lookupButton(ButtonType.OK);
        ((Button)okButton).setText("Collect");
        ((Button)okButton).setOnAction(event -> {
            gameManager.startAction();
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
            gameManager.drawCard();
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
        Text text = new Text(gameManager.getCardInfo());
        vbox.getChildren().add(text);

        Node okButton = cardDialog.getDialogPane().lookupButton(ButtonType.OK);
        ((Button)okButton).setText("Do");
        ((Button)okButton).setOnAction(event -> {
            if(gameManager.implementCard()) {
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


    private void createJokerDialog() {
        Dialog jokerDialog = new Dialog();
        jokerDialog.setDialogPane(jokerScreen);
        jokerDialog.getDialogPane().setHeader(new Text(gameManager.getJokerName(-1)));
        VBox jokerContent = gameManager.getJokerContent();

        Node okButton = jokerDialog.getDialogPane().lookupButton(ButtonType.OK);
        ((Button)okButton).setText("Do");
        ((Button)okButton).setOnAction(event -> {
            if(gameManager.jokerActions()) {
                jokerDialog.close();
                updateSquares();
                getPlayerTexts();
                checkSquare();
            }
            else {
                if(gameManager.getCurrentPlayer().isInJail())
                {
                    jailFinishDialog();
                }
                jokerDialog.close();
                updateSquares();
                getPlayerTexts();
            }
        });
        jokerDialog.getDialogPane().setContent(jokerContent);
        jokerDialog.showAndWait();
    }

    //done
    private void jailFinishDialog() {
        Dialog jailFinish = new Dialog();
        jailFinish.setDialogPane(jailFinishScreen);

        VBox vBox = new VBox();
        Text text = new Text(gameManager.jokerText());
        vBox.getChildren().add(text);

        Node okButton = jailFinish.getDialogPane().lookupButton(ButtonType.OK);
        ((Button)okButton).setText("Do");
        ((Button)okButton).setOnAction(event -> {
            gameManager.releasePlayer(false);
            jailFinish.close();
            updateSquares();
            getPlayerTexts();
        });
        jailFinish.getDialogPane().setContent(vBox);
        jailFinish.show();
    }

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

        playerSelection.setItems(FXCollections.observableArrayList(gameManager.playersInAuction()));
        TextField price = (TextField) priceBox.getChildren().get(1);

        if(index < 0) {
            propertyName.setText("Property: " + ((Property)(gameManager.getCurrentSquare())).getName());
        }
        else {
            propertyName.setText("Property: " + ((Property)(gameManager.getSquare(index))).getName());
        }

        if(isAuction) {
             header.setText("Auction Property");
        }
        else
            header.setText("Sell Property");

       // propertyName.setText("Property: " + ((Property)(gameEngine.getCurrentSquare())).getName());

        auctionOrSell.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                return new Pair<>((String) playerSelection.getValue(), price.getText());
            }
            return null;
        });

        //auction.show();
        Optional<Pair<String, String>> result = auctionOrSell.showAndWait();
        result.ifPresent(pair -> {
            if(isAuction) {
                gameManager.auctionProperty(index, (String) playerSelection.getValue(), Integer.parseInt(price.getText()) );
            }
            else {
                //selling a mortgaged property -- ask the new owner if they want to lift the mortgage
                gameManager.sellProperty(index, (String) playerSelection.getValue(), Integer.parseInt(price.getText()));
            }
            if(gameManager.soldMortgaged(index)) {
                createMortgageLiftDialog(index);
            }
            auctionOrSell.close();
            updateSquares();
            getPlayerTexts();
        });
    }

    //done kontrol edelim
    private void createMortgageLiftDialog(int index) {
        Dialog mortgageLifting = new Dialog();
        mortgageLifting.setDialogPane(mortgageLiftingScreen);
        VBox vbox = gameManager.getMortgageLiftingInfo(index);

        //cancel button
       /* if (mortgageLifting.getDialogPane().getButtonTypes().size() < 1)
            mortgageLifting.getDialogPane().getButtonTypes().add(ButtonType.CLOSE); */

        mortgageLifting.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Node closeButton = mortgageLifting.getDialogPane().lookupButton(ButtonType.CLOSE);
        ((Button)closeButton).setText("Lift mortgage later");
        ((Button)closeButton).setOnAction(event -> {
            gameManager.liftMortgageLater(index);
            mortgageLifting.close();
            updateSquares();
            getPlayerTexts();
        });

        //ok button
        Node okButton = mortgageLifting.getDialogPane().lookupButton(ButtonType.OK);
        ((Button)okButton).setText("Lift mortgage now");
        ((Button)okButton).setOnAction(event -> {
            gameManager.unmortgageProperty(index);
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

        VBox vBox = (VBox) propertyDialog.getDialogPane().getContent();
        VBox vBox2 = (VBox) vBox.getChildren().get(0);
        Text propertyName = (Text) vBox2.getChildren().get(0);

        propertyName.setText(gameManager.getPropertyName(index));

        if (vBox.getChildren().size() < 3)
            vBox.getChildren().add(gameManager.getPropertyContent(index));
        else
            vBox.getChildren().set(1, gameManager.getPropertyContent(index));

        Property property;
        if(index < 0 ) {
            property = (Property)(gameManager.getCurrentSquare());
        }
        else {
            property = (Property) (gameManager.getSquare(index));
        }

        //todo @öykü if owned --> arka plan owner, property isminin arkası color group olacak

        vBox2.setStyle("-fx-background-color: rgb(" +  (property.getColorGroup().getColor().getRed() * 255) + ", " + (property.getColorGroup().getColor().getGreen() * 255) + ", " + (property.getColorGroup().getColor().getBlue() * 255) + "); -fx-font: 'Source Sans Pro'; -fx-font-family: 'Source Sans Pro'; -fx-font-size: 30;");

        //todo @öykü if owned --> arka plan owner, property isminin arkası color group olacak

        vBox2.setStyle("-fx-background-color: rgb(" +  (property.getColorGroup().getColor().getRed() * 255)
                + ", " + (property.getColorGroup().getColor().getGreen() * 255) + ", "
                + (property.getColorGroup().getColor().getBlue() * 255)
                + "); -fx-font: 'Source Sans Pro'; -fx-font-family: 'Source Sans Pro'; -fx-font-size: 30;");

        if(property.isOwned()) {
            propertyDialog.getDialogPane().setStyle("-fx-background-color: rgb("
                    + (property.getOwner().getColor().getRed() * 255) + ", "
                    + (property.getOwner().getColor().getGreen() * 255) + ", "
                    + (property.getOwner().getColor().getBlue() * 255)
                    + "); -fx-font: 'Source Sans Pro'; -fx-font-family: 'Source Sans Pro'; -fx-font-size: 25;");
        }
        else {
            propertyDialog.getDialogPane().setStyle("-fx-background-color: rgb(182, 216, 184); -fx-font: 'Source Sans Pro'; -fx-font-family: 'Source Sans Pro'; -fx-font-size: 25;");
        }

        ArrayList<String> buttonNames = gameManager.getPropertyButtons(index);

        //add cancel button
        if (propertyDialog.getDialogPane().getButtonTypes().size() < 1)
            propertyDialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        Node closeButton = propertyDialog.getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.setDisable(true);

        //dialog.getDialogPane().getScene().getWindow().setOnCloseRequest(event -> event.consume());

        VBox buttonBox = new VBox();
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
                        gameManager.buyProperty();
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
                        gameManager.mortgageProperty(index);
                        propertyDialog.close();
                        getPlayerTexts();
                    });
                    buttonBox.getChildren().add(mortgageBtn);
                    break;

                case "unmortgage":
                    Button unmortgageBtn = new Button("Unmortgage");
                    unmortgageBtn.setOnAction(event -> {
                        gameManager.unmortgageProperty(index);
                        getPlayerTexts();
                        propertyDialog.close();
                    });
                    buttonBox.getChildren().add(unmortgageBtn);
                    break;

                case "add house":
                    Button addHouseBtn = new Button("Add House");
                    addHouseBtn.setOnAction(event -> {
                        gameManager.addHouse(index);
                        getPlayerTexts();
                        propertyDialog.close();
                    });
                    buttonBox.getChildren().add(addHouseBtn);
                    break;

                case "sell house":
                    Button sellHouseBtn = new Button("Sell House");
                    sellHouseBtn.setOnAction(event -> {
                        gameManager.sellHouse(index);
                        getPlayerTexts();
                        propertyDialog.close();
                    });
                    buttonBox.getChildren().add(sellHouseBtn);
                    break;

                case "add hotel":
                    Button addHotelBtn = new Button("Add hotel");
                    addHotelBtn.setOnAction(event -> {
                        gameManager.addHotel(index);
                        getPlayerTexts();
                        propertyDialog.close();
                    });
                    buttonBox.getChildren().add(addHotelBtn);
                    break;

                case "sell hotel":
                    Button sellHotelBtn = new Button("Sell Hotel");
                    sellHotelBtn.setOnAction(event -> {
                        gameManager.sellHotel(index);
                        getPlayerTexts();
                        propertyDialog.close();
                    });
                    buttonBox.getChildren().add(sellHotelBtn);
                    break;

                case "pay rent":
                    Button rentBtn = new Button("Pay Rent");
                    rentBtn.setOnAction(event -> {
                        gameManager.takeRent();
                        getPlayerTexts();
                        propertyDialog.close();
                    });
                    buttonBox.getChildren().add(rentBtn);
                    break;
            }
        }
        if (vBox.getChildren().size() < 3)
            vBox.getChildren().add(buttonBox);
        else
            vBox.getChildren().set(2,buttonBox);
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

                    System.out.println(pos +"----- " + gameManager.getSquare(pos).getType());
                    // create rectangle of correct color for tile
                    StackPane stackPane = (StackPane) gridPane.getChildren().get(pos);
                    Rectangle tile = (Rectangle) stackPane.getChildren().get(0);

                    Rectangle propertyRect = new Rectangle();
                    Pos propPos;

                    if ((pos > 0 && pos < 10) || (pos > 20 && pos < 30)) {
                        propertyRect.setWidth(60);
                        propertyRect.setHeight(30);
                        propPos = Pos.TOP_CENTER;
                    }
                    else if(pos > 10 && pos < 20)  {
                        propertyRect.setWidth(30);
                        propertyRect.setHeight(60);
                        propPos = Pos.CENTER_LEFT;
                    }
                    else if(pos > 30) {
                        propertyRect.setWidth(30);
                        propertyRect.setHeight(60);
                        propPos = Pos.CENTER_RIGHT;
                    }
                    else {
                        propertyRect.setWidth(90);
                        propertyRect.setHeight(30);
                        propPos = Pos.TOP_CENTER;
                    }

                    //set tile colors
                    System.out.println(gameManager.getSquare(pos).getType());

                    if (gameManager.getSquare(pos).getType() == SquareType.PROPERTY) {
                        Property property = (Property)(gameManager.getSquare(pos));
                        if (property.isOwned()) {
                            Player owner = property.getOwner();
                            tile.setFill(owner.getColor());
                        }
                        else
                            tile.setFill(Color.WHITESMOKE);
                        propertyRect.setFill(property.getColorGroup().getColor());
                    }
                    else if(gameManager.getSquare(pos).getType() == SquareType.JOKER){
                        tile.setFill(rgb(203, 142, 110));
                        Text text = new Text(gameManager.getJokerName(pos));
                        stackPane.getChildren().add(text);
                        propertyRect = null;
                    }
                    else if(gameManager.getSquare(pos).getType() == SquareType.CHANCEANDCOMMUNITYCHEST){
                        String name = "";
                            if(((ChanceAndCommunityChest)(gameManager.getSquare(pos))).isChance()) {
                                name = "chance";
                            }
                            else {
                                name = "chest";
                            }
                        Image image = FileManager.getImage("src/sample/icons/" + name + ".png", 50, 50);
                        ImageView imageView = new ImageView(image);
                        stackPane.getChildren().add(imageView);
                        tile.setFill(rgb(99, 142, 99));
                        propertyRect = null;
                    }
                    else { //start square
                        Image image = FileManager.getImage("src/sample/icons/go.png", 90, 90);
                        ImageView imageView = new ImageView(image);
                        stackPane.getChildren().add(imageView);
                        tile.setFill(Color.ALICEBLUE);
                        propertyRect = null;
                    }

                    // find players on tile and set text
                    ArrayList<Integer> playerPositions = gameManager.getPlayerPositions();
                    String playersOnTile = "";
                    ArrayList<Image> pawns = new ArrayList<>();

                    for (int i = 0; i < playerPositions.size(); i++) {
                        int position = playerPositions.get(i);
                        if (col >= row) {
                            if (sum == position) {
                                playersOnTile = playersOnTile + gameManager.getPlayerNames().get(i) + "\n";
                                pawns.add(gameManager.getPlayers().get(i).getIcon());
                            }
                        } else {
                            if (40 - sum == position) {
                                playersOnTile = playersOnTile + gameManager.getPlayerNames().get(i) + "\n";
                                pawns.add(gameManager.getPlayers().get(i).getIcon());
                            }
                        }
                    }

                    Text text = new Text(playersOnTile);
                    Font font2 = Font.font("Source Sans Pro", 10);
                    text.setFont(font2); //size of the player texts

                    if ((row == 0) | (col == 0) | (row == 10) | (col == 10)) {
                        if(propertyRect != null) {
                            stackPane.getChildren().add(propertyRect);
                            StackPane.setAlignment(propertyRect, propPos);
                        }
                        for(Image image : pawns) {
                            ImageView view = new ImageView(image);
                            view.setFitHeight(50);
                            view.setFitWidth(50);
                            stackPane.getChildren().add(view);
                        }
                        stackPane.getChildren().addAll(text);
                    }
                }
            }
        }
        return gridPane;
    }

    //updates the board tiles on screen
    private void updateSquares() {

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

                    //make the tiles clickable
                    tile.setOnMouseClicked(event -> {
                        position = pos;
                        System.out.println(gameManager.canClick(pos));
                        if(gameManager.canClick(pos)) {
                            createPropertyDialog(pos);
                        }
                    });

                    //determine square colors
                    Rectangle propertyRect = new Rectangle();
                    Pos propPos;

                    if ((pos > 0 && pos < 10) || (pos > 20 && pos < 30)) {
                        propertyRect.setWidth(60);
                        propertyRect.setHeight(20);
                        propPos = Pos.TOP_CENTER;
                    }
                    else if(pos > 10 && pos < 20)  {
                        propertyRect.setWidth(20);
                        propertyRect.setHeight(60);
                        propPos = Pos.CENTER_LEFT;
                    }
                    else if(pos > 30) {
                        propertyRect.setWidth(20);
                        propertyRect.setHeight(60);
                        propPos = Pos.CENTER_RIGHT;
                    }
                    else {
                        propertyRect.setWidth(90);
                        propertyRect.setHeight(20);
                        propPos = Pos.TOP_CENTER;
                    }

                    String jokerText = "";
                    if (gameManager.getSquare(pos).getType() == SquareType.PROPERTY) {
                        Property property = (Property) gameManager.getSquare(pos);
                        if (property.isOwned()) {
                            Player owner = property.getOwner();
                            tile.setFill(owner.getColor());
                        }
                        else
                            tile.setFill(Color.WHITESMOKE);
                        propertyRect.setFill(property.getColorGroup().getColor());
                    }

                    else if(gameManager.getSquare(pos).getType() == SquareType.JOKER){
                        propertyRect = null;
                        Text text = new Text(gameManager.getJokerName(pos));
                        stackPane.getChildren().add(text);
                        tile.setFill(rgb(203, 142, 110));
                    }

                    else if(gameManager.getSquare(pos).getType() == SquareType.CHANCEANDCOMMUNITYCHEST){
                        String name = "";
                        if(((ChanceAndCommunityChest)(gameManager.getSquare(pos))).isChance()) {
                            name = "chance";
                        }
                        else {
                            name = "chest";
                        }
                        Image image = FileManager.getImage("src/sample/icons/" + name + ".png", 50, 50);
                        ImageView imageView = new ImageView(image);
                        stackPane.getChildren().add(imageView);
                        propertyRect = null;
                        tile.setFill(rgb(99, 142, 99));
                    }
                    else {
                        propertyRect = null;
                        Image image = FileManager.getImage("src/sample/icons/go.png", 90, 90);
                        ImageView imageView = new ImageView(image);
                        stackPane.getChildren().add(imageView);
                        tile.setFill(Color.ALICEBLUE);
                    }

                    // find players on tile and set text
                    ArrayList<Integer> playerPositions = gameManager.getPlayerPositions();
                    String playersOnTile = "";
                    ArrayList<Image> pawns = new ArrayList<>();

                    for (int i = 0; i < 40; i++){
                        if (stackPane.getChildren().size() > 2 )
                            stackPane.getChildren().remove(1);
                    }

                    for (int i = 0; i < playerPositions.size(); i++) {
                        int position = playerPositions.get(i);
                        if (col >= row) {
                            if (sum == position) {
                                playersOnTile = playersOnTile + gameManager.getPlayerNames().get(i) + "\n";
                                pawns.add(gameManager.getPlayers().get(i).getIcon());
                            }
                        } else {
                            if (40 - sum == position) {
                                playersOnTile = playersOnTile + gameManager.getPlayerNames().get(i) + "\n";
                                pawns.add(gameManager.getPlayers().get(i).getIcon());
                            }

                        }
                    }

                    Text text = new Text(playersOnTile);
                    Font font2 = Font.font("Source Sans Pro", 15);
                    text.setFont(font2); //size of the player texts

                    if ((row == 0) | (col == 0) | (row == 10) | (col == 10)) {
                        if(propertyRect != null) {
                            stackPane.getChildren().add(propertyRect);
                            StackPane.setAlignment(propertyRect, propPos);
                        }
                        for(Image image : pawns) {
                            ImageView view = new ImageView(image);
                            view.setFitHeight(50);
                            view.setFitWidth(50);
                            stackPane.getChildren().add(view);
                        }
                       stackPane.getChildren().add(text);
                    }
                }
            }
        }
    }

    public Scene getScene() { return scene; }

}
