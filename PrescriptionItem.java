public class PrescriptionItem {

    String medication; // medication name
    int numPerDose; // the number of units of the medication for each dose
    int numRepeats; // the number of dose cycles

    // constructor
    public PrescriptionItem(String medication, int numPerDose, int numRepeats) {
        this.medication = medication;
        this.numPerDose = numPerDose;
        this.numRepeats = numRepeats;
    }

    // get the needed number of medications for the prescription
    public int getNeedNumber(){
        return this.numPerDose * this.numRepeats;
    }

    // get (create) the needed date for the medications when dose cycles ends 
    public PharmacyDate getNeedExpDate(){        
        return Pharmacy.currentDate.plusDays(this.numPerDose * this.numRepeats);
    }




}
