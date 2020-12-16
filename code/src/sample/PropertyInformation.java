package sample;

import javafx.scene.paint.Color;

public class PropertyInformation {
    boolean isOwned;
    String name;
    Color color;
    int buyingPrice;
    int rent;
    int rentOneHouse;
    int rentTwoHouses;
    int rentThreeHouses;
    int rentFourHouses;
    int rentHotel;
    int housePrice;
    int hotelPrice;
    int houseNo;
    boolean hotel;
    Player owner;
    int mortgagePrice;
    int mortgageLiftingPrice;

    public PropertyInformation(boolean isOwned,
            String name,
            Color color,
            int buyingPrice,
            int rent,
            int rentOneHouse,
            int rentTwoHouses,
            int rentThreeHouses,
            int rentFourHouses,
            int rentHotel,
            int housePrice,
            int hotelPrice,
            int houseNo,
            boolean hotel,
            Player owner,
            int mortgagePrice, int mortgageLiftingPrice) {
        this.isOwned = isOwned;
        this.name = name;
        this.color = color;
        this.buyingPrice = buyingPrice;
        this.rent = rent;
        this.rentOneHouse = rentOneHouse;
        this.rentTwoHouses = rentTwoHouses;
        this.rentThreeHouses = rentThreeHouses;
        this.rentFourHouses = rentFourHouses;
        this.rentHotel = rentHotel;
        this.housePrice = housePrice;
        this.hotelPrice = hotelPrice;
        this.houseNo = houseNo;
        this.hotel = hotel;
        this.owner = owner;
        this.mortgagePrice = mortgagePrice;
        this.mortgageLiftingPrice = mortgageLiftingPrice;
    }
}
