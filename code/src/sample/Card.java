package sample;

public class Card {
    private String prompt;
    private String action;
    int amount;
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
}
