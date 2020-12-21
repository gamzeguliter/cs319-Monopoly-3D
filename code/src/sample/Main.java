package sample;

import sample.entities.Board;

public class Main {

    public static void main(String[] args) {
        // generates default board so that it always exists
        FileManager.writeBoardToFolder(new Board());
        ScreenManager screenManager = ScreenManager.getInstance();
        screenManager.launchWithoutArgs();
    }

}
