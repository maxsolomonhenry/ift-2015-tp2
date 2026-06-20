import java.util.TreeMap;

public class Pharmacy {

    private static final PharmacyDate MIN_DATE = new PharmacyDate(2000, 1, 1);
    private static final PharmacyDate MAX_DATE = new PharmacyDate(2027, 1, 1);
    
    // CREATE CLASS VARIABLE FOR CURRENT DATE TO ACCESS AND CHANGE GLOBALY
    static PharmacyDate currentDate = new PharmacyDate();

    // create a variable to store information about the whole medication stock 
    TreeMap<String, TreeMap<PharmacyDate, InventoryItem>> stock = new TreeMap<>();
    
    String buffer = new String();

    public Pharmacy() {
    }

    public String executeApprov(InputIterator iter) {
        while (!iter.next().contains(";")) {
            buffer = iter.peek();
            ApprovItem item = Parser.parseApprovItem(buffer);

            // Check if this medication is there. If not, make an entry.
            if (!stock.containsKey(item.name)){
                stock.put(item.name, new TreeMap<PharmacyDate, InventoryItem>());
            }

            TreeMap<PharmacyDate, InventoryItem> medStore = stock.get(item.name);

            // Look for entries of this expiry date and add if not there.
            if (!medStore.containsKey(item.expiry)) {
                medStore.put(item.expiry, new InventoryItem(0, 0));
            }

            // Add to supply.
            InventoryItem current = medStore.get(item.expiry);
            current.numAvailable += item.quantity;

            medStore.put(item.expiry, current);
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
            store.forEach((date, item) -> {

                // Skip expired drugs or empty entries.
                if (item.numAvailable == 0 || date.compareTo(currentDate) < 0) {
                    return;
                }

                result.append(String.format("%s %d %s\n", 
                    medication, item.numAvailable, date));
            });
        });

        // for debuggins
        if (Tp2.DEBUG)
            result.append("Done STOCK\n");        


        return result.append("\n").toString();
    }

    public String executePrescription(InputIterator iter) {

        // create result string builder to store outputs
        StringBuilder result = new StringBuilder();
        result.append("PRESCRIPTION\n");

        // loop while presription has medications
        while (!iter.next().contains(";")) {
            buffer = iter.peek();

            // create a Prescription item instance from the input line
            PrescriptionItem item = Parser.parsePrescriptionItem(buffer);

            // get the required endDate for the dose cycles of the prescription item
            PharmacyDate endDate = item.getEndDate();

            // status message to report
            String status = "COMMANDE";

            // get stock tree for this medication
            TreeMap<PharmacyDate, InventoryItem> medicationStock = stock.get(item.medication);

            // if medication does not exist in stock, we create it and add to stock
            if (medicationStock == null){
                medicationStock = new TreeMap<>();
                stock.put(item.medication, medicationStock);
            }

            // for this medication stock we get inventory item for this expiry (end date)
            InventoryItem inventoryItem = medicationStock.get(endDate);

            // if medication with the required end date does not exist, create it and add to stock
            if (inventoryItem == null){
                inventoryItem = new InventoryItem(0, 0);
                medicationStock.put(endDate, inventoryItem);
            }

            // updated numRequested
            inventoryItem.numRequested += item.numTotal;

            // if there is enough left then status = OK else leave status = COMMANCE
            if ((inventoryItem.numAvailable - inventoryItem.numRequested) >= item.numTotal)
                status = "OK";

            // return a status message.
            result.append(String.format("%s %d %d %s\n", item.medication, item.numPerDose, item.numRepeats, status));
        }

        // for debuggins
        if (Tp2.DEBUG)
            result.append("Done PRESCRIPTION\n");

        return result.append("\n").toString();
    }

    public String executeDate(InputIterator iter) {
        // Strip "DATE" text.
        String[] parts = iter.peek().split(" +");
        setCurrentDate(Parser.parseDate(parts[1]));

        // flag for returning empty order list
        boolean emptyOrderList = true;

        // building order list
        StringBuilder result = new StringBuilder();
        result.append(
            String.format(
                "%s COMMANDE\n",
                currentDate
            )
        );

        // iterate through medications
        for (String medicationName : stock.keySet()) {

            TreeMap<PharmacyDate, InventoryItem> medicationStock = stock.get(medicationName);

            // iteratate through all medication stock
            for (PharmacyDate expiryDate : medicationStock.keySet()){

                InventoryItem inventoryItem = medicationStock.get(expiryDate);

                // if a medication has expeired, remove it
                if (expiryDate.compareTo(currentDate) < 0){
                    inventoryItem.numAvailable = 0;
                }

                // find ordered drugs
                if (inventoryItem.numAvailable < inventoryItem.numRequested){

                    // list ordered drugs
                    result.append(
                        String.format(
                            "%s %d \n",
                            medicationName,
                            inventoryItem.numRequested - inventoryItem.numAvailable
                        )
                    );

                    // remove drugs from odered list
                    inventoryItem.numRequested = 0;

                    // change flag
                    emptyOrderList = false;
                }

            }

        }

        // if order list is empty, result is OK
        if (emptyOrderList){
            result.delete(0, result.length());
            result.append(
                String.format(
                    "%s OK\n",
                    currentDate
                )
            );
        }

        // for debuggins
        if (Tp2.DEBUG)
            result.append("Done DATE\n");

        return result.append("\n").toString();
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
