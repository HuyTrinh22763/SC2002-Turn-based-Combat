package entity.battlerules;

import entity.combatants.AttributeManager;
import entity.combatants.Combatant;
import entity.combatants.Enemy;
import entity.combatants.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActionExecutor implements ActionProcessor {

    public ActionResult execute(ActionRequest request) {
        if (request == null || request.getActor() == null || request.getActionType() == null) {
            return ActionResult.failure(null, "Invalid action request.");
        }

        // If combatant is dead, no action is executed
        if (!request.getActor().isAlive()) {
            return ActionResult.failure(request.getActionType(),
                    request.getActor().getName() + " cannot act because they are eliminated.");
        }

        switch (request.getActionType()) {
            case BASIC_ATTACK:
                return executeBasicAttack(request);
            case DEFEND:
                return executeDefend(request);
            case USE_ITEM:
                return executeUseItem(request);
            case SPECIAL_SKILL:
                return executeSpecialSkill(request);
            default:
                return ActionResult.failure(request.getActionType(), "Unsupported action.");
        }
    }

    private ActionResult executeBasicAttack(ActionRequest request) {
        Combatant actor = request.getActor();
        Combatant target = request.getSelectedTarget();
        if (target == null || !target.isAlive()) {
            return ActionResult.failure(ActionType.BASIC_ATTACK, "Invalid Basic Attack target.");
        }

        if (actor instanceof Enemy && target instanceof Player) {
            Player playerTarget = (Player) target;
            if (playerTarget.isSmokeBombActive()) {
                playerTarget.onSmokeAttack();

                List<String> messages = new ArrayList<>();
                messages.add(actor.getName()
                        + " uses BasicAttack on "
                        + target.getName()
                        + " but Smoke Bomb reduces the damage to 0.");
                return ActionResult.success(ActionType.BASIC_ATTACK, 0, 0, false, messages);
            }
        }

        int damage = AttributeManager.calculateDamage(actor.getAttack(), target.getDefense());
        target.takeDamage(damage);
        boolean eliminated = !target.isAlive();

        List<String> messages = new ArrayList<>();
        messages.add(String.format("%s uses BasicAttack on %s for %d damage.",
                actor.getName(), target.getName(), damage));
        if (eliminated) {
            messages.add(target.getName() + " is eliminated.");
        }

        return ActionResult.success(ActionType.BASIC_ATTACK, damage, 0, eliminated, messages);
    }

    private ActionResult executeDefend(ActionRequest request) {
        Player player = request.getActorAsPlayer();
        if (player == null) {
            return ActionResult.failure(ActionType.DEFEND, "Only player combatants can Defend.");
        }
        player.activateDefend();

        return ActionResult.success(ActionType.DEFEND, 0, 0, false,
                Collections.singletonList(player.getName()
                        + " uses Defend: +10 DEF for current and next round."));
    }

    private ActionResult executeUseItem(ActionRequest request) {
        Player player = request.getActorAsPlayer();
        if (player == null) {
            return ActionResult.failure(ActionType.USE_ITEM, "Only player combatants can use items.");
        }
        if (request.getItemIndex() == null) {
            return ActionResult.failure(ActionType.USE_ITEM, "Item index is required.");
        }
        
        // After each turn, the inventory size is checked whether if a new item has been used
        int beforeSize = player.getInventory().size();
        String message = player.useInventoryItem(request.getItemIndex(),
                defaultList(request.getEnemies()), request.getSelectedTarget());
        int afterSize = player.getInventory().size();
        boolean consumed = afterSize < beforeSize;

        if (!consumed) {
            return ActionResult.failure(ActionType.USE_ITEM, message);
        }

        return ActionResult.success(ActionType.USE_ITEM, 0, 0, false,
                Collections.singletonList(message));
    }

    private ActionResult executeSpecialSkill(ActionRequest request) {
        Player player = request.getActorAsPlayer();
        if (player == null) {
            return ActionResult.failure(ActionType.SPECIAL_SKILL,
                    "Only player combatants can use special skills.");
        }

        List<Combatant> targets = player.resolveSpecialTargets(request.getEnemies(), request.getSelectedTarget());
        String message = player.usePlayerSpecial(targets);
        if (isSpecialSkillFailureMessage(message)) {
            return ActionResult.failure(ActionType.SPECIAL_SKILL, message);
        }
        return ActionResult.success(ActionType.SPECIAL_SKILL, 0, 0, false,
                Collections.singletonList(message));
    }

    private static boolean isSpecialSkillFailureMessage(String message) {
        if (message == null) {
            return true;
        }
        String lower = message.toLowerCase();
        return lower.contains("cooldown") || lower.contains("no target");
    }

    private List<Combatant> defaultList(List<Combatant> list) {
        return list == null ? Collections.emptyList() : list;
    }
}
