public class PrescriptionItem {

    String medication; // medication name
    int numPerDose; // the number of units of the medication for each dose
    int numRepeats; // the number of dose cycles
    int numTotal; // the total required number upon constructing


    public PrescriptionItem(String medication, int numPerDose, int numRepeats) {
        this.medication = medication;
        this.numPerDose = numPerDose;
        this.numRepeats = numRepeats;
        this.numTotal = this.numPerDose * this.numRepeats;
    }

    // get (create) the date when cycle of doses ends
    public PharmacyDate getEndDate(){        
        return Pharmacy.currentDate.plusDays(this.numTotal);
    }

}
