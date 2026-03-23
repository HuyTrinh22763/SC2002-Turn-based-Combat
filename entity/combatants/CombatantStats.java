package entity.combatants;

public interface CombatantStats {

    String getName();

    int getMaxHp();

    int getCurrentHp();

    int getAttack();

    int getDefense();

    int getSpeed();

    boolean isAlive();
}
