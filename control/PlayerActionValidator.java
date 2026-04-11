package control;

import entity.battlerules.ActionRequest;
import entity.battlerules.ActionType;
import entity.combatants.Combatant;
import entity.combatants.Player;

import java.util.List;

public class PlayerActionValidator {

    public String validate(Player player, List<Combatant> enemies, ActionRequest playerRequest) {
        if (playerRequest == null) {
            return "Player action request is missing.";
        }

        if (playerRequest.getActor() != player) {
            return "The action request actor does not match the player.";
        }

        ActionType actionType = playerRequest.getActionType();
        if (actionType == null) {
            return "Player action type is missing.";
        }

        if (actionType == ActionType.BASIC_ATTACK) {
            Combatant target = playerRequest.getSelectedTarget();
            if (target == null) {
                return "Basic Attack requires a target.";
            }
            if (!isCurrentEnemy(target, enemies)) {
                return "Basic Attack target must be a current enemy.";
            }
            if (!target.isAlive()) {
                return "Basic Attack target is already eliminated.";
            }
        }

        if (actionType == ActionType.SPECIAL_SKILL) {
            if (!player.canUseSpecial()) {
                return String.format("Special Skill is on cooldown for %d more turns!",
                        player.getSpecialCooldown());
            }
            Combatant target = playerRequest.getSelectedTarget();
            if (!player.isValidSpecialTargetSelection(target)) {
                return "Special Skill target requirements not met for this player class.";
            }
            if (target != null && !isCurrentEnemy(target, enemies)) {
                return "Special Skill target must be a current enemy.";
            }
        }

        if (actionType == ActionType.USE_ITEM && playerRequest.getItemIndex() == null) {
            return "Using an item requires an item index.";
        }

        return null;
    }

    private boolean isCurrentEnemy(Combatant target, List<Combatant> enemies) {
        for (Combatant enemy : enemies) {
            if (enemy == target) {
                return true;
            }
        }
        return false;
    }
}
