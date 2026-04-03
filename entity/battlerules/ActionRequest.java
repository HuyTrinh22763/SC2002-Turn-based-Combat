package entity.battlerules;

import entity.combatants.Combatant;
import entity.combatants.Player;

import java.util.List;

public class ActionRequest {

    private final Combatant actor;
    private final ActionType actionType;
    private final Combatant selectedTarget;
    private final List<Combatant> enemies;
    private final Integer itemIndex;

    public ActionRequest(Combatant actor, ActionType actionType, Combatant selectedTarget,
            List<Combatant> enemies, Integer itemIndex) {
        this.actor = actor;
        this.actionType = actionType;
        this.selectedTarget = selectedTarget;
        this.enemies = enemies;
        this.itemIndex = itemIndex;
    }

    public Combatant getActor() {
        return actor;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public Combatant getSelectedTarget() {
        return selectedTarget;
    }

    public List<Combatant> getEnemies() {
        return enemies;
    }

    public Integer getItemIndex() {
        return itemIndex;
    }

    // Used to check if the actor is a "Player", so that they can use 'Defend' in ActionExecutor.java
    public Player getActorAsPlayer() { 
        return actor instanceof Player ? (Player) actor : null;
    }
}
