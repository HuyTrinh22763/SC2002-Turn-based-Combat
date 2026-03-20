package models.combatants;

/**
 * Quick test class to verify all combatants work correctly
 */
public class TestCombatants {

    public static void main(String[] args) {
        System.out.println("=== TESTING COMBATANTS ===\n");

        // Test Warrior
        System.out.println("--- Testing Warrior ---");
        Warrior warrior = new Warrior("Aragorn");
        System.out.println(warrior);
        System.out.println("Using special (should work):");
        Goblin goblin1 = new Goblin("Test Goblin");
        System.out.println(warrior.usePlayerSpecial(goblin1));
        System.out.println(goblin1);
        System.out.println("Using special again (should be on cooldown):");
        System.out.println(warrior.usePlayerSpecial(goblin1));

        System.out.println("\n--- Testing Wizard ---");
        Wizard wizard = new Wizard("Gandalf");
        System.out.println(wizard);
        Wolf wolf1 = new Wolf("Test Wolf");
        System.out.println(wizard.usePlayerSpecial(wolf1));
        System.out.println(wizard);

        System.out.println("\n--- Testing Damage and Healing ---");
        System.out.println("Warrior takes 30 damage:");
        warrior.takeDamage(30);
        System.out.println(warrior);
        System.out.println("Warrior heals 20:");
        warrior.heal(20);
        System.out.println(warrior);

        System.out.println("\n--- Testing Death ---");
        Goblin goblin2 = new Goblin("Weak Goblin");
        System.out.println(goblin2);
        System.out.println("Dealing 100 damage:");
        goblin2.takeDamage(100);
        System.out.println("Is alive? " + goblin2.isAlive());
        System.out.println(goblin2);

        System.out.println("\n--- Testing Turn Start/End ---");
        System.out.println("Stunning wolf for 2 turns:");
        wolf1.setStunned(true);
        wolf1.setStunDuration(2);
        System.out.println("Wolf turn start (stun duration: " + wolf1.getStunDuration() + ")");
        wolf1.onTurnStart();
        System.out.println("After turn start: " + wolf1);
        System.out.println("Wolf turn start again:");
        wolf1.onTurnStart();
        System.out.println("After turn start: " + wolf1);

        System.out.println("\n=== ALL TESTS COMPLETE ===");
    }
}
