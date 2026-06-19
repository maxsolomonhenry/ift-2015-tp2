import java.util.PriorityQueue;

public class MedicationItem implements Comparable<MedicationItem>{
    
    // medication name (constant and encapsulated)
    private final String name;

    // heap to store medication inventory record (encapsulated)
    private PriorityQueue<InventoryItem> inventory;

    // constructor
    public MedicationItem(String name){
        this.name = name;
        this.inventory = new PriorityQueue<InventoryItem>();
    }

    // get name
    public String getName(){
        return name;
    }

    // comparator
    @Override
    public int compareTo(MedicationItem other){
        return this.name.compareTo(other.name);
    }

    // add new inventory record about this medication
    public void addRecord(InventoryItem newRecord){
        inventory.add(newRecord);
    }


}
