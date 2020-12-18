package sample.screens;

import javafx.scene.Scene;
import sample.ScreenManager;

public abstract class Screen {
    protected Scene scene;
    ScreenManager screenManager;

    public Screen(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    public abstract Scene getScene();
}
