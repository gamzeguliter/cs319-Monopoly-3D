package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    int position = 0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Creating a Scene by passing the group object, height and width
        GameScreen gameScreen = new GameScreen();
        Scene scene = gameScreen.getScene();

        //setting color to the scene
        scene.setFill(Color.LIGHTGRAY);

        //Setting the title to Stage.
        primaryStage.setTitle("Sample Application");

        //Adding the scene to Stage
        primaryStage.setScene(scene);

        //Displaying the contents of the stage
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
