package control;

import entity.combatants.Player;
import entity.combatants.PlayerClass;

public interface PlayerFactory {
    Player createPlayer(PlayerClass playerClass, String name);
}
