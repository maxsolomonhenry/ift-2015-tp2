import java.util.TreeSet;

public class Pharmacy {

    private static final PharmacyDate MIN_DATE = new PharmacyDate(2000, 1, 1);
    private static final PharmacyDate MAX_DATE = new PharmacyDate(2027, 1, 1);

    TreeSet<MedicationItem> stock = new TreeSet<>();
    PharmacyDate currentDate = new PharmacyDate();

    public Pharmacy() {
    }

    public String executePrescription(InputIterator iter) {
        while (!iter.peek().contains(";")) {
            iter.next();
        }

        String result = "Done Prescription";
        return result;
    }

    public String executeDate(PharmacyDate val) {
        setCurrentDate(val);
        return buildDateMessage();
    }

    private String buildDateMessage() {
        StringBuilder result = new StringBuilder();
        result.append(String.format(
                "%s OK",
                currentDate));

        return result.toString();
    }

    private void setCurrentDate(PharmacyDate val) {
        if (val.compareTo(MIN_DATE) < 0 || val.compareTo(MAX_DATE) > 0) {
            throw new IllegalArgumentException(String.format(
                "Date %s is out of range (%s to %s)",
                val,
                MIN_DATE,
                MAX_DATE
            ));
        }
        currentDate = val;
    }
}
