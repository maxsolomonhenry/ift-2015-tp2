public class InventoryItem {
    int numAvailable;
    int numOrdered;

    public InventoryItem(int numAvailable, int numOrdered) {
        this.numAvailable = numAvailable;
        this.numOrdered = numOrdered;
    }

    // Second constructor that assigns numOrdered=0 by default.
    public InventoryItem(int numAvailable) {
        this(numAvailable, 0);
    }
}
