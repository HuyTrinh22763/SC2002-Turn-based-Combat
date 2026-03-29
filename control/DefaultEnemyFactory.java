package control;

import entity.combatants.Combatant;
import entity.combatants.EnemyType;
import entity.combatants.Goblin;
import entity.combatants.Wolf;

public class DefaultEnemyFactory implements EnemyFactory {

    @Override
    public Combatant createEnemy(EnemyType enemyType, String name) {
        if (enemyType == null) {
            throw new IllegalArgumentException("Enemy type cannot be null.");
        }

        switch (enemyType) {
            case GOBLIN:
                return new Goblin(name);
            case WOLF:
                return new Wolf(name);
            default:
                throw new IllegalArgumentException("Unsupported enemy type: " + enemyType);
        }
    }
}
