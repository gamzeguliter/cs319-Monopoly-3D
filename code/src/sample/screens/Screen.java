package sample.screens;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class Screen {
    Scene scene;

    public Screen() {
    }

    public abstract Scene drawScene() throws IOException;
}
