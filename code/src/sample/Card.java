package sample;

public class Card {
    private String prompt;
    private String action; //todo
    int amount;

    public Card(String prompt, String action,int amount) {
        this.prompt =prompt;
        this.action = action;
        this.amount = amount;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    //String prompt -- Bastıracağı metin

}
