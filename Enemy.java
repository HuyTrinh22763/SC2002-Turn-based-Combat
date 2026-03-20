package models.combatants;

import models.enums.EnemyType;

/**
 * Abstract base class for all enemy characters.
 */
public abstract class Enemy extends AbstractCombatant {

    protected final EnemyType enemyType;

    public Enemy(String name, EnemyType enemyType) {
        super(name,
                enemyType.getBaseHp(),
                enemyType.getBaseAttack(),
                enemyType.getBaseDefense(),
                enemyType.getBaseSpeed());

        this.enemyType = enemyType;
    }

    public EnemyType getEnemyType() {
        return enemyType;
    }

    /**
     * Enemy's AI decision making
     * @return The action the enemy will take
     */
    public abstract String decideAction();

    @Override
    public String toString() {
        return String.format("%s (%s) %s",
                name, enemyType.getDisplayName(), super.toString());
    }
}