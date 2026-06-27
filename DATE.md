FUNCTION executeDate(stock, newDate) DO

    result <- create empty string builder for printing result

    1. LOOP while stock has medication supply DO

        medicationName <- get next medication from stock
        medicationSupply <- get supply for this medication from stock
        totalNumOrdered <- 0 (to count order amount for this medication)

        2. LOOP while medicationSupply has inventory items DO

            InventroyItem <- get next inventory item 
                            from medicationSupply sub-tree
            expDate <- get its expiry date 
                        from medicationSupply sub-tree
            numOrdered <- get number ordered from this inventory item    

            if expDate < curDate (inventoryItem has expired) DO
                totalNumOrdered <- totalNumOrdered + numOrdered (add to order)
                REMOVE (expDate, inventoryItem) from medicationSupply

            if numOrdered > 0 (inventoryItem has smth to order) DO
                totalNumOrdered <- totalNumOrdered + numOrdered (add to order)
                REMOVE (expDate, inventoryItem) from medicationSupply


        IF totalNumOrdered > 0 (medication has orders) DO
            result <- add medicationName and totalNumOrderd

        IF medicationSupply is empty DO
            REMOVE medicationSupply from stock


    RETURN result

