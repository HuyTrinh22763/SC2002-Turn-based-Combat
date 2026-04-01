package boundary;

public class DisplayHelper {

    public static final String HEAVY_SEP = "=".repeat(60);
    public static final String LIGHT_SEP = "-".repeat(60);

    private DisplayHelper() {
    }

    public static void printHeavySeparator() {
        System.out.println(HEAVY_SEP);
    }

    public static void printLightSeparator() {
        System.out.println(LIGHT_SEP);
    }

    public static void printBlankLine() {
        System.out.println();
    }

    public static void printCentredTitle(String title) {
        int totalWidth = 60;
        int padding = Math.max(0, (totalWidth - title.length()) / 2);
        System.out.println(" ".repeat(padding) + title);
    }
    
    public static void printStat(String label, Object value) {
        System.out.printf("  %-12s: %s%n", label, value);
    }
}
