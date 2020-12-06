package sample;

public class Property {

    // properties
    private String name;
    private int buyingPrice;
    private int sellingPrice;
    private int mortgagePrice;
    private int housePrice;
    private int rent;
    private int noOfHouses;
    private boolean hotel;
    private Player owner;
    private boolean isOwned;
    private boolean isMortgaged;

    public Property(String name, int buyingPrice, int sellingPrice, int mortgagePrice, int housePrice, int rent) {
        this.name = name;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
        this.mortgagePrice = mortgagePrice;
        this.housePrice = housePrice;
        this.rent = rent;
        noOfHouses = 0;
        hotel = false;
        isOwned = false;
        isMortgaged = false;
        owner = null;
    }

    public boolean hasOwner() {
        return owner != null;
    }

    //TODO buy house, sell house gibi şeyler -- gameengine?
    //TODO sell property make isOwned false set owner to null
    public boolean setOwner(Player newOwner) {
        if(isOwned) {
            return false;
        }
        else
        {
            owner = newOwner;
            isOwned = true;
            return true;
        }
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

    public int getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(int sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getMortgagePrice() {
        return mortgagePrice;
    }

    public void setMortgagePrice(int mortgagePrice) {
        this.mortgagePrice = mortgagePrice;
    }

    public int getHousePrice() {
        return housePrice;
    }

    public void setHousePrice(int housePrice) {
        this.housePrice = housePrice;
    }

    public int getRent() {
        return rent;
    }

    public void setRent(int rent) {
        this.rent = rent;
    }

    public int getNoOfHouses() {
        return noOfHouses;
    }

    public void setNoOfHouses(int noOfHouses) {
        this.noOfHouses = noOfHouses;
    }

    public boolean isHotel() {
        return hotel;
    }

    public void setHotel(boolean hotel) {
        this.hotel = hotel;
    }

    public boolean isOwned() {
        return isOwned;
    }

    public void setOwned(boolean owned) {
        isOwned = owned;
    }

    public boolean isMortgaged() {
        return isMortgaged;
    }

    public void setMortgaged(boolean mortgaged) {
        isMortgaged = mortgaged;
    }

    public boolean addHouse() {
        if(noOfHouses == 4 || hotel)
        {
            return false;
        }
        else {
            noOfHouses++;
            //TODO update rent
            return true;
        }
    }
}
