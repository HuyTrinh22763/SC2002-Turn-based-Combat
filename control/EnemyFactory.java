package control;

import entity.combatants.Combatant;
import entity.combatants.EnemyType;

public interface EnemyFactory {
    Combatant createEnemy(EnemyType enemyType, String name);
}
