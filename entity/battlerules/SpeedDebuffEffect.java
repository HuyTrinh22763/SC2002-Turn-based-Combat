package entity.battlerules;

import entity.combatants.Combatant;
import entity.combatants.TurnObserver;

public class SpeedDebuffEffect implements TurnObserver {

    private final int reductionAmount;
    private int durationRemaining;

    public SpeedDebuffEffect(int reductionAmount, int duration) {
        this.reductionAmount = reductionAmount;
        this.durationRemaining = duration;
    }

    @Override
    public void onAttached(Combatant target) {
        if (target != null) {
            target.modifySpeed(-reductionAmount);
        }
    }

    @Override
    public void onTurnStart(Combatant target) {
        // No action required on turn start
    }

    @Override
    public void onTurnEnd(Combatant target) {
        if (durationRemaining > 0) {
            durationRemaining--;
            if (durationRemaining == 0 && target != null) {
                target.modifySpeed(reductionAmount);
            }
        }
    }

    @Override
    public boolean isExpired() {
        return durationRemaining <= 0;
    }
}
