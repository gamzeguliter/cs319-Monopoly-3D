package sample;

import javafx.application.Application;
import javafx.stage.Stage;
import sample.screens.MainMenuScreen;
import sample.screens.Screen;

public class ScreenManager extends Application {
    Screen screen;
    Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        screen = new MainMenuScreen(this);
        stage = primaryStage;

        primaryStage.setScene(screen.getScene());
        primaryStage.show();
    }

    public static void main(String[] args) {
        FileManager.writeBoardToFolder(new Board());
        launch(args);
    }

    public void changeScreen(Screen nextScreen) {
        screen = nextScreen;
        stage.setScene(screen.getScene());
    }
}
