package sample;

public class Joker extends Square {

    //attributes
    private int movement;
    private int money;
    private int suspendedTourNo;
    private String name;

    //constructor
    public Joker(int movement, int money, int suspendedTourNo,String name) {
        super(SquareType.JOKER);
        this.movement = movement;
        this.money = money;
        this.suspendedTourNo = suspendedTourNo;
        this.name = name;
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

}
