package entity.combatants;

public enum PlayerClass {
    WARRIOR("Warrior", 260, 40, 20, 30),
    WIZARD("Wizard", 200, 50, 10, 20);

    private final String displayName;
    private final int baseHp;
    private final int baseAttack;
    private final int baseDefense;
    private final int baseSpeed;

    PlayerClass(String displayName, int baseHp, int baseAttack, int baseDefense, int baseSpeed) {
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
