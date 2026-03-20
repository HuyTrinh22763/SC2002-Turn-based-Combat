package models.enums;

/**
 * Enum representing the different enemy types.
 * Each type has unique base stats.
 */
public enum EnemyType {
    GOBLIN("Goblin", 55, 35, 15, 25),
    WOLF("Wolf", 40, 45, 5, 35);

    private final String displayName;
    private final int baseHp;
    private final int baseAttack;
    private final int baseDefense;
    private final int baseSpeed;

    EnemyType(String displayName, int baseHp, int baseAttack, int baseDefense, int baseSpeed) {
        this.displayName = displayName;
        this.baseHp = baseHp;
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
        this.baseSpeed = baseSpeed;
    }

    public String getDisplayName() { return displayName; }
    public int getBaseHp() { return baseHp; }
    public int getBaseAttack() { return baseAttack; }
    public int getBaseDefense() { return baseDefense; }
    public int getBaseSpeed() { return baseSpeed; }
}