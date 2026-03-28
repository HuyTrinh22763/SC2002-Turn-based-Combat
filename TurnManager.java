package control;

import entity.battlerules.ActionExecutor;
import entity.battlerules.ActionRequest;
import entity.battlerules.ActionResult;
import entity.battlerules.ActionType;
import entity.combatants.Combatant;
import entity.combatants.Enemy;
import entity.combatants.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TurnManager {

    private final Player player;
    private final List<Combatant> enemies;
    private final TurnOrderStrategy turnOrderStrategy;
    private final ActionExecutor actionExecutor;

    private BattleState battleState;
    private int roundNumber;
    private List<Combatant> currentTurnOrder;
    private int currentTurnIndex;

    public TurnManager(Player player, List<Combatant> enemies, TurnOrderStrategy turnOrderStrategy) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }
        if (turnOrderStrategy == null) {
            throw new IllegalArgumentException("TurnOrderStrategy cannot be null.");
        }

        this.player = player;
        this.enemies = new ArrayList<>();

        if (enemies == null) { 
            throw new IllegalArgumentException("Enemies list cannot be null.");
        }
        
        for (Combatant enemy : enemies) {
            if (enemy != null) {
                this.enemies.add(enemy);
            }
        }
        
        

        this.turnOrderStrategy = turnOrderStrategy;
        this.actionExecutor = new ActionExecutor();

        this.battleState = BattleState.ONGOING;
        this.roundNumber = 0;
        this.currentTurnOrder = new ArrayList<>();
        this.currentTurnIndex = 0;
    }

    public void startNewRound() {
        if (isBattleOver()) {
            return;
        }

        roundNumber++;
        currentTurnOrder = turnOrderStrategy.determineOrder(getAliveCombatants());
        currentTurnIndex = 0;
        updateBattleState();
    }

    public boolean hasMoreTurnsInRound() {
        return currentTurnIndex < currentTurnOrder.size();
    }

    public Combatant getCurrentActor() {
        if (!hasMoreTurnsInRound()) {
            return null;
        }
        return currentTurnOrder.get(currentTurnIndex);
    }

    public ActionResult processCurrentTurn(ActionRequest playerRequest) {
        if (isBattleOver()) {
            return ActionResult.failure(null, "Battle is already over.");
        }

        Combatant actor = getCurrentActor();
        if (actor == null) {
            return ActionResult.failure(null, "No combatant is available to act.");
        }

        if (!actor.isAlive()) {
            ActionResult result = ActionResult.failure(
                    null,
                    actor.getName() + " is eliminated and cannot act."
            );
            finishTurn(actor);
            return result;
        }

        actor.onTurnStart();

        if (actor.isStunned()) {
            ActionResult result = ActionResult.failure(
                    null,
                    actor.getName() + " is stunned and skips the turn."
            );
            finishTurn(actor);
            return result;
        }

        ActionResult result;
        if (actor == player) {
            result = processPlayerTurn(playerRequest);
        } else if (actor instanceof Enemy) {
            result = processEnemyTurn((Enemy) actor);
        } else {
            result = ActionResult.failure(null,
                    actor.getName() + " is not a supported combatant type.");
        }

        finishTurn(actor);
        return result;
    }

    public void endRound() {
        player.onRoundEnd();
        updateBattleState();
    }

    public BattleState getBattleState() {
        return battleState;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public Player getPlayer() {
        return player;
    }

    public List<Combatant> getEnemiesView() {
        return Collections.unmodifiableList(enemies);
    }

    public List<Combatant> getCurrentTurnOrderView() {
        return Collections.unmodifiableList(currentTurnOrder);
    }

    public boolean isBattleOver() {
        return battleState != BattleState.ONGOING;
    }

    private ActionResult processPlayerTurn(ActionRequest playerRequest) {
        String validationMessage = validatePlayerRequest(playerRequest);
        if (validationMessage != null) {
            return ActionResult.failure(
                    playerRequest == null ? null : playerRequest.getActionType(),
                    validationMessage
            );
        }

        return actionExecutor.execute(playerRequest);
    }

    private String validatePlayerRequest(ActionRequest playerRequest) {
        if (playerRequest == null) {
            return "Player action request is missing.";
        }

        if (playerRequest.getActor() != player) {
            return "The action request actor does not match the player.";
        }

        ActionType actionType = playerRequest.getActionType();
        if (actionType == null) {
            return "Player action type is missing.";
        }

        if (actionType == ActionType.BASIC_ATTACK) {
            Combatant target = playerRequest.getSelectedTarget();
            if (target == null) {
                return "Basic Attack requires a target.";
            }
            if (!isCurrentEnemy(target)) {
                return "Basic Attack target must be a current enemy.";
            }
            if (!target.isAlive()) {
                return "Basic Attack target is already eliminated.";
            }
        }

        if (actionType == ActionType.SPECIAL_SKILL) {
            if (playerRequest.getSelectedTarget() != null) {
                Combatant target = playerRequest.getSelectedTarget();
                if (!isCurrentEnemy(target)) {
                    return "Special Skill target must be a current enemy.";
                }
            }
        }

        if (actionType == ActionType.USE_ITEM) {
            if (playerRequest.getItemIndex() == null) {
                return "Using an item requires an item index.";
            }
        }

        return null;
    }

    private ActionResult processEnemyTurn(Enemy enemy) {
        if (!player.isAlive()) {
            return ActionResult.failure(
                    ActionType.BASIC_ATTACK,
                    enemy.getName() + " cannot attack because the player is already defeated."
            );
        }

        String decision = enemy.decideAction();
        ActionType actionType = mapEnemyDecisionToActionType(decision);

        if (actionType == null) {
            return ActionResult.failure(
                    null,
                    enemy.getName() + " has an unsupported action decision: " + decision
            );
        }

        if (actionType == ActionType.BASIC_ATTACK && player.isSmokeBombActive()) {
            player.onSmokeAttack();

            List<String> messages = new ArrayList<>();
            messages.add(enemy.getName()
                    + " uses BasicAttack on "
                    + player.getName()
                    + " but Smoke Bomb reduces the damage to 0.");

            return ActionResult.success(ActionType.BASIC_ATTACK, 0, 0, false, messages);
        }

        ActionRequest enemyRequest = new ActionRequest(
                enemy,
                actionType,
                player,
                null,
                null
        );

        return actionExecutor.execute(enemyRequest);
    }

    private ActionType mapEnemyDecisionToActionType(String decision) {
        if (decision == null) {
            return null;
        }

        if ("ATTACK".equalsIgnoreCase(decision)) {
            return ActionType.BASIC_ATTACK;
        }

        return null;
    }

    private void finishTurn(Combatant actor) {
        actor.onTurnEnd();
        updateBattleState();
        currentTurnIndex++;
    }

    private void updateBattleState() {
        if (!player.isAlive()) {
            battleState = BattleState.PLAYER_DEFEAT;
            return;
        }

        boolean allEnemiesDefeated = true;
        for (Combatant enemy : enemies) {
            if (enemy != null && enemy.isAlive()) {
                allEnemiesDefeated = false;
                break;
            }
        }

        if (allEnemiesDefeated) {
            battleState = BattleState.PLAYER_VICTORY;
        } else {
            battleState = BattleState.ONGOING;
        }
    }

    private List<Combatant> getAliveCombatants() {
        List<Combatant> aliveCombatants = new ArrayList<>();

        if (player.isAlive()) {
            aliveCombatants.add(player);
        }

        for (Combatant enemy : enemies) {
            if (enemy != null && enemy.isAlive()) {
                aliveCombatants.add(enemy);
            }
        }

        return aliveCombatants;
    }

    private boolean isCurrentEnemy(Combatant target) {
        for (Combatant enemy : enemies) {
            if (enemy == target) {
                return true;
            }
        }
        return false;
    }
}





