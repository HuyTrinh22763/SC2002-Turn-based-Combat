package control;

import entity.battlerules.ActionProcessor;
import entity.battlerules.DifficultyLevel;
import entity.battlerules.LevelCatalog;
import entity.battlerules.LevelDefinition;
import entity.combatants.Player;

public final class TurnManagerFactory {

    private TurnManagerFactory() {
    }

    public static TurnManager fromLevelNumber(Player player, TurnOrderStrategy turnOrderStrategy, int levelNumber) {
        LevelDefinition levelDefinition = LevelCatalog.getByLevelNumber(levelNumber);
        if (levelDefinition == null) {
            throw new IllegalArgumentException("Unknown level number: " + levelNumber);
        }
        return fromLevelDefinition(player, turnOrderStrategy, levelDefinition);
    }

    public static TurnManager fromDifficulty(Player player, TurnOrderStrategy turnOrderStrategy,
            DifficultyLevel difficultyLevel) {
        LevelDefinition levelDefinition = LevelCatalog.getByDifficulty(difficultyLevel);
        if (levelDefinition == null) {
            throw new IllegalArgumentException("Unknown difficulty level: " + difficultyLevel);
        }
        return fromLevelDefinition(player, turnOrderStrategy, levelDefinition);
    }

    private static TurnManager fromLevelDefinition(Player player, TurnOrderStrategy turnOrderStrategy,
            LevelDefinition levelDefinition) {
        ActionProcessor actionProcessor = new ActionExecutor();
        EnemyFactory enemyFactory = new DefaultEnemyFactory();
        return new TurnManager(player, null, turnOrderStrategy, actionProcessor, levelDefinition, enemyFactory);
    }
}
