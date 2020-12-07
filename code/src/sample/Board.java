package sample;

import java.util.ArrayList;

public class Board {

    // properties
    Square[] squares;
    ArrayList<ColorGroup> colorGroups;
    /*
    property array -?
        aynı name'de 2 group a izin verme!
        property'i colorGroupa koyarken, property içindeki colorgroup ismine bak

        property(name, ...., string colorgroup)

       if(colorgroup yoksa yarat, varsa içindeki property array e bu property 'i ekle
     */


    // constructors
    // TODO: implement constructor with filemanager

    // default constructor
    public Board() {
        colorGroups = createGroups();
        squares = testSquares();
    }

    private ArrayList<ColorGroup> createGroups() {
        ArrayList<ColorGroup> colors = new ArrayList<ColorGroup>();
        colors.add(new ColorGroup("Red"));
        colors.add(new ColorGroup("Pink"));
        return colors;
    }


    //TODO load board constructor


    public ArrayList<ColorGroup> getColorGroups() {
        return colorGroups;
    }

    // private methods
    public SquareType getSquareType(int squareNo) {
        return squares[squareNo].getType();
    }

    /*public boolean canBuildHouse(Property property, Player currentPlayer) {
        for(ColorGroup group : colorGroups) {
            if(property.getColorGroup() == group.getGroupName()) {
                //check both if all props are owned by player and checks for controlled housing
                return group.addHouse(property, currentPlayer);
            }
        }
        return false;
    }
*/
    String[] propertyNames = {"New York", "Boston", "Paris", "Copenhagen", "Berlin", "İstanbul",
        "Ankara", "Chicago", "Rome", "Milan", "London", "Seul", "Beijing", "Luzern", "Bern", "Oslo",
        "Barcelona", "Madrid", "Amsterdam", "Munich"};

    private Square[] testSquares() {
        Square[] squares = new Square[40];
        squares[0] = new Start(20);
        for (int i = 1; i < 40; i++) {
            if(i % 4 == 0)
                squares[i] = new Joker(9, 10, 0);
            else if(i % 4 == 1)
                squares[i] = new ChanceAndCommunityChest();
            else if (i % 4 == 2) {
                Property newProp = new Property(propertyNames[i % 20], colorGroups.get(0), 100, 100, 180, 50, 10);
                squares[i] = newProp;
                colorGroups.get(0).addProperty(newProp);
            }
            else {
                Property newProp = new Property(propertyNames[i % 20], colorGroups.get(1), 100, 100, 180, 50, 10);
                squares[i] = newProp;
                colorGroups.get(1).addProperty(newProp);
            }
        }
        return squares;
    }
}
