package entity.battlerules;

import entity.combatants.EnemyType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class LevelCatalog {

    private static final List<LevelDefinition> DEFAULT_LEVELS = buildDefaultLevels();

    private LevelCatalog() {
    }

    private static List<LevelDefinition> buildDefaultLevels() {
        List<LevelDefinition> levels = new ArrayList<>();
        levels.add(new LevelDefinition(
                1,
                DifficultyLevel.EASY,
                EnemyWave.of(EnemyType.GOBLIN, 3),
                null));
        levels.add(new LevelDefinition(
                2,
                DifficultyLevel.MEDIUM,
                EnemyWave.of(EnemyType.GOBLIN, 1, EnemyType.WOLF, 1),
                EnemyWave.of(EnemyType.WOLF, 2)));
        levels.add(new LevelDefinition(
                3,
                DifficultyLevel.HARD,
                EnemyWave.of(EnemyType.GOBLIN, 2),
                EnemyWave.of(EnemyType.GOBLIN, 1, EnemyType.WOLF, 2)));
        return Collections.unmodifiableList(levels);
    }

    public static List<LevelDefinition> getDefaultLevels() {
        return DEFAULT_LEVELS;
    }

    public static LevelDefinition getByLevelNumber(int levelNumber) {
        for (LevelDefinition level : getDefaultLevels()) {
            if (level.getLevelNumber() == levelNumber) {
                return level;
            }
        }
        return null;
    }

    public static LevelDefinition getByDifficulty(DifficultyLevel difficulty) {
        if (difficulty == null) {
            return null;
        }
        return getByLevelNumber(difficulty.getLevelNumber());
    }
}
