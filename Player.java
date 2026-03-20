package models.combatants;

import models.enums.PlayerClass;

public abstract class Player extends AbstractCombatant {

    protected final PlayerClass playerClass;
    protected int level;
    protected int experience;

    public Player(String name, PlayerClass playerClass) {
        super(name,
                playerClass.getBaseHp(),
                playerClass.getBaseAttack(),
                playerClass.getBaseDefense(),
                playerClass.getBaseSpeed());

        this.playerClass = playerClass;
        this.level = 1;
        this.experience = 0;
    }

    public PlayerClass getPlayerClass() {
        return playerClass;
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public void gainExperience(int exp) {
        this.experience += exp;
        while (experience >= level * 100) {
            levelUp();
        }
    }

    protected void levelUp() {
        level++;
        System.out.println(name + " leveled up to level " + level + "!");
    }

    public abstract String usePlayerSpecial(Combatant target);

    @Override
    public String toString() {
        return String.format("%s (Lvl %d %s) %s",
                name, level, playerClass.getDisplayName(), super.toString());
    }
}