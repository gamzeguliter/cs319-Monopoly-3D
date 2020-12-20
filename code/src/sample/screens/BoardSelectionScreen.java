package sample.screens;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import sample.entities.Board;
import sample.ScreenManager;
import sample.FileManager;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class BoardSelectionScreen extends Screen {

    AnchorPane boardSelectionScreen = FXMLLoader.load(getClass().getResource("../layouts/BoardSelectionScreen.fxml"));
    int imWidth = 100;
    int imHeight = 100;

    ArrayList<String> boardNames;

    ScrollPane boardsScrollPane;
    HBox boardsBox;

    String cameFrom;

    public BoardSelectionScreen(ScreenManager screenManager, String cameFrom) throws IOException {
        super(screenManager);
        this.cameFrom = cameFrom;
        initializeScene();
    }

    private void initializeScene() {
        VBox mainPane = (VBox) boardSelectionScreen.getChildren().get(1);

        boardsScrollPane = (ScrollPane) mainPane.getChildren().get(1);

        boardsBox = (HBox) boardsScrollPane.getContent();

        // TODO: get rid of outer for loop when we have more boards
        for (int i = 0; i < 20; i++) {
            boardNames = getBoardNames();
            for (String boardName : boardNames) {
                VBox board = getBoardBox(boardName);
                board.setBorder(new Border(new BorderStroke(Color.BLACK,
                                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                boardsBox.getChildren().add(board);
            }
        }

        boardsScrollPane.setContent(boardsBox);

        // TODO: if selection is for editor, add new board button

        scene = new Scene(boardSelectionScreen);
    }

    private VBox getBoardBox(String boardName) {
        VBox mainBox = new VBox();
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setSpacing(10);

        Image boardImage = FileManager.getBoardIcon(boardName);
        ImageView boardImageView = new ImageView(boardImage);
        Label boardLabel = new Label(boardName);
        Button btnSelect = new Button("Select");
        btnSelect.setOnAction(actionEvent -> {
            try {
                selectBoard(mainBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        mainBox.getChildren().addAll(boardImageView, boardLabel, btnSelect);

        return mainBox;
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    private ArrayList<String> getBoardNames() {
        String boardsDirectory = System.getProperty("user.dir") + "/boards";

        File file = new File(boardsDirectory);
        String[] boardNames = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });

        return new ArrayList<String>(Arrays.asList(boardNames));
    }

    private void selectBoard(VBox boardBox) throws IOException {
        String boardName = getBoardNameFromBoardBox(boardBox);

        if (cameFrom.equals("play")) {
            PlayerManagerScreen playerManagerScreen = new PlayerManagerScreen(screenManager, boardName);
            screenManager.changeScreen(playerManagerScreen);
        } else if (cameFrom.equals("edit")) {
            Board board = FileManager.readBoardFromFolder(boardName);
            board.updatePropertyGroups();
            EditorScreen editorScreen = new EditorScreen(screenManager, board);
            screenManager.changeScreen(editorScreen);
        }
    }

    private String getBoardNameFromBoardBox(VBox boardBox) {
        int index = boardsBox.getChildren().indexOf(boardBox);
        return boardNames.get(index);
    }
}
