package entity.combatants;

import entity.battlerules.ActionType;

public class Wolf extends Enemy {

    public Wolf(String name) {
        super(name, EnemyType.WOLF);
    }

    @Override
    public ActionType decideAction() {
        return ActionType.BASIC_ATTACK;
    }

}
