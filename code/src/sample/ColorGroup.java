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

    public void addProperty(Property prop) {
        properties.add(prop);
    }


    //todo
    public void removeProperty(Property prop) {

    }

    //todo: addHotel, sellHouse/hotel + to sell a property no house should be present on colorgroup etc

    //to add a house: the set must be complete,
    // the player should have balance,
    // there must be equal housing,
    // none of the properties should be mortgaged
    public boolean addHouse(Property property, Player player) {
        //TODO check for equal housing -- look at the rules
        if(!isImprovable()
                || property.isHotel()
                || player.getBalance() < property.getHousePrice()
                || property.getNoOfHouses() >= 4) {
            return false;
        }
        else {
            int houseNo = property.getNoOfHouses();
            for(Property propertyInGroup: properties) {
                if(propertyInGroup == property) {
                    continue;
                }
                else {
                    if(houseNo + 1 > propertyInGroup.getNoOfHouses() + 1)
                    {
                        return false;
                    }
                }
            }
            updateImprovability(player);
            return property.addHouse();
        }
        //todo when a property has a house, the money of all props in color group should be updated?
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

    //checks if all properties of this color group is owned by the player("owner")
    // none of the properties should be mortgaged
    //if all of them have hotel, it is not improvable
    public void updateImprovability(Player owner) {
        int hotelNo = 0;
        for (Property property: properties) {
            if (!property.isOwned() || property.getOwner() != owner || property.isMortgaged()) {
                improvable = false;
                return;
            }
            else if(property.isHotel()) {
                hotelNo++;
            }
        }
        if(hotelNo == properties.size()) {
            improvable = false;
        }
        else {
            improvable = true;
        }
    }

    public boolean isImprovable() {
        return improvable;
    }
}

