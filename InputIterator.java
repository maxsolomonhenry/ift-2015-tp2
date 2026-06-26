import java.util.Iterator;

public class InputIterator {

    // Permits passing around the input data and ability to advance.
    Iterator<String> it;
    String buffer;

    public InputIterator(Iterator<String> it) {
        this.it = it;
    }

    public boolean hasNext() {
        return it.hasNext();
    }

    public String next() {
        buffer = it.next();
        return buffer;
    }

    public String current() {
        return buffer;
    }
}
