import java.util.TreeMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;

public class Pharmacy {

    private static final PharmacyDate MIN_DATE = new PharmacyDate(2000, 1, 1);
    private static final PharmacyDate MAX_DATE = new PharmacyDate(2027, 1, 1);
    
    private PharmacyDate currentDate = new PharmacyDate();

    // Stock stores all the relevant information.
    //
    // Medications are keyed by their name (String). For each medication, we 
    // store an expiry date and
    TreeMap<String, TreeMap<PharmacyDate, InventoryItem>> stock = new TreeMap<>();
 
    // Presciption counter to label incoming prescriptions.
    private int prescriptionCount = 0; 
    
    // Working memory for the input.
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
                medStore.put(item.expiry, new InventoryItem(0));
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

        // Present report.
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
                if (item.numAvailable != 0 && date.compareTo(currentDate) > 0){
                    result.append(String.format("%s %d %s\n", 
                        medication, item.numAvailable, date));
                }

            });
        });

        // For debugging.
        if (Tp2.DEBUG) {
            System.out.println("Done STOCK\n");
        }

        return result.append("\n").toString();
    }


    public String executePrescription(InputIterator iter)
    {

        // Increment prescription counter to label incoming medications.
        prescriptionCount += 1;

        // Create result string builder to store outputs.
        StringBuilder result = new StringBuilder();
        result.append(String.format("PRESCRIPTION %d\n", prescriptionCount));

        // Loop while presription has medications.
        while (!iter.next().contains(";")) {
            buffer = iter.peek();

            // Create a Prescription item instance from the input line.
            PrescriptionItem item = Parser.parsePrescriptionItem(buffer);

            // Get the needed number of medications to provide for the prescription
            int numNeed = item.getNeedNumber();

            // Get the earliest expiration date numNeed for medications.
            PharmacyDate expNeed = currentDate.plusDays(numNeed);

            // Status message to report. Default status = COMMANDE.
            String status = "COMMANDE";

            // Get supply for this medication.
            TreeMap<PharmacyDate, InventoryItem> medicationSupply = stock.get(item.medication);

            // If this medication does not exist in stock, we create it and add to stock.
            if (medicationSupply == null){
                medicationSupply = new TreeMap<>();
                stock.put(item.medication, medicationSupply);
            }

            // Search to find the earliest possible supply. To do so we iterate 
            // through the sub-tree starting at the ideal expiry date.
            //
            // Create a tail tree, then make an iterator from this tail. This 
            // subtree has elements with keys bigger or equal to the desired 
            // expiry date. The bool true indicates that we include the node 
            // with the starting key, if present.
            NavigableMap<PharmacyDate, InventoryItem> medicationStockTail = 
                medicationSupply.tailMap(expNeed, true);

            Iterator<InventoryItem> iterator =
                medicationStockTail.values().iterator();

            // Iterate over each inventory item until (and if) we find enough 
            // supply to satisfy the prescription.
            while (iterator.hasNext() && numNeed>0)
            {
                InventoryItem inventoryItem = iterator.next();
                
                // If it has more available than needed, then we're done.
                if (inventoryItem.numAvailable >= numNeed)
                {
                    inventoryItem.numAvailable -= numNeed;
                    numNeed = 0;
                    status = "OK";
                }
            }

            // If not found, create an entry and add to stock. This will
            // register as an order request.
            if (numNeed > 0)
            {   
                medicationSupply.put(expNeed, new InventoryItem(0, numNeed));
            }
            
            // Return status message.
            result.append(String.format("%s %d %d  %s\n", item.medication, item.numPerDose, item.numRepeats, status));
        }

        if (Tp2.DEBUG) {
            System.out.println("Done PRESCRIPTION\n");
        }

        return result.append("\n").toString();
    }


    public String executeDate(InputIterator iter) {
        // Strip "DATE" text from the command string and take the date data.
        String[] parts = iter.peek().split(" +");
        setCurrentDate(Parser.parseDate(parts[1]));

        boolean isOrderListEmpty = true;

        // To build the result of the output. Add COMMANDES by default.
        StringBuilder result = new StringBuilder();
        result.append(
            String.format(
                "%s COMMANDES :\n",
                currentDate
            )
        );

        // Iterate through all medications.
        for (String medicationName : stock.keySet()) {

            TreeMap<PharmacyDate, InventoryItem> medicationSupply = stock.get(medicationName);

            // Track number needed for this medication.
            int totalNumOrdered = 0;

            // Iteratate through all medication stock to update records. Defining
            // an iterator (rather than for loop) to faciliate in-line deletion.
            Iterator<Map.Entry<PharmacyDate, InventoryItem>> it = medicationSupply.entrySet().iterator();
            while (it.hasNext()){

                // Get next key and value pair.
                Map.Entry<PharmacyDate, InventoryItem> entry = it.next();

                PharmacyDate expiryDate = entry.getKey();
                InventoryItem inventoryItem = entry.getValue();

                // If a medication has expired, remove it but add items to order.
                if (expiryDate.compareTo(currentDate) < 0){
                    totalNumOrdered += inventoryItem.numOrdered;
                    it.remove();
                    continue;
                }

                // If we need to order this inventory item add numOrdered to 
                // totalNumOrdered. Reset numOrdered to 0.
                if (inventoryItem.numOrdered > 0)
                {
                    totalNumOrdered += inventoryItem.numOrdered;
                    inventoryItem.numOrdered = 0;
                }
            }

            // If totalNumOrdered is not 0 then change flag and add to the result.
            if (totalNumOrdered > 0) 
            {
                // Indicate that we should make a report.
                isOrderListEmpty = false; 
                result.append(
                    String.format(
                        "%s %d\n",
                        medicationName,
                        totalNumOrdered
                    )
                );
            }

        }

        // If order list is empty, result is "OK."
        if (isOrderListEmpty){
            result.delete(0, result.length());
            result.append(
                String.format(
                    "%s OK\n",
                    currentDate
                )
            );
        }

        if (Tp2.DEBUG) {
            System.out.println("Done DATE\n");
        }

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
