FUNCTION executePrescription(stock, prescription, curDate) DO
    
    result <- create empty string builder for printing result
    result <- add prescription count

    1. LOOP while prescription has medication

        medication <- parse next line with medication
        numNeed <- calculate needed number of this medication
        expNeed <- calculate needed expiry date of this medication
        medicationSupply <- from stock get subtree 
                            with supply for this medication

        IF medicationSupply is null DO
            create empty medicationSupply and add to stock
        
        medicationSupplyTail <- get tail tree of medicationSupply 
                                consisting of items with valid expiry date
        
        2. LOOP for each valid inventoryItem in medicationSupplyTail

            numAvailalbe <- get available number of drugs
                            for this inventoryItem

            IF we can satisfy prescription with this inventoryItem DO
                numAvailable <- numAvailable - numNeed
                numNeed <- 0 (reset numNeed)
                BREAK LOOP 


        IF numNeed >0 (prescription was not satisfied) DO
            create inventoryItem
            numOrdered <- put numNeed to inventoryItem
            add (expNeed, inventoryItem) to medicationSupply


        result <- add medication and its status (OK or COMMANDE)

    RETURN result