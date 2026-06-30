FUNCTION executeApprove(stock, approv) DO

    WHILE approvisionnement has medications DO
        approvItem <- parse next line with medication
        name <- get name of medication
        quantity <- get quantity of medication
        expDate <- get expire date of medication

        IF NOT stock has medicationSupply with the name DO
            CREATE new empty medicationSupply
            put name and medicationSupply to stock tree 
                                as key and element pair

        medicationSupply <- get by medication name from stock

        IF NOT medicationSupply has inventory with expDate DO
            CREATE new empty inventoryItem
            put expDate and inventoryItem to medicationSupply tree
                                as key and element pair

        inventoryItem <- get by expDate from medicationSupply
        numAvailalbe <- get available number from inventoryItem
        numAvailable <- numAvailable + quantity
                        (update avaialble quantity)

        put expDate and inventoryItem to medicationSupply tree
                                as key and element pair

    RETURN "OK"


