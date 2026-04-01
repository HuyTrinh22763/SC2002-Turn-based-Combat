package boundary;

import entity.Combatant;
import entity.EnemyType;
import entity.Inventory;
import entity.Item;
import entity.PlayerClass;

import java.util.List;

/**
 * Displays the loading/setup screen before the game begins.
 * Covers: player selection, item selection, difficulty selection.
 */
public class LoadingScreenUI {

    private static final String SEPARATOR = "=".repeat(60);
    private static final String THIN_SEP  = "-".repeat(60);

    private final InputHandler input;

    public LoadingScreenUI(InputHandler input) {
        this.input = input;
    }

    // ----------------------------------------------------------------
    // Banner
    // ----------------------------------------------------------------

    public void displayWelcomeBanner() {
        System.out.println(SEPARATOR);
        System.out.println("        TURN-BASED COMBAT ARENA");
        System.out.println(SEPARATOR);
        System.out.println();
    }

    // ----------------------------------------------------------------
    // Player class selection
    // ----------------------------------------------------------------

    public void displayPlayerClasses() {
        System.out.println("=== CHOOSE YOUR CHARACTER ===");
        System.out.println();
        PlayerClass[] classes = PlayerClass.values();
        for (int i = 0; i < classes.length; i++) {
            PlayerClass pc = classes[i];
            System.out.printf("[%d] %s%n", i + 1, pc.getDisplayName());
            System.out.printf("    HP: %-5d  ATK: %-5d  DEF: %-5d  SPD: %-5d%n",
                    pc.getBaseHp(), pc.getBaseAttack(),
                    pc.getBaseDefense(), pc.getBaseSpeed());
            System.out.println();
        }
    }

    /**
     * @return 0-based index of chosen PlayerClass
     */
    public int promptPlayerClassSelection() {
        displayPlayerClasses();
        System.out.println("Enter the number of your chosen class:");
        return input.readInt(1, PlayerClass.values().length) - 1;
    }

    public String promptPlayerName() {
        return input.readLine("Enter your character's name: ");
    }

    // ----------------------------------------------------------------
    // Item selection
    // ----------------------------------------------------------------

    public void displayAvailableItems(List<Item> availableItems) {
        System.out.println(THIN_SEP);
        System.out.println("=== AVAILABLE ITEMS (choose 2; duplicates allowed) ===");
        for (int i = 0; i < availableItems.size(); i++) {
            System.out.printf("[%d] %s%n", i + 1, availableItems.get(i).getName());
        }
        System.out.println();
    }

    /**
     * @return 0-based index of the chosen item
     */
    public int promptItemSelection(List<Item> availableItems, int slotNumber) {
        displayAvailableItems(availableItems);
        System.out.printf("Select item for slot %d:%n", slotNumber);
        return input.readInt(1, availableItems.size()) - 1;
    }

    public void displayInventory(Inventory inventory) {
        System.out.println("Your inventory:");
        List<Item> items = inventory.getItemsView();
        if (items.isEmpty()) {
            System.out.println("  (empty)");
        } else {
            for (int i = 0; i < items.size(); i++) {
                System.out.printf("  [%d] %s%n", i + 1, items.get(i).getName());
            }
        }
        System.out.println();
    }

    // ----------------------------------------------------------------
    // Difficulty / level selection
    // ----------------------------------------------------------------

    public void displayDifficultyLevels() {
        System.out.println(THIN_SEP);
        System.out.println("=== SELECT DIFFICULTY ===");
        System.out.println("[1] Easy   - Initial: 3 Goblins");
        System.out.println("[2] Medium - Initial: 1 Goblin + 1 Wolf  |  Backup: 2 Wolves");
        System.out.println("[3] Hard   - Initial: 2 Goblins  |  Backup: 1 Goblin + 2 Wolves");
        System.out.println();
    }

    /**
     * @return difficulty level 1–3
     */
    public int promptDifficultySelection() {
        displayDifficultyLevels();
        System.out.println("Enter difficulty (1-3):");
        return input.readInt(1, 3);
    }

    // ----------------------------------------------------------------
    // Enemy roster preview
    // ----------------------------------------------------------------

    public void displayEnemyRoster() {
        System.out.println(THIN_SEP);
        System.out.println("=== ENEMY ROSTER ===");
        for (EnemyType et : EnemyType.values()) {
            System.out.printf("  %s — HP: %-5d  ATK: %-5d  DEF: %-5d  SPD: %-5d%n",
                    et.getDisplayName(),
                    et.getBaseHp(), et.getBaseAttack(),
                    et.getBaseDefense(), et.getBaseSpeed());
        }
        System.out.println();
    }
}