package entity.items;

import java.util.List;

import entity.battlerules.SpeedDebuffEffect;
import entity.combatants.Combatant;
import entity.combatants.Player;

public class Caltrops implements Item {

    @Override
    public String getName() {
        return "Caltrops";
    }

    @Override
    public String use(Player user, List<Combatant> enemies, Combatant selectedTarget) {
        for (Combatant enemy : enemies) {
            if (enemy.isAlive()) {
                enemy.addStatusEffect(new SpeedDebuffEffect(20, 2));
            }
        }
        return user.getName() + " threw Caltrops! All enemies lose 20 speed for 2 turns!";
    }

    @Override
    public String cannotUseMessage(Player user, List<Combatant> enemies, Combatant selectedTarget) {
        return "Caltrops cannot be used right now.";
    }
}
