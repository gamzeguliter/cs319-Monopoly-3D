package sample;

public class Card {
    private String prompt;
    private String action; //todo

    public Card(String prompt, String action) {
        this.prompt =prompt;
        this.action = action;
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
