import java.util.TreeMap;

import javax.print.DocFlavor.STRING;

import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;

public class Pharmacy {

    private static final PharmacyDate MIN_DATE = new PharmacyDate(2000, 1, 1);
    private static final PharmacyDate MAX_DATE = new PharmacyDate(2027, 1, 1);
    
    // CREATE CLASS VARIABLE FOR CURRENT DATE TO ACCESS AND CHANGE GLOBALY
    static PharmacyDate currentDate = new PharmacyDate();

    // create a variable to store information about the whole medication stock 
    TreeMap<String, TreeMap<PharmacyDate, InventoryItem>> stock = new TreeMap<>();
 
    // STATIC (GLOBAL) CLASS VARIABLE COUTING THE NUMBER OF PRESCCRIPTIONS
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
                if (item.numAvailable != 0 && item.numAvailable > item.numRequested != date.compareTo(currentDate) < 0){
                    result.append(String.format("%s %d %s\n", 
                        medication, item.numAvailable - item.numRequested, date));
                }

            });
        });

        // for debuggins
        if (Tp2.DEBUG)
            result.append("Done STOCK\n");        


        return result.append("\n").toString();
    }


    public String executePrescription(InputIterator iter) {

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

            // status message to report. Default status = OK.
            String status = "OK";

            // get stock tree for this medication
            TreeMap<PharmacyDate, InventoryItem> medicationStock = stock.get(item.medication);

            // if medication does not exist in stock, we create it and add to stock
            if (medicationStock == null){
                medicationStock = new TreeMap<>();
                stock.put(item.medication, medicationStock);
            }

            // for this medication stock we get inventory item for the required end date
            InventoryItem inventoryItem = medicationStock.get(expNeed);

            // if medication with the required end date does not exist, create it and add to stock
            if (inventoryItem == null){
                inventoryItem = new InventoryItem(0, 0);
                medicationStock.put(expNeed, inventoryItem);
            }

            // create a navigable tail tree medicationStockTail from medicationStock
            // this tree has elements with keys bigger or equal to expNeed
            // true indicates that we include the node with key = expNeed
            NavigableMap<PharmacyDate, InventoryItem> medicationStockTail = 
                medicationStock.tailMap(expNeed, true);

            // create an iterator to efficiently iterate over values of medicationStockTail
            // values are InventoryItem instances
            Iterator<InventoryItem> iterator =
                medicationStockTail.values().iterator();

            // we iteratively update numRequested and numNeed for each inventory item in medicationStockTail
            // while either we have inventory items in medication stock
            // or we have not satisfied the numNeed
            while (iterator.hasNext() && numNeed>0){
                inventoryItem = iterator.next();
                int delta = inventoryItem.numAvailable - inventoryItem.numRequested;
                if (delta > 0){
                    if (delta >= numNeed){
                        inventoryItem.numRequested += numNeed;
                        numNeed = 0;
                    }
                    else{
                        numNeed -= delta;
                        inventoryItem.numRequested = inventoryItem.numAvailable; 
                    }

                }
            }

            // if we could not satisfy the needed number of medications
            // we need need to order the rest
            // we updated numRequested at inventoryItem with the target expDate
            // we set status = COMMANDE
            if (numNeed > 0){
                inventoryItem = medicationStock.get(expNeed);
                inventoryItem.numRequested = numNeed;
                status = "COMMANDE";
            }

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
                "%s COMMANDES :\n",
                currentDate
            )
        );

        // iterate through all medications
        for (String medicationName : stock.keySet()) {

            TreeMap<PharmacyDate, InventoryItem> medicationStock = stock.get(medicationName);

            // the total number of requested type of medication regardless the expiryDate
            int totalNumRequested = 0;

            // iteratate through all medication stock to update records
            for (PharmacyDate expiryDate : medicationStock.keySet()){

                InventoryItem inventoryItem = medicationStock.get(expiryDate);

                // if a medication has expeired, remove it
                if (expiryDate.compareTo(currentDate) < 0){
                    inventoryItem.numAvailable = 0;
                    inventoryItem.numRequested = 0;
                }


                // when there is more items left than ordered
                // we updated numAvailable and reset numRequested 
                if (inventoryItem.numAvailable >= inventoryItem.numRequested){
                    inventoryItem.numAvailable -= inventoryItem.numRequested;
                    inventoryItem.numRequested = 0;
                }

                // when there are not enough available items
                // then we update totalNumRequested
                // we reset numAvailable
                // we reset numRequested (remove drugs from oder list)
                else{
                    totalNumRequested += inventoryItem.numRequested - inventoryItem.numAvailable; 
                    inventoryItem.numRequested = 0;
                    inventoryItem.numAvailable = 0;
                }

            }

            // if totalNumRequested is not empty then add it to the result
            if (totalNumRequested > 0) {
                result.append(
                    String.format(
                        "%s %d\n",
                        medicationName,
                        totalNumRequested
                    )
                );

            // change flag
            emptyOrderList = false;
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
