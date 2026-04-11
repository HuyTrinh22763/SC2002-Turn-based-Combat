package entity.combatants;

import entity.battlerules.ActionProcessor;
import entity.battlerules.ActionRequest;
import entity.battlerules.ActionResult;
import java.util.List;

public interface Combatant extends CombatantStats, Damageable, CombatStatModifier, Stunnable,
        SpecialSkillCooldown, TurnLifecycle {
    void addStatusEffect(TurnObserver effect);

    default void prepareTurn(ActionRequest request) {
        // No-op by default for enemies to prevent interface pollution
    }

    ActionResult performTurn(ActionProcessor processor, List<Combatant> enemies);

    int getLevelSpecialKills();

    int getLevelSpecialBonus();

    void resetLevelSpecialProgressForLevelEnd();

    PlayerClass getPlayerClass();
}
