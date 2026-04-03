package entity.combatants;

import entity.battlerules.ActionType;

public class Goblin extends Enemy {

    public Goblin(String name) {
        super(name, EnemyType.GOBLIN);
    }

    @Override
    public ActionType decideAction() {
        return ActionType.BASIC_ATTACK;
    }

}