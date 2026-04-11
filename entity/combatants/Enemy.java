package entity.combatants;

import entity.battlerules.ActionType;
import entity.battlerules.ActionResult;
import entity.battlerules.ActionRequest;
import entity.battlerules.ActionProcessor;
import java.util.List;
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
    public ActionResult performTurn(ActionProcessor processor, List<Combatant> enemies) {
        if (enemies == null || enemies.isEmpty() || !enemies.get(0).isAlive()) {
             return ActionResult.failure(
                    ActionType.BASIC_ATTACK,
                    getName() + " cannot attack because the target is already defeated."
            );
        }

        ActionType actionType = decideAction();
        if (actionType == null) {
            return ActionResult.failure(
                    null,
                    getName() + " has an unsupported action decision."
            );
        }

        ActionRequest enemyRequest = new ActionRequest(
                this,
                actionType,
                enemies.get(0),
                null,
                null
        );

        return processor.execute(enemyRequest);
    }

    @Override
    public String toString() {
        return String.format("%s (%s) %s",
                name, enemyType.getDisplayName(), super.toString());
    }
}