package sample.entities;

public class Card {
    private String prompt;
    private String action;
    private int amount;
    private String promptInfo;

    public Card(String prompt, String action, int amount) {
        this.prompt =prompt;
        this.action = action;
        this.amount = amount;
        promptInfo = "";
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

    public String getPromptInfo() {
        return promptInfo;
    }
    public void setPromptInfo(String promptInfo) {
        this.promptInfo = promptInfo;
    }

    public void setAmount(int amount) { this.amount = amount; }
    public int getAmount() { return amount; }
}
