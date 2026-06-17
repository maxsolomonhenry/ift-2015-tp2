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