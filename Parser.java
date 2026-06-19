public class Parser {
    public static PharmacyDate parseDate(String line) {
        String[] parts = line.split(" +");
        String[] ymd = parts[1].split("-");

        return new PharmacyDate(
                Integer.parseInt(ymd[0]),
                Integer.parseInt(ymd[1]),
                Integer.parseInt(ymd[2])
        );
    }

    public static State parseCommand(String line) {
        String[] parts = line.split(" +");
        return State.from(parts[0]);
    }
}
