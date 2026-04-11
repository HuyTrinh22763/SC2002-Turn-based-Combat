package control;

import entity.battlerules.ActionProcessor;
import entity.battlerules.ActionRequest;
import entity.battlerules.ActionResult;
import entity.battlerules.ActionType;
import entity.combatants.Player;

import java.util.Collections;

public class DefendExecutor implements ActionProcessor {

    @Override
    public ActionResult execute(ActionRequest request) {
        Player player = request.getActorAsPlayer();
        if (player == null) {
            return ActionResult.failure(ActionType.DEFEND, "Only players can Defend.");
        }
        player.activateDefend();
        return ActionResult.success(ActionType.DEFEND, 0, 0, false,
                Collections.singletonList(player.getName() + " uses Defend: +10 DEF for current and next round."));
    }
}
