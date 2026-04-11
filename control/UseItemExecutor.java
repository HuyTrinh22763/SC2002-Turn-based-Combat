package control;

import entity.battlerules.ActionProcessor;
import entity.battlerules.ActionRequest;
import entity.battlerules.ActionResult;
import entity.battlerules.ActionType;
import entity.combatants.Player;

import java.util.Collections;

public class UseItemExecutor implements ActionProcessor {

    @Override
    public ActionResult execute(ActionRequest request) {
        Player player = request.getActorAsPlayer();
        if (player == null) {
            return ActionResult.failure(ActionType.USE_ITEM, "Only players can use items.");
        }

        if (request.getItemIndex() == null) {
            return ActionResult.failure(ActionType.USE_ITEM, "Using an item requires an item index.");
        }
        
        int beforeSize = player.getInventory().size();
        String message = player.useInventoryItem(request.getItemIndex(), 
                request.getEnemies() != null ? request.getEnemies() : Collections.emptyList(), 
                request.getSelectedTarget());
        int afterSize = player.getInventory().size();
        boolean consumed = afterSize < beforeSize;

        if (!consumed) {
            return ActionResult.failure(ActionType.USE_ITEM, message);
        }

        return ActionResult.success(ActionType.USE_ITEM, 0, 0, false, Collections.singletonList(message));
    }
}
