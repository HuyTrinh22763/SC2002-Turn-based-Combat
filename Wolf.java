package models.combatants;

import models.enums.EnemyType;

/**
 * Wolf enemy - High speed, high attack, low defense
 * Can attack twice if conditions are met
 */
public class Wolf extends Enemy {

    private boolean hasAttackedThisTurn;

    public Wolf(String name) {
        super(name, EnemyType.WOLF);
        this.hasAttackedThisTurn = false;
    }

    @Override
    public String decideAction() {
        // Wolves are aggressive and attack the lowest HP target
        return "ATTACK";
    }

    @Override
    public void onTurnStart() {
        super.onTurnStart();
        hasAttackedThisTurn = false;
    }

    @Override
    public void onTurnEnd() {
        super.onTurnEnd();
        // Wolves can attack twice if they're below half health
        if (!hasAttackedThisTurn && currentHp < maxHp / 2) {
            System.out.println(name + " attacks again in its rage!");
            // This will be handled by the battle system
            hasAttackedThisTurn = true;
        }
    }

    /**
     * Called by battle system when wolf attacks
     */
    public void markAttacked() {
        this.hasAttackedThisTurn = true;
    }

    public boolean canAttackAgain() {
        return !hasAttackedThisTurn && currentHp < maxHp / 2;
    }
}