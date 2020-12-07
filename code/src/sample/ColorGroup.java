package sample;

import java.util.ArrayList;

public class ColorGroup {

    private String groupName;
    private ArrayList<Property> properties;
    private int[] colors;

    public ColorGroup(String groupName) {
        this.groupName = groupName;
        properties = new ArrayList<Property>();
        colors = new int[3];
    }

    public void addProperty(Property prop) {
        properties.add(prop);
    }


    //todo
    public void removeProperty(Property prop) {

    }

    //todo: addHotel, sellHouse/hotel + to sell a property no house should be present on colorgroup etc

    public boolean addHouse(Property property, Player player) {
        //TODO check for equal housing -- look at the rules
        for(Property propsInGroup : properties) {
            if(!propsInGroup.isOwned())
                return false;
            else if(propsInGroup.getOwner() != player) {
                return false;
            }
        }
        return property.addHouse();
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
    public boolean isComplete(Player owner) {
        for (Property property: properties) {
            if(!property.isOwned())
            {
                return false;
            }
            else if(property.getOwner() != owner)
            {
                return false;
            }
        }
        return true;
    }

}

