package sample;

public class Player {
    //properties
    String name;
    // TODO: Pawn pawn;
    private int position;
    private int color;
    private int balance;
    private boolean isSuspended;
    private boolean isBankrupt;

    //constructors
    public Player(String name, int color, int balance) {
        this.name = name;
        this.color = color;
        this.balance = balance;

        position = 0;
        isSuspended = false;
        isBankrupt = false;
    }

    //private methods

    //public methods
    boolean isSuspended() { return isSuspended; }
    boolean isBankrupt() { return isBankrupt; }

    //getters and setters
    // TODO: implement pawn related methods
    // getPawn()
    // setPawn()

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getColor() { return color; }
    public void setColor(int color) { this.color = color; }

    public int getBalance() { return balance; }
    public void setBalance(int balance) { this.balance = balance; }

    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }

}
