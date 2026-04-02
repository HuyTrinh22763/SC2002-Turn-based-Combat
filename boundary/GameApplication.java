package boundary;

import java.util.Scanner;

public class GameApplication {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            CliGameSession session = new CliGameSession(scanner);
            session.run();
        } finally {
            scanner.close();
        }
    }
}
