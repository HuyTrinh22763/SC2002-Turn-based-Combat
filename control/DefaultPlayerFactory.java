package control;

import entity.combatants.Player;
import entity.combatants.PlayerClass;

public class DefaultPlayerFactory implements PlayerFactory {

    @Override
    public Player createPlayer(PlayerClass playerClass, String name) {
        if (playerClass == null) {
            throw new IllegalArgumentException("Player class cannot be null.");
        }

        return playerClass.create(name);
    }
}
