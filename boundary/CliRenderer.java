package boundary;

import control.BattleState;
import entity.battlerules.ActionResult;
import entity.battlerules.DifficultyLevel;
import entity.battlerules.LevelCatalog;
import entity.battlerules.LevelDefinition;
import entity.combatants.Combatant;
import entity.combatants.EnemyType;
import entity.combatants.Player;
import entity.combatants.PlayerClass;
import entity.items.Item;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class CliRenderer {

    private static final int WIDTH = 78;

    public void clearScreen() {
        System.out.print("\u001b[H\u001b[2J");
        System.out.flush();
    }

    public void renderSetupPage() {
        boxTitle("TURN-BASED COMBAT ARENA");
        boxTitle("GAME SETUP");
        printPlayerClasses();
        printEnemyTypes();
        printDifficultyLevels();
        printLine();
    }

    private void printPlayerClasses() {
        System.out.println("PLAYERS:");
        for (PlayerClass playerClass : PlayerClass.values()) {
            System.out.println("  - " + playerClass.getDisplayName()
                    + " [HP " + playerClass.getBaseHp()
                    + " | ATK " + playerClass.getBaseAttack()
                    + " | DEF " + playerClass.getBaseDefense()
                    + " | SPD " + playerClass.getBaseSpeed() + "]");
        }
        System.out.println();
    }

    private void printEnemyTypes() {
        System.out.println("ENEMIES:");
        for (EnemyType enemyType : EnemyType.values()) {
            System.out.println("  - " + enemyType.getDisplayName()
                    + " [HP " + enemyType.getBaseHp()
                    + " | ATK " + enemyType.getBaseAttack()
                    + " | DEF " + enemyType.getBaseDefense()
                    + " | SPD " + enemyType.getBaseSpeed() + "]");
        }
        System.out.println();
    }

    private void printDifficultyLevels() {
        System.out.println("DIFFICULTY LEVELS:");
        for (DifficultyLevel difficultyLevel : DifficultyLevel.values()) {
            LevelDefinition definition = LevelCatalog.getByDifficulty(difficultyLevel);
            if (definition == null) {
                continue;
            }
            String initial = waveSummary(definition.getInitialWave().getEnemiesView());
            String backup = definition.hasBackupWave()
                    ? waveSummary(definition.getBackupWave().getEnemiesView())
                    : "None";
            System.out.println("  - " + difficultyLevel.name()
                    + " (Level " + definition.getLevelNumber() + ")"
                    + " | Initial: " + initial
                    + " | Backup: " + backup);
        }
    }

    public void renderSetupSummary(GameSetup setup) {
        printSection("SELECTED SETTINGS");
        printBullet("Player Class: " + setup.getPlayerClass().getDisplayName());
        printBullet("Difficulty  : " + setup.getDifficultyLevel().name());
        printBullet("Item Slot 1 : " + setup.getSelectedItemsView().get(0).getDisplayName());
        printBullet("Item Slot 2 : " + setup.getSelectedItemsView().get(1).getDisplayName());
        printLine();
    }

    public void renderBattleStart(GameSetup setup) {
        boxTitle("BATTLE START");
        renderSetupSummary(setup);
    }

    public void renderRoundHeader(int roundNumber, List<Combatant> turnOrder, Player player,
            List<Combatant> enemies) {
        printSection("ROUND " + roundNumber);
        System.out.println("TURN ORDER:");
        for (int i = 0; i < turnOrder.size(); i++) {
            Combatant combatant = turnOrder.get(i);
            System.out.println("  " + (i + 1) + ". " + combatant.getName() + " (SPD " + combatant.getSpeed() + ")");
        }
        System.out.println();
        System.out.println("PLAYER STATUS:");
        System.out.println("  " + formatCombatantStatus(player) + " | CD " + player.getSpecialCooldown());
        System.out.println();
        System.out.println("ENEMY STATUS:");
        List<Combatant> aliveEnemies = getAliveEnemies(enemies);
        if (aliveEnemies.isEmpty()) {
            System.out.println("  - All enemies eliminated.");
        } else {
            for (Combatant enemy : aliveEnemies) {
                System.out.println("  - " + formatCombatantStatus(enemy));
            }
        }
        printLine();
    }

    public void renderRoundSnapshot(int roundNumber, Player player, List<Combatant> enemies) {
        printSection("END OF ROUND " + roundNumber + " SNAPSHOT");
        System.out.println("Player:");
        System.out.println("  - " + formatCombatantStatus(player) + " | CD " + player.getSpecialCooldown());
        System.out.println("Enemies:");
        for (Combatant enemy : enemies) {
            if (enemy != null) {
                System.out.println("  - " + formatCombatantStatus(enemy));
            }
        }
        printLine();
    }

    public void printActorTurn(Combatant actor, boolean isPlayerTurn) {
        printSection("TURN EVENT");
        System.out.println("Actor : " + actor.getName());
        System.out.println("Type  : " + (isPlayerTurn ? "Player Turn" : "Enemy Turn"));
        if (!isPlayerTurn) {
            System.out.println("Action: BasicAttack");
        }
        printLine();
    }

    public void printPlayerActions(boolean hasItems) {
        printSection("ACTION MENU");
        System.out.println("  1) BasicAttack");
        System.out.println("  2) Defend");
        System.out.println("  3) Item" + (hasItems ? "" : " [Unavailable: no items left]"));
        System.out.println("  4) SpecialSkill");
        printLine();
    }

    public void printAliveEnemies(List<Combatant> aliveEnemies) {
        printSection("TARGET SELECTION");
        for (int i = 0; i < aliveEnemies.size(); i++) {
            Combatant enemy = aliveEnemies.get(i);
            System.out.println("  " + (i + 1) + ") " + formatCombatantStatus(enemy));
        }
        printLine();
    }

    public void printInventory(List<Item> items) {
        printSection("INVENTORY");
        for (int i = 0; i < items.size(); i++) {
            System.out.println("  " + (i + 1) + ") " + items.get(i).getName());
        }
        printLine();
    }

    public void printActionResult(ActionResult result) {
        printSection("ACTION RESULT");
        if (result == null) {
            System.out.println("  - No action result.");
        } else {
            for (String message : result.getMessages()) {
                System.out.println("  - " + message);
            }
        }
        printLine();
    }

    public void renderLiveTurnState(int roundNumber, Player player, List<Combatant> enemies) {
        printSection("LIVE UPDATE - ROUND " + roundNumber);
        System.out.println("Player:");
        System.out.println("  - " + formatCombatantStatus(player) + " | CD " + player.getSpecialCooldown());
        System.out.println("Enemies:");
        for (Combatant enemy : enemies) {
            if (enemy != null) {
                System.out.println("  - " + formatCombatantStatus(enemy));
            }
        }
        printLine();
    }

    public void renderResultPage(BattleState battleState, Player player, List<Combatant> enemies, int totalRounds) {
        boxTitle("GAME COMPLETION SCREEN");
        if (battleState == BattleState.PLAYER_VICTORY) {
            System.out.println("Congratulations, you have defeated all your enemies.");
            System.out.println("Statistics: Remaining HP: " + player.getCurrentHp() + "/" + player.getMaxHp()
                    + " | Total Rounds: " + totalRounds);
        } else if (battleState == BattleState.PLAYER_DEFEAT) {
            int enemiesRemaining = getAliveEnemies(enemies).size();
            System.out.println("Defeated. Don't give up, try again!");
            System.out.println("Statistics: Enemies remaining: " + enemiesRemaining
                    + " | Total Rounds Survived: " + totalRounds);
        } else {
            System.out.println("Battle ended unexpectedly.");
        }
        printLine();
    }

    public void printPostGameMenu(List<String> options) {
        printMenu("NEXT STEP", options);
    }

    public void printSelectPlayerClassMenu(List<String> options) {
        printMenu("SELECT PLAYER CLASS", options);
    }

    public void printSelectDifficultyMenu(List<String> options) {
        printMenu("SELECT DIFFICULTY", options);
    }

    public void printSelectItemSlot(int slot, List<String> options) {
        printMenu("SELECT ITEM FOR SLOT " + slot, options);
    }

    private void printMenu(String title, List<String> options) {
        printSection(title);
        for (int i = 0; i < options.size(); i++) {
            System.out.println("  " + (i + 1) + ") " + options.get(i));
        }
        printLine();
    }

    public void printInvalidItemActionFallback() {
        System.out.println("You have no items left. Please choose another action.");
        printLine();
    }

    private String waveSummary(List<EnemyType> enemies) {
        Map<EnemyType, Integer> counts = new EnumMap<>(EnemyType.class);
        for (EnemyType type : enemies) {
            counts.put(type, counts.getOrDefault(type, 0) + 1);
        }

        List<String> parts = new ArrayList<>();
        for (EnemyType type : EnemyType.values()) {
            Integer count = counts.get(type);
            if (count != null && count > 0) {
                parts.add(count + " " + type.getDisplayName());
            }
        }
        return parts.isEmpty() ? "None" : String.join(", ", parts);
    }

    private String formatCombatantStatus(Combatant combatant) {
        String status = combatant.isAlive() ? "ALIVE" : "ELIMINATED";
        String stun = combatant.isStunned() ? ", STUNNED(" + combatant.getStunDuration() + ")" : "";
        return combatant.getName() + " [HP " + combatant.getCurrentHp() + "/" + combatant.getMaxHp()
                + " | ATK " + combatant.getAttack()
                + " | DEF " + combatant.getDefense()
                + " | SPD " + combatant.getSpeed()
                + "] " + status + stun;
    }

    private List<Combatant> getAliveEnemies(List<Combatant> enemies) {
        List<Combatant> aliveEnemies = new ArrayList<>();
        for (Combatant enemy : enemies) {
            if (enemy != null && enemy.isAlive()) {
                aliveEnemies.add(enemy);
            }
        }
        return aliveEnemies;
    }

    private void printLine() {
        System.out.println(repeat("=", WIDTH));
    }

    private void printSection(String title) {
        System.out.println();
        System.out.println("[" + title + "]");
    }

    private void boxTitle(String title) {
        printLine();
        System.out.println(center(title));
        printLine();
    }

    private void printBullet(String text) {
        System.out.println("- " + text);
    }

    private String repeat(String ch, int count) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            builder.append(ch);
        }
        return builder.toString();
    }

    private String center(String text) {
        if (text.length() >= WIDTH) {
            return text;
        }
        int leftPadding = (WIDTH - text.length()) / 2;
        return repeat(" ", leftPadding) + text;
    }
}
