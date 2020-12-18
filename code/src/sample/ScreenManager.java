package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import sample.screens.EditorScreen;
import sample.screens.GameScreen;
import sample.screens.MainMenuScreen;

public class ScreenManager extends Application {

    int position = 0;

    Screen screen;

    @Override
    public void start(Stage primaryStage) throws Exception{
        MainMenuScreen mainMenuScreen = new MainMenuScreen();
        Scene mainMenuScene = mainMenuScreen.getScene();

        //Adding the scene to Stage
        primaryStage.setScene(mainMenuScene);//game or editor--burdan değiştirin

        //Displaying the contents of the stage
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    private static void fileManagerTest() {
        JSONObject jo = new JSONObject();
        JSONArray jr = new JSONArray();
        jr.put(1);
        jr.put(2);
        jr.put(3);
        jo.put("arr", jr);

        System.out.println(((JSONArray) jo.get("arr")).get(1));
    }
}
