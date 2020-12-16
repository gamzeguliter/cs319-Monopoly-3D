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

public class Main extends Application {

    int position = 0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Stage gameStage, editStage;
        //Creating a Scene by passing the group object, height and width

        //ScreenBehaviour menuScreen = new MenuScreen();
        //ScreenBehaviour gameScreen = new GameScreen();

        /*ScreenBehaviour menuScreen = new MenuScreen();
        ScreenBehaviour menuScreen = new MenuScreen();*/

        //Screen currentScreen = new Screen(menuScreen);
        //if(((MenuScreen)menuScreen).clickedPlay()) {
        //    currentScreen.setBehaviour(gameScreen);
        //}
        //currentScreen.setBehaviour(gameScreen);
        //primaryStage.setScene(currentScreen.drawScreen());


        GameScreen gameScreen = new GameScreen();
        Scene gameScene = gameScreen.getScene();

        EditorScreen editorScreen = new EditorScreen();
        Scene editorScene = editorScreen.getScene();

        //setting color to the scene
        gameScene.setFill(Color.rgb(203, 227, 199));
        editorScene.setFill(Color.rgb(203, 227, 199));

        //Setting the title to Stage.
        primaryStage.setTitle("Monopoly");
        //gameStage.setTitle();

        //MenuScreen menuScreen = new MenuScreen();
        //Scene menuScene = menuScreen.getScene();
        //Adding the scene to Stage
        primaryStage.setScene(editorScene);//game or editor--burdan değiştirin


        //Displaying the contents of the stage
        primaryStage.show();

    }


    public static void main(String[] args) {
        //fileManagerTest();

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
