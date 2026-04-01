package boundary;

import entity.Combatant;
import entity.Item;
import entity.Player;

import java.util.List;

public class BattleUI {

    private static final String SEPARATOR = "=".repeat(60);
    private static final String THIN_SEP  = "-".repeat(60);

    private final InputHandler input;

    public BattleUI(InputHandler input) {
        this.input = input;
    }

    public void displayRoundHeader(int roundNumber) {
        System.out.println();
        System.out.println(SEPARATOR);
        System.out.printf("  ROUND %d%n", roundNumber);
        System.out.println(SEPARATOR);
    }

    public void displayCombatantStatus(Player player, List<Combatant> enemies) {
        System.out.println(THIN_SEP);
        System.out.println("[ PLAYER ]");
        System.out.println("  " + formatCombatantStatus(player));
        System.out.printf("  Inventory: %s%n", formatInventory(player));
        System.out.printf("  Special Cooldown: %d rounds%n", player.getSpecialCooldown());
        System.out.println();

        System.out.println("[ ENEMIES ]");
        for (int i = 0; i < enemies.size(); i++) {
            Combatant e = enemies.get(i);
            String status = e.isAlive() ? formatCombatantStatus(e) : e.getName() + " [ELIMINATED]";
            System.out.printf("  [%d] %s%n", i + 1, status);
        }
        System.out.println(THIN_SEP);
    }

    private String formatCombatantStatus(Combatant c) {
        String stun = c.isStunned() ? " (STUNNED " + c.getStunDuration() + " turn(s))" : "";
        return String.format("%s — HP: %d/%d  ATK: %d  DEF: %d  SPD: %d%s",
                c.getName(), c.getCurrentHp(), c.getMaxHp(),
                c.getAttack(), c.getDefense(), c.getSpeed(), stun);
    }

    private String formatInventory(Player player) {
        List<Item> items = player.getInventory().getItemsView();
        if (items.isEmpty()) return "(no items)";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(items.get(i).getName());
        }
        return sb.toString();
    }

    public void displayActionMenu(Player player) {
        System.out.println("Choose your action:");
        System.out.println("  [1] Basic Attack");
        System.out.println("  [2] Defend");

        if (!player.getInventory().isEmpty()) {
            System.out.println("  [3] Use Item");
        } else {
            System.out.println("  [3] Use Item  (no items left)");
        }

        String cooldownNote = player.canUseSpecial()
                ? ""
                : "  (cooldown: " + player.getSpecialCooldown() + " rounds)";
        System.out.println("  [4] Special Skill" + cooldownNote);
    }

    public int promptActionSelection(Player player) {
        displayActionMenu(player);
        return input.readInt(1, 4);
    }

    public void displayAliveEnemies(List<Combatant> enemies) {
        System.out.println("Select a target:");
        for (int i = 0; i < enemies.size(); i++) {
            Combatant e = enemies.get(i);
            if (e.isAlive()) {
                System.out.printf("  [%d] %s%n", i + 1, formatCombatantStatus(e));
            }
        }
    }

    public int promptTargetSelection(List<Combatant> enemies) {
        displayAliveEnemies(enemies);
        return input.readInt(1, enemies.size()) - 1;
    }

    public void displayItemMenu(Player player) {
        System.out.println("Select an item to use:");
        List<Item> items = player.getInventory().getItemsView();
        for (int i = 0; i < items.size(); i++) {
            System.out.printf("  [%d] %s%n", i + 1, items.get(i).getName());
        }
    }
    
    public int promptItemSelection(Player player) {
        displayItemMenu(player);
        return input.readInt(1, player.getInventory().size()) - 1;
    }

    public void displayActionResult(String message) {
        System.out.println("  >> " + message);
    }

    public void displayStunnedSkip(Combatant combatant) {
        System.out.printf("  ** %s is STUNNED and skips their turn!%n", combatant.getName());
    }

    public void displayEliminated(Combatant combatant) {
        System.out.printf("  ✗ %s has been ELIMINATED!%n", combatant.getName());
    }

    public void displayBackupSpawn(List<Combatant> newEnemies) {
        System.out.println();
        System.out.println("  !! BACKUP SPAWN — new enemies have entered the arena !!");
        for (Combatant e : newEnemies) {
            System.out.println("    + " + formatCombatantStatus(e));
        }
        System.out.println();
    }


    public void displayEndOfRoundSummary(int roundNumber, Player player, List<Combatant> enemies) {
        System.out.println();
        System.out.println(THIN_SEP);
        System.out.printf("End of Round %d:%n", roundNumber);
        System.out.printf("  %s%n", formatCombatantStatus(player));
        for (Combatant e : enemies) {
            String status = e.isAlive() ? formatCombatantStatus(e) : e.getName() + " [✗]";
            System.out.println("  " + status);
        }
        System.out.println(THIN_SEP);
    }

    public void displayTurnOrder(List<Combatant> order) {
        System.out.print("Turn order: ");
        for (int i = 0; i < order.size(); i++) {
            if (i > 0) System.out.print(" → ");
            System.out.print(order.get(i).getName());
        }
        System.out.println();
    }
}
