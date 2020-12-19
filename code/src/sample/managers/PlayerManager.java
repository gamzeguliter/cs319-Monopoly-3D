package sample.managers;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import sample.Player;
import sample.Utils;

import java.util.ArrayList;

public class PlayerManager {

    public ArrayList<Player> generatePlayers(ArrayList<String> playerNames,
                                             ArrayList<Image> iconChoices,
                                             ArrayList<Color> playerColors) {
        ArrayList<Player> players = new ArrayList<>();

        for (int i = 0; i < playerNames.size(); i++) {
            String name = playerNames.get(i);
            Image iconChoice = iconChoices.get(i);
            Color color = playerColors.get(i);

            // TODO: add iconchoice when they can draw, also remove balance when available
            players.add(new Player(name, color, 100, iconChoice));
        }

        return players;
    }
}
