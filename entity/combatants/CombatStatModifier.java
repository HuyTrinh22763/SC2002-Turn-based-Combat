package entity.combatants;

public interface CombatStatModifier {

    void modifyAttack(int amount);

    void modifyDefense(int amount);

    void modifySpeed(int amount);

    void resetTemporaryModifiers();
}
