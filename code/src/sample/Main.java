package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.screens.EditorScreen;
import sample.screens.GameScreen;

public class Main extends Application {

    int position = 0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Stage gameStage, editStage;
        //Creating a Scene by passing the group object, height and width
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
        Board board = new Board("default2");
        FileManager.writeBoardToFolder(board);
    }
}
