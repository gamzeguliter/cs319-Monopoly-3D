package sample.screens;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import sample.ScreenManager;

import java.io.IOException;

public class MainMenuScreen extends Screen {

    int width = 1366;
    int height = 768;

    public MainMenuScreen(ScreenManager screenManager) {
        super(screenManager);
        initializeScene();
    }

    private void initializeScene() {
        GridPane buttonsGrid = new GridPane();
        buttonsGrid.setAlignment(Pos.CENTER);
        buttonsGrid.setVgap(15.0f);

        double buttonWidth = 200;
        double buttonHeight = 50;

        Button btnPlayAGame = new Button("Play a Game");
        btnPlayAGame.setPrefSize(buttonWidth, buttonHeight);
        btnPlayAGame.setOnAction(actionEvent -> dispatchPlayAGame());

        Button btnEditABoard = new Button("Edit a Board");
        btnEditABoard.setPrefSize(buttonWidth, buttonHeight);
        btnEditABoard.setOnAction(actionEvent -> dispatchEditABoard());

        Button btnViewHelp = new Button("View Help");
        btnViewHelp.setPrefSize(buttonWidth, buttonHeight);
        btnViewHelp.setOnAction(actionEvent -> dispatchViewHelp());

        Button btnViewCredits = new Button("View Credits");
        btnViewCredits.setPrefSize(buttonWidth, buttonHeight);
        btnViewCredits.setOnAction(actionEvent -> dispatchViewCredits());

        Button btnExit = new Button("Exit");
        btnExit.setPrefSize(buttonWidth, buttonHeight);
        btnExit.setOnAction(actionEvent -> dispatchExit());

        buttonsGrid.addColumn(0,
                btnPlayAGame,
                btnEditABoard,
                btnViewHelp,
                btnViewCredits,
                btnExit);

        StackPane pane = new StackPane();
        pane.getChildren().add(buttonsGrid);

        scene = new Scene(pane, width, height);
    }


    @Override
    public Scene getScene() {
        return scene;
    }

    private void dispatchPlayAGame() {
        screenManager.changeScreen(new BoardSelectionScreen(screenManager));
    }

    private void dispatchEditABoard() {
        Screen nextScreen;
        try {
            nextScreen = new EditorScreen(screenManager);
            screenManager.changeScreen(nextScreen);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dispatchViewHelp() {

    }

    private void dispatchViewCredits() {

    }

    private void dispatchExit() {
        Platform.exit();
    }
}
