package sample;

import javafx.scene.image.Image;

public class Pawn {
    private String name;
    private Image image;
    
    public Pawn(String name, Image image){
        this.name = name;
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}
