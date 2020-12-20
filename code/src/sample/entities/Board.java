package sample.entities;

import javafx.scene.paint.Color;
import sample.entities.Card;
import sample.entities.CardDeck;
import sample.squares.ChanceAndCommunityChest;
import sample.squares.*;

import java.util.ArrayList;

public class Board {

    // properties
    public String name;
    public Square[] squares;
    public CardDeck chanceDeck;
    public CardDeck chestDeck;
    public ArrayList<ColorGroup> colorGroups;
    public String currency;

    int rentRate;
    int mortgageRate;


    // constructors

    // test constructor
    public Board() {
        colorGroups = createGroups();
        squares = getDefaultBoard();
        name = "default";
        chestDeck = new CardDeck();
        chestDeck.generateChestCardDeck();
        chanceDeck = new CardDeck();
        chanceDeck.generateChanceCardDeck();
        currency = "Dollars";
        rentRate = 80;
        mortgageRate = 50;
    }

    public Board(String name) {
        chestDeck = new CardDeck();
        chestDeck.generateChestCardDeck();
        chanceDeck = new CardDeck();
        chanceDeck.generateChanceCardDeck();
        this.name = name;
    }

    public void updatePropertyGroups() {
        for (Square square : squares) {
            if (square.getType() == SquareType.PROPERTY) {
                Property property = (Property) square;
                getColorGroup(property.getGroupName()).addProperty(property);
            }
        }
    }

    public Card drawChanceCard() { return chanceDeck.drawCard(); }
    public Card drawChestCard() { return chestDeck.drawCard(); }

    public ColorGroup getColorGroup(String groupName) {
        for (ColorGroup colorGroup : colorGroups){
            if (colorGroup.getGroupName().equals(groupName)) {
                return colorGroup;
            }
        }
        System.out.println("ERROR: Could not find ColorGroup in colorGroups: " + groupName);
        return null;
    }

    // getters and setters

    public void setName(String name) { this.name = name; }
    public String getName() { return name; }

    public void setColorGroups(ArrayList<ColorGroup> colorGroups) { this.colorGroups = colorGroups; }
    public ArrayList<ColorGroup> getColorGroups() { return colorGroups; }

    public void setSquares(Square[] squares) { this.squares = squares; }
    public Square[] getSquares() { return squares; }

    public void setCurrency(String currency) { this.currency = currency; }
    public String getCurrency() { return currency; }

    public void setMortgageRate(int mortgageRate) { this.mortgageRate = mortgageRate; }
    public int getMortgageRate() { return mortgageRate; }

    public void setRentRate(int rentRate) { this.rentRate = rentRate; }
    public int getRentRate() { return rentRate; }


    private Square[] getDefaultBoard(){
        Square[] squares = new Square[40];
        squares[0] = new Start(5000);

        ColorGroup brown = new ColorGroup("Brown",Color.BROWN);
        ColorGroup blue = new ColorGroup("Blue",Color.BLUE);
        ColorGroup green = new ColorGroup("Green",Color.GREEN);
        ColorGroup yellow = new ColorGroup("Yellow",Color.YELLOW);
        ColorGroup orange = new ColorGroup("Orange",Color.ORANGE);
        ColorGroup pink = new ColorGroup("Pink",Color.PINK);
        ColorGroup babyBlue = new ColorGroup("Baby Blue",Color.LIGHTBLUE);
        ColorGroup red = new ColorGroup("Red",Color.RED);
        colorGroups.add(brown);
        colorGroups.add(blue);
        colorGroups.add(green);
        colorGroups.add(yellow);
        colorGroups.add(orange);
        colorGroups.add(pink);
        colorGroups.add(babyBlue);
        colorGroups.add(red);

        squares[1] = new Property("Ankara",colorGroups.get(0),1500,80,50);
        squares[2] = new ChanceAndCommunityChest(false);
        squares[3] = new Property("Izmir",colorGroups.get(0),770,80,50);
        squares[4] = new Joker( 0, -10,0, "Income \n Tax");
        squares[5] = new Joker(0,0,2,"Jail");
        squares[6] = new Property("Adana",colorGroups.get(6),880,80,50);
        squares[7] = new ChanceAndCommunityChest( true);
        squares[8] = new Property("Istanbul",colorGroups.get(6),910,80,50);
        squares[9] = new Property("Usak",colorGroups.get(6),710,80,50);
        squares[10] = new Joker(2,20,0,"Visitor");
        squares[11] = new Property("Rize",colorGroups.get(5),600,80,50);
        squares[12] = new Joker(0,-20,0,"Traffic");
        squares[13] = new Property("Izmit",colorGroups.get(5),690,80,50);
        squares[14] = new Property("Bursa",colorGroups.get(5),690,80,50);
        squares[15] = new Joker(3,20,0,"Prize");
        squares[16] = new Property("Urfa",colorGroups.get(4),700,80,50);
        squares[17] = new ChanceAndCommunityChest(false);
        squares[18] = new Property("Edirne",colorGroups.get(4),730,80,50);
        squares[19] = new Property("Bolu",colorGroups.get(4),700,80,50);
        squares[20] = new Joker(0,0,0,"Free");
        squares[21] = new Property("Mugla",colorGroups.get(7),680,80,50);
        squares[22] = new ChanceAndCommunityChest(true);
        squares[23] = new Property("Hatay",colorGroups.get(7),688,80,50);
        squares[24] = new Property("Agrı",colorGroups.get(7),678,80,50);
        squares[25] = new Joker(0,-15,2,"Opps");
        squares[26] = new Property("Artvin",colorGroups.get(3),578,80,50);
        squares[27] = new Property("Sivas",colorGroups.get(3),590,80,50);
        squares[28] = new Joker(-2,-15,0,"Go Back");
        squares[29] = new Property("Antep",colorGroups.get(3),730,80,50);
        squares[30] = new Joker(0,0,2,"Jail");
        squares[31] = new Property("Bartın",colorGroups.get(2),500,80,50);
        squares[32] = new Property("Mus",colorGroups.get(2),510,80,50);
        squares[33] = new ChanceAndCommunityChest(false);
        squares[34] = new Property("Igdır",colorGroups.get(2),530,80,50);
        squares[35] = new Joker(0,20,2,"Weird");
        squares[36] = new ChanceAndCommunityChest(true);
        squares[37] = new Property("Mersın",colorGroups.get(1),730,80,50);
        squares[38] = new Joker(0,30,0,"Super");
        squares[39] = new Property("Aydın",colorGroups.get(1),724,80,50);

        return squares;

    }

    private ArrayList<ColorGroup> createGroups() {
        ArrayList<ColorGroup> colors = new ArrayList<>();
        return colors;
    }
}
