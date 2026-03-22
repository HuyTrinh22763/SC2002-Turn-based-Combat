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
        return user.getName()
                + " uses Smoke Bomb: enemy BasicAttacks against you can deal 0 until the effect ends.";

    }

    
    @Override
    public String cannotUseMessage(Player user, List<Combatant> enemies, Combatant selectedTarget) {
        return "Smoke Bomb cannot be used.";
    }
}
