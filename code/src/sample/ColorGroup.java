package sample;

import java.util.ArrayList;

public class ColorGroup {

    private String groupName;
    private ArrayList<Property> properties;
    private int[] colors;
    private boolean improvable;

    public ColorGroup(String groupName) {
        this.groupName = groupName;
        properties = new ArrayList<Property>();
        colors = new int[3];
        improvable = false;
    }

    public void addProperty(Property property) {
        properties.add(property);
    }

    public void removeProperty(Property property) {
        properties.remove(property);
    }

    //todo: addHotel, sellHouse/hotel + to sell a property no house should be present on colorgroup etc

    //to add a house: the set must be complete,
    // the player should have balance,
    // there must be balanced housing,
    // none of the properties should be mortgaged,
    // property shouldn't have hotel,
    // property shouldn't have already 4 houses
    public boolean addHouse(Property property, Player player) {
        if (this.isComplete(player)
                || isBalancedHousing(property)
                || property.isHotel()
                || player.getBalance() < property.getHousePrice()
                || property.getNoOfHouses() >= 4
                || !isMortgaged()) {
            return false;
        }
        return property.addHouse();
        //todo when a property has a house, the money of all props in color group should be updated?
    }

    private boolean isComplete(Player player) {
        for (Property propertyInGroup : properties) {
            if (!propertyInGroup.isOwned() || propertyInGroup.getOwner() != player)
                return false;
        }
        return true;
    }

    //this method checks if any of the properties in color group is mortgaged
    private boolean isMortgaged() {
        for (Property property : properties) {
            if (property.isMortgaged())
                return false;
        }
        return true;
    }

    private boolean isBalancedHousing(Property property) {
        int houseNo = property.getNoOfHouses();
        for (Property propertyInGroup : properties) {
            if (propertyInGroup == property) {
                continue;
            } else {
                if (houseNo + 1 > propertyInGroup.getNoOfHouses() + 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int[] getColors() {
        return colors;
    }

    public void setColors(int[] colors) {
        this.colors = colors;
    }

}


