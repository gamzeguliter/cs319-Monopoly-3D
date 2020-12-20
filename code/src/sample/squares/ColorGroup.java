package sample.squares;

import javafx.scene.paint.Color;
import org.json.JSONObject;
import sample.entities.Player;

import java.util.ArrayList;

public class ColorGroup implements JSONable {

    private String groupName;
    private ArrayList<Property> properties;
    Color color;

    public ColorGroup(String groupName) {
        this.groupName = groupName;
        properties = new ArrayList<Property>();
        // TODO: add a way to properly initialize color
        color = Color.RED;
    }

    public ColorGroup(JSONObject jo) {
        properties = new ArrayList<Property>();
        extractPropertiesFromJSON(jo);
    }

    public void addProperty(Property property) {
        if (property.getColorGroup() != null)
            property.getColorGroup().removeProperty(property);
        property.setColorGroup(this);
        properties.add(property);

    }

    public void removeProperty(Property property) {
        properties.remove(property);
    }
    public ArrayList<Property> getProperties() {
       return properties;
    }


    //to add a house: the set must be complete,
    // the player should have balance,
    // there must be balanced housing,
    // none of the properties should be mortgaged,
    // property shouldn't have hotel,
    // property shouldn't have already 4 houses
    public boolean canAddHouse(Property property, Player player) {
        if (!isComplete(player)
                || !isBalancedHousing(property, true)
                || property.isHotel()
                || player.getBalance() < property.getHousePrice()
                || property.getNoOfHouses() >= 4
                || isMortgaged()) {
            return false;
        }
        return true;
    }

    public void completeRentUpdate() {
        for(Property property : properties) {
            property.setCurrentRent(property.getCurrentRent() * 2);
        }
    }

    public boolean canSellHouse(Property property, Player player) {
        if (!this.isComplete(player)
                || isBalancedHousing(property, false)
                || property.isHotel()
                || property.getNoOfHouses() == 0
                || isMortgaged()) {
            return false;
        }
        return true;
    }

    public boolean canAddHotel(Property property, Player player) {
        if (!this.isComplete(player)
                || !allHousesComplete()
                || property.isHotel()
                || isMortgaged()) {
            return false;
        }
        return true;
    }

    public boolean canSellHotel(Property property) {
        if (property.isHotel()) {
            return true;
        }
        return false;
    }

    //checks if all properties in color
    private boolean allHousesComplete() {
        for(Property property: properties) {
            if(property.getNoOfHouses() < 4) {
                return false;
            }
        }
        return true;
    }

    public boolean isComplete(Player player) {
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

    private boolean isBalancedHousing(Property property, boolean addHouse) {
        int houseNo = property.getNoOfHouses();
        for (Property propertyInGroup : properties) {
            if (propertyInGroup == property) {
                continue;
            } else {
                if(addHouse) {
                    if (houseNo + 1 > propertyInGroup.getNoOfHouses() + 1) {
                        return false;
                    }
                }
                //check balanced housing for sell house
                else {
                    if (houseNo - 1 < propertyInGroup.getNoOfHouses() - 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    //checks if the given property can be mortgaged
    public boolean canMortgage(Property property) {
        //if the group is not complete, property can be mortgaged
        if(!isComplete(property.getOwner())) {
            return true;
        }
        else {
            //check if there are building on any property
            return !checkBuildings();
        }
    }

    //checks if there are any buildings on any property of the color group
    public boolean checkBuildings() {
        for(Property property : properties) {
            if(property.getNoOfHouses() > 0 || property.isHotel()) {
                return true;
            }
        }
        return false;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public JSONObject getJSON() {
        JSONObject jo = new JSONObject();
        jo.put("groupName", groupName);
        jo.put("r", color.getRed());
        jo.put("g", color.getGreen());
        jo.put("b", color.getBlue());
        return jo;
    }

    public void extractPropertiesFromJSON(JSONObject jo) {
        if (jo == null) {
            System.out.println("ERROR: JSONObject passed to ChanceAndCommunityChest was null");
        }
        groupName = jo.getString("groupName");
        double r = jo.getDouble("r");
        double g = jo.getDouble("g");
        double b = jo.getDouble("b");
        color = Color.color(r, g, b);
    }

}


