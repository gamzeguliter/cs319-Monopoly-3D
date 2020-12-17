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

    public CardDeck() {
        cards = new Card[16];
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

