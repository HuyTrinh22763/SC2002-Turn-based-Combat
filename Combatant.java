package models.combatants;

public interface Combatant {

    // Basic getters
    String getName();
    int getMaxHp();
    int getCurrentHp();
    int getAttack();
    int getDefense();
    int getSpeed();

    // Combat actions
    void takeDamage(int damage);
    void heal(int amount);
    boolean isAlive();

    // Temporary modifiers
    void modifyAttack(int amount);
    void modifyDefense(int amount);
    void modifySpeed(int amount);
    void resetTemporaryModifiers();

    // Status effect tracking
    boolean isStunned();
    void setStunned(boolean stunned);
    int getStunDuration();
    void setStunDuration(int duration);

    // Special ability tracking
    boolean canUseSpecial();
    void useSpecial();
    void reduceCooldown();
    int getSpecialCooldown();

    // Turn management
    void onTurnStart();
    void onTurnEnd();
}