package sample;

import java.util.ArrayList;

public class ColorGroup extends Square{

    private String groupName;
    private ArrayList<Property> properties;
    private int[] colors;
    private ArrayList<Integer> boardIndexes;
    private int propertyNo;


    public ColorGroup(String groupName) {
        super(SquareType.COLORGROUP);
        this.groupName = groupName;
        properties = new ArrayList<Property>();
        boardIndexes = new ArrayList<Integer>();
        colors = new int[3];
        propertyNo = 0;
    }

    private Property findProperty(int boardIndex) {
        for (int propertyIndex: boardIndexes) {
            if (propertyIndex == boardIndex) {
                return properties.get(propertyIndex);
            }
        }
        return null;
    }

    //TODO check house no on other properties -- have to make balanced housing
    public boolean addHouse(int index) {
        Property property = findProperty(index);
        if(property != null)
            return property.addHouse();
        else
            return false;
    }

    public int propertyBuyingPrice(int index) {
        Property property = findProperty(index);
        return property.getBuyingPrice();
    }

    public int propertySellingPrice(int index) {
        Property property = findProperty(index);
        return property.getSellingPrice();
    }

    public Player propertyOwner(int index) {
        Property property = findProperty(index);
        return property.getOwner();
    }

    public int propertyHousePrice(int index) {
        Property property = findProperty(index);
        return property.getHousePrice();
    }

    public int propertyMortgagePrice(int index) {
        Property property = findProperty(index);
        return property.getMortgagePrice();
    }

    public int propertyNoOfHouses(int index) {
        Property property = findProperty(index);
        return property.getNoOfHouses();
    }

    public int propertyRent(int index) {
        Property property = findProperty(index);
        return property.getRent();
    }

    public String propertyName(int index) {
        Property property = findProperty(index);
        return property.getName();
    }

    //todo implement board index array
    //TODO add property --> also save index on the board index array

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public ArrayList<Property> getProperties() {
        return properties;
    }

    public void setProperties(ArrayList<Property> properties) {
        this.properties = properties;
    }

    public int[] getColors() {
        return colors;
    }

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    public int getPropertyNo() {
        return propertyNo;
    }

    public void setPropertyNo(int propertyNo) {
        this.propertyNo = propertyNo;
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
