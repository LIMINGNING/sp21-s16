package lec7_lists4;

public class AList {
    private int[] items;
    private int size;

    // The next item we want to add, will go in position...size!
    // The last item in the list is at position...size - 1.
    /** Creates an empty list. */
    public AList() {
        items = new int[100];
        size = 0;
    }

    /** Insert x into the back of the list. */
    public void addLast(int x) {
        items[size] = x;
        size += 1;
    }

    /** Returns the item from the back of the list. */
    public int getLast() {
        return items[size - 1];
    }

    /** Gets the ith item in the list (0 is the front). */
    public int get(int i) {
        return items[i];
    }

    /** Returns the number of items in the list. */
    public int size() {
        return size;
    }
}
