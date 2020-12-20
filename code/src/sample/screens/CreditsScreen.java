package sample.screens;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import sample.ScreenManager;


import java.io.IOException;

public class CreditsScreen extends Screen {

    private Label infoLabel;
    private Label creditsLabel;
    AnchorPane creditsScreen = FXMLLoader.load(getClass().getResource("../layouts/CreditsScreen.fxml"));

    public CreditsScreen(ScreenManager screenManager) throws IOException {
        super(screenManager);
        creditsLabel = new Label("CREDITS");
        infoLabel = new Label("Cansu Moran\nElif Gamze Güliter\nMelisa Taşpınar\nÖykü Irmak Hatipoğlu\nYiğit Gürses");
        initializeScene();
    }

    private void initializeScene() {
        VBox vbox = (VBox) creditsScreen.getChildren().get(2);
        VBox vbox2 = (VBox) creditsScreen.getChildren().get(1);
        Button goBack = (Button) vbox2.getChildren().get(0);
        goBack.setOnAction(action -> {
            try {
                screenManager.changeScreen(new MainMenuScreen(screenManager));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        infoLabel.setAlignment(Pos.CENTER);
        creditsLabel.setAlignment(Pos.CENTER);
        creditsLabel.setStyle("-fx-font-size: 60;");
        vbox.getChildren().addAll(creditsLabel, infoLabel);
        vbox.setAlignment(Pos.CENTER);
        scene = new Scene(creditsScreen);
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}