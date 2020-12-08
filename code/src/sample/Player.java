package sample;


import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Player {
    //properties
    String name;
    // TODO: Pawn pawn;
    private int position;
    private Color color;
    private int balance;
    private int suspendedTourNo;
    private boolean isSuspended;
    private boolean isBankrupt;
    private boolean out;
    private ArrayList<Property> ownedProperties;

    //constructors
    public Player(String name, Color color, int balance) {
        this.name = name;
        this.color = color;
        this.balance = balance;
        position = 0;
        isSuspended = false;
        isBankrupt = false;
        suspendedTourNo = 0;
        out = false;
        ownedProperties = new ArrayList<Property>();
    }

    //private methods

    //public methods
    public void suspend(int tourNo) {
        isSuspended = true;
        suspendedTourNo = tourNo;
    }

    public void buyProperty(Property property) {
        ownedProperties.add(property);
    }

    public void sellProperty(Property property) {
        ownedProperties.remove(property);
    }

    public int getSuspendedTourNo() {
        return suspendedTourNo;
    }
    boolean isSuspended() { return isSuspended; }
    boolean isBankrupt() { return isBankrupt; }

    //getters and setters
    // TODO: implement pawn related methods
    // getPawn()
    // setPawn()

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }

    public int getBalance() { return balance; }
    public void setBalance(int balance) { this.balance = balance; }

    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }

    public void pay(int money) {
        balance -= money;
    }

    public void gain(int money) {
        balance += money;
    }

    public boolean isOut() {
        return out;
    }

    public void resign() {
        out = true;
    }
}
