package sample;

import java.util.ArrayList;

public class CardDeck {
    //todo
    /* card array
    * initialize etmek için chance prompt + action arrays
    * ve chest prompt + action arrays
    * */

    //chance action -- [0, 1, 2]
    //action["money to user, money to all users]
    /* 0-- para çek oyuncudan
    1-- para ver
    2-- her oyuncadan para çek ve şu ankine ver
    3--

    /*
    prompt array: "Go back x spaces", --> go back 4 spaces
    action 1,
     */

   /* chest action -- [ 0, 1, 3]
     */
    Card[] cards;
    String[] chanceActions;
    String[]  chancePrompts;
    String place;
    int amount ;

    public CardDeck() {
        cards = new Card[16];
        chanceActions = new String[9];
        amount = 0;
        chanceActions[0] = "go to a place";
        chanceActions[1] = "go to jail";
        chanceActions[2] = "get out of jail";
        chanceActions[3] = "go to nearest";
        chanceActions[4] = "earn x money";
        chanceActions[5] = "go back x places";
        chanceActions[6] = "pay money with house";
        chanceActions[7] = "pay money";
        chanceActions[8] = "pay each player";

        chancePrompts = new String[16];
        chancePrompts[0] = "Advance to Go";
        chancePrompts[1] = "Advance to Illinois Ave";
        chancePrompts[2] = "Advance to St. Charles Place";
        chancePrompts[3] = "Advance token to nearest Utility.";
        chancePrompts[4] = "Advance token to the nearest  ";
        chancePrompts[5] = "Bank pays you dividend of $50";
        chancePrompts[6] = "Get out of Jail Free. ";
        chancePrompts[7] = "Go Back "+ amount+ "Spaces"; /// will be changed
        chancePrompts[8] = "Go to Jail. Go directly to Jail.";
        chancePrompts[9] = "Make general repairs on all your property: For each house pay $25, For each hotel {pay} $100";
        chancePrompts[10] = "Take a trip to Reading Railroad "; // will be changed
        chancePrompts[11] = "Take a walk on the Boardwalk. Advance token to Boardwalk."; // will be changed
        chancePrompts[12] = "You have been elected Chairman of the Board. Pay each player $50.";
        chancePrompts[13] = "Your building loan matures. Receive" +amount ;
        chancePrompts[14] = "You have won a crossword competition. Collect "+ amount ;
        chancePrompts[15] = "pay money";


        /*ChancePrompt
        chestprompt
        chanceactions
        promptactions
         */

    }

    public void generateChanceCardDeck() {
        //for loop -- (16)
        /*
        random number
        Card card = new Card(prompt[rand], action[rand], amount sayı)
        cards[i] = card
         */
    }

    public void generateChestCardDeck() {

    }

    public Card drawCard() {
        return null;
    }
}

