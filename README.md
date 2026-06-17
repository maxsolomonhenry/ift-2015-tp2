# TP2

Make a pharamcy stocking app.

## Architecture

### Hierarchy
Hierarchy is as follows:
- `stockTree (Tree<MedicationItem>)`
- `MedicationItem`:
    - `name (String)`: Medication name (comparable)
    - `inventory (MinHeap<InventoryItem>)`
- `InventoryItem`:
   - `date (Date)`: Date class (comparable)
   - `quantity (Integer)`: amount available

### Classes
- `MedicationItem`
    - name (String, comparable), inventory (MinHeap<InventoryItem>)
- `InventoryItem`
    - date (Date, comparable), quantity (Integer)
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
- [ ] Make InventoryItem class
- [ ] Make MedicationItem class
- [ ] Make stock tree
- [ ] Logic to write output

### Max
- [ ] Read file input logic
- [ ] Implement classes to support reading input (Prescirption etc.)

### Vlad
- [ ] Date class with validity checking and order (preimplemented?)
    - `Date(year (int), month (int), day (int))`
    - `validate()` (called in the constructor): makes sure that the date is a real one (e.g., no Feb 29 _unless it's a leap year_).
    - `comparable()`: returns -1 if thisdate is earlier than thatdate...