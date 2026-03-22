package entity;

public class Wolf extends Enemy {

    public Wolf(String name) {
        super(name, EnemyType.WOLF);
    }

    @Override
    public String decideAction() {
        return "ATTACK";
    }

}
