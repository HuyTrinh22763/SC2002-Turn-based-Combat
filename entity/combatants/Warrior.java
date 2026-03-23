package entity.combatants;

import java.util.List;

public class Warrior extends Player {

    public Warrior(String name) {
        super(name, PlayerClass.WARRIOR);
    }

    @Override
    public String usePlayerSpecial(List<Combatant> targets) {
        if (!canUseSpecial()) {
            return name + "'s Shield Bash is on cooldown for " + specialCooldown + " more turns!";
        }
        return executeShieldBash(targets, true);
    }

    @Override
    public String usePlayerSpecialWithoutCooldown(List<Combatant> targets) {
        return executeShieldBash(targets, false);
    }

    private String executeShieldBash(List<Combatant> targets, boolean applyCooldown) {
        if (targets == null || targets.isEmpty()) {
            return name + " has no target for Shield Bash!";
        }

        Combatant target = targets.get(0);
        int damageDealt = AttributeManager.calculateDamage(getAttack(), target.getDefense());
        target.takeDamage(damageDealt);
        target.setStunDuration(2);

        if (applyCooldown) {
            useSpecial();
        }

        return String.format("%s uses SHIELD BASH on %s for %d damage and stuns them!",
                name, target.getName(), damageDealt);
    }
}
