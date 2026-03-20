package models.combatants;

import models.enums.PlayerClass;

public class Warrior extends Player {

    public Warrior(String name) {
        super(name, PlayerClass.WARRIOR);
    }

    @Override
    public String usePlayerSpecial(Combatant target) {
        if (!canUseSpecial()) {
            return name + "'s Shield Bash is on cooldown for " + specialCooldown + " more turns!";
        }

        int damage = (int)(getAttack() * 1.5);
        target.takeDamage(damage);
        target.setStunned(true);
        target.setStunDuration(2);

        useSpecial();

        return String.format("%s uses SHIELD BASH on %s for %d damage and stuns them!",
                name, target.getName(), damage);
    }

    @Override
    protected void levelUp() {
        super.levelUp();
        this.maxHp += 20;
        this.currentHp = this.maxHp;
        this.baseAttack += 5;
        this.baseDefense += 5;
        this.currentAttack = this.baseAttack;
        this.currentDefense = this.baseDefense;

        System.out.println(name + " grows stronger! HP+20, ATK+5, DEF+5");
    }
}