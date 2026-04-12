package control;

import entity.battlerules.ActionProcessor;
import entity.battlerules.ActionRequest;
import entity.battlerules.ActionResult;
import entity.battlerules.ActionType;
import entity.combatants.Combatant;

import java.util.Collections;

public class WaitExecutor implements ActionProcessor {

    @Override
    public ActionResult execute(ActionRequest request) {
        Combatant actor = request.getActor();
        if (actor == null) {
            return ActionResult.failure(ActionType.WAIT, "Wait action requires an actor.");
        }

        int maxHp = actor.getMaxHp();
        int healAmount = (int) (maxHp * 0.1);
        int oldHp = actor.getCurrentHp();
        actor.heal(healAmount);
        int actualHealed = actor.getCurrentHp() - oldHp;

        String message = String.format("%s uses Wait and recovers %d HP.", actor.getName(), actualHealed);

        return ActionResult.success(ActionType.WAIT, 0, actualHealed, false, Collections.singletonList(message));
    }
}
