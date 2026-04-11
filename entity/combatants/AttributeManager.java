package entity.combatants;


public class AttributeManager {

    private static final int MIN_HP = 0;
    private static final int MAX_HP = 999;
    private static final int MIN_ATTRIBUTE = 0;
    private static final int MAX_ATTRIBUTE = 999;

    
    public static int clampHp(int hp) {
        return Math.max(MIN_HP, Math.min(MAX_HP, hp));
    }

    
    public static int clampAttack(int attack) {
        return Math.max(MIN_ATTRIBUTE, Math.min(MAX_ATTRIBUTE, attack));
    }

    
    public static int clampDefense(int defense) {
        return Math.max(MIN_ATTRIBUTE, Math.min(MAX_ATTRIBUTE, defense));
    }

    
    public static int clampSpeed(int speed) {
        return Math.max(MIN_ATTRIBUTE, Math.min(MAX_ATTRIBUTE, speed));
    }

    public static int calculateDamage(int attackerAttack, int targetDefense) {
        int damage = attackerAttack - targetDefense;
        return Math.max(0, damage);
    }

    
    public static boolean isAlive(int currentHp) {
        return currentHp > MIN_HP;
    }
}
