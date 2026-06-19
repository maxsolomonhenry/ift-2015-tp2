// a class to contain a record of cetain medication - expiry date and quantity
public class InventoryItem implements Comparable<InventoryItem> {
    
    // expiration date for this medication record (encapsulated)
    private PharmacyDate expiryDate;

    // quantity of medication for this record (encapsualted) 
    private int quantity;

    // comparator
    @Override
    public int compareTo(InventoryItem other){
        return expiryDate.compareTo(other.expiryDate);
    }

    // getting quantity of resting amount of medication for this record
    public int getQuantity(){
        return quantity;
    }

    // set/update quantity of the rest amount of medication of this record
    public void setQuantity(int newQuantity){
        quantity = newQuantity;
    }


}
