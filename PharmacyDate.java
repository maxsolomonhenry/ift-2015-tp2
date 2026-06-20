import java.time.LocalDate;
import java.time.DateTimeException;

// a custom class wrapping existing class java.time.LocalDate
public class PharmacyDate implements Comparable<PharmacyDate>{

    // encapsulate date 
    private LocalDate date;

    // default constructor
    public PharmacyDate() {
        this(1900, 1, 1);
    }

    // constructor from year, month, day
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

    // constructor from LocalDate
    public PharmacyDate(LocalDate date){
        this.date = date;
    }


    // getting date instance variable
    public LocalDate getDate(){
        return date;
    }

    // plus X days to PharmacyDate and return (create) new one
    public PharmacyDate plusDays(int days){
        return new PharmacyDate(date.plusDays(days));
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