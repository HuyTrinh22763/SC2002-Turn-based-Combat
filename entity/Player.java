package entity;

import java.util.List;

public abstract class Player extends AbstractCombatant {

    protected final PlayerClass playerClass;
    protected int level;

    public Player(String name, PlayerClass playerClass) {
        super(name,
                playerClass.getBaseHp(),
                playerClass.getBaseAttack(),
                playerClass.getBaseDefense(),
                playerClass.getBaseSpeed());

        this.playerClass = playerClass;
        this.level = 1;
    }

    public PlayerClass getPlayerClass() {
        return playerClass;
    }

    public int getLevel() {
        return level;
    }

    public abstract String usePlayerSpecial(List<Combatant> targets);

    @Override
    public String toString() {
        return String.format("%s (Lvl %d %s) %s",
                name, level, playerClass.getDisplayName(), super.toString());
    }
}