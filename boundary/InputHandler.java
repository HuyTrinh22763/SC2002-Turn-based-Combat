package boundary;
import java.util.Scanner;
public class InputHandler {

    private final Scanner scanner;

    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }

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
