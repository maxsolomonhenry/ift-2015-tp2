import java.util.TreeMap;
import java.util.Iterator;
import java.util.NavigableMap;

public class Pharmacy {

    private static final PharmacyDate MIN_DATE = new PharmacyDate(2000, 1, 1);
    private static final PharmacyDate MAX_DATE = new PharmacyDate(2027, 1, 1);
    
    static PharmacyDate currentDate = new PharmacyDate();

    // Stock stores all the relevant information.
    //
    // Medications are keyed by their name (String). For each medication, we 
    // store an expiry date and
    TreeMap<String, TreeMap<PharmacyDate, InventoryItem>> stock = new TreeMap<>();
 
    // STATIC (GLOBAL) CLASS VARIABLE COUNTING THE NUMBER OF PRESCCRIPTIONS
    static int prescriptionCount = 0; 
    
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

        // for debuggins
        if (Tp2.DEBUG) {
            System.out.println("Done STOCK\n");
        }

        return result.append("\n").toString();
    }


    public String executePrescription(InputIterator iter)
    {

        // augment the number of prescriptions
        prescriptionCount += 1;

        // create result string builder to store outputs
        StringBuilder result = new StringBuilder();
        result.append(String.format("PRESCRIPTION %d\n", prescriptionCount));

        // loop while presription has medications
        while (!iter.next().contains(";")) {
            buffer = iter.peek();

            // create a Prescription item instance from the input line
            PrescriptionItem item = Parser.parsePrescriptionItem(buffer);

            // get the needed number of medications to provide for the prescription
            int numNeed = item.getNeedNumber();

            // get the minimum needed expiration date expNeed for medications for the prescription 
            PharmacyDate expNeed = item.getNeedExpDate();

            // status message to report. Default status = COMMANDE.
            String status = "COMMANDE";

            // get stock tree for this medication - thisMedicationStock
            TreeMap<PharmacyDate, InventoryItem> thisMedicationStock = stock.get(item.medication);

            // if this medication stock does not exist in stock, we create it and add to stock
            if (thisMedicationStock == null){
                thisMedicationStock = new TreeMap<>();
                stock.put(item.medication, thisMedicationStock);
            }

            // for this medication stock we get this inventory item for the required end date
            InventoryItem thisInventoryItem = thisMedicationStock.get(expNeed);

            // if thisInventoryItem (medicaiton item for the required date) does not exist, create it and add to stock
            if (thisInventoryItem == null){
                thisInventoryItem = new InventoryItem(0);
                thisMedicationStock.put(expNeed, thisInventoryItem);
            }

            // create a navigable tail tree thisMedicationStockTail from thisMedicationStock
            // this tree has elements with keys bigger or equal to expNeed
            // true indicates that we include the node with key = expNeed
            NavigableMap<PharmacyDate, InventoryItem> thisMedicationStockTail = 
                thisMedicationStock.tailMap(expNeed, true);

            // create an iterator to efficiently iterate over values of thisMedicationStockTail
            // values are InventoryItem instances
            Iterator<InventoryItem> iterator =
                thisMedicationStockTail.values().iterator();

            // we iterate over each inventory item in thisMedicationStockTail
            // until we have inventory item or until we find item to satisfy the prescription
            while (iterator.hasNext() && numNeed>0)
            {
                // get next inventory item
                InventoryItem inventoryItem = iterator.next();
                
                // if it has more available than needed, then we satisfied the needed
                if (inventoryItem.numAvailable >= numNeed)
                {
                    inventoryItem.numAvailable -= numNeed;
                    numNeed = 0;
                    status = "OK"; // put status is OK
                }
            }

            // if after the loop, we were unsuccessful in satisfying the demand
            // we need to order more of thisInventoryItem (with the required expiry date)
            // numOrdered is set
            // numAvailable is 0
            if (numNeed > 0)
            {
                thisInventoryItem.numOrdered = numNeed -  thisInventoryItem.numAvailable;
                thisInventoryItem.numAvailable = 0;
            }
            
            // return a status message.
            result.append(String.format("%s %d %d  %s\n", item.medication, item.numPerDose, item.numRepeats, status));
        }

        if (Tp2.DEBUG) {
            System.out.println("Done PRESCRIPTION\n");
        }

        return result.append("\n").toString();
    }


    public String executeDate(InputIterator iter) {
        // Strip "DATE" text.
        String[] parts = iter.peek().split(" +");
        setCurrentDate(Parser.parseDate(parts[1]));

        // flag for returning empty order list
        boolean emptyOrderList = true;

        // building result output
        // add commande by default
        StringBuilder result = new StringBuilder();
        result.append(
            String.format(
                "%s COMMANDES :\n",
                currentDate
            )
        );

        // iterate through all medications
        for (String medicationName : stock.keySet()) {

            TreeMap<PharmacyDate, InventoryItem> medicationStock = stock.get(medicationName);

            // the total number of requested type of medication regardless the expiryDate
            int totalNumOrdered = 0;

            // iteratate through all medication stock to update records
            for (PharmacyDate expiryDate : medicationStock.keySet()){

                InventoryItem inventoryItem = medicationStock.get(expiryDate);

                // if a medication has expired, remove it but add items to order
                if (expiryDate.compareTo(currentDate) < 0){
                    totalNumOrdered += inventoryItem.numOrdered;
                    inventoryItem.numAvailable = 0;
                    inventoryItem.numOrdered = 0;
                }

                // if we need to order this inventory item
                // add numOrdered to totalNumOrdered
                // reset numOrdered to 0
                if (inventoryItem.numOrdered >0)
                {
                    totalNumOrdered += inventoryItem.numOrdered;
                    inventoryItem.numOrdered = 0;
                }
            }

            // if totalNumOrdered is not 0 then change flag and add to the resul
            if (totalNumOrdered > 0) 
            {
                emptyOrderList = false; // update flag
                result.append(
                    String.format(
                        "%s %d\n",
                        medicationName,
                        totalNumOrdered
                    )
                );
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
