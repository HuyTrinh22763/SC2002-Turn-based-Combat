package entity.battlerules;

import entity.combatants.Combatant;
import entity.combatants.Player;
import java.util.List;


public enum ActionType {
    BASIC_ATTACK {
        @Override
        public String validate(Player actor, List<Combatant> enemies, ActionRequest nextAction) {
            Combatant target = nextAction.getSelectedTarget();
            if (target == null) return "Basic Attack requires a target.";
            if (!enemies.contains(target)) return "Basic Attack target must be a current enemy.";
            if (!target.isAlive()) return "Basic Attack target is already eliminated.";
            return null;
        }
    },
    DEFEND {
        @Override
        public String validate(Player actor, List<Combatant> enemies, ActionRequest nextAction) {
            return null;
        }
    },
    USE_ITEM {
        @Override
        public String validate(Player actor, List<Combatant> enemies, ActionRequest nextAction) {
            if (nextAction.getItemIndex() == null) {
                return "Using an item requires an item index.";
            }
            return null;
        }
    },
    SPECIAL_SKILL {
        @Override
        public String validate(Player actor, List<Combatant> enemies, ActionRequest nextAction) {
            if (!actor.canUseSpecial()) {
                return String.format("Special Skill is on cooldown for %d more turns!", actor.getSpecialCooldown());
            }
            Combatant target = nextAction.getSelectedTarget();
            if (!actor.isValidSpecialTargetSelection(target)) {
                return "Special Skill target requirements not met for this player class.";
            }
            if (target != null && !enemies.contains(target)) {
                return "Special Skill target must be a current enemy.";
            }
            return null;
        }
    };

    public abstract String validate(Player actor, List<Combatant> enemies, ActionRequest nextAction);
}

