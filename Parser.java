public class Parser {
    public static PharmacyDate parseDate(String buffer) {
        String[] ymd = buffer.split("-");

        return new PharmacyDate(
                Integer.parseInt(ymd[0]),
                Integer.parseInt(ymd[1]),
                Integer.parseInt(ymd[2])
        );
    }

    public static State parseCommand(String buffer) {
        String[] parts = buffer.trim().split("\\s+");
        return State.from(parts[0]);
    }

    public static ApprovItem parseApprovItem(String buffer) {
        String[] parts = buffer.trim().split("\\s+");

        // Vars here named for clarity (n.b. less efficient).
        String name = parts[0];
        int quantity = Integer.parseInt(parts[1]);
        PharmacyDate date = parseDate(parts[2]);

        return new ApprovItem(name, quantity, date);
    }

    public static PrescriptionItem parsePrescriptionItem(String buffer) {
        String[] parts = buffer.trim().split("\\s+");

        // Vars here named for clarity (n.b. less efficient).
        String medication = parts[0];
        int numPerDose = Integer.parseInt(parts[1]);
        int numRepeats = Integer.parseInt(parts[2]);

        return new PrescriptionItem(medication, numPerDose, numRepeats);
    }
}
