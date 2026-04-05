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
            if (raw.equalsIgnoreCase("exit") || raw.equals("0")) {
                if (confirmExit()) {
                    throw new QuitGameException();
                } else {
                    continue;
                }
            }
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
        while (true) {
            System.out.print("Press Enter to continue... (or '0' to exit) ");
            String raw = scanner.nextLine().trim();
            if (raw.equalsIgnoreCase("exit") || raw.equals("0")) {
                if (confirmExit()) {
                    throw new QuitGameException();
                } else {
                    continue;
                }
            }
            break;
        }
    }

    private boolean confirmExit() {
        while (true) {
            System.out.print("Are you sure you want to quit? (y/n): ");
            String raw = scanner.nextLine().trim().toLowerCase();
            if (raw.equals("y") || raw.equals("yes")) {
                return true;
            } else if (raw.equals("n") || raw.equals("no")) {
                return false;
            }
            System.out.println("Invalid input. Please enter 'y' or 'n'.");
        }
    }

    public PostGameOption choosePostGameOption(int maxOptions) {
        PostGameOption[] options = PostGameOption.values();
        int choice = readMenuChoice("> ", 1, maxOptions);
        return options[choice - 1];
    }
}
