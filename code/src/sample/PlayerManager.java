package sample;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import sample.entities.Player;

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

            players.add(new Player(name, color, 5000, iconChoice));
        }

        return players;
    }
}
