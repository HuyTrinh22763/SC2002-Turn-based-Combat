package entity.combatants;

import java.util.List;
import java.util.Collections;

import entity.items.Inventory;
import entity.items.Item;
import entity.battlerules.ActionProcessor;
import entity.battlerules.ActionRequest;
import entity.battlerules.ActionResult;
import entity.battlerules.ActionType;

public abstract class Player extends AbstractCombatant {

    protected final PlayerClass playerClass;
    protected final Inventory inventory;
    private static final int SMOKE_BOMB_USAGE = 3;
    private static final int DEFEND_ROUNDS = 2;
    private int smokeBombEffectRemaining;
    private int defendRoundsRemaining;
    private ActionRequest nextAction;

    public Player(String name, PlayerClass playerClass) {
        super(name,
                playerClass.getBaseHp(),
                playerClass.getBaseAttack(),
                playerClass.getBaseDefense(),
                playerClass.getBaseSpeed());

        this.playerClass = playerClass;
        this.inventory = new Inventory();
        this.smokeBombEffectRemaining = 0;
        this.defendRoundsRemaining = 0;
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

    public void activateDefend() {
        if (defendRoundsRemaining == 0) {
            modifyDefense(10);
        }
        defendRoundsRemaining = DEFEND_ROUNDS;
    }


    public void onRoundEnd() {
        if (defendRoundsRemaining > 0) {
            defendRoundsRemaining--;
            if (defendRoundsRemaining == 0) {
                modifyDefense(-10);
            }
        }
    }

    @Override
    public void prepareTurn(ActionRequest request) {
        this.nextAction = request;
    }

    @Override
    public ActionResult performTurn(ActionProcessor processor, List<Combatant> enemies) {
        if (nextAction == null) {
            return ActionResult.failure(null, "Player action request is missing.");
        }

        String validationError = validateAction(enemies);
        if (validationError != null) {
            return ActionResult.failure(nextAction.getActionType(), validationError);
        }

        return processor.execute(nextAction);
    }

    private String validateAction(List<Combatant> enemies) {
        ActionType actionType = nextAction.getActionType();
        if (actionType == null) {
            return "Player action type is missing.";
        }

        return actionType.validate(this, enemies, nextAction);
    }

    public void onSmokeAttack() {
        if (smokeBombEffectRemaining > 0) {
            smokeBombEffectRemaining--;
        }
    }

    public abstract String usePlayerSpecial(List<Combatant> targets);
    public abstract String usePlayerSpecialWithoutCooldown(List<Combatant> targets);
    public abstract boolean isValidSpecialTargetSelection(Combatant selectedTarget);

    public List<Combatant> resolveSpecialTargets(List<Combatant> enemies, Combatant selectedTarget) {
        return enemies == null ? Collections.emptyList() : enemies;
    }

    public int getLevelSpecialKills() {
        return 0;
    }

    public int getLevelSpecialBonus() {
        return 0;
    }

    public void resetLevelSpecialProgressForLevelEnd() {
    }

    @Override
    public String toString() {
        return String.format("%s %s %s",
                name,  playerClass.getDisplayName(), super.toString());
    }
}