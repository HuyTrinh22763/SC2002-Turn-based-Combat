package control;

import entity.battlerules.ActionType;
import entity.combatants.PlayerClass;

public class TurnSpecialSnapshot {

    private final int roundNumber;
    private final int turnNumber;
    private final String actorName;
    private final PlayerClass playerClass;
    private final ActionType actionType;
    private final int deltaSpecialKills;
    private final int deltaSpecialBonus;
    private final int totalSpecialKillsAfterTurn;
    private final int totalSpecialBonusAfterTurn;

    public TurnSpecialSnapshot(int roundNumber, int turnNumber, String actorName, PlayerClass playerClass,
            ActionType actionType, int deltaSpecialKills, int deltaSpecialBonus, int totalSpecialKillsAfterTurn,
            int totalSpecialBonusAfterTurn) {
        this.roundNumber = roundNumber;
        this.turnNumber = turnNumber;
        this.actorName = actorName;
        this.playerClass = playerClass;
        this.actionType = actionType;
        this.deltaSpecialKills = deltaSpecialKills;
        this.deltaSpecialBonus = deltaSpecialBonus;
        this.totalSpecialKillsAfterTurn = totalSpecialKillsAfterTurn;
        this.totalSpecialBonusAfterTurn = totalSpecialBonusAfterTurn;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public String getActorName() {
        return actorName;
    }

    public PlayerClass getPlayerClass() {
        return playerClass;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public int getDeltaSpecialKills() {
        return deltaSpecialKills;
    }

    public int getDeltaSpecialBonus() {
        return deltaSpecialBonus;
    }

    public int getTotalSpecialKillsAfterTurn() {
        return totalSpecialKillsAfterTurn;
    }

    public int getTotalSpecialBonusAfterTurn() {
        return totalSpecialBonusAfterTurn;
    }
}
