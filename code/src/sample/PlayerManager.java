package sample;

import javafx.scene.paint.Color;

import java.util.ArrayList;

public class PlayerManager {

    public ArrayList<Player> generatePlayers(ArrayList<String> playerNames,
                                             ArrayList<Integer> iconChoices,
                                             ArrayList<Color> playerColors) {
        ArrayList<Player> players = new ArrayList<>();

        for (int i = 0; i < playerNames.size(); i++) {
            String name = playerNames.get(i);
            int iconChoice = iconChoices.get(i);
            Color color = playerColors.get(i);

            // TODO: add iconchoice when they can draw, also remove balance when available
            players.add(new Player(name, color, 100));
        }

        return players;
    }
}
