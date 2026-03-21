package entity;

import java.util.List;

public abstract class Player extends AbstractCombatant {

    protected final PlayerClass playerClass;
    protected final Inventory inventory;
    protected int smokeBombTurnsRemaining;

    public Player(String name, PlayerClass playerClass) {
        super(name,
                playerClass.getBaseHp(),
                playerClass.getBaseAttack(),
                playerClass.getBaseDefense(),
                playerClass.getBaseSpeed());

        this.playerClass = playerClass;
        this.inventory = new Inventory();
        this.smokeBombTurnsRemaining = 0;
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
        smokeBombTurnsRemaining = 2;
    }

    public boolean isSmokeBombActive() {
        return smokeBombTurnsRemaining > 0;
    }

    public void onRoundEnd() {
        if (smokeBombTurnsRemaining > 0) {
            smokeBombTurnsRemaining--;
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