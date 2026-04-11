package control;

import entity.battlerules.ActionProcessor;
import entity.battlerules.ActionRequest;
import entity.battlerules.ActionResult;
import entity.battlerules.ActionType;

import java.util.HashMap;
import java.util.Map;

public class ActionExecutor implements ActionProcessor {

    private final Map<ActionType, ActionProcessor> executors;

    public ActionExecutor() {
        executors = new HashMap<>();
        executors.put(ActionType.BASIC_ATTACK, new BasicAttackExecutor());
        executors.put(ActionType.DEFEND, new DefendExecutor());
        executors.put(ActionType.USE_ITEM, new UseItemExecutor());
        executors.put(ActionType.SPECIAL_SKILL, new SpecialSkillExecutor());
    }

    @Override
    public ActionResult execute(ActionRequest request) {
        if (request == null || request.getActor() == null || request.getActionType() == null) {
            return ActionResult.failure(null, "Invalid action request.");
        }
        if (!request.getActor().isAlive()) {
            return ActionResult.failure(request.getActionType(),
                    request.getActor().getName() + " cannot act because they are eliminated.");
        }

        ActionProcessor executor = executors.get(request.getActionType());
        if (executor != null) {
            return executor.execute(request);
        }

        return ActionResult.failure(request.getActionType(), "No executor found for " + request.getActionType());
    }
}
