package sample.screens;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import sample.ScreenManager;

import java.io.IOException;

public class MainMenuScreen extends Screen {

    AnchorPane mainMenuScreen = FXMLLoader.load(getClass().getResource("../layouts/MainMenuScreen.fxml"));

    public MainMenuScreen(ScreenManager screenManager) throws IOException {
        super(screenManager);
        initializeScene();
    }

    private void initializeScene() {
        VBox buttonBox = (VBox) mainMenuScreen.getChildren().get(2);

        Button btnPlayAGame = (Button) buttonBox.getChildren().get(0);
        btnPlayAGame.setOnAction(actionEvent -> {
            try {
                dispatchPlayAGame();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Button btnEditABoard = (Button) buttonBox.getChildren().get(1);
        btnEditABoard.setOnAction(actionEvent -> {
            try {
                dispatchEditABoard();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Button btnViewHelp = (Button) buttonBox.getChildren().get(2);
        btnViewHelp.setOnAction(actionEvent -> dispatchViewHelp());

        Button btnViewCredits = (Button) buttonBox.getChildren().get(3);
        btnViewCredits.setOnAction(actionEvent -> dispatchViewCredits());

        Button btnExit = (Button) buttonBox.getChildren().get(4);
        btnExit.setOnAction(actionEvent -> dispatchExit());

        scene = new Scene(mainMenuScreen);
    }


    @Override
    public Scene getScene() {
        return scene;
    }

    private void dispatchPlayAGame() throws IOException {

        //screenManager.changeScreen(new BoardSelectionScreen(screenManager));
        screenManager.changeScreen(new BoardSelectionScreen(screenManager, "play"));
    }

    private void dispatchEditABoard() throws IOException {
        screenManager.changeScreen(new BoardSelectionScreen(screenManager, "edit"));
    }

    private void dispatchViewHelp() {
        try{ screenManager.changeScreen(new HelpScreen(screenManager) ); } catch(Exception e){

        }
    }

    private void dispatchViewCredits() {
        try{ screenManager.changeScreen(new CreditsScreen(screenManager) ); } catch(Exception e){}
    }

    private void dispatchExit() {
        Platform.exit();
    }
}
