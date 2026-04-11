package control;

import entity.battlerules.ActionProcessor;
import entity.battlerules.ActionRequest;
import entity.battlerules.ActionResult;
import entity.battlerules.ActionType;
import entity.combatants.Combatant;
import entity.combatants.Player;

import java.util.Collections;
import java.util.List;

public class SpecialSkillExecutor implements ActionProcessor {

    @Override
    public ActionResult execute(ActionRequest request) {
        Player player = request.getActorAsPlayer();
        if (player == null) {
            return ActionResult.failure(ActionType.SPECIAL_SKILL, "Only players can use special skills.");
        }

        if (!player.canUseSpecial()) {
            return ActionResult.failure(ActionType.SPECIAL_SKILL, 
                String.format("Special Skill is on cooldown for %d more turns!", player.getSpecialCooldown()));
        }
        
        Combatant target = request.getSelectedTarget();
        if (!player.isValidSpecialTargetSelection(target)) {
            return ActionResult.failure(ActionType.SPECIAL_SKILL, "Special Skill target requirements not met for this player class.");
        }
        
        if (target != null && request.getEnemies() != null && !request.getEnemies().contains(target)) {
            return ActionResult.failure(ActionType.SPECIAL_SKILL, "Special Skill target must be a current enemy.");
        }

        List<Combatant> targets = player.resolveSpecialTargets(request.getEnemies(), request.getSelectedTarget());
        String message = player.usePlayerSpecial(targets);
        
        if (message != null && (message.toLowerCase().contains("cooldown") || message.toLowerCase().contains("no target"))) {
            return ActionResult.failure(ActionType.SPECIAL_SKILL, message);
        }
        
        return ActionResult.success(ActionType.SPECIAL_SKILL, 0, 0, false, Collections.singletonList(message));
    }
}
