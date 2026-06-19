import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeSet;
import java.util.List;
import java.util.Iterator;

public class Tp2 {

    private static boolean DEBUG = true;

    public static void main(String [] args) throws IOException {

        Path inputPath = Paths.get(args[0]);
        Path outputPath = Paths.get(args[1]);

        Pharmacy pharmacy = new Pharmacy();

        State state = State.ACCEPT_COMMAND;
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

        String lastResult = "";
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
                case APPROV -> lastResult = pharmacy.executeApprov(iter);
                case STOCK -> lastResult = pharmacy.executeStock(iter);
                case PRESCRIPTION -> lastResult = pharmacy.executePrescription(iter);
                case DATE -> lastResult = pharmacy.executeDate(
                    Parser.parseDate(iter.peek())
                );
                case END -> { }

            }

            if (DEBUG) {
                System.out.println(lastResult);
            }
            state = State.ACCEPT_COMMAND;
        }
    }
}
