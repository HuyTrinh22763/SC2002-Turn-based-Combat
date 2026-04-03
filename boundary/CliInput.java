package boundary;

import entity.combatants.Combatant;
import entity.items.Item;

import java.util.List;
import java.util.Scanner;

public class CliInput {

    public enum PostGameOption {
        REPLAY_SAME,
        NEW_GAME,
        EXIT
    }

    private final Scanner scanner;

    public CliInput(Scanner scanner) {
        this.scanner = scanner;
    }

    public int readMenuChoice(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String raw = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(raw);
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                // Fall through to retry prompt.
            }
            System.out.println("Invalid input. Please enter a number between " + min + " and " + max + ".");
        }
    }

    public int chooseEnemyTarget(List<Combatant> aliveEnemies) {
        return readMenuChoice("Choose enemy target: ", 1, aliveEnemies.size()) - 1;
    }

    public int chooseItem(List<Item> items) {
        return readMenuChoice("Choose item to use: ", 1, items.size()) - 1;
    }



    public void waitForEnter() {
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }

    public PostGameOption choosePostGameOption(int maxOptions) {
        PostGameOption[] options = PostGameOption.values();
        int choice = readMenuChoice("> ", 1, maxOptions);
        return options[choice - 1];
    }
}
