package entity;

import java.util.List;

public class SmokeBomb implements Item {

    @Override
    public String getName() {
        return "Smoke Bomb";
    }

    @Override
    public String use(Player user, List<Combatant> enemies, Combatant selectedTarget) {
        user.activateSmokeBomb();
        return user.getName() + " uses Smoke Bomb: enemy attacks deal 0 damage for current and next turn.";
    }

    
    @Override
    public String cannotUseMessage(Player user, List<Combatant> enemies, Combatant selectedTarget) {
        return "Smoke Bomb cannot be used.";
    }
}
