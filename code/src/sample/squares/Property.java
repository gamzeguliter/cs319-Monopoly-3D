package sample.squares;

import javafx.scene.paint.Color;
import org.json.JSONObject;
import sample.Board;
import sample.Player;


public class Property extends Square {

    // properties
    private String name;
    private int buyingPrice;
    private int mortgagePrice;
    private int mortgageLiftingPrice;
    int rentOneHouse;
    int rentTwoHouses;
    int rentThreeHouses;
    int rentFourHouses;
    int rentHotel;
    int housePrice;
    int hotelPrice;
    private int rent;
    private int currentRent;
    private int noOfHouses;
    private boolean hotel;
    private Player owner;
    private boolean isOwned;
    private boolean isMortgaged;
    private ColorGroup colorGroup;
    private Board board;
    String groupName;

    /* TODO IMPORTANT ISSUE:
When storing property in file system, only store name, colorgroup, buying price
when reading from file and creating square, give the rent and mortgage rate of the board!!!!!
 */
    public Property(String name, ColorGroup colorGroup, int buyingPrice, int rentRate, int mortgageRate) {
        super(SquareType.PROPERTY);
        this.name = name;
        this.colorGroup = colorGroup;
        groupName = colorGroup.getGroupName();
        this.buyingPrice = buyingPrice;
        this.mortgagePrice = buyingPrice * mortgageRate / 100;
        this.mortgageLiftingPrice = mortgagePrice + mortgagePrice * 10 / 100;
        this.rent = buyingPrice * rentRate / 100;
        currentRent = rent;
        noOfHouses = 0;
        hotel = false;
        isOwned = false;
        isMortgaged = false;
        owner = null;
        setPricesAndRent();
    }

    public Property(JSONObject jo, Board board) {
        super(SquareType.PROPERTY);
        this.board = board;
        extractPropertiesFromJSON(jo);
        noOfHouses = 0;
        hotel = false;
        isOwned = false;
        isMortgaged = false;
        owner = null;
    }

    public ColorGroup getColorGroup() {
        return colorGroup;
    }

    public void setColorGroup(ColorGroup group) {
        this.colorGroup = group;
    }

    public boolean setOwner(Player newOwner) {
        owner = newOwner;
        isOwned = true;
        return true;
    }

    public int getCurrentRent() {
        return currentRent;
    }

    public void setCurrentRent(int rent) {
        currentRent = rent;
    }

    public Player getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(int buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public int getHousePrice() {
        return housePrice;
    }

    public int getRent() {
        return rent;
    }

    public int getNoOfHouses() {
        return noOfHouses;
    }

    public boolean isHotel() {
        return hotel;
    }

    public boolean isOwned() {
        return isOwned;
    }

    public boolean isMortgaged() {
        return isMortgaged;
    }

    public void addHouse() {
        noOfHouses++;
        owner.pay(housePrice);
        switch (noOfHouses) {
            case(1):
                currentRent = rentOneHouse;
                break;

            case(2):
                currentRent = rentTwoHouses;
                break;

            case(3):
                currentRent = rentThreeHouses;
                break;

            case(4):
                currentRent = rentFourHouses;
                break;
        }
    }

    public void sellHouse() {
        noOfHouses--;
        owner.gain(housePrice / 2);
        switch (noOfHouses) {
            case(0):
                currentRent = rent * 2; //given that they need to have all the properties in set
            case(1):
                currentRent = rentOneHouse;
                break;

            case(2):
                currentRent = rentTwoHouses;
                break;

            case(3):
                currentRent = rentThreeHouses;
                break;

            case(4):
                currentRent = rentFourHouses;
                break;
        }
    }

    public void addHotel() {
        hotel = true;
        owner.pay(hotelPrice);
        currentRent = rentHotel;
    }

    public void sellHotel() {
        hotel = false;
        owner.gain(hotelPrice / 2);
        currentRent = rentFourHouses;
    }

    public boolean buyProperty(Player player) {
        setOwner(player);
        player.buyProperty(this, buyingPrice);
        return true;
    }

    public void mortgageProperty() {
        this.isMortgaged = true;
    }

    public void unmortgageProperty() {
        this.isMortgaged = false;
    }

    private void setPricesAndRent() {
        int rentOneHouse = rent * 5;
        int rentTwoHouses = rent * 15;
        int rentThreeHouses = rent * 40;
        int rentFourHouses  = rent * 50;
        int rentHotel = rent * 65;
        int housePrice = rent * 10;
        int hotelPrice = 5 * housePrice;
    }

    public int getMortgagePrice() {
        return mortgagePrice;
    }

    public int getMortgageLiftingPrice() {
        return mortgageLiftingPrice;
    }

    public int getRentOneHouse() {
        return rentOneHouse;
    }

    public int getRentTwoHouses() {
        return rentTwoHouses;
    }

    public int getRentThreeHouses() {
        return rentThreeHouses;
    }

    public int getRentFourHouses() {
        return rentFourHouses;
    }

    public int getRentHotel() {
        return rentHotel;
    }

    public int getHotelPrice() {
        return hotelPrice;
    }

    public void reset() {
        owner = null;
        isOwned = false;
        isMortgaged = false;
        currentRent = rent;
        noOfHouses = 0;
        hotel = false;
    }

    @Override
    public JSONObject getJSON() {
        JSONObject jo = new JSONObject();
        jo.put("type", "Property");
        jo.put("name", name);
        jo.put("groupName", colorGroup.getGroupName());
        jo.put("buyingPrice", buyingPrice);
        return jo;
    }

    @Override
    public void extractPropertiesFromJSON(JSONObject jo) {
        if (jo == null) {
            System.out.println("ERROR: JSONObject passed to Property was null");
        }

        String type = jo.getString("type");
        if (!type.equals("Property")) {
            System.out.println("ERROR: Property initialized with wrong type of JSONObject: " + type);
        }

        name = jo.getString("name");
        groupName = jo.getString("groupName");
        buyingPrice = jo.getInt("buyingPrice");

        float rentRate = board.getRentRate();
        rent = (int) (buyingPrice * rentRate / 100);
        currentRent = rent;
        setPricesAndRent();

        float mortgageRate = board.getMortgageRate();
        mortgagePrice = (int) (buyingPrice * mortgageRate / 100);
        mortgageLiftingPrice = mortgagePrice + mortgagePrice * 10 / 100;
    }

    public void setGroupName(String groupName) { this.groupName = groupName; }
    public String getGroupName() { return groupName; }

}
