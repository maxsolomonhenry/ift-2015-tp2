import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Tp2 {

    // GLOBAL CONSTANT FOR DEBUGGING, ACCESSIBLE AND CHANGABLE FROM EVERYWHERE
    static final boolean DEBUG = true;

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

        // For storing output.
        String result = "";
        StringBuilder results = new StringBuilder();

        // State machine directs the engine.
        while (state != State.END) {
            switch (state) {
                case ACCEPT_COMMAND -> {
                    if (!iter.hasNext()) {
                        state = State.END;
                        continue;
                    }
                    state = Parser.parseCommand(iter.next());
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

            results.append(result);
            state = State.ACCEPT_COMMAND;
        }

        // Write to output.
        Files.writeString(outputPath, results);
    }
}
