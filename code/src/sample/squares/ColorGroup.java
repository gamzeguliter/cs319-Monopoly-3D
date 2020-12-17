package sample.squares;

import javafx.scene.paint.Color;
import org.json.JSONObject;
import sample.Player;
import sample.squares.Property;

import java.util.ArrayList;

public class ColorGroup {

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
        extractPropertiesFromJson(jo);
    }

    public void addProperty(Property property) {
        property.getColorGroup().removeProperty(property);
        property.setColorGroup(this);
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public JSONObject getJson() {
        JSONObject jo = new JSONObject();
        jo.put("groupName", groupName);
        jo.put("r", color.getRed());
        jo.put("g", color.getGreen());
        jo.put("b", color.getBlue());
        return jo;
    }

    public void extractPropertiesFromJson(JSONObject jo) {
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


