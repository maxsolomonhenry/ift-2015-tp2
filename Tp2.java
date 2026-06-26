import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Tp2 {

    // Debugging flag.
    static final boolean DEBUG = false;

    public static void main(String [] args) throws IOException {

        Path inputPath = Paths.get(args[0]);
        Path outputPath = Paths.get(args[1]);

        // Engine.
        Pharmacy pharmacy = new Pharmacy();
        State state = State.ACCEPT_COMMAND;

        // For parsing the input.
        List<String> lines = Files.readAllLines(inputPath);
        InputIterator iter = new InputIterator(lines.iterator());

        if (DEBUG) {
            System.out.println("BEGIN INPUT:");
            for (String line : lines) {
                System.out.println(line);
            }
            System.out.println("END INPUT");
        }

        if (DEBUG) {
            System.out.println("\nBEGIN OUTPUT");
        }

        // For storing latest (local) output.
        String result = "";

        // For storing all outputs. `StringBuilder` can concatenate in O(1).
        StringBuilder results = new StringBuilder();

        // State machine directs the engine, and passes the `iter` object around
        // to advance through the input.
        while (state != State.END) {
            switch (state) {
                case ACCEPT_COMMAND -> {
                    if (!iter.hasNext()) {
                        // If you've reached the end of the input, quit. 
                        state = State.END;
                        continue;
                    }

                    String line = iter.next().trim();

                    // Skip lines that are empty or just a semicolon.
                    if (line.equals(";") || line.isEmpty()) {
                        continue;
                    }

                    // If state is "ACCEPT_COMMAND," update the state and go 
                    // back to the while loop (re-renter the case-switch).
                    //
                    // All other states pass through below the switch statement, 
                    // which appends results and sets the state back to 
                    // "ACCEPT_COMMAND".
                    state = Parser.parseCommand(line);
                    continue;
                }
                case APPROV -> result = pharmacy.executeApprov(iter);
                case STOCK -> result = pharmacy.executeStock(iter);
                case PRESCRIPTION -> result = pharmacy.executePrescription(iter);
                case DATE -> result = pharmacy.executeDate(iter);
                case END -> { }

            }

            if (DEBUG) {
                System.out.print(result);
            }

            // All commands get here, collect the result, and switch state back
            // to "ACCEPT_COMMAND" to prepare for the next command.
            results.append(result);
            state = State.ACCEPT_COMMAND;
        }

        // Write to output.
        Files.writeString(outputPath, results);
    }
}
