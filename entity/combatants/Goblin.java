package entity.combatants;

public class Goblin extends Enemy {

    public Goblin(String name) {
        super(name, EnemyType.GOBLIN);
    }

    @Override
    public String decideAction() {
        return "ATTACK";
    }

}