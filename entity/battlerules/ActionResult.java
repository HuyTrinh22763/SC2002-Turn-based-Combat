package entity.battlerules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActionResult {

    private final boolean success;
    private final ActionType actionType;
    private final int damageDealt;
    private final int healedAmount;
    private final boolean targetEliminated;
    private final List<String> messages;

    private ActionResult(boolean success, ActionType actionType, int damageDealt, int healedAmount,
            boolean targetEliminated, List<String> messages) {
        this.success = success;
        this.actionType = actionType;
        this.damageDealt = damageDealt;
        this.healedAmount = healedAmount;
        this.targetEliminated = targetEliminated;
        this.messages = messages;
    }

    public static ActionResult success(ActionType actionType, int damageDealt, int healedAmount,
            boolean targetEliminated, List<String> messages) {
        return new ActionResult(true, actionType, damageDealt, healedAmount, targetEliminated,
                new ArrayList<>(messages));
    }

    public static ActionResult failure(ActionType actionType, String message) {
        List<String> messages = new ArrayList<>();
        messages.add(message);
        return new ActionResult(false, actionType, 0, 0, false, messages);
    }

    public boolean isSuccess() {
        return success;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public int getDamageDealt() {
        return damageDealt;
    }

    public int getHealedAmount() {
        return healedAmount;
    }

    public boolean isTargetEliminated() {
        return targetEliminated;
    }

    public List<String> getMessages() {
        return Collections.unmodifiableList(messages);
    }
}
