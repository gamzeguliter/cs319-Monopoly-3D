package sample;

import javafx.application.Application;
import javafx.stage.Stage;
import sample.entities.Board;
import sample.screens.MainMenuScreen;
import sample.screens.Screen;

import java.io.IOException;

public class ScreenManager extends Application {
    Screen screen;
    Stage stage;

    private static ScreenManager instance = new ScreenManager();

    public static ScreenManager getInstance() { return instance; }

    @Override
    public void start(Stage primaryStage) throws IOException {
        screen = new MainMenuScreen(this);
        stage = primaryStage;

        primaryStage.setScene(screen.getScene());
        primaryStage.show();
    }

    public void launchWithArgs(String[] args) {
        launch(args);
    }

    public void launchWithoutArgs() {
        String[] args = new String[0];
        launchWithArgs(args);
    }

    public void changeScreen(Screen nextScreen) {
        screen = nextScreen;
        stage.setScene(screen.getScene());
    }
}
