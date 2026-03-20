package models.stats;

/**
 * Utility class for managing attribute calculations and clamping.
 * Ensures values stay within valid ranges.
 */
public class AttributeManager {

    private static final int MIN_HP = 0;
    private static final int MAX_HP = 999;
    private static final int MIN_ATTRIBUTE = 0;
    private static final int MAX_ATTRIBUTE = 999;

    /**
     * Clamps HP value between MIN_HP and MAX_HP
     */
    public static int clampHp(int hp) {
        return Math.max(MIN_HP, Math.min(MAX_HP, hp));
    }

    /**
     * Clamps attack value between MIN_ATTRIBUTE and MAX_ATTRIBUTE
     */
    public static int clampAttack(int attack) {
        return Math.max(MIN_ATTRIBUTE, Math.min(MAX_ATTRIBUTE, attack));
    }

    /**
     * Clamps defense value between MIN_ATTRIBUTE and MAX_ATTRIBUTE
     */
    public static int clampDefense(int defense) {
        return Math.max(MIN_ATTRIBUTE, Math.min(MAX_ATTRIBUTE, defense));
    }

    /**
     * Clamps speed value between MIN_ATTRIBUTE and MAX_ATTRIBUTE
     */
    public static int clampSpeed(int speed) {
        return Math.max(MIN_ATTRIBUTE, Math.min(MAX_ATTRIBUTE, speed));
    }

    /**
     * Calculates damage with defense reduction
     * Damage = attackerAttack - targetDefense (minimum 0)
     */
    public static int calculateDamage(int attackerAttack, int targetDefense) {
        int damage = attackerAttack - targetDefense;
        return Math.max(0, damage);
    }

    /**
     * Checks if combatant is alive (HP > 0)
     */
    public static boolean isAlive(int currentHp) {
        return currentHp > MIN_HP;
    }
}