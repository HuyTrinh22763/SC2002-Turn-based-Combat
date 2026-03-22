package entity;

import java.util.List;

public abstract class Player extends AbstractCombatant {

    protected final PlayerClass playerClass;
    protected final Inventory inventory;
    private static final int SMOKE_BOMB_USAGE = 3;
    private int smokeBombEffectRemaining;

    public Player(String name, PlayerClass playerClass) {
        super(name,
                playerClass.getBaseHp(),
                playerClass.getBaseAttack(),
                playerClass.getBaseDefense(),
                playerClass.getBaseSpeed());

        this.playerClass = playerClass;
        this.inventory = new Inventory();
        this.smokeBombEffectRemaining = 0;
    }

    public PlayerClass getPlayerClass() {
        return playerClass;
    }


    public Inventory getInventory() {
        return inventory;
    }

    public void addItemToInventory(Item item) {
        inventory.addItem(item);
    }

    public String useInventoryItem(int index, List<Combatant> enemies, Combatant selectedTarget) {
        return inventory.useItem(index, this, enemies, selectedTarget);
    }

    public void activateSmokeBomb() {
        smokeBombEffectRemaining = SMOKE_BOMB_USAGE;
    }

    public boolean isSmokeBombActive() {
        return smokeBombEffectRemaining > 0;
    }

    public void onRoundEnd() {
    }

    /** After each enemy BasicAttack vs this player that dealt 0 damage because of Smoke Bomb. */
    public void onSmokeBombEnemyAttack() {
        if (smokeBombEffectRemaining > 0) {
            smokeBombEffectRemaining--;
        }
    }

    public abstract String usePlayerSpecial(List<Combatant> targets);
    public abstract String usePlayerSpecialWithoutCooldown(List<Combatant> targets);

    @Override
    public String toString() {
        return String.format("%s %s %s",
                name,  playerClass.getDisplayName(), super.toString());
    }
}