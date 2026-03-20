package models.combatants;

import models.enums.EnemyType;

/**
 * Goblin enemy - Balanced stats, can gang up
 */
public class Goblin extends Enemy {

    public Goblin(String name) {
        super(name, EnemyType.GOBLIN);
    }

    @Override
    public String decideAction() {
        // Goblins will try to gang up on the weakest target
        // This is just a placeholder - the actual battle system will handle targeting
        return "ATTACK";
    }

    @Override
    public void onTurnStart() {
        super.onTurnStart();
        // Goblins get +5 ATK for each other goblin alive (handled by battle system)
    }
}