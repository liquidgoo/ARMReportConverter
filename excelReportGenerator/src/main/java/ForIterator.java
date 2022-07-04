import java.util.Iterator;
import java.util.NoSuchElementException;

public class ForIterator implements Iterator<Integer> {
    private final int first;
    private final int last;
    private final int step;

    private int current;


    public ForIterator(int first, int last, int step) {
        this.first = first;
        this.current = first;
        this.last = last;
        this.step = step;
    }

    public ForIterator(int first, int last) {
        this.first = first;
        this.current = first;
        this.last = last;
        this.step = 1;
    }

    @Override
    public boolean hasNext() {
        return (current < last && step > 0) || (current > last && step < 0);
    }

    @Override
    public Integer next() {
        if (!hasNext()) throw new NoSuchElementException();
        int temp = current;
        current += step;
        return temp;
    }
}
