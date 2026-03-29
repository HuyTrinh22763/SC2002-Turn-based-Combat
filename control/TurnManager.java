package control;

import entity.battlerules.ActionExecutor;
import entity.battlerules.ActionProcessor;
import entity.battlerules.ActionRequest;
import entity.battlerules.ActionResult;
import entity.battlerules.ActionType;
import entity.battlerules.DifficultyLevel;
import entity.battlerules.EnemyWave;
import entity.battlerules.LevelCatalog;
import entity.battlerules.LevelDefinition;
import entity.combatants.Combatant;
import entity.combatants.Enemy;
import entity.combatants.EnemyType;
import entity.combatants.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TurnManager {

    private final Player player;
    private final List<Combatant> enemies;
    private final TurnOrderStrategy turnOrderStrategy;
    private final ActionProcessor actionProcessor;
    private final PlayerActionValidator playerActionValidator;
    private final LevelDefinition levelDefinition;
    private final EnemyFactory enemyFactory;

    private BattleState battleState;
    private int roundNumber;
    private List<Combatant> currentTurnOrder;
    private int currentTurnIndex;
    private boolean backupWaveSpawned;
    private int spawnedEnemyCounter;

    public TurnManager(Player player, List<Combatant> enemies, TurnOrderStrategy turnOrderStrategy) {
        this(player, enemies, turnOrderStrategy, new ActionExecutor());
    }

    public TurnManager(Player player, List<Combatant> enemies, TurnOrderStrategy turnOrderStrategy,
            ActionProcessor actionProcessor) {
        this(player, enemies, turnOrderStrategy, actionProcessor, null, null);
    }

    public TurnManager(Player player, TurnOrderStrategy turnOrderStrategy, DifficultyLevel difficultyLevel) {
        this(player, turnOrderStrategy, new ActionExecutor(), requireLevelByDifficulty(difficultyLevel),
                new DefaultEnemyFactory());
    }

    public TurnManager(Player player, TurnOrderStrategy turnOrderStrategy, int levelNumber) {
        this(player, turnOrderStrategy, new ActionExecutor(), requireLevelByNumber(levelNumber),
                new DefaultEnemyFactory());
    }

    public TurnManager(Player player, TurnOrderStrategy turnOrderStrategy, ActionProcessor actionProcessor,
            LevelDefinition levelDefinition, EnemyFactory enemyFactory) {
        this(player, createInitialEnemies(levelDefinition, enemyFactory), turnOrderStrategy, actionProcessor,
                levelDefinition, enemyFactory);
    }

    private TurnManager(Player player, List<Combatant> enemies, TurnOrderStrategy turnOrderStrategy,
            ActionProcessor actionProcessor, LevelDefinition levelDefinition, EnemyFactory enemyFactory) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }
        if (turnOrderStrategy == null) {
            throw new IllegalArgumentException("TurnOrderStrategy cannot be null.");
        }
        if (actionProcessor == null) {
            throw new IllegalArgumentException("ActionProcessor cannot be null.");
        }
        if (levelDefinition != null && enemyFactory == null) {
            throw new IllegalArgumentException("EnemyFactory is required when LevelDefinition is used.");
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
        this.actionProcessor = actionProcessor;
        this.playerActionValidator = new PlayerActionValidator();
        this.levelDefinition = levelDefinition;
        this.enemyFactory = enemyFactory;

        this.battleState = BattleState.ONGOING;
        this.roundNumber = 0;
        this.currentTurnOrder = new ArrayList<>();
        this.currentTurnIndex = 0;
        this.backupWaveSpawned = false;
        this.spawnedEnemyCounter = this.enemies.size();
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
        String validationMessage = playerActionValidator.validate(player, enemies, playerRequest);
        if (validationMessage != null) {
            return ActionResult.failure(
                    playerRequest == null ? null : playerRequest.getActionType(),
                    validationMessage
            );
        }

        return actionProcessor.execute(playerRequest);
    }

    private ActionResult processEnemyTurn(Enemy enemy) {
        if (!player.isAlive()) {
            return ActionResult.failure(
                    ActionType.BASIC_ATTACK,
                    enemy.getName() + " cannot attack because the player is already defeated."
            );
        }

        ActionType actionType = enemy.decideAction();

        if (actionType == null) {
            return ActionResult.failure(
                    null,
                    enemy.getName() + " has an unsupported action decision."
            );
        }

        ActionRequest enemyRequest = new ActionRequest(
                enemy,
                actionType,
                player,
                null,
                null
        );

        return actionProcessor.execute(enemyRequest);
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
            if (shouldSpawnBackupWave()) {
                spawnBackupWave();
                battleState = BattleState.ONGOING;
            } else {
                battleState = BattleState.PLAYER_VICTORY;
            }
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

    private boolean shouldSpawnBackupWave() {
        return levelDefinition != null
                && levelDefinition.hasBackupWave()
                && !backupWaveSpawned;
    }

    private void spawnBackupWave() {
        EnemyWave backupWave = levelDefinition.getBackupWave();
        if (backupWave == null) {
            return;
        }

        for (EnemyType enemyType : backupWave.getEnemiesView()) {
            spawnedEnemyCounter++;
            Combatant newEnemy = enemyFactory.createEnemy(enemyType, enemyType.getDisplayName() + " " + spawnedEnemyCounter);
            if (newEnemy != null) {
                enemies.add(newEnemy);
            }
        }
        backupWaveSpawned = true;
    }

    private static List<Combatant> createInitialEnemies(LevelDefinition levelDefinition, EnemyFactory enemyFactory) {
        List<Combatant> initialEnemies = new ArrayList<>();
        if (levelDefinition == null || enemyFactory == null || levelDefinition.getInitialWave() == null) {
            return initialEnemies;
        }

        int index = 0;
        for (EnemyType enemyType : levelDefinition.getInitialWave().getEnemiesView()) {
            index++;
            Combatant enemy = enemyFactory.createEnemy(enemyType, enemyType.getDisplayName() + " " + index);
            if (enemy != null) {
                initialEnemies.add(enemy);
            }
        }
        return initialEnemies;
    }

    private static LevelDefinition requireLevelByDifficulty(DifficultyLevel difficultyLevel) {
        LevelDefinition definition = LevelCatalog.getByDifficulty(difficultyLevel);
        if (definition == null) {
            throw new IllegalArgumentException("Unknown difficulty level: " + difficultyLevel);
        }
        return definition;
    }

    private static LevelDefinition requireLevelByNumber(int levelNumber) {
        LevelDefinition definition = LevelCatalog.getByLevelNumber(levelNumber);
        if (definition == null) {
            throw new IllegalArgumentException("Unknown level number: " + levelNumber);
        }
        return definition;
    }
}





