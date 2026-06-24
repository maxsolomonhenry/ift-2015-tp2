# TP2

Make a pharamcy stocking app.

## Scripts

1. script to compare expected outputs and outputs produced by TP2

run in bush:
./compare_outputs.sh



## Architecture

### Hierarchy
Hierarchy is as follows:
- `stockTree (TreeSet<MedicationItem>)`
- `MedicationItem`:
    - `name (String)`: Medication name (comparable)
    - `inventory (PriorityQueue<InventoryItem>)`
- `InventoryItem`:
   - `expiryDate (Date)`: Date class (comparable)
   - `quantity (Integer)`: amount available

### Classes
- `MedicationItem`
    - name (String, comparable), inventory (MinHeap<InventoryItem>)
    - compareTo on name
    - getName to get name of the medication
    - addrecord to add new InventoryItem instace to inventory heap
- `InventoryItem`
    - expiryDate (Date, comparable), quantity (Integer)
    - compareTo on expiryDate
    - setQuanity on quanity to overwrite quantity
- `Date`
    - validation logic (doesn't validate leap years, num days in certain months, etc.)
    - comparable implementation
- ?`Prescription` (for requests)
    - unique id
    - medications (List<MedicationRequest>)
- ?`MedicationRequest`
    - medication name
    - dose per cycle
    - num cycles
- `ProcessingQueue` (Queue<Prescription>)
    - for FIFO handling of prescriptions.

## TODO
- [ ] Double check that lines with ";" in them don't get ignored.
- [ ] DATE
- [ ] PRESCRIPTION


### Max
- [x] Logic to write output
- [x] Read file input logic
- [x] Implement classes to support reading input (Prescirption etc.)
- [x] APPROV
- [x] STOCK

### Vlad
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