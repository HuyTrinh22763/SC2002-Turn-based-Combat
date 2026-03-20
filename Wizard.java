package models.combatants;

import models.enums.PlayerClass;

public class Wizard extends Player {

    private int arcaneBlastKills;

    public Wizard(String name) {
        super(name, PlayerClass.WIZARD);
        this.arcaneBlastKills = 0;
    }

    @Override
    public String usePlayerSpecial(Combatant target) {
        if (!canUseSpecial()) {
            return name + "'s Arcane Blast is on cooldown for " + specialCooldown + " more turns!";
        }

        int damage = getAttack() * 2;
        target.takeDamage(damage);

        if (!target.isAlive()) {
            arcaneBlastKills++;
            modifyAttack(10);
            System.out.println(name + "'s Arcane Blast grows stronger! +10 ATK!");
        }

        useSpecial();

        return String.format("%s casts ARCANE BLAST on %s for %d damage! (Kills: %d)",
                name, target.getName(), damage, arcaneBlastKills);
    }

    @Override
    protected void levelUp() {
        super.levelUp();
        this.maxHp += 15;
        this.currentHp = this.maxHp;
        this.baseAttack += 10;
        this.baseDefense += 2;
        this.currentAttack = this.baseAttack;
        this.currentDefense = this.baseDefense;

        System.out.println(name + " grows more powerful! HP+15, ATK+10, DEF+2");
    }

    public int getArcaneBlastKills() {
        return arcaneBlastKills;
    }
}
