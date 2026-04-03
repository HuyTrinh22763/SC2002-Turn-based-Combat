package entity.battlerules;

public class LevelDefinition {

    private final int levelNumber;
    private final DifficultyLevel difficulty;
    private final EnemyWave initialWave;
    private final EnemyWave backupWave;

    public LevelDefinition(int levelNumber, DifficultyLevel difficulty, EnemyWave initialWave, EnemyWave backupWave) {
        this.levelNumber = levelNumber;
        this.difficulty = difficulty;
        this.initialWave = initialWave;
        this.backupWave = backupWave;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public DifficultyLevel getDifficulty() {
        return difficulty;
    }

    public EnemyWave getInitialWave() {
        return initialWave;
    }

    public EnemyWave getBackupWave() {
        return backupWave;
    }

    public boolean hasBackupWave() {
        return backupWave != null && backupWave.totalEnemies() > 0;
    }
}
