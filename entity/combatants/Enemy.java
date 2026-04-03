package entity.combatants;

import entity.battlerules.ActionType;

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

    public abstract ActionType decideAction();

    @Override
    public String toString() {
        return String.format("%s (%s) %s",
                name, enemyType.getDisplayName(), super.toString());
    }
}