package sample.screens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import sample.ScreenManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;


public class PlayerManagerScreen extends Screen {

    Scene scene;
    String boardName = "default";

    int width = 1366;
    int height = 768;

    HBox playersBox;
    Label infoLabel;
    int playerCount;

    ArrayList<TextField> nameFields;
    ArrayList<ImageView> iconViews;
    ArrayList<Integer> iconChoices;
    ArrayList<ColorPicker> colorPickers;

    PlayerManagerScreen(ScreenManager screenManager, String boardName) {
        super(screenManager);
        this.boardName = boardName;
        playerCount = 1;
        nameFields = new ArrayList<TextField>();
        iconViews = new ArrayList<ImageView>();
        iconChoices = new ArrayList<Integer>();
        colorPickers = new ArrayList<ColorPicker>();
        infoLabel = new Label("");
        initializeScene();
    }

    private void initializeScene() {
        VBox mainBox = new VBox();
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setSpacing(25);

        playersBox = new HBox();
        playersBox.setAlignment(Pos.CENTER);
        playersBox.setMaxHeight(height/2);
        playersBox.setSpacing(20f);

        iconChoices.add(0);
        VBox player1Box = generatePlayerBox("player1");

        Button btnAddPlayer = new Button("Add Player");
        btnAddPlayer.setOnAction(actionEvent -> addPlayer());

        playersBox.getChildren().addAll(player1Box, btnAddPlayer);

        Button btnDone = new Button("Done");
        btnDone.setOnAction(actionEvent -> done());

        mainBox.getChildren().addAll(playersBox, btnDone, infoLabel);

        StackPane pane = new StackPane();
        pane.getChildren().add(mainBox);

        scene = new Scene(pane, width, height);
    }

    private VBox generatePlayerBox(String playerName) {
        VBox vBox = new VBox();
        vBox.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        vBox.setPadding(new Insets(10f));
        vBox.setSpacing(10f);

        // name inputs
        HBox nameBox = new HBox();

        nameBox.setAlignment(Pos.CENTER);
        Label nameLabel = new Label("Name: ");
        TextField nameField = new TextField(playerName);
        nameFields.add(nameField);

        nameBox.getChildren().addAll(nameLabel, nameField);

        // icon inputs
        HBox iconBox = new HBox();

        iconBox.setAlignment(Pos.CENTER);
        Image iconImage = getImage(iconChoices.get(playerCount-1) + ".png");
        ImageView iconView = new ImageView(iconImage);
        iconViews.add(iconView);

        NextButton btnNextIcon = new NextButton();
        btnNextIcon.setText("next");
        btnNextIcon.playerNo = playerCount;
        btnNextIcon.setOnAction(actionEvent -> changeToNextIcon(btnNextIcon.playerNo));

        iconBox.getChildren().addAll(iconView, btnNextIcon);

        HBox colorBox = new HBox();
        colorBox.setAlignment(Pos.CENTER);

        Label colorLabel = new Label("Color: ");

        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setBackground(new Background(new BackgroundFill(Color.rgb(203, 227, 199), CornerRadii.EMPTY, Insets.EMPTY)));
        colorPicker.setStyle("-fx-font: 'Source Sans Pro';" + "-fx-font-family: 'Source Sans Pro';" + "-fx-font-size: 10;");
        colorPickers.add(colorPicker);

        colorBox.getChildren().addAll(colorLabel, colorPicker);

        vBox.getChildren().addAll(nameBox, iconBox, colorBox);

        return vBox;
    }

    private void addPlayer() {
        // set initial icon choice as player count
        iconChoices.add(playerCount);
        playerCount++;

        VBox playerBox = generatePlayerBox("player" + playerCount);
        playersBox.getChildren().add(playerCount-1, playerBox);

        // remove add player button
        if (playerCount == 4) {
            playersBox.getChildren().remove(4);
        }
    }

    private void changeToNextIcon(int playerNo) {
        iconChoices.set(playerNo-1, (iconChoices.get(playerNo-1) + 1) % 4);
        iconViews.get(playerNo-1).setImage(getImage(iconChoices.get(playerNo-1) + ".png"));
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    // TODO: do this with utils!
    private Image getImage(String imageName) {
        InputStream stream = null;
        try {
            stream = new FileInputStream(System.getProperty("user.dir") +
                    "\\boards\\" + boardName + "\\icons\\" + imageName);
            return new Image(stream, 100, 100, false, false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void done() {
        if ( !isValid() )
            return;

        // finalize
    }

    private boolean isValid() {
        for (int i = 0; i < playerCount-1; i++) {
            for (int j = i+1; j < playerCount; j++) {
                // check if two players have same name
                if (nameFields.get(i).getText().equals( nameFields.get(j).getText() )) {
                    infoLabel.setText("Two players have the same name!");
                    return false;
                }

                // check if two players have same icon
                if (iconChoices.get(i).equals(iconChoices.get(j))) {
                    infoLabel.setText("Two players have the same icon!");
                    return false;
                }

                // check if two players have same color
                if (colorPickers.get(i).getValue() == colorPickers.get(j).getValue()) {
                    infoLabel.setText("Two players have the same color!");
                    return false;
                }
            }
        }

        if (playerCount < 2) {
            infoLabel.setText("At least 2 players are needed!");
            return false;
        }

        return true;
    }
}

class NextButton extends Button {
    int playerNo;
}
