import java.time.LocalDate;
import java.time.DateTimeException;

// a custom class wrapping existing class java.time.LocalDate
public class PharmacyDate implements Comparable<PharmacyDate>{

    // encapsulate date 
    private final LocalDate date;

    // constructor from year, month, day
    public PharmacyDate() {
        this(1900, 1, 1);
    }

    public PharmacyDate(int year, int month, int day){
        try{
            this.date = LocalDate.of(year, month, day);
        }

        // reporting error if date is invalid
        catch (DateTimeException e){
            throw new IllegalArgumentException(
                String.format("Date %4d-%2d-%2d is invalid", 
                    year, month, day
                )
            );
        }  
    }

    // getting date instance variable
    public LocalDate getDate(){
        return date;
    }
    

    // comparator
    @Override
    public int compareTo(PharmacyDate other){
        return this.date.compareTo(other.date);
    }

    // to string
    @Override
    public String toString(){
        return date.toString();
    } 


}