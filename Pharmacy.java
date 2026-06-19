import java.util.TreeMap;

public class Pharmacy {

    private static final PharmacyDate MIN_DATE = new PharmacyDate(2000, 1, 1);
    private static final PharmacyDate MAX_DATE = new PharmacyDate(2027, 1, 1);

    TreeMap<String, TreeMap<PharmacyDate, Integer>> stock = new TreeMap<>();
    PharmacyDate currentDate = new PharmacyDate();
    String buffer = new String();

    public Pharmacy() {
    }

    public String executeApprov(InputIterator iter) {
        while (!iter.next().contains(";")) {
            buffer = iter.peek();
            ApprovItem item = Parser.parseApprovItem(buffer);

            // Check if this medication is there. If not, make an entry.
            if (!stock.containsKey(item.name)){
                stock.put(item.name, new TreeMap<PharmacyDate, Integer>());
            }

            TreeMap<PharmacyDate, Integer> medStore = stock.get(item.name);

            // Look for entries of this expiry date and add if not there.
            if (!medStore.containsKey(item.expiry)) {
                medStore.put(item.expiry, 0);
            }

            // Add to supply.
            int prev = medStore.get(item.expiry);
            medStore.put(item.expiry, prev + item.quantity);
        }
        return "APPROV OK\n";
    }

    public String executeStock(InputIterator iter) {
        // Advance iterator (';' may be on newline).
        while (!iter.peek().contains(";")) {
            iter.next();
        }

        return buildStockMessage();
    }

    public String buildStockMessage() {
        StringBuilder result = new StringBuilder();

        result.append("STOCK " + currentDate + "\n");

        // Iterate through medications by name in order.
        // For each medication, list quantity and expiry date.
        stock.forEach((medication, store) -> {
            store.forEach((date, quantity) -> {

                // Skip expired drugs or empty entries.
                if (quantity == 0 || date.compareTo(currentDate) < 0) {
                    return;
                }

                result.append(String.format("%s %d %s\n", 
                    medication, quantity, date));
            });
        });

        return result.toString();
    }

    public String executePrescription(InputIterator iter) {

        StringBuilder result = new StringBuilder();

        while (!iter.next().contains(";")) {
            buffer = iter.peek();

            PrescriptionItem item = Parser.parsePrescriptionItem(buffer);

            // Check if there is enough left (including other backordered items)
            // and return a status message.
            // Make expiry date calculation as well.
        }

        result.append("Done PRESCRIPTION\n");
        return result.toString();
    }

    public String executeDate(InputIterator iter) {
        // Strip "DATE" text.
        String[] parts = iter.peek().split(" +");
        setCurrentDate(Parser.parseDate(parts[1]));

        // Inventory update:
        // - List ordered drugs.
        // - Delete ordered drugs from order list.
        // - etc... see assignment.
    
        return buildDateMessage();
    }

    private String buildDateMessage() {
        // TODO
        StringBuilder result = new StringBuilder();
        result.append(String.format(
                "%s OK\n",
                currentDate));

        return result.toString();
    }

    private void setCurrentDate(PharmacyDate val) {
        if (val.compareTo(MIN_DATE) < 0 || val.compareTo(MAX_DATE) > 0) {
            throw new IllegalArgumentException(String.format(
                "Date %s is out of range (%s to %s)",
                val,
                MIN_DATE,
                MAX_DATE
            ));
        }
        currentDate = val;
    }
}
