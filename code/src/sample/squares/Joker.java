package sample.squares;

import org.json.JSONObject;
import sample.squares.Square;
import sample.squares.SquareType;

public class Joker extends Square {

    //attributes
    private int movement;
    private int money;
    private int suspendedTourNo;
    private String name;

    //constructor
    public Joker(int movement, int money, int suspendedTourNo, String name) {
        super(SquareType.JOKER);
        this.movement = movement;
        this.money = money;
        this.suspendedTourNo = suspendedTourNo;
        this.name = name;
    }

    public Joker(JSONObject jo) {
        super(SquareType.JOKER);
        extractPropertiesFromJSON(jo);
    }

    //methods
    //TODO pictures

    public int getMovement() {
        return movement;
    }

    public void setMovement(int movement) {
        this.movement = movement;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getSuspendedTourNo() {
        return suspendedTourNo;
    }

    public void setSuspendedTourNo(int suspendedTourNo) {
        this.suspendedTourNo = suspendedTourNo;
    }

    public boolean isSuspended() {
        return suspendedTourNo != 0;
    }

    public boolean isMovementAction() {
        return movement != 0;
    }

    public boolean isMoneyAction() {
        return money != 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public JSONObject getJSON() {
        JSONObject jo = new JSONObject();
        jo.put("type", "Joker");
        jo.put("name", name);
        jo.put("movement", movement);
        jo.put("money", money);
        jo.put("suspendedTourNo", suspendedTourNo);
        return jo;
    }

    @Override
    public void extractPropertiesFromJSON(JSONObject jo) {
        if (jo == null) {
            System.out.println("ERROR: JSONObject passed to Joker was null");
        }

        String type = jo.getString("type");
        if (!type.equals("Joker")) {
            System.out.println("ERROR: Joker initialized with wrong type of JSONObject: " + type);
        }

        movement = jo.getInt("movement");
        money = jo.getInt("money");
        suspendedTourNo = jo.getInt("suspendedTourNo");
        name = jo.getString("name");
    }

}
