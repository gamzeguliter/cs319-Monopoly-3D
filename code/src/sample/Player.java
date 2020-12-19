package sample;


import javafx.scene.paint.Color;
import sample.squares.Property;

import java.util.ArrayList;

public class Player {
    //properties
    String name;
    Pawn pawn;
    private int position;
    private Color color;
    private int balance;
    private int jailTime;
    private boolean out;
    public ArrayList<Property> ownedProperties;

    //constructors
    public Player(String name, Color color, int balance) {
        this.name = name;
        this.color = color;
        this.balance = balance;
        position = 0;
        jailTime = 0;
        out = false;
        ownedProperties = new ArrayList<Property>();
    }

    //private methods

    //public methods

    public void buyProperty(Property property, int price) {
        ownedProperties.add(property);
        pay(price);
    }

    public void sellProperty(Property property, int price) {
        ownedProperties.remove(property);
        gain(price);
    }

    public int getjailTime() {
        return jailTime;
    }

    public int setJailTime(int jail) {
        return jailTime = jail;
    }

    public boolean isInJail() { return jailTime > 0; }

    //getters and setters
    // TODO: implement pawn related methods
    // getPawn()
    // setPawn()

    public void endJail() {
        jailTime = 0;
    }

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

    public void out() {
        out = true;
    }

    public boolean isBankrupt() {
        return balance < 0;
    }
}
