package lec7_lists4;

public class AList<Pineapple> {
    private Pineapple[] items;
    private int size;

    // The next item we want to add, will go in position...size!
    // The last item in the list is at position...size - 1.
    /** Creates an empty list. */
    public AList() {
        items = (Pineapple[]) new Object[100];
        size = 0;
    }

    /** Resizes the underlying array to the target capacity. */
    private void resize(int capacity) {
        Pineapple[] a = (Pineapple[]) new Object[capacity];
        System.arraycopy(items,0,a,0,size);
        items = a;
    }

    /** Insert x into the back of the list. */
    public void addLast(Pineapple x) {
        if (size == items.length) {
            resize(size * 2);
        }

        items[size] = x;
        size += 1;
    }

    /** Returns the item from the back of the list. */
    public Pineapple getLast() {
        return items[size - 1];
    }

    /** Gets the ith item in the list (0 is the front). */
    public Pineapple get(int i) {
        return items[i];
    }

    /** Returns the number of items in the list. */
    public int size() {
        return size;
    }

    /**
     * Deletes item from back of the list and returns deleted item.
     */
    public Pineapple removeLast() {
        Pineapple returnItem = getLast();
        items[size - 1] = null;
        size -= 1;
        return returnItem;
    }
}
