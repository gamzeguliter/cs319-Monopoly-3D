package sample;

import javafx.scene.paint.Color;
import sample.squares.ChanceAndCommunityChest;
import sample.squares.*;

import java.util.ArrayList;

public class Board {

    // properties
    private String name;
    Square[] squares;
    CardDeck chanceDeck;
    CardDeck chestDeck;
    // TODO: make this a <String, ColorGroup> map?
    ArrayList<ColorGroup> colorGroups;
    String currency;
    /*
    property array -?
        aynı name'de 2 group a izin verme!
        property'i colorGroupa koyarken, property içindeki colorgroup ismine bak

        property(name, ...., string colorgroup)

       if(colorgroup yoksa yarat, varsa içindeki property array e bu property 'i ekle
     */


    // constructors
    // TODO: this exists so i can create empty board!!, change later
    public Board(int empty) {

    }

    // default constructor
    public Board() {
        colorGroups = createGroups();
        squares = testSquares();
        name = "defaultName";
        chestDeck = new CardDeck();
        chestDeck.generateChestCardDeck();
        chanceDeck = new CardDeck();
        chanceDeck.generateChanceCardDeck();
        currency = "Meteors";
    }

    public Card drawChanceCard() {
        return chanceDeck.drawCard();
    }

    public Card drawChestCard() {
        return chestDeck.drawCard();
    }

    public Board(String name) {
        colorGroups = createGroups();
        squares = testSquares();
        this.name = name;
    }

    private ArrayList<ColorGroup> createGroups() {
        ArrayList<ColorGroup> colors = new ArrayList<ColorGroup>();

        ColorGroup red = new ColorGroup("Red");
        red.setColor(Color.NAVAJOWHITE);
        colors.add(red);

        ColorGroup pink = new ColorGroup("Pink");
        pink.setColor(Color.HOTPINK);
        colors.add(pink);

        return colors;
    }


    //TODO load board constructor

    // private methods
    public SquareType getSquareType(int squareNo) {
        return squares[squareNo].getType();
    }

    String[] propertyNames = {"New York", "Boston", "Paris", "Copenhagen", "Berlin", "İstanbul",
        "Ankara", "Chicago", "Rome", "Milan", "London", "Seul", "Beijing", "Luzern", "Bern", "Oslo",
        "Barcelona", "Madrid", "Amsterdam", "Munich"};

    private Square[] testSquares() {
        Square[] squares = new Square[40];
        squares[0] = new Start(20);
        for (int i = 1; i < 40; i++) {
            if(i % 4 == 0)
                squares[i] = new Joker(9, 10, 0,"");
            else if(i % 4 == 1)
                squares[i] = new ChanceAndCommunityChest(true);
            else if (i % 4 == 2) {
                Property newProp = new Property(propertyNames[i % 20], colorGroups.get(0), 100, 80, 50);
                squares[i] = newProp;
                colorGroups.get(0).addProperty(newProp);
            }
            else {
                Property newProp = new Property(propertyNames[i % 20], colorGroups.get(1), 100, 80, 50);
                squares[i] = newProp;
                colorGroups.get(1).addProperty(newProp);
            }
        }
        return squares;
    }

    // getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public ColorGroup getColorGroup(String groupName) {
        for (ColorGroup colorGroup : colorGroups){
            if (colorGroup.getGroupName().equals(groupName)) {
                return colorGroup;
            }
        }
        System.out.println("ERROR: Could not find ColorGroup in colorGroups: " + groupName);
        return null;
    }

    public ArrayList<ColorGroup> getColorGroups() {
        return colorGroups;
    }

    public void setColorGroups(ArrayList<ColorGroup> colorGroups) {
        this.colorGroups = colorGroups;
    }

    public Square[] getSquares() {
        return squares;
    }

    public void setSquares(Square[] squares) {
        this.squares = squares;
    }
}
