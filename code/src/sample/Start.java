package sample;

public class Start extends Square{

    private int money;

    public Start(int money) {
        super(SquareType.START);
        this.money = money;
    }

    public int getMoney() {
            return money;
        }

    public void setMoney(int money) {
        this.money = money;
    }
}
