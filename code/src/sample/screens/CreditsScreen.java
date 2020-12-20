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
    AnchorPane creditsScreen = FXMLLoader.load(getClass().getResource("../layouts/CreditsScreen.fxml"));

    public CreditsScreen(ScreenManager screenManager) throws IOException {
        super(screenManager);
        infoLabel = new Label("CREDITS\nCansu Moran\nElif Gamze Güliter\nMelisa Taşpınar\nÖykü Irmak Hatipoğlu\nYiğit Gürses");
        initializeScene();
    }

    private void initializeScene() {
        VBox vbox = (VBox) creditsScreen.getChildren().get(2);
        VBox vbox2 = (VBox) creditsScreen.getChildren().get(1);
        Button goBack = (Button) vbox2.getChildren().get(0); //todo go back goes back

        infoLabel.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(infoLabel);
        vbox.setAlignment(Pos.CENTER);
        scene = new Scene(creditsScreen);
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}