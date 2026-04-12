package control;

import entity.battlerules.ActionProcessor;
import entity.battlerules.ActionRequest;
import entity.battlerules.ActionResult;
import entity.battlerules.ActionType;
import entity.combatants.AttributeManager;
import entity.combatants.Combatant;
import entity.combatants.Enemy;
import entity.combatants.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BasicAttackExecutor implements ActionProcessor {

    @Override
    public ActionResult execute(ActionRequest request) {
        Combatant actor = request.getActor();
        Combatant target = request.getSelectedTarget();

        if (actor == null || target == null) {
            return ActionResult.failure(ActionType.BASIC_ATTACK, "Basic Attack requires an actor and a target.");
        }

        if (actor instanceof Enemy && target instanceof Player) {
            Player playerTarget = (Player) target;
            if (playerTarget.isSmokeBombActive()) {
                return ActionResult.success(ActionType.BASIC_ATTACK, 0, 0, false,
                        Collections.singletonList(actor.getName() + " uses BasicAttack on " + target.getName()
                                + " but Smoke Bomb reduces the damage to 0."));
            }
        }

        int damage = AttributeManager.calculateDamage(actor.getAttack(), target.getDefense());
        target.takeDamage(damage);
        boolean eliminated = !target.isAlive();

        List<String> messages = new ArrayList<>();
        messages.add(String.format("%s uses BasicAttack on %s for %d damage.", actor.getName(), target.getName(), damage));
        if (eliminated) {
            messages.add(target.getName() + " is eliminated.");
        }

        return ActionResult.success(ActionType.BASIC_ATTACK, damage, 0, eliminated, messages);
    }
}
