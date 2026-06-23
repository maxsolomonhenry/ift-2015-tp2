public class InventoryItem {
    int numAvailable;
    int numOrdered;

    public InventoryItem(int numAvailable) {
        this.numAvailable = numAvailable;
        this.numOrdered = 0;
    }
}
