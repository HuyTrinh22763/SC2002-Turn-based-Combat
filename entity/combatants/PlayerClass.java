package entity.combatants;

public enum PlayerClass {
    WARRIOR("Warrior", 260, 40, 20, 30, true){
        @Override
        public Player create(String name) {
            return new Warrior(name);
        }
    },
    WIZARD("Wizard", 200, 50, 10, 20, false){
        @Override
        public Player create(String name) {
            return new Wizard(name);
        }
    };

    private final String displayName;
    private final int baseHp;
    private final int baseAttack;
    private final int baseDefense;
    private final int baseSpeed;
    private final boolean requiresTargetForSpecialSkill;

    PlayerClass(String displayName, int baseHp, int baseAttack, int baseDefense, int baseSpeed, boolean requiresTargetForSpecialSkill) {
        this.displayName = displayName;
        this.baseHp = baseHp;
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
        this.baseSpeed = baseSpeed;
        this.requiresTargetForSpecialSkill = requiresTargetForSpecialSkill;
    }

    public String getDisplayName() { return displayName; }
    public int getBaseHp() { return baseHp; }
    public int getBaseAttack() { return baseAttack; }
    public int getBaseDefense() { return baseDefense; }
    public int getBaseSpeed() { return baseSpeed; }
    public boolean requiresTargetForSpecialSkill() { return requiresTargetForSpecialSkill; }
    public abstract Player create(String name);
}
