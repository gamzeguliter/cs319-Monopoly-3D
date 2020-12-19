package sample;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Utils {

    public static Image getImage(String relativePath, int width, int height) {
        InputStream stream;
        try {
            stream = new FileInputStream(System.getProperty("user.dir") + "/" + relativePath);
            return new Image(stream, width, height, false, false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
