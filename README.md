# TP2

Make a pharamcy stocking app.

## Architecture

### Hierarchy
Hierarchy is as follows:
- `stockTree (TreeSet<MedicationItem>)`
- `MedicationItem`:
    - `name (String)`: Medication name (comparable)
    - `inventory (PriorityQueue<InventoryRecord>)`
- `InventoryRecord`:
   - `expiryDate (CustomDate)`: CustomDate class (comparable)
   - `quantity (Integer)`: amount available

### Classes
- `MedicationItem`
    - name (String, comparable), inventory (MinHeap<InventoryRecord>)
    - compareTo on name
    - getName to get name of the medication
    - addrecord to add new InventoryRecord instace to inventory heap
- `InventoryRecord`
    - expiryDate (Date, comparable), quantity (Integer)
    - compareTo on expiryDate
    - setQuanity on quanity to overwrite quantity
- `CustomDate`
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
- [ ] Logic to write output

### Max
- [ ] Read file input logic
- [ ] Implement classes to support reading input (Prescirption etc.)

### Vlad
- [x] Date class with validity checking and order (preimplemented?)
    - `CustomDate(year (int), month (int), day (int))`
    - `validate()` (called in the constructor): makes sure that the date is a real one (e.g., no Feb 29 _unless it's a leap year_).
    - `comparable()`: returns -1 if thisdate is earlier than thatdate...

- [ ] Make MedicationItem class to store name of medication and its inventory record with exp date and qt
    - `MedicationItem(String name, PriorityQueue<InventoryRecord> inventory)`
    - `compareTo()` returns -1 if this.name is earlier than other.name
    - `addRecord(InventoryRecord newRecord)` add newRecord to the priority queue

- [ ] Make InventoryItem class
    - `InventoryItem(CustomDate expiryDate, int quantity)`
    - `compareTo()` returns -1 if this.expiryDate is sooner than other.expiryDate
    - `setQuantity(int newQuantity)` set updated quanity for this record
- [x] Make stock tree
    - to create stock tree in TP2 as TreeSet storing MedicationItem