# TP2

Make a pharamcy stocking app.

## Scripts

1. script to compare expected outputs and outputs produced by TP2

run in bush:
./compare_outputs.sh



## Architecture

## TODO
- [x] Double check that lines with ";" in them don't get ignored.
- [x] Comments and cleanup.
- [x] What is the deal with `NavigableMap`? I think this adds unnecessary complexity.
- [ ] Report writeup (empirical analysis).
- [x] Redesignate the responsibility of the expiry date from prescription item to the Pharmacy engine.
- [ ] Clean up empty inventory items.

### Max
- [x] Logic to write output
- [x] Read file input logic
- [x] Implement classes to support reading input (Prescirption etc.)
- [x] APPROV
- [x] STOCK

### Vlad
- [x] DATE
- [x] PRESCRIPTION
- [x] Date class with validity checking and order (preimplemented?)
    - `Date(year (int), month (int), day (int))`
    - `validate()` (called in the constructor): makes sure that the date is a real one (e.g., no Feb 29 _unless it's a leap year_).
    - `comparable()`: returns -1 if thisdate is earlier than thatdate...

- [ ] Make MedicationItem class to store name of medication and its inventory record with exp date and qt
    - `MedicationItem(String name, PriorityQueue<InventoryItem> inventory)`
    - `compareTo()` returns -1 if this.name is earlier than other.name
    - `addRecord(InventoryItem newRecord)` add newRecord to the priority queue

- [ ] Make InventoryItem class
    - `InventoryItem(Date expiryDate, int quantity)`
    - `compareTo()` returns -1 if this.expiryDate is sooner than other.expiryDate
    - `setQuantity(int newQuantity)` set updated quanity for this record
- [x] Make stock tree
    - to create stock tree in TP2 as TreeSet storing MedicationItem