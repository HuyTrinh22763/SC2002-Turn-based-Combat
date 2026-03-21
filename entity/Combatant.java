package entity;

public interface Combatant {

    String getName();
    int getMaxHp();
    int getCurrentHp();
    int getAttack();
    int getDefense();
    int getSpeed();

    void takeDamage(int damage);
    void heal(int amount);
    boolean isAlive();

    void modifyAttack(int amount);
    void modifyDefense(int amount);
    void modifySpeed(int amount);
    void resetTemporaryModifiers();

    boolean isStunned();
    void setStunned(boolean stunned);
    int getStunDuration();
    void setStunDuration(int duration);

    boolean canUseSpecial();
    void useSpecial();
    void reduceCooldown();
    int getSpecialCooldown();

    void onTurnStart();
    void onTurnEnd();
}