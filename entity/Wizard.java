package entity;

import java.util.List;

public class Wizard extends Player {

    private int arcaneBlastKills;
    private int arcaneBlastAttackBonus;

    public Wizard(String name) {
        super(name, PlayerClass.WIZARD);
        this.arcaneBlastKills = 0;
    }

    @Override
    public int getAttack() {
        return AttributeManager.clampAttack(currentAttack + attackModifier + arcaneBlastAttackBonus);
    }

    @Override
    public String usePlayerSpecial(List<Combatant> targets) {
        if (!canUseSpecial()) {
            return name + "'s Arcane Blast is on cooldown for " + specialCooldown + " more turns!";
        }
        return executeArcaneBlast(targets, true);
    }

    @Override
    public String usePlayerSpecialWithoutCooldown(List<Combatant> targets) {
        return executeArcaneBlast(targets, false);
    }

    private String executeArcaneBlast(List<Combatant> targets, boolean applyCooldown) {
        if (targets == null || targets.isEmpty()) {
            return name + " has no targets for Arcane Blast!";
        }

        int attackSnapshot = getAttack();
        int killsThisCast = 0;

        for (Combatant enemy : targets) {
            if (enemy == null || !enemy.isAlive()) {
                continue;
            }
            boolean wasAlive = enemy.isAlive();
            enemy.takeDamage(AttributeManager.calculateDamage(attackSnapshot, enemy.getDefense()));
            if (wasAlive && !enemy.isAlive()) {
                killsThisCast++;
            }
        }

        for (int i = 0; i < killsThisCast; i++) {
            arcaneBlastAttackBonus += 10;
            arcaneBlastKills++;
        }

        if (applyCooldown) {
            useSpecial();
        }

        return String.format(
                "%s casts ARCANE BLAST (snapshot ATK %d) - %d kill(s) this cast | total Arcane ATK bonus: +%d",
                name, attackSnapshot, killsThisCast, arcaneBlastAttackBonus);
    }


    // Must call this method at the end of each level
    public void resetArcaneBlastBonusForLevelEnd() {
        arcaneBlastAttackBonus = 0;
    }

    public int getArcaneBlastKills() {
        return arcaneBlastKills;
    }

    public int getArcaneBlastAttackBonus() {
        return arcaneBlastAttackBonus;
    }
}
