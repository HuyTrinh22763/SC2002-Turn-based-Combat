package boundary;

import entity.Combatant;
import entity.Player;

import java.util.List;

/**
 * Displays the victory/defeat screen and post-game options.
 */
public class GameCompletionUI {

    private static final String SEPARATOR = "=".repeat(60);

    private final InputHandler input;

    public GameCompletionUI(InputHandler input) {
        this.input = input;
    }

    // ----------------------------------------------------------------
    // Victory
    // ----------------------------------------------------------------

    public void displayVictory(Player player, int totalRounds) {
        System.out.println();
        System.out.println(SEPARATOR);
        System.out.println("  *** CONGRATULATIONS! ***");
        System.out.println("  You have defeated all your enemies!");
        System.out.println(SEPARATOR);
        System.out.printf("  Remaining HP   : %d / %d%n",
                player.getCurrentHp(), player.getMaxHp());
        System.out.printf("  Total Rounds   : %d%n", totalRounds);

        // Inventory leftovers
        if (!player.getInventory().isEmpty()) {
            System.out.print("  Items Remaining: ");
            player.getInventory().getItemsView().forEach(i -> System.out.print(i.getName() + " "));
            System.out.println();
        } else {
            System.out.println("  Items Remaining: (none)");
        }
        System.out.println(SEPARATOR);
        System.out.println();
    }

    // ----------------------------------------------------------------
    // Defeat
    // ----------------------------------------------------------------

    public void displayDefeat(List<Combatant> remainingEnemies, int totalRounds) {
        System.out.println();
        System.out.println(SEPARATOR);
        System.out.println("  You have been DEFEATED.");
        System.out.println("  Don't give up, try again!");
        System.out.println(SEPARATOR);

        long aliveCount = remainingEnemies.stream().filter(Combatant::isAlive).count();
        System.out.printf("  Enemies Remaining  : %d%n", aliveCount);
        System.out.printf("  Total Rounds Survived: %d%n", totalRounds);
        System.out.println(SEPARATOR);
        System.out.println();
    }

    // ----------------------------------------------------------------
    // Post-game menu
    // ----------------------------------------------------------------

    /**
     * Displays the post-game menu and returns the player's choice:
     *   1 = replay with same settings
     *   2 = new game (return to home screen)
     *   3 = exit
     */
    public int promptPostGameChoice() {
        System.out.println("What would you like to do?");
        System.out.println("  [1] Replay with same settings");
        System.out.println("  [2] New game (return to home screen)");
        System.out.println("  [3] Exit");
        return input.readInt(1, 3);
    }
}