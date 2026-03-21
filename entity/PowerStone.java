package entity;

import java.util.List;

public class PowerStone implements Item {

    @Override
    public String getName() {
        return "Power Stone";
    }

    @Override
    public String use(Player user, List<Combatant> enemies, Combatant selectedTarget) {
        if (user instanceof Warrior) {
            if (selectedTarget == null) {
                return "Power Stone failed: Warrior requires a selected target.";
            }
            return user.usePlayerSpecialWithoutCooldown(List.of(selectedTarget));
        }
        return user.usePlayerSpecialWithoutCooldown(enemies);
    }

    @Override
    public boolean canUse(Player user, List<Combatant> enemies, Combatant selectedTarget){
        return !(user instanceof Warrior) || selectedTarget != null;
    }

    @Override
    public String cannotUseMessage(Player user, List<Combatant> enemies, Combatant selectedTarget){
        return "Power Stone failed: Warrior requires a selected target";
    }
}
