package entity.items;

import java.util.List;

import entity.combatants.Combatant;
import entity.combatants.Player;

public class PowerStone implements Item {

    @Override
    public String getName() {
        return "Power Stone";
    }

    @Override
    public String use(Player user, List<Combatant> enemies, Combatant selectedTarget) {
        return user.usePlayerSpecialWithoutCooldown(user.resolveSpecialTargets(enemies, selectedTarget));
    }

    @Override
    public boolean canUse(Player user, List<Combatant> enemies, Combatant selectedTarget){
        return user.isValidSpecialTargetSelection(selectedTarget);
    }

    @Override
    public String cannotUseMessage(Player user, List<Combatant> enemies, Combatant selectedTarget){
        return "Power Stone failed: special skill target requirements not met.";
    }
}
