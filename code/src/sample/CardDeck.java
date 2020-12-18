package sample;

import java.util.ArrayList;
import java.util.Random;

public class CardDeck {

    Card[] cards;
    String[] chanceActions;
    String[] chancePrompts;

    String[] chestPrompts;
    String[] chestActions;
    String place;
    int amount;

    public CardDeck() {
        cards = new Card[16];
        chanceActions = new String[10];
        amount = 0;

        /*
        todo prompt update
         */

        //Initializing Chance Prompts and Actions
        chanceActions[0] = "Go to a place";
        chanceActions[1] = "Go to jail";
        chanceActions[2] = "Get out of jail";
        chanceActions[3] = "Go to nearest joker";
        chanceActions[4] = "Earn money";
        chanceActions[5] = "Go back places";
        chanceActions[6] = "Pay money for house and hotel";
        chanceActions[7] = "Pay money";
        chanceActions[8] = "Pay each player";
        chanceActions[9] = "Go to go";

        chancePrompts = new String[10];

        chancePrompts[0] = "Go to ";
        chancePrompts[1] = "Go to jail";
        chancePrompts[2] = "Get out of jail";
        chancePrompts[3] = "Go to nearest joker square, if there is no joker in the game, do nothing";
        chancePrompts[4] = "Earn ";
        chancePrompts[5] = "Go back ";
        chancePrompts[6] = "Pay 25 for house and hotel";
        chancePrompts[7] = "Pay ";
        chancePrompts[8] = "Pay ";
        chancePrompts[9] = "Go to Go";

        //Initializing Chest Prompts and Actions

        chestPrompts = new String[7];
        chestPrompts[0] = "Advance to Go";
        chestPrompts[1] = "Pay ";
        chestPrompts[2] = "Earn ";
        chestPrompts[3] = "Earn ";
        chestPrompts[4] = "Get out of Jail Free. ";
        chestPrompts[5] = "Pay ";
        chestPrompts[6] = "Go to Jail. Go directly to Jail.";


        chestActions = new String[7];
        chestActions[0] = "Advance to Go";
        chestActions[1] = "Pay money";
        chestActions[2] = "Earn x from each player";
        chestActions[3] = "Earn money";
        chestActions[4] = "Get out of jail";
        chestActions[5] = "Pay money for house and hotel";
        chestActions[6] = "Go to jail";

    }

    public void generateChanceCardDeck() {
        for (int i = 0; i < 16; i++) {
            Card card;
            if (i == 0) {
                // go to go
                amount = ((int) (Math.random() * (4 - 1))) + 1;
                card = new Card(chancePrompts[9], chanceActions[9], amount);
                cards[i] = card;
            }
            if (i == 1 || i == 2 || i == 11 || i == 12) {
                // go to a special place
                amount = ((int) (Math.random() * (4 - 1))) + 1;
                card = new Card(chancePrompts[0], chanceActions[0], amount);
                cards[i] = card;
            }

            if (i == 3 || i == 4) {
                //go to nearest x
                amount = ((int) (Math.random() * (4 - 1))) + 1;
                card = new Card(chancePrompts[3], chanceActions[3], amount);
                cards[i] = card;
            }
            if (i == 5 || i == 15 || i == 6 || i == 14) { // 6 is jail
                //earn money
                amount = ((int) (Math.random() * (100 - 1))) + 1;
                card = new Card(chancePrompts[4] + amount, chanceActions[4], amount);
                cards[i] = card;
            }

            if (i == 7) {
                //go back x spaces
                amount = ((int) (Math.random() * (4 - 1))) + 1;
                card = new Card(chancePrompts[5] + amount + " places", chanceActions[5], amount);
                cards[i] = card;
            }

            if (i == 9) {
                // pay for house and hotel
                amount = ((int) (Math.random() * (10 - 1))) + 1;
                card = new Card("", chanceActions[6], amount);
                cards[i] = card;
            }

            if (i == 10 || i == 8) {
                //pay money
                amount = ((int) (Math.random() * (20 - 1))) + 1;
                card = new Card(chancePrompts[7] + amount, chanceActions[7], amount);
                cards[i] = card;
            }
            if (i == 13) {
                //pay each player
                amount = ((int) (Math.random() * (10 - 1))) + 1;
                card = new Card(chancePrompts[8] + amount, chanceActions[8], amount);
                cards[i] = card;
            }


        }


    }

    public void generateChestCardDeck() {

        for (int i = 0; i < 16; i++) {
            Card card;
            if (i == 0) {
                // go to go
                amount = ((int) (Math.random() * (4 - 1))) + 1;
                card = new Card(chestPrompts[0], chestActions[0], amount);
                cards[i] = card;
            }
            if (i == 1 || i == 3 || i == 7 || i == 8 || i == 10 || i == 13 || i == 15 || i == 16) {// 5 is the jail can be changed
                // earn money
                amount = ((int) (Math.random() * (100 - 1))) + 1;
                card = new Card(chestPrompts[3] + amount, chestActions[3], amount);
                cards[i] = card;
            }

            if (i == 2 || i == 11 || i == 12 || i == 4) { // 4  is the jail , can be changed
                // pay money
                amount = ((int) (Math.random() * (50 - 1))) + 1;
                card = new Card(chestPrompts[1] + amount, chestActions[1], amount);
                cards[i] = card;
            }
            if (i == 6 || i == 9) {
                // earn money from each player
                amount = ((int) (Math.random() * (20 - 1))) + 1;
                card = new Card(chestPrompts[2] + amount, chestActions[2], amount);
                cards[i] = card;
            }
            if (i == 14) {
                // pay for houses and hotels
                amount = ((int) (Math.random() * (30 - 1))) + 1;
                card = new Card(chestPrompts[5], chestActions[5], amount);
                cards[i] = card;
            }
        }
    }

    public Card drawCard() {
        double number = Math.random();
        int index = (int)(number * 16);
        return cards[index];
    }
}

