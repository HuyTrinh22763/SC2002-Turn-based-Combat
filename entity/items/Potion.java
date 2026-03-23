package entity.items;

import java.util.List;

import entity.combatants.Combatant;
import entity.combatants.Player;

public class Potion implements Item {

    private static final int HEAL_AMOUNT = 100;

    @Override
    public String getName() {
        return "Potion";
    }

    @Override
    public String use(Player user, List<Combatant> enemies, Combatant selectedTarget) {
        int before = user.getCurrentHp();
        user.heal(HEAL_AMOUNT);
        int healed = user.getCurrentHp() - before;
        return user.getName() + " uses Potion and restores " + healed + " HP.";
    }

    @Override
    public String cannotUseMessage(Player user, List<Combatant> enemies, Combatant selectedTarget) {
        return "Potion cannot be used.";
    }
}
