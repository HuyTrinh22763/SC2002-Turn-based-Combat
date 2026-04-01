package boundary;

import java.util.Scanner;

/**
 * Handles all raw user input from the command line.
 * Single Responsibility: reading and validating input only.
 */
public class InputHandler {

    private final Scanner scanner;

    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads an integer in [min, max]. Keeps prompting until valid.
     */
    public int readInt(int min, int max) {
        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("Please enter a number between %d and %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * Reads a non-empty string from stdin.
     */
    public String readLine(String prompt) {
        System.out.print(prompt);
        String line = scanner.nextLine().trim();
        while (line.isEmpty()) {
            System.out.print("Input cannot be empty. " + prompt);
            line = scanner.nextLine().trim();
        }
        return line;
    }

    public void close() {
        scanner.close();
    }
}