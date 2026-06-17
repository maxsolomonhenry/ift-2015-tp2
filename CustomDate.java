import java.time.LocalDate;
import java.time.DateTimeException;

// a custom class wrapping existing class java.time.LocalDate
public class CustomDate implements Comparable<CustomDate>{

    // encapsulate date 
    private final LocalDate date;

    // constructor from year, month, day
    public CustomDate(int year, int month, int day){
        try{
            this.date = LocalDate.of(year, month, day);
        
        }

        // reporting error if date is invalid
        catch (DateTimeException e){
            throw new IllegalArgumentException(
                "date " + year + "-" + month + "-" + day + "is invalid"
            );
        }  
    }

    // getting date instance variable
    public LocalDate getDate(){
        return date;
    }
    

    // comparator
    @Override
    public int compareTo(CustomDate other){
        return this.date.compareTo(other.date);
    }

    // to string
    @Override
    public String toString(){
        return date.toString();
    } 


}