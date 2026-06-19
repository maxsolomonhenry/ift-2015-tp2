public class PrescriptionItem {

    String medication;
    int numPerDose;
    int numRepeats;

    public PrescriptionItem(String medication, int numPerDose, int numRepeats) {
        this.medication = medication;
        this.numPerDose = numPerDose;
        this.numRepeats = numRepeats;
    }
}
