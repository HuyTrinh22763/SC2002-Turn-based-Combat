package entity.battlerules;

public enum DifficultyLevel {
    EASY(1),
    MEDIUM(2),
    HARD(3);

    private final int levelNumber;

    DifficultyLevel(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public int getLevelNumber() {
        return levelNumber;
    }
}
